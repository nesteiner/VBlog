package com.example.backend

import com.example.backend.model.*
import com.example.backend.repository.*
import com.example.backend.request.RegisterArticleRequest
import com.example.backend.request.RegisterCategoryRequest
import com.example.backend.request.RegisterRoleRequest
import com.example.backend.request.RegisterUserRequest
import com.example.backend.service.ArticleService
import com.example.backend.service.CategoryService
import com.example.backend.service.RoleService
import com.example.backend.service.UserService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.userdetails.UsernameNotFoundException
import java.sql.Timestamp
import java.time.LocalDateTime

@SpringBootTest
class BackendApplicationTests {
    @Autowired
    lateinit var roleService: RoleService
    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var tagRepository: TagRepository
    @Autowired
    lateinit var categoryRepository: CategoryRepository
    @Autowired
    lateinit var articleRepository: ArticleRepository
    @Autowired
    lateinit var articleService: ArticleService
    @Autowired
    lateinit var roleRepository: RoleRepository
    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var imageRepository: ImageItemRepository

    @Test
    fun clearAndInjectData() {
        articleRepository.deleteAll()
        userRepository.deleteAll()
        roleRepository.deleteAll()
        categoryRepository.deleteAll()
        tagRepository.deleteAll()
        imageRepository.deleteAll()

        val roles = listOf(
            Role(1L, "admin"),
            Role(2L, "student")
        )

        roleRepository.saveAll(roles)

        val categories = listOf(
            Category(1L, "default"),
            Category(2L, "hello"),
            Category(3L, "world")
        )

        categoryRepository.saveAll(categories)

        val tags = listOf(
            Tag(1L, "tag1"),
            Tag(2L, "tag2")
        )

        tagRepository.saveAll(tags)

        val images = listOf(
            ImageItem(1L, "扳手.png", "/home/steiner/workspace/VBlog/storage/0d832122-4416-427a-ae8e-d36f7f50e56f_20230401_扳手.png "),
            ImageItem(2L, "225默认头像.png", "/home/steiner/workspace/VBlog/storage/d12dbed5-959b-4d84-b0b9-4e65c9ce2637_20230401_225默认头像.png")
        )

        imageRepository.saveAll(images)

        val users = listOf(
            User(1L,
                "admin",
                "5f4dcc3b5aa765d61d8327deb882cf99",
                "steiner",
                true,
                "steiner3044@163.com",
                "http://localhost/api/image/download/1",
                Timestamp.valueOf(
                    LocalDateTime.now()),
                listOf(roles[0])
            ),

            User(1L,
                "hello",
                "5f4dcc3b5aa765d61d8327deb882cf99",
                "world",
                true,
                "steiner3044@163.com",
                "http://localhost/api/image/download/2",
                Timestamp.valueOf(
                    LocalDateTime.now()),
                listOf(roles[1])
            ),
        )

        userRepository.saveAll(users)

    }

    @Test
    fun injectRoles() {
        val roleRequests = listOf<RegisterRoleRequest>(
            RegisterRoleRequest("admin"),
            RegisterRoleRequest("user")
        )

        roleService.insertOne(roleRequests[0])
        roleService.insertOne(roleRequests[1])
    }

    @Test
    fun injectUsers() {
        var roleUser = roleService.findOne("user")
        var roleAdmin = roleService.findOne("admin")

        if (roleUser == null) {
            roleUser = roleService.insertOne(RegisterRoleRequest("user"))
        }

        if(roleAdmin == null) {
            roleAdmin = roleService.insertOne(RegisterRoleRequest("admin"))
        }

        val userHello = userService.findOne("hello")
        val userAdmin = userService.findOne("admin")

        if (userHello != null) {
            userHello.userface = "http://localhost/api/image/download/1"
            userHello.roles = listOf(roleUser)
            userService.updateOne(userHello)
        }

        if (userAdmin != null) {
            userAdmin.userface = "http://localhost/api/image/download/2"
            userAdmin.roles = listOf(roleAdmin)
            userService.updateOne(userAdmin)
        }

    }

