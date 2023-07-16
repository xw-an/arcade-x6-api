# Arcade-X6 API
  Arcade-X6 API 是一个基于 Spring Boot 的后端服务项目，该项目主要用于业务场景的流程编排。  
  项目主要包含四个模块：arcade-analysis、arcade-api、arcade-common 和 arcade-mvc-config。

## 模块介绍
### arcade-analysis
arcade-analysis 模块主要负责处理与组件相关的逻辑。它包含了处理组件数据的各种业务逻辑和算法，为其他模块提供数据分析和处理的功能。

### arcade-api
arcade-api 模块提供了对外的 HTTP 接口，用于与客户端应用程序进行交互。它实现了各种 RESTful API，提供了组件管理、流程编排和任务管理等功能。客户端应用程序可以通过调用这些接口来实现与流程编排平台的交互。
- 业务组件：支持条件组件、提取组件、业务组件等
- 流程编排：支持不同版本的各组件之间的编排等
- 任务管理：支持流程实例执行，日志跟踪等

### arcade-common
arcade-common 模块是一个公共模块，包含了项目中共享的工具类、常量、配置文件等。它提供了一些通用的功能和组件，可以被其他模块引用和复用。

### arcade-mvc-config
arcade-mvc-config 模块是用于处理 HTTP 请求的拦截器配置。它定义了一些全局的拦截器，用于对请求进行预处理、鉴权、日志记录等操作。

## 快速开始
以下是快速开始项目的步骤：
1. 本地创建一个mysql数据库：库名为：arcade_x6
2. 执行项目的arcade-api模块下的resource目录中的sql文件夹下的所有建表/插入测试数据的sql文件
3. 克隆项目到本地：git clone https://github.com/xw-an/arcade-x6-api.git
4. 进入项目目录：cd arcade-x6-api
5. 构建项目：mvn clean install
6. 运行项目：mvn spring-boot:run

## 联系我
如果您有任何疑问或建议，请通过以下方式联系我：
- 邮箱：302661277@qq.com<br>
我将尽快回复您的消息，并感谢您对项目的关注和支持！

## 相关的前端项目
- 项目仓库地址：[https://github.com/xw-an/arcade-x6.git](https://github.com/xw-an/arcade-x6.git)
- 项目描述：该项目对应的前端项目
