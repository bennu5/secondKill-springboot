### 整体思路
1. 前端请求
2. 相关信息放到MQ，响应请求
3. 服务方接收消息，查询 Redis 消费请求，记录结果
4. Redis 数据同步到数据库



### Redis
1. 安装
2. 修改 redis.conf (默认仅允许自己访问，不接受网络访问)
 bind 127.0.0.1 # 将这行代码注释，监听所有的ip地址，外网可以访问
 protected-mode no # 把yes改成no，允许外网访问
 daemonize yes # 把no改成yes，后台运行
 （阿里云注意开启网络端口配置）
3. 启动
cd src
./redis-server ../redis.conf

   