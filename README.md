### 智灵OJ在线判题系统

本项目是一个基于Spring Cloud微服务 + Spring Boot + Docker 实现的编程题目评测系统。系统能够编译执行用户提交的代码，并根据执行结果和管理员预设的测试用例得出用户本次提交的结果。

项目分为三大模块：用户模块、题目模块、判题模块

1. 用户模块：
   - 注册
   - 登录
2. 题目模块：
   - 增删改题目（管理员）
   - 查询题目
   - 提交代码
   - 查询提交记录
3. 判题模块：
   - 提交判题
   - 判断结果

为了降低模块之间的耦合程度，将代码沙箱从判题模块中剥离出来作为一个独立的模块，通过API与判题模块进行交互。代码沙箱模块仅负责编译执行代码、返回运行结果。

代码沙箱仓库地址：https://github.com/zzz123q/genie-oj-sandbox

#### 业务流程

![](/images/业务流程.png)

####  单体项目微服务化

原单体项目所在仓库地址：https://github.com/zzz123q/genie-oj-backend

将原本用户模块、题目模块、判题模块所在的单体项目解耦，拆分为数个可以独立运行的服务。这样可以使较重的服务（判题模块）与其余服务分离开来，不影响各自的运行。

各个服务划分的结果如下：

- 注册中心：Nacos
- 微服务网关（genie-oj-backend-gateway）：聚合所有接口，统一接收前端请求
- 公共模块：
  - 公用模块（genie-oj-backend-common）：全局异常处理、请求响应封装类、常量类、工具类等
  - 模型模块（genie-oj-backend-model）：实体类、DTO类、VO类、枚举类等
  - 内部接口模块（genie-oj-backend-service-client）：项目内部服务需要使用的接口定义
- 用户服务（genie-oj-backend-user-service）
- 题目服务（genie-oj-backend-question-service）
- 判题服务（genie-oj-backend-judge-service）