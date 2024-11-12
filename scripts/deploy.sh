#!/bin/bash

cd /home/ubuntu/backend || exit

REPOSITORY=/home/ubuntu/backend
LOG_BASE_DIR="$REPOSITORY/logs"
LOG_INFO_DIR="$LOG_BASE_DIR/info"
LOG_WARN_DIR="$LOG_BASE_DIR/warn"
LOG_ERROR_DIR="$LOG_BASE_DIR/error"
LOG_BACKUP_DIR="$LOG_BASE_DIR/backup"

echo "> 현재 구동 중인 애플리케이션 pid 확인"

CURRENT_PID=$(pgrep -f ${REPOSITORY}.*.jar)

echo "현재 구동 중인 애플리케이션 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
  echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> 로그 디렉토리 생성 및 권한 설정"

mkdir -p $LOG_INFO_DIR
mkdir -p $LOG_WARN_DIR
mkdir -p $LOG_ERROR_DIR
mkdir -p $LOG_BACKUP_DIR/info
mkdir -p $LOG_BACKUP_DIR/warn
mkdir -p $LOG_BACKUP_DIR/error

sudo chmod -R 775 $LOG_BASE_DIR
sudo chown -R ubuntu:ubuntu $LOG_BASE_DIR

echo "> 새 애플리케이션 배포"

JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR NAME: $JAR_NAME"

echo "> $JAR_NAME 에 실행권한 추가"

chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"

nohup java -jar \
  -Dspring.config.location=classpath:/application.yml,classpath:/application-prod.yml \
  -Dlogging.config=$REPOSITORY/logback-spring.xml \
  $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
