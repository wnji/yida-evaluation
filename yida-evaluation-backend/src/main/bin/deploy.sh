#!/bin/bash
PROG_NAME=$0
ACTION=$1
ONLINE_OFFLINE_WAIT_TIME=6  # 实例上下线的等待时间
APP_START_TIMEOUT=15    # 等待应用启动的时间
APP_PORT=8981          # 应用端口
HEALTH_CHECK_URL=http://yds.dashen-tech.com:${APP_PORT}/yida-evaluation-backend  # 应用健康检查URL
HEALTH_CHECK_FILE_DIR=/home/dev/status   # 脚本会在这个目录下生成nginx-status文件
APP_HOME=/home/dev/yida/yida-evaluation-backend # 从package.tgz中解压出来的jar包放到这个目录下
APP_NAME=yida-evaluation-backend
JAR_NAME=yida-evaluation-backend.war # jar包的名字
APP_LOG=${APP_HOME}/logs/yida-evaluation-backend.log # 应用的日志文件
JAVA_OUT=${APP_HOME}/logs/yida-evaluation-backend.log  #应用的启动日志
JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.201.b09-2.el7_6.x86_64/jre/bin #机器的java环境

# 创建出相关目录
mkdir -p ${HEALTH_CHECK_FILE_DIR}
mkdir -p ${APP_HOME}
mkdir -p ${APP_HOME}/logs
usage() {
    echo "Usage: $PROG_NAME {start|stop|online|offline|restart}"
    exit 2
}
online() {
    # 挂回SLB
    touch -m $HEALTH_CHECK_FILE_DIR/nginx-status || exit 1
    echo "wait app online in ${ONLINE_OFFLINE_WAIT_TIME} seconds..."
    sleep ${ONLINE_OFFLINE_WAIT_TIME}
}
offline() {
    # 摘除SLB
    rm -f $HEALTH_CHECK_FILE_DIR/nginx-status || exit 1
    echo "wait app offline in ${ONLINE_OFFLINE_WAIT_TIME} seconds..."
    sleep ${ONLINE_OFFLINE_WAIT_TIME}
}
health_check() {
    exptime=0
    echo "checking ${HEALTH_CHECK_URL}"
    while true
        do
            status_code=`/usr/bin/curl -L -o /dev/null --connect-timeout 5 -s -w %{http_code}  ${HEALTH_CHECK_URL}`
            echo "\r code is $status_code"
            if [ x$status_code != x200 ];then
                sleep 1
                ((exptime++))

                echo -n -e "\rWait app to pass health check: $exptime..."
            else
                break
            fi
            if [ $exptime -gt ${APP_START_TIMEOUT} ]; then
                echo
                echo 'app start failed'
               exit 0
            fi
        done
    echo "check ${HEALTH_CHECK_URL} success"
}
start_application() {
    cd ${APP_HOME} || exit 1
        echo "java stdout log: ${JAVA_OUT}"
    rm -rf ${APP_HOME}/app
    mkdir -p ${APP_HOME}/app
    unzip -oq ${APP_HOME}/${JAR_NAME} -d ${APP_HOME}/app
    chmod +x ${APP_HOME}/app/WEB-INF/startup.sh
    dos2unix ${APP_HOME}/app/WEB-INF/startup.sh
    cd ${APP_HOME}/app/WEB-INF/
    nohup sh ${APP_HOME}/app/WEB-INF/startup.sh  > ${JAVA_OUT} 2>&1 &

    #nohup  ${JAVA_HOME}/java $JAVA_OPTS -jar ${JAR_NAME} > ${JAVA_OUT} 2>&1 &

}

stop_application() {
   echo "stop java build"
       times=60
       for e in $(seq 60)
       do
           sleep 1
           COSTTIME=$(($times - $e ))
           checkjavapid=`ps -ef|grep java|grep $APP_NAME|grep -v deploy.sh|grep -v jbossctl| grep -v restart.sh |grep -v grep`
           if [[ $checkjavapid ]];then
                   checkjavapid=`ps -ef|grep java|grep $APP_NAME|grep -v deploy.sh|grep -v jbossctl | grep -v restart.sh |grep -v grep|awk '{print $2}'`
                   kill -9 $checkjavapid
                   echo -n -e  "\r        -- stopping java lasts `expr $COSTTIME` seconds."
           else
                   break;
           fi
       done
       echo ""
}
start() {
    start_application
    health_check
    online
}
stop() {
    offline
    stop_application
}
case "$ACTION" in
    start)
        start
    ;;
    stop)
        stop
    ;;
    online)
        online
    ;;
    offline)
        offline
    ;;
    restart)
        stop
        start
    ;;
    *)
        usage
    ;;
esac
