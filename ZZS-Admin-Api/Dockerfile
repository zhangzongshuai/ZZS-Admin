# 定义父镜像
FROM openjdk:18
# 维护者信息
MAINTAINER zzs

# 打包时的可传递的参数
#ARG APP_NAME

# 设置环境变量-运行时也可传参进来
#ENV APP_NAME ${APP_NAME}

# 将jar包添加到容器指定目录
ADD target/zzsadmin-0.0.1-SNAPSHOT.jar /home/app/zzsadmin.jar
# 定义容器启动执行的命令
CMD java -jar /home/app/zzsadmin.jar > /home/app/zzsadmin.log 2>&1
