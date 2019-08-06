package com.yida.modules.yde.api.security;

import com.yida.modules.yde.api.session.SessionService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@WebFilter(urlPatterns = "/v1/*", filterName = "securityFilter")
public class SecurityFilter implements Filter {
    private static final Log LOGGER = LogFactory.getLog(SecurityFilter.class);


    @Autowired
    private Environment env;

    private List<String> excludedExts;

    private static final Set<String> ALLOWED_PATHS = new HashSet<String>();

    @Autowired
    private SessionService sessionService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String excludedExtConf = env.getProperty("excludedExt");
        if(excludedExtConf!=null) {
            excludedExts = Arrays.asList(excludedExtConf.split(","));
        }
        else{
            excludedExts = new LinkedList<String>();
        }

        String allowedPathConf = env.getProperty("allowedPath");
        String pathPrefix = env.getProperty("v1ApiPath");
        if(allowedPathConf!=null) {
            String[] allowedPaths = allowedPathConf.split(",");

            for(String path: allowedPaths){
                ALLOWED_PATHS.add(pathPrefix + path);
            }
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        //HttpServletResponse response=(HttpServletResponse) servletResponse;

        String path = request.getRequestURI().substring(request.getContextPath().length()).replaceAll("[/]+$", "");
        boolean allowedPath = ALLOWED_PATHS.contains(path);

        if(allowedPath){
            filterChain.doFilter(servletRequest, servletResponse);

            return;
        }

        for(String ext: excludedExts){
            if(path.contains(ext)){
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }

        String token = servletRequest.getParameter("token");
        if(!sessionService.sessionExists(token)){
            String needLoginUrl = env.getProperty("v1ApiPath") + "/needLogin";
            request.getRequestDispatcher(needLoginUrl).forward(servletRequest, servletResponse);
            return;
        }
        else{
            filterChain.doFilter(servletRequest, servletResponse);

            return;
        }
    }

    @Override
    public void destroy(){

    }
}
