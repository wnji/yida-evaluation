package com.yida.modules.yde.service;

import com.jeesite.common.service.CrudService;
import com.yida.modules.yde.api.command.LoginCommand;
import com.yida.modules.yde.api.command.LogoutCommand;
import com.yida.modules.yde.api.security.TokenService;
import com.yida.modules.yde.api.session.SessionService;
import com.yida.modules.yde.api.session.UserSession;
import com.yida.modules.yde.api.v1.model.RestResponse;
import com.yida.modules.yde.dao.YdeUserDao;
import com.yida.modules.yde.entity.YdeUser;
import com.yida.modules.yde.util.Md5Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Date;

@Service
@Transactional
public class YdeSecurityService extends CrudService<YdeUserDao, YdeUser> {

    private static final Logger LOGGER = LoggerFactory.getLogger(YdeSecurityService.class);

    @Value("${yida_cloud_login_url}")
    private String yida_cloud_login_url;

    @Value("${yida_cloud_context_path}")
    private String yida_cloud_context_path;

    @Value("${mockLogin}")
    private String mockLogin;

    @Autowired
    private UserSession userSession;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private TokenService tokenService;

    private PoolingHttpClientConnectionManager cm;
    private CloseableHttpClient httpclient;

    public YdeSecurityService(){
        cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(200);
        cm.setDefaultMaxPerRoute(20);

        httpclient = HttpClients.custom()
                .setConnectionManager(cm)
                .build();

    }

    public void finalize(){
        try {
            httpclient.close();
            cm.close();
        } catch (Exception ex) {
            LOGGER.error("", ex);
        }
    }

    public RestResponse login(LoginCommand command){

        JSONObject yidaObj = getUserFromYidaCloud(command.getUsername(), command.getPassword());
        if(yidaObj==null){
            return new RestResponse(null, String.valueOf(org.springframework.http.HttpStatus.FORBIDDEN.value()), "用户名密码错误");
        }

        String token = tokenService.generateToken(command.getUsername());

        sessionService.createSesseionByToken(token);

        YdeUser ydeUser  =  dao.selectByUserNo(command.getUsername());

        JSONObject yidaObjData = getJSONObjectValue(yidaObj, "data");

        Date now = new Date();

        if(ydeUser==null){
            ydeUser = new YdeUser();

            ydeUser.setYidaUserNo(command.getUsername());

            ydeUser.setCreateDate(now);
            ydeUser.setUpdateDate(now);

            ydeUser.setEvaluationTimes("0");
            ydeUser.setEvaluationModules("0");

            ydeUser.setClassName(getStringValue(yidaObjData, "classesName", null));
            ydeUser.setMajorName(getStringValue(yidaObjData, "professionalName", null));

            ydeUser.setPhoto(getStringValue(yidaObjData, "photo", null));
            ydeUser.setRealName(getStringValue(yidaObjData, "name", null));
            ydeUser.setSchool(getStringValue(yidaObjData, "schoolname", null));
            ydeUser.setSchoolYear(getStringValue(yidaObjData, "editorial", null));

            save(ydeUser);
        }
        else {
            ydeUser.setUpdateDate(now);

            ydeUser.setClassName(getStringValue(yidaObjData, "classesName", ydeUser.getClassName()));
            ydeUser.setMajorName(getStringValue(yidaObjData, "professionalName", ydeUser.getMajorName()));

            ydeUser.setPhoto(getStringValue(yidaObjData, "photo", ydeUser.getPhoto()));
            ydeUser.setRealName(getStringValue(yidaObjData, "name", ydeUser.getRealName()));
            ydeUser.setSchool(getStringValue(yidaObjData, "schoolname", ydeUser.getSchool()));
            ydeUser.setSchoolYear(getStringValue(yidaObjData, "editorial", ydeUser.getSchoolYear()));

            update(ydeUser);
        }

        userSession.setUser(token, ydeUser);

        RestResponse  response = new RestResponse(token);

        return  response;
    }

    private JSONObject getJSONObjectValue(JSONObject obj, String key) {
        if (obj.has(key)) {
            return obj.getJSONObject(key);
        }

        return null;
    }

    private String getStringValue(JSONObject obj, String key, String defaultValue) {

        if (obj != null && obj.has(key)) {
            Object value = obj.get(key);
            if (value instanceof String) {
                return (String) value;
            }
            else{
                try {
                    String str = value.toString();
                    return str;
                } catch (Exception e) {
                    logger.error("", e);
                }
            }
        }

        return defaultValue;
    }

    public RestResponse logout(LogoutCommand logoutCommand){
        sessionService.invalidateSesseion(logoutCommand.getToken());

        return  new RestResponse();
    }

    public RestResponse getUserInfo(String token){
        YdeUser ydeUser = userSession.getUser(token);

        return new RestResponse(ydeUser);
    }

    /**
     * 与益达云平台对接，校验用户名和密码，如果成功返回用户信息
     * @param username
     * @param password
     * @return
     */
    private JSONObject getUserFromYidaCloud(String username, String password){
        if(StringUtils.equalsIgnoreCase(mockLogin, "true")){
            return this.mockGetUserFromYidaCloud(username, password);
        }

        String url = yida_cloud_login_url;
        HttpPost post = new HttpPost(url);
        JSONObject response = null;

        String md5Password = yidaMd5(password);

        String str = "yida-" + username + md5Password;
        String signature = yidaMd5(str);

        String json = "{\"no\":\""+ username + "\",\"pwd\":\"" + md5Password + "\", \"signature\":\"" + signature +"\"}";
        CloseableHttpResponse res = null;
        try {
            StringEntity s = new StringEntity(json);
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");//发送json数据需要设置contentType
            post.setEntity(s);
            res = httpclient.execute(post);
            if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                String result = EntityUtils.toString(res.getEntity());// 返回json格式：
                response = new JSONObject(result);

                if(response.getInt("state")!=1) {
                    return null;
                }
            }
        } catch (Exception e) {
            return null;
        }
        finally {
            this.closeHttpResponse(res);
        }
        return response;
    }

    private JSONObject mockGetUserFromYidaCloud(String username, String password){

        JSONObject obj = new JSONObject();
        return obj;
    }

    /**
     * 益达云平台的md5算法
     * @param str
     * @return
     */
    private String yidaMd5(String str){
        try {
            return Md5Utils.getMd5Str(str);
        } catch (Exception e) {
        logger.error("MD5加密出现错误", e);
        }

        return null;
    }

    private void closeHttpResponse(CloseableHttpResponse response){
        if(response!=null){
            try {
                response.close();
            }
            catch (Exception ex){
                LOGGER.error("", ex);
            }
        }
    }
}
