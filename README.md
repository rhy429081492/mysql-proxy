### Java Spring template project

This project is based on a GitLab [Project Template](https://docs.gitlab.com/ee/gitlab-basics/create-project.html).

Improvements can be proposed in the [original project](https://gitlab.com/gitlab-org/project-templates/spring).

### CI/CD with Auto DevOps

This template is compatible with [Auto DevOps](https://docs.gitlab.com/ee/topics/autodevops/).

If Auto DevOps is not already enabled for this project, you can [turn it on](https://docs.gitlab.com/ee/topics/autodevops/#enabling-auto-devops) in the project settings.

#说明
代理服务器端采用反射调用目标函数
service包内包含两个服务
Service 服务器端口监听服务  用于监听客户端使用驱动发出的报文
ProxyServer 数据库代理服务
代理服务：
一，C3P0连接池的配置 ProxyCore
    配置文件 c3p0-service.xml
    C3P0连接池采用按名配置的方式，首先使用dom4j解析配置文件，获取配置数据库的
    名称与验证信息（database，user，password）
    使用C3P0根据获取的名称解析配置文件。
二，连接请求队列Allocation
    采用先请求先分配的策略分配连接池连接。每个连接池都有对应的唯一队列。
三，客户端连接池ConnectionSet
    存储所有的客户端连接镜像，客户端请求时，根据其线程序号，从池中取出对应镜像操作。
    数据库连接镜像：NioConnection
    包含 唯一的Connection对象和若干Statement等数据库对象与客户端形成一一映射关系
    连接超时问题，在NioConnection中对该对象的最后一次使用有时间记录，并有独立的线程对该时间进行检测，当连接长时间未使用就会被自动关闭。
四，报文
    ClientPacket 驱动-->服务器代理
    ServerPacket 服务器代理-->驱动
    UrlInfo 解析报文中的 URL
五，反射处理
    Deal为核心处理类
    Executor为处理接口
    不同的数据库连接对象的动作解析交由不同的处理对象进行处理
    Deal负责将处理完后的消息打包发回
    exchange包作为反射处理的辅助包
    由于报文中存在，null、包装类的问题，在进行反射调用方法时，需要进行修正
 