    @Test
    fun injectTagsAndCategories() {
        val tags = listOf<Tag>(
            Tag(1L, "tag1"),
            Tag(2L, "tag2")
        )

        tagRepository.saveAll(tags)

        val categories = listOf<Category>(
            Category(1L, "hello"),
            Category(2L, "world")
        )

        categoryRepository.saveAll(categories)

    }
    @Test
    fun injectArticles() {
        val tags = listOf<Tag>(
            Tag(1L, "tag1"),
            Tag(2L, "tag2")
        )

        tagRepository.saveAll(tags)

        val categories = listOf<Category>(
            Category(1L, "hello"),
            Category(2L, "world")
        )

        categoryRepository.saveAll(categories)

        val userHello = userService.findOne("hello")!!
        val articles = listOf<RegisterArticleRequest>(
            RegisterArticleRequest(
                title = "Linux中安装zookeeper",
                markdownContent = "最近打算出一个系列，介绍Dubbo的使用。\\n\\n---\\n分布式应用现在已经越来越广泛，Spring Could也是一个不错的一站式解决方案，不过据我了解国内目前貌似使用阿里Dubbo的公司比较多，一方面这个框架也确实很OK，另一方面可能也是因为Dubbo的中文文档比较全的缘故，据Dubbo官网上的消息，阿里已经重新开始了对Dubbo的维护，这也算是使用Dubbo的互联网公司的福音了吧。OK，废话不多说，今天我们就先来看看如何在Linux上安装zookeeper。\\n\\n---\\n\\n了解过Dubbo的小伙伴都知道，Dubbo官方建议我们使用的注册中心就是zookeeper，zookeeper本来是Hadoop的一个子项目，现在发展成了Apache的顶级项目，看名字就知道Zookeeper就是动物园管理员，管理Hadoop(大象)、Hive(蜂房/蜜蜂)等动物。Apache上的Zookeeper分Linux版和Windows版，但是考虑到实际生产环境都是Linux，所以我们这里主要介绍Linux上Zookeeper的安装，Windows上Zookeeper的安装则比较简单，下载解压即可，和Tomcat差不多。\\n\\nOK，废话不多说，接下来我们就来看看zookeeper的安装步骤。\\n\\n---\\n环境：\\n>1.VMware® Workstation 12 Pro  \\n>2.CentOS7  \\n>3.zookeeper-3.4.10(本文写作时的最新稳定版)  \\n\\n---\\n# 安装步骤\\n1.下载zookeeper\\n\\nzookeeper下载地址如下，小伙伴们可以在第一个地址中选择适合自己的zookeeper版本，也可以直接点击第二个地址下载我们本文使用的zookeeper。\\n\\n>1.[http://mirrors.hust.edu.cn/apache/zookeeper/](http://mirrors.hust.edu.cn/apache/zookeeper/)  \\n\\n>2.[http://mirrors.hust.edu.cn/apache/zookeeper/zookeeper-3.4.10/zookeeper-3.4.10.tar.gz](http://mirrors.hust.edu.cn/apache/zookeeper/zookeeper-3.4.10/zookeeper-3.4.10.tar.gz)\\n\\n\\n\\n2.将下载好的zookeeper上传到Linux服务器上\\n\\n上传方式多种多样，我这里采用了xftp，小伙伴们也可以直接使用putty上传，上传结果如下：\\n![这里写图片描述](http://img.blog.csdn.net/20170825114622362?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)\\n\\n3.将文件解压到/opt目录下  \\n\\n![这里写图片描述](http://img.blog.csdn.net/20170825115122378?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)\\n\\n4.进入到刚刚解压好的目录中，创建两个文件夹，分别是data和logs，如下：\\n\\n![这里写图片描述](http://img.blog.csdn.net/20170825115324970?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)\\n\\n5.将解压后zookeeper-3.4.10文件夹下的zoo_sample.cfg文件拷贝一份命名为zoo.cfg，如下：\\n\\n![这里写图片描述](http://img.blog.csdn.net/20170825115426251?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)\\n\\n6.修改zoo.cfg文件，添加data和log目录，如下：\\n\\n![这里写图片描述](http://img.blog.csdn.net/20170825115527367?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)\\n\\n>1.2888 端口号是zookeeper服务之间通信的端口   \\n>2.3888 是zookeeper 与其他应用程序通信的端口  \\n>3.initLimit：这个配置项是用来配置 Zookeeper 接受客户端（这里所说的客户端不是用户连接 Zookeeper服务器的客户端，而是 Zookeeper 服务器集群中连接到 Leader 的 Follower 服务器）初始化连接时最长能忍受多少个心跳时间间隔数。当已经超过 10 个心跳的时间（也就是 tickTime）长度后 Zookeeper 服务器还没有收到客户端的返回信息，那么表明这个客户端连接失败。总的时间长度就是 5*2000=10 秒。  \\n>4.syncLimit：这个配置项标识 Leader 与 Follower 之间发送消息，请求和应答时间长度，最长不能超过多少个 tickTime 的时间长度，总的时间长度就是 2*2000=4 秒  \\n>5.server.A=B:C:D：其中 A 是一个数字，表示这个是第几号服务器；B 是这个服务器的IP地址或/etc/hosts文件中映射了IP的主机名；C 表示的是这个服务器与集群中的 Leader 服务器交换信息的端口；D 表示的是万一集群中的 Leader 服务器挂了，需要一个端口来重新进行选举，选出一个新的 Leader，而这个端口就是用来执行选举时服务器相互通信的端口。如果是伪集群的配置方式，由于 B 都是一样，所以不同的 Zookeeper 实例通信端口号不能一样，所以要给它们分配不同的端口号。\\n\\n7.在 dataDir=/opt/zookeeper-3.4.10/data下创建 myid文件 编辑myid文件，并在对应的IP的机器上输入对应的编号。如在zookeeper上，myid文件内容就是1。如果只在单点上进行安装配置，那么只有一个server.1。如下：\\n\\n![这里写图片描述](http://img.blog.csdn.net/20170825115647920?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)\\n\\n8.在.bash_profile文件中增加zookeeper配置：\\n\\n![这里写图片描述](http://img.blog.csdn.net/20170825115729473?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)\\n\\n9.使配置生效\\n\\n![这里写图片描述](http://img.blog.csdn.net/20170825115807787?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)\\n\\n10.关闭防火墙\\n\\n![这里写图片描述](http://img.blog.csdn.net/20170825115848488?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)\\n\\n11.启动并测试\\n\\n![这里写图片描述](http://img.blog.csdn.net/20170825115938795?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)\\n\\n>启动之后如果能看到Mode:standalone就表示启动成功了。\\n\\n12.关闭zookeeper\\n\\n![这里写图片描述](http://img.blog.csdn.net/20170825121021364?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)\\n\\n13.配置开机启动zookeeper\\n\\n![这里写图片描述](http://img.blog.csdn.net/20170825121059827?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)\\n\\n>**注意注意** 在centos7中，/etc/rc.local的权限被降低了，所以需要执行如下命令赋予其可执行权限\\n```chmod +x /etc/rc.d/rc.local```\\n\\n\\nOK,以上就是我们在CentOS7中安装zookeeper的全过程，做好这一切之后我们就可以在Dubbo中使用这个注册中心了，这个我们放在下一篇博客中介绍。\\n\\n更多JavaEE资料请关注公众号：\\n\\n![](http://img.blog.csdn.net/20170823174820001?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)",
                htmlContent = "<p>最近打算出一个系列，介绍Dubbo的使用。</p>\\n<hr />\\n<p>分布式应用现在已经越来越广泛，Spring Could也是一个不错的一站式解决方案，不过据我了解国内目前貌似使用阿里Dubbo的公司比较多，一方面这个框架也确实很OK，另一方面可能也是因为Dubbo的中文文档比较全的缘故，据Dubbo官网上的消息，阿里已经重新开始了对Dubbo的维护，这也算是使用Dubbo的互联网公司的福音了吧。OK，废话不多说，今天我们就先来看看如何在Linux上安装zookeeper。</p>\\n<hr />\\n<p>了解过Dubbo的小伙伴都知道，Dubbo官方建议我们使用的注册中心就是zookeeper，zookeeper本来是Hadoop的一个子项目，现在发展成了Apache的顶级项目，看名字就知道Zookeeper就是动物园管理员，管理Hadoop(大象)、Hive(蜂房/蜜蜂)等动物。Apache上的Zookeeper分Linux版和Windows版，但是考虑到实际生产环境都是Linux，所以我们这里主要介绍Linux上Zookeeper的安装，Windows上Zookeeper的安装则比较简单，下载解压即可，和Tomcat差不多。</p>\\n<p>OK，废话不多说，接下来我们就来看看zookeeper的安装步骤。</p>\\n<hr />\\n<p>环境：</p>\\n<blockquote>\\n<p>1.VMware® Workstation 12 Pro<br />\\n2.CentOS7<br />\\n3.zookeeper-3.4.10(本文写作时的最新稳定版)</p>\\n</blockquote>\\n<hr />\\n<h1>安装步骤</h1>\\n<p>1.下载zookeeper</p>\\n<p>zookeeper下载地址如下，小伙伴们可以在第一个地址中选择适合自己的zookeeper版本，也可以直接点击第二个地址下载我们本文使用的zookeeper。</p>\\n<blockquote>\\n<p>1.<a href=\\\"http://mirrors.hust.edu.cn/apache/zookeeper/\\\" target=\\\"_blank\\\">http://mirrors.hust.edu.cn/apache/zookeeper/</a></p>\\n</blockquote>\\n<blockquote>\\n<p>2.<a href=\\\"http://mirrors.hust.edu.cn/apache/zookeeper/zookeeper-3.4.10/zookeeper-3.4.10.tar.gz\\\" target=\\\"_blank\\\">http://mirrors.hust.edu.cn/apache/zookeeper/zookeeper-3.4.10/zookeeper-3.4.10.tar.gz</a></p>\\n</blockquote>\\n<p>2.将下载好的zookeeper上传到Linux服务器上</p>\\n<p>上传方式多种多样，我这里采用了xftp，小伙伴们也可以直接使用putty上传，上传结果如下：<br />\\n<img src=\\\"http://img.blog.csdn.net/20170825114622362?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast\\\" alt=\\\"这里写图片描述\\\" /></p>\\n<p>3.将文件解压到/opt目录下</p>\\n<p><img src=\\\"http://img.blog.csdn.net/20170825115122378?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast\\\" alt=\\\"这里写图片描述\\\" /></p>\\n<p>4.进入到刚刚解压好的目录中，创建两个文件夹，分别是data和logs，如下：</p>\\n<p><img src=\\\"http://img.blog.csdn.net/20170825115324970?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast\\\" alt=\\\"这里写图片描述\\\" /></p>\\n<p>5.将解压后zookeeper-3.4.10文件夹下的zoo_sample.cfg文件拷贝一份命名为zoo.cfg，如下：</p>\\n<p><img src=\\\"http://img.blog.csdn.net/20170825115426251?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast\\\" alt=\\\"这里写图片描述\\\" /></p>\\n<p>6.修改zoo.cfg文件，添加data和log目录，如下：</p>\\n<p><img src=\\\"http://img.blog.csdn.net/20170825115527367?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast\\\" alt=\\\"这里写图片描述\\\" /></p>\\n<blockquote>\\n<p>1.2888 端口号是zookeeper服务之间通信的端口<br />\\n2.3888 是zookeeper 与其他应用程序通信的端口<br />\\n3.initLimit：这个配置项是用来配置 Zookeeper 接受客户端（这里所说的客户端不是用户连接 Zookeeper服务器的客户端，而是 Zookeeper 服务器集群中连接到 Leader 的 Follower 服务器）初始化连接时最长能忍受多少个心跳时间间隔数。当已经超过 10 个心跳的时间（也就是 tickTime）长度后 Zookeeper 服务器还没有收到客户端的返回信息，那么表明这个客户端连接失败。总的时间长度就是 5<em>2000=10 秒。<br />\\n4.syncLimit：这个配置项标识 Leader 与 Follower 之间发送消息，请求和应答时间长度，最长不能超过多少个 tickTime 的时间长度，总的时间长度就是 2</em>2000=4 秒<br />\\n5.server.A=B:C:D：其中 A 是一个数字，表示这个是第几号服务器；B 是这个服务器的IP地址或/etc/hosts文件中映射了IP的主机名；C 表示的是这个服务器与集群中的 Leader 服务器交换信息的端口；D 表示的是万一集群中的 Leader 服务器挂了，需要一个端口来重新进行选举，选出一个新的 Leader，而这个端口就是用来执行选举时服务器相互通信的端口。如果是伪集群的配置方式，由于 B 都是一样，所以不同的 Zookeeper 实例通信端口号不能一样，所以要给它们分配不同的端口号。</p>\\n</blockquote>\\n<p>7.在 dataDir=/opt/zookeeper-3.4.10/data下创建 myid文件 编辑myid文件，并在对应的IP的机器上输入对应的编号。如在zookeeper上，myid文件内容就是1。如果只在单点上进行安装配置，那么只有一个server.1。如下：</p>\\n<p><img src=\\\"http://img.blog.csdn.net/20170825115647920?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast\\\" alt=\\\"这里写图片描述\\\" /></p>\\n<p>8.在.bash_profile文件中增加zookeeper配置：</p>\\n<p><img src=\\\"http://img.blog.csdn.net/20170825115729473?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast\\\" alt=\\\"这里写图片描述\\\" /></p>\\n<p>9.使配置生效</p>\\n<p><img src=\\\"http://img.blog.csdn.net/20170825115807787?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast\\\" alt=\\\"这里写图片描述\\\" /></p>\\n<p>10.关闭防火墙</p>\\n<p><img src=\\\"http://img.blog.csdn.net/20170825115848488?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast\\\" alt=\\\"这里写图片描述\\\" /></p>\\n<p>11.启动并测试</p>\\n<p><img src=\\\"http://img.blog.csdn.net/20170825115938795?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast\\\" alt=\\\"这里写图片描述\\\" /></p>\\n<blockquote>\\n<p>启动之后如果能看到Mode:standalone就表示启动成功了。</p>\\n</blockquote>\\n<p>12.关闭zookeeper</p>\\n<p><img src=\\\"http://img.blog.csdn.net/20170825121021364?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast\\\" alt=\\\"这里写图片描述\\\" /></p>\\n<p>13.配置开机启动zookeeper</p>\\n<p><img src=\\\"http://img.blog.csdn.net/20170825121059827?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast\\\" alt=\\\"这里写图片描述\\\" /></p>\\n<blockquote>\\n<p><strong>注意注意</strong> 在centos7中，/etc/rc.local的权限被降低了，所以需要执行如下命令赋予其可执行权限<br />\\n<code>chmod +x /etc/rc.d/rc.local</code></p>\\n</blockquote>\\n<p>OK,以上就是我们在CentOS7中安装zookeeper的全过程，做好这一切之后我们就可以在Dubbo中使用这个注册中心了，这个我们放在下一篇博客中介绍。</p>\\n<p>更多JavaEE资料请关注公众号：</p>\\n<p><img src=\\\"http://img.blog.csdn.net/20170823174820001?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast\\\" alt=\\\"\\\" /></p>\\n",
                summary = "最近打算出一个系列，介绍Dubbo的使用。\\n\\n分布式应用现在已经越来越广泛，Spring Could",
                category = categories[0],
                state = Article.PUBLISHED,
                tags = tags
            ),

            RegisterArticleRequest(
                title = "Ajax上传图片以及上传之前先预览",
                markdownContent = "手头上有几个小项目用到了easyUI，一开始决定使用easyUI就注定了项目整体上前后端分离，基本上所有的请求都采用Ajax来完成。在文件上传的时候用到了Ajax上传文件，以及图片在上传之前的预览效果，解决了这两个小问题，和小伙伴们分享下。\\n\\n---\\n# 上传之前的预览\\n\\n## 方式一\\n先来说说图片上传之前的预览问题。这里主要采用了HTML5中的FileReader对象来实现，关于FileReader对象，如果小伙伴们不了解，可以查看这篇博客[HTML5学习之FileReader接口](http://blog.csdn.net/zk437092645/article/details/8745647/)。我们来看看实现方式：\\n```\\n<!DOCTYPE html>\\n<html lang=\\\"en\\\">\\n<head>\\n    <meta charset=\\\"UTF-8\\\">\\n    <title>Ajax上传文件</title>\\n    <script src=\\\"jquery-3.2.1.js\\\"></script>\\n</head>\\n<body>\\n用户名：<input id=\\\"username\\\" type=\\\"text\\\"><br>\\n用户图像：<input id=\\\"userface\\\" type=\\\"file\\\" onchange=\\\"preview(this)\\\"><br>\\n<div id=\\\"preview\\\"></div>\\n<input type=\\\"button\\\" id=\\\"btnClick\\\" value=\\\"上传\\\">\\n<script>\\n    \$(\\\"#btnClick\\\").click(function () {\\n        var formData = new FormData();\\n        formData.append(\\\"username\\\", \$(\\\"#username\\\").val());\\n        formData.append(\\\"file\\\", \$(\\\"#userface\\\")[0].files[0]);\\n        \$.ajax({\\n            url: \\'/fileupload\\',\\n            type: \\'post\\',\\n            data: formData,\\n            processData: false,\\n            contentType: false,\\n            success: function (msg) {\\n                alert(msg);\\n            }\\n        });\\n    });\\n    function preview(file) {\\n        var prevDiv = document.getElementById(\\'preview\\');\\n        if (file.files && file.files[0]) {\\n            var reader = new FileReader();\\n            reader.onload = function (evt) {\\n                prevDiv.innerHTML = \\'<img src=\\\"\\' + evt.target.result + \\'\\\" />\\';\\n            }\\n            reader.readAsDataURL(file.files[0]);\\n        } else {\\n            prevDiv.innerHTML = \\'<div class=\\\"img\\\" style=\\\"filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale,src=\\\\\\'\\' + file.value + \\'\\\\\\'\\\"></div>\\';\\n        }\\n    }\\n</script>\\n</body>\\n</html>\\n```\\n\\n这里对于支持FileReader的浏览器直接使用FileReader来实现，不支持FileReader的浏览器则采用微软的滤镜来实现（注意给图片上传的input标签设置onchange函数）。\\n实现效果如下：\\n![这里写图片描述](http://img.blog.csdn.net/20170825184056537?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)\\n\\n\\n## 方式二\\n\\n除了这种方式之外我们也可以采用网上现成的一个jQuery实现的方式。这里主要参考了[这里](http://keleyi.com/keleyi/phtml/image/16.htm)。\\n不过由于原文年代久远，里边使用的```\$.browser.msie```从jQuery1.9就被移除掉了，所以如果我们想使用这个得做一点额外的处理，我修改后的uploadPreview.js文件内容如下：\\n```\\njQuery.browser={};(function(){jQuery.browser.msie=false; jQuery.browser.version=0;if(navigator.userAgent.match(/MSIE ([0-9]+)./)){ jQuery.browser.msie=true;jQuery.browser.version=RegExp.\$1;}})();\\njQuery.fn.extend({\\n    uploadPreview: function (opts) {\\n        var _self = this,\\n            _this = \$(this);\\n        opts = jQuery.extend({\\n            Img: \\\"ImgPr\\\",\\n            Width: 100,\\n            Height: 100,\\n            ImgType: [\\\"gif\\\", \\\"jpeg\\\", \\\"jpg\\\", \\\"bmp\\\", \\\"png\\\"],\\n            Callback: function () {}\\n        }, opts || {});\\n        _self.getObjectURL = function (file) {\\n            var url = null;\\n            if (window.createObjectURL != undefined) {\\n                url = window.createObjectURL(file)\\n            } else if (window.URL != undefined) {\\n                url = window.URL.createObjectURL(file)\\n            } else if (window.webkitURL != undefined) {\\n                url = window.webkitURL.createObjectURL(file)\\n            }\\n            return url\\n        };\\n        _this.change(function () {\\n            if (this.value) {\\n                if (!RegExp(\\\"\\\\.(\\\" + opts.ImgType.join(\\\"|\\\") + \\\")\$\\\", \\\"i\\\").test(this.value.toLowerCase())) {\\n                    alert(\\\"选择文件错误,图片类型必须是\\\" + opts.ImgType.join(\\\"，\\\") + \\\"中的一种\\\");\\n                    this.value = \\\"\\\";\\n                    return false\\n                }\\n                if (\$.browser.msie) {\\n                    try {\\n                        \$(\\\"#\\\" + opts.Img).attr(\\'src\\', _self.getObjectURL(this.files[0]))\\n                    } catch (e) {\\n                        var src = \\\"\\\";\\n                        var obj = \$(\\\"#\\\" + opts.Img);\\n                        var div = obj.parent(\\\"div\\\")[0];\\n                        _self.select();\\n                        if (top != self) {\\n                            window.parent.document.body.focus()\\n                        } else {\\n                            _self.blur()\\n                        }\\n                        src = document.selection.createRange().text;\\n                        document.selection.empty();\\n                        obj.hide();\\n                        obj.parent(\\\"div\\\").css({\\n                            \\'filter\\': \\'progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)\\',\\n                            \\'width\\': opts.Width + \\'px\\',\\n                            \\'height\\': opts.Height + \\'px\\'\\n                        });\\n                        div.filters.item(\\\"DXImageTransform.Microsoft.AlphaImageLoader\\\").src = src\\n                    }\\n                } else {\\n                    \$(\\\"#\\\" + opts.Img).attr(\\'src\\', _self.getObjectURL(this.files[0]))\\n                }\\n                opts.Callback()\\n            }\\n        })\\n    }\\n});\\n```\\n\\n然后在我们的html文件中引入这个js文件即可：\\n```\\n<!DOCTYPE html>\\n<html lang=\\\"en\\\">\\n<head>\\n    <meta charset=\\\"UTF-8\\\">\\n    <title>Ajax上传文件</title>\\n    <script src=\\\"jquery-3.2.1.js\\\"></script>\\n    <script src=\\\"uploadPreview.js\\\"></script>\\n</head>\\n<body>\\n用户名：<input id=\\\"username\\\" type=\\\"text\\\"><br>\\n用户图像：<input id=\\\"userface\\\" type=\\\"file\\\" onchange=\\\"preview(this)\\\"><br>\\n<div><img id=\\\"ImgPr\\\" width=\\\"200\\\" height=\\\"200\\\"/></div>\\n<input type=\\\"button\\\" id=\\\"btnClick\\\" value=\\\"上传\\\">\\n<script>\\n    \$(\\\"#btnClick\\\").click(function () {\\n        var formData = new FormData();\\n        formData.append(\\\"username\\\", \$(\\\"#username\\\").val());\\n        formData.append(\\\"file\\\", \$(\\\"#userface\\\")[0].files[0]);\\n        \$.ajax({\\n            url: \\'/fileupload\\',\\n            type: \\'post\\',\\n            data: formData,\\n            processData: false,\\n            contentType: false,\\n            success: function (msg) {\\n                alert(msg);\\n            }\\n        });\\n    });\\n    \$(\\\"#userface\\\").uploadPreview({Img: \\\"ImgPr\\\", Width: 120, Height: 120});\\n</script>\\n</body>\\n</html>\\n```\\n\\n这里有如下几点需要注意：\\n>1.HTML页面中要引入我们自定义的uploadPreview.js文件\\n>2.预先定义好要显示预览图片的img标签，该标签要有id。\\n>3.查找到img标签然后调用uploadPreview函数\\n\\n执行效果如下：  \\n![这里写图片描述](http://img.blog.csdn.net/20170825190203757?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)\\n\\n# Ajax上传图片文件\\n\\nAjax上传图片文件就简单了，没有那么多方案，核心代码如下：\\n```\\n        var formData = new FormData();\\n        formData.append(\\\"username\\\", \$(\\\"#username\\\").val());\\n        formData.append(\\\"file\\\", \$(\\\"#userface\\\")[0].files[0]);\\n        \$.ajax({\\n            url: \\'/fileupload\\',\\n            type: \\'post\\',\\n            data: formData,\\n            processData: false,\\n            contentType: false,\\n            success: function (msg) {\\n                alert(msg);\\n            }\\n        });\\n```\\n核心就是定义一个FormData对象，将要上传的数据包装到这个对象中去。然后在ajax上传数据的时候设置data属性就为formdata，processData属性设置为false，表示jQuery不要去处理发送的数据，然后设置contentType属性的值为false，表示不要设置请求头的contentType属性。OK，主要就是设置这三个，设置成功之后，其他的处理就和常规的ajax用法一致了。\\n\\n后台的处理代码大家可以在文末的案例中下载，这里我就不展示不出来了。\\n\\nOK，以上就是我们对Ajax上传图片以及图片预览的一个简介，有问题的小伙伴欢迎留言讨论。\\n\\n案例下载地址[http://download.csdn.net/download/u012702547/9950813](http://download.csdn.net/download/u012702547/9950813)\\n\\n由于CSDN下载现在必须要积分，不得已设置了1分，如果小伙伴没有积分，文末留言我发给你。\\n\\n以上。\\n",
                htmlContent = "<p>手头上有几个小项目用到了easyUI，一开始决定使用easyUI就注定了项目整体上前后端分离，基本上所有的请求都采用Ajax来完成。在文件上传的时候用到了Ajax上传文件，以及图片在上传之前的预览效果，解决了这两个小问题，和小伙伴们分享下。</p>\\n<hr />\\n<h1>上传之前的预览</h1>\\n<h2>方式一</h2>\\n<p>先来说说图片上传之前的预览问题。这里主要采用了HTML5中的FileReader对象来实现，关于FileReader对象，如果小伙伴们不了解，可以查看这篇博客<a href=\\\"http://blog.csdn.net/zk437092645/article/details/8745647/\\\" target=\\\"_blank\\\">HTML5学习之FileReader接口</a>。我们来看看实现方式：</p>\\n<pre><code class=\\\"lang-\\\">&lt;!DOCTYPE html&gt;\\n&lt;html lang=&quot;en&quot;&gt;\\n&lt;head&gt;\\n    &lt;meta charset=&quot;UTF-8&quot;&gt;\\n    &lt;title&gt;Ajax上传文件&lt;/title&gt;\\n    &lt;script src=&quot;jquery-3.2.1.js&quot;&gt;&lt;/script&gt;\\n&lt;/head&gt;\\n&lt;body&gt;\\n用户名：&lt;input id=&quot;username&quot; type=&quot;text&quot;&gt;&lt;br&gt;\\n用户图像：&lt;input id=&quot;userface&quot; type=&quot;file&quot; onchange=&quot;preview(this)&quot;&gt;&lt;br&gt;\\n&lt;div id=&quot;preview&quot;&gt;&lt;/div&gt;\\n&lt;input type=&quot;button&quot; id=&quot;btnClick&quot; value=&quot;上传&quot;&gt;\\n&lt;script&gt;\\n    \$(&quot;#btnClick&quot;).click(function () {\\n        var formData = new FormData();\\n        formData.append(&quot;username&quot;, \$(&quot;#username&quot;).val());\\n        formData.append(&quot;file&quot;, \$(&quot;#userface&quot;)[0].files[0]);\\n        \$.ajax({\\n            url: \\'/fileupload\\',\\n            type: \\'post\\',\\n            data: formData,\\n            processData: false,\\n            contentType: false,\\n            success: function (msg) {\\n                alert(msg);\\n            }\\n        });\\n    });\\n    function preview(file) {\\n        var prevDiv = document.getElementById(\\'preview\\');\\n        if (file.files &amp;&amp; file.files[0]) {\\n            var reader = new FileReader();\\n            reader.onload = function (evt) {\\n                prevDiv.innerHTML = \\'&lt;img src=&quot;\\' + evt.target.result + \\'&quot; /&gt;\\';\\n            }\\n            reader.readAsDataURL(file.files[0]);\\n        } else {\\n            prevDiv.innerHTML = \\'&lt;div class=&quot;img&quot; style=&quot;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale,src=\\\\\\'\\' + file.value + \\'\\\\\\'&quot;&gt;&lt;/div&gt;\\';\\n        }\\n    }\\n&lt;/script&gt;\\n&lt;/body&gt;\\n&lt;/html&gt;\\n</code></pre>\\n<p>这里对于支持FileReader的浏览器直接使用FileReader来实现，不支持FileReader的浏览器则采用微软的滤镜来实现（注意给图片上传的input标签设置onchange函数）。<br />\\n实现效果如下：<br />\\n<img src=\\\"http://img.blog.csdn.net/20170825184056537?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast\\\" alt=\\\"这里写图片描述\\\" /></p>\\n<h2>方式二</h2>\\n<p>除了这种方式之外我们也可以采用网上现成的一个jQuery实现的方式。这里主要参考了<a href=\\\"http://keleyi.com/keleyi/phtml/image/16.htm\\\" target=\\\"_blank\\\">这里</a>。<br />\\n不过由于原文年代久远，里边使用的<code>\$.browser.msie</code>从jQuery1.9就被移除掉了，所以如果我们想使用这个得做一点额外的处理，我修改后的uploadPreview.js文件内容如下：</p>\\n<pre><code class=\\\"lang-\\\">jQuery.browser={};(function(){jQuery.browser.msie=false; jQuery.browser.version=0;if(navigator.userAgent.match(/MSIE ([0-9]+)./)){ jQuery.browser.msie=true;jQuery.browser.version=RegExp.\$1;}})();\\njQuery.fn.extend({\\n    uploadPreview: function (opts) {\\n        var _self = this,\\n            _this = \$(this);\\n        opts = jQuery.extend({\\n            Img: &quot;ImgPr&quot;,\\n            Width: 100,\\n            Height: 100,\\n            ImgType: [&quot;gif&quot;, &quot;jpeg&quot;, &quot;jpg&quot;, &quot;bmp&quot;, &quot;png&quot;],\\n            Callback: function () {}\\n        }, opts || {});\\n        _self.getObjectURL = function (file) {\\n            var url = null;\\n            if (window.createObjectURL != undefined) {\\n                url = window.createObjectURL(file)\\n            } else if (window.URL != undefined) {\\n                url = window.URL.createObjectURL(file)\\n            } else if (window.webkitURL != undefined) {\\n                url = window.webkitURL.createObjectURL(file)\\n            }\\n            return url\\n        };\\n        _this.change(function () {\\n            if (this.value) {\\n                if (!RegExp(&quot;\\\\.(&quot; + opts.ImgType.join(&quot;|&quot;) + &quot;)\$&quot;, &quot;i&quot;).test(this.value.toLowerCase())) {\\n                    alert(&quot;选择文件错误,图片类型必须是&quot; + opts.ImgType.join(&quot;，&quot;) + &quot;中的一种&quot;);\\n                    this.value = &quot;&quot;;\\n                    return false\\n                }\\n                if (\$.browser.msie) {\\n                    try {\\n                        \$(&quot;#&quot; + opts.Img).attr(\\'src\\', _self.getObjectURL(this.files[0]))\\n                    } catch (e) {\\n                        var src = &quot;&quot;;\\n                        var obj = \$(&quot;#&quot; + opts.Img);\\n                        var div = obj.parent(&quot;div&quot;)[0];\\n                        _self.select();\\n                        if (top != self) {\\n                            window.parent.document.body.focus()\\n                        } else {\\n                            _self.blur()\\n                        }\\n                        src = document.selection.createRange().text;\\n                        document.selection.empty();\\n                        obj.hide();\\n                        obj.parent(&quot;div&quot;).css({\\n                            \\'filter\\': \\'progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)\\',\\n                            \\'width\\': opts.Width + \\'px\\',\\n                            \\'height\\': opts.Height + \\'px\\'\\n                        });\\n                        div.filters.item(&quot;DXImageTransform.Microsoft.AlphaImageLoader&quot;).src = src\\n                    }\\n                } else {\\n                    \$(&quot;#&quot; + opts.Img).attr(\\'src\\', _self.getObjectURL(this.files[0]))\\n                }\\n                opts.Callback()\\n            }\\n        })\\n    }\\n});\\n</code></pre>\\n<p>然后在我们的html文件中引入这个js文件即可：</p>\\n<pre><code class=\\\"lang-\\\">&lt;!DOCTYPE html&gt;\\n&lt;html lang=&quot;en&quot;&gt;\\n&lt;head&gt;\\n    &lt;meta charset=&quot;UTF-8&quot;&gt;\\n    &lt;title&gt;Ajax上传文件&lt;/title&gt;\\n    &lt;script src=&quot;jquery-3.2.1.js&quot;&gt;&lt;/script&gt;\\n    &lt;script src=&quot;uploadPreview.js&quot;&gt;&lt;/script&gt;\\n&lt;/head&gt;\\n&lt;body&gt;\\n用户名：&lt;input id=&quot;username&quot; type=&quot;text&quot;&gt;&lt;br&gt;\\n用户图像：&lt;input id=&quot;userface&quot; type=&quot;file&quot; onchange=&quot;preview(this)&quot;&gt;&lt;br&gt;\\n&lt;div&gt;&lt;img id=&quot;ImgPr&quot; width=&quot;200&quot; height=&quot;200&quot;/&gt;&lt;/div&gt;\\n&lt;input type=&quot;button&quot; id=&quot;btnClick&quot; value=&quot;上传&quot;&gt;\\n&lt;script&gt;\\n    \$(&quot;#btnClick&quot;).click(function () {\\n        var formData = new FormData();\\n        formData.append(&quot;username&quot;, \$(&quot;#username&quot;).val());\\n        formData.append(&quot;file&quot;, \$(&quot;#userface&quot;)[0].files[0]);\\n        \$.ajax({\\n            url: \\'/fileupload\\',\\n            type: \\'post\\',\\n            data: formData,\\n            processData: false,\\n            contentType: false,\\n            success: function (msg) {\\n                alert(msg);\\n            }\\n        });\\n    });\\n    \$(&quot;#userface&quot;).uploadPreview({Img: &quot;ImgPr&quot;, Width: 120, Height: 120});\\n&lt;/script&gt;\\n&lt;/body&gt;\\n&lt;/html&gt;\\n</code></pre>\\n<p>这里有如下几点需要注意：</p>\\n<blockquote>\\n<p>1.HTML页面中要引入我们自定义的uploadPreview.js文件<br />\\n2.预先定义好要显示预览图片的img标签，该标签要有id。<br />\\n3.查找到img标签然后调用uploadPreview函数</p>\\n</blockquote>\\n<p>执行效果如下：<br />\\n<img src=\\\"http://img.blog.csdn.net/20170825190203757?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast\\\" alt=\\\"这里写图片描述\\\" /></p>\\n<h1>Ajax上传图片文件</h1>\\n<p>Ajax上传图片文件就简单了，没有那么多方案，核心代码如下：</p>\\n<pre><code class=\\\"lang-\\\">        var formData = new FormData();\\n        formData.append(&quot;username&quot;, \$(&quot;#username&quot;).val());\\n        formData.append(&quot;file&quot;, \$(&quot;#userface&quot;)[0].files[0]);\\n        \$.ajax({\\n            url: \\'/fileupload\\',\\n            type: \\'post\\',\\n            data: formData,\\n            processData: false,\\n            contentType: false,\\n            success: function (msg) {\\n                alert(msg);\\n            }\\n        });\\n</code></pre>\\n<p>核心就是定义一个FormData对象，将要上传的数据包装到这个对象中去。然后在ajax上传数据的时候设置data属性就为formdata，processData属性设置为false，表示jQuery不要去处理发送的数据，然后设置contentType属性的值为false，表示不要设置请求头的contentType属性。OK，主要就是设置这三个，设置成功之后，其他的处理就和常规的ajax用法一致了。</p>\\n<p>后台的处理代码大家可以在文末的案例中下载，这里我就不展示不出来了。</p>\\n<p>OK，以上就是我们对Ajax上传图片以及图片预览的一个简介，有问题的小伙伴欢迎留言讨论。</p>\\n<p>案例下载地址<a href=\\\"http://download.csdn.net/download/u012702547/9950813\\\" target=\\\"_blank\\\">http://download.csdn.net/download/u012702547/9950813</a></p>\\n<p>由于CSDN下载现在必须要积分，不得已设置了1分，如果小伙伴没有积分，文末留言我发给你。</p>\\n<p>以上。</p>\\n",
                summary = "手头上有几个小项目用到了easyUI，一开始决定使用easyUI就注定了项目整体上前后端分离，基本上",
                category = categories[0],
                state = Article.PUBLISHED,
                tags = tags
            )
        )

        articles.forEach { articleService.insertOne(it) }
    }

    @Test
    fun testFind() {
        val userid = userService.findOne("hello")!!.id!!
        val state = Article.PUBLISHED
        val pageable = PageRequest.of(0, 6)
        articleRepository.findAllByState(state, userid, pageable).content.forEach {
            println(it.id)
        }

//        articleRepository.findTest(state, userid).forEach {
//            println(it)
//        }
    }

    @Test
    fun testThrow() {
        try {
            throw UsernameNotFoundException("user name not found")
        } catch (exception: UsernameNotFoundException) {
            println(exception.message)
        }
    }
}
