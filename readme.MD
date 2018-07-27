工程目录结构：
fangyuanyouyue
 - fangyuanyouyue-base					//基础工程，公共部分的东西
 - fangyuanyouyue-common
	- fangyuanyouyue-common-redis		//通用redis
	- fangyuanyouyue-common-rabbitmq		//通用rabbitmq
	- fangyuanyouyue-common-scheduler	//通用定时任务调度
	- fangyuanyouyue-common-message		//通用消息发送
 - fangyuanyouyue-microservice
	- fangyuanyouyue-microservice-user	//用户相关微服务
	- fangyuanyouyue-microservice-goods	//商品相关微服务
	- fangyuanyouyue-microservice-forum	//论坛相关微服务
 - fangyuanyouyue-server
	- fangyuanyouyue-server-eureka		//服务治理eureka
	- fangyuanyouyue-server-config		//配置服务，可能会废弃
	- fangyuanyouyue-server-zuul			//网关，用户授权放这里

	