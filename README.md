# smart-nginx
集中管理nginx配置文件与服务状态



#软件定位
    为中小企业提供简单、方便、快捷的,集中式Nginx管理平台
    出发点：越来越多的项目，测试或开发环境通过一台机器进行Nginx反向代理，映射到外网，
          同时项目对外销售并负责运维，各种配置，各种环境，各种地址，数量多，重复性工作较多
          各种转发规则（域名、端口、路径）较为混乱


#环境与启动
    本文使用maven管理，spring boot项目，启动比较简单。
    ide中直接main运行NginxManagerApplication
    命令行启动：    java -jar xxx.jar
    或后台模式：    nohup java -jar xxx.jar &


#目前已实现功能（接口实现）：
    1、添加、删除、修改linux主机，获取linux主机列表
    2、添加、删除、修改nginx实例，获取nginx实例列表
    3、获取nginx实例中server列表（目前不支持修改）
    4、获取、添加、删除nginx实例中server下location列表（目前不支持修改）
    5、已实现日志实时查看功能

#后期计划：
    1、添加权限功能，能分配到每个用户精确到控制一个nginx实例/linux主机
    