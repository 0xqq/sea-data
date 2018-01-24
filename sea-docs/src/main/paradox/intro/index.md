# 概述

**Sea Data 数据处理平台提供了强大的提取、转换和加载（ETL）功能，使用了一种突破性的、元数据驱动的方法。** Sea Data
基于 Akka 框架构建。

## 范围

Sea Data产品系列总体设计是以市场及客户需求为指导，在参照业界规范及市场主流产品的基础上，根据稳定性、统一性、安全性、开放性、实用性和易用性的原则，定义Sea Data系统的系统架构、系统功能、关键技术、逻辑模型、外部接口、安全管理、优化管理、运维管理以及技术要求，为研发人员、测试人员和运维人员提供指导。

本设计书下列章节中述及的Sea Data系列（可简称 **Sea**），如未特别注明均指通用的产品，不仅仅局限于数据采集服务。

## 方案组成

**Sea** 方案由以下主要部分组成：

- @ref[《数据处理子系统》](../data/index.md)：提供数据处理业务所需支持的各种数据模拟，即可作为单独的程序执行，也可作
  为组件供引擎子系统调用执行。
- @ref[《引擎子系统》](../engine/index.md)：解析业务流程，执行每个任务（子任务）的所有阶段业务逻辑，包括通用阶段（如
  日志、数据库操作、消息通知……），以及业务阶段（如文件采集等）。
- @ref[《调度子系统》](../scheduler/index.md)：加载业务（数据）处理流程文件，生成任务并对任务进行调度，对调度资源和
  策略进行管理。
- @ref[《业务编排子系统》](../choreography/index.md)：生成业务（数据）处理流程文件。
- @ref[《监、管子系统》](../console/index.md)：对系统运行状况、任务执行情况进行监查，并可管理系统。
- @ref[《业务文件规范》](../business_spec/index.md)：业务编排子系统定义的配置文件格式。
- @ref[《组件规范》](../component_spec/index.md)：采集组件实现规范，SPI。
- @ref[《系统间通信规范》](../ic_spec/index.md)：各子系统进程间通信消息规范。

基于 Akka Cluster 和 Akka Cluster role，各子系统以不同的角色存在于 **Sea** ，子系统之间基于 Akka/Akka Cluster 实现服务发现、
注册，消息通信，并提供丰富的引擎路由和负载均衡能力。

## 使用对象

**Sea** 产品最终使用对象为：

- 开发人员：根据方案制定的标准，格式，二次开发手册，进行业务开发。
- 测试人员：使用产品提供的工具，对开发人员的开发成果进行验证。
- 运维人员：使用产品加载开发人员提供的业务或者自行开发的业务。