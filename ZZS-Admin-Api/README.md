# ZZS-Admin
##### jar启动命令
```shell script
# 关于2>&1的含义将标准错误输出重定向到标准输出。符号>&是一个整体。
nohup java -jar xxx.jar > xxx.log 2>&1 &
```
##### 添加参数
```shell script
# 注释:–运行jar包并将日志写入到xxx.log中
nohup java -Xms800m -Xmx800m -jar xxx.jar > xxx.log 2>&1 &

# nohup意思就是使用后台方式运行java命令。
nohup java -jar xxx.jar >/dev/null 2>&1 &
```

> /dev/null意思是将标准信息输出到一个黑洞（/dev/null），就是不输出标准信息。

> 2>&1意思是将错误信息和标准信息使用同一个文件，就是不输出错误信息。如果需要输出错误，可以将命令改为：
```shell script
nohup java -jar xxx.jar >/dev/null nohup.out &
```

##### docker打包
```shell script
docker build -f Dockerfile -t zzsadmin:1.0 .
```
##### 镜像上传
```shell script
docker login --username=zhangzs registry.cn-hangzhou.aliyuncs.com
docker tag [ImageId] registry.cn-hangzhou.aliyuncs.com/zzszone/zzsadmin:[镜像版本号]
docker push registry.cn-hangzhou.aliyuncs.com/zzszone/zzsadmin:[镜像版本号]
```
docker tag 24fd8e6735e3 registry.cn-hangzhou.aliyuncs.com/zzszone/zzsadmin:1.0
docker push registry.cn-hangzhou.aliyuncs.com/zzszone/zzsadmin:1.0
##### 镜像拉取
```shell script
docker login --username=zhangzs registry.cn-hangzhou.aliyuncs.com
docker pull registry.cn-hangzhou.aliyuncs.com/zzszone/zzsadmin:[镜像版本号]
```
##### 创建容器
```shell script
docker run --name zzsadmin_service -d -p 5000:5000 registry.cn-hangzhou.aliyuncs.com/zzszone/zzsadmin:[镜像版本号]
```
docker pull registry.cn-hangzhou.aliyuncs.com/zzszone/zzsadmin:1.0
docker run --name zzsadmin -d -p 5000:5000 registry.cn-hangzhou.aliyuncs.com/zzszone/zzsadmin:1.0
