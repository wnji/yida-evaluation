#!/usr/bin/env bash

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

install() {
    cd ${APP_HOME} || exit 1
        echo "Start to install application..."
    rm -rf ${APP_HOME}/app
    mkdir -p ${APP_HOME}/app
    unzip -oq ${APP_HOME}/${JAR_NAME} -d ${APP_HOME}/app
    chmod +x ${APP_HOME}/app/WEB-INF/startup.sh
    dos2unix ${APP_HOME}/app/WEB-INF/startup.sh
    echo "Installation completed"
}

start_application() {
    cd ${APP_HOME} || exit 1
        echo "java stdout log: ${JAVA_OUT}"
    install
    cd ${APP_HOME}/app/WEB-INF/
    nohup sh ${APP_HOME}/app/WEB-INF/startup.sh  >> ${JAVA_OUT} 2>&1 &

}

stop_application() {
   echo "stop java process"
       times=60
       for e in $(seq 60)
       do
           sleep 1
           COSTTIME=$(($times - $e ))
           checkjavapid=`ps -ef|grep java|grep $APP_NAME|grep -v appctrl.sh|grep -v jbossctl| grep -v restart.sh |grep -v grep`
           if [[ $checkjavapid ]];then
                   checkjavapid=`ps -ef|grep java|grep $APP_NAME|grep -v appctrl.sh|grep -v jbossctl | grep -v restart.sh |grep -v grep|awk '{print $2}'`
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
    install)
        install
    ;;
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
