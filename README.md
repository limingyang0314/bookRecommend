# bookRecommend
Training project in Northeast software company  
基本结构  
-src  
--main  
---java  
----org  
-----mySpringWeb  
------backend 后端业务  
-------controller 控制器  
-------middleware 中间件  
-------model 模型  
-------service 服务  
  
注意事项：  
1.本项目采用SpringBoot框架编写。  
2.数据返回的标准格式需使用org.mySpringWeb.backend.middleware.ReturnFormat.format()方法处理。  
3.如使用Redis，请阅读org.mySpringWeb.backend.service.RedisService类，含有对所有Redis操作的封装。  
4.目前登陆验证采用Token验证机制，登陆成功后会返回Token和过期时间，后续将加入Session机制。  
