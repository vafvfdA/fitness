# 健身饮食管理应用

这是一个移动端优先的个人健身饮食管理项目，前端使用微信小程序，后端使用 Java 和 Spring Boot。

## 目录结构

```text
fitness/
  backend/   Java Spring Boot 后端
  miniapp/   微信小程序前端
  sdd/       项目规格、架构、变更和验收文档
```

## 后端运行

进入后端目录：

```powershell
cd backend
mvn test
mvn spring-boot:run
```

## Docker Desktop 本地数据库

推荐用 Docker Desktop 启动本地 MySQL，避免手工安装和配置 Windows MySQL 服务。

先打开 Docker Desktop，然后在项目根目录执行：

```powershell
docker compose up -d mysql
```

查看容器状态：

```powershell
docker compose ps
```

停止容器：

```powershell
docker compose stop mysql
```

停止并删除容器，但保留数据卷：

```powershell
docker compose down
```

如果要连数据也一起清空：

```powershell
docker compose down -v
```

### Docker MySQL 默认账号

```text
主机：127.0.0.1
端口：3307
数据库：fitness
普通用户：fitness
普通用户密码：fitness
root 用户：root
root 密码：root
```

Navicat 连接建议使用：

```text
主机：127.0.0.1
端口：3307
用户名：fitness
密码：fitness
数据库：fitness
```

本项目默认把容器内 MySQL 的 `3306` 映射到本机 `3307`，避免和其他项目或 Windows MySQL 服务抢 `3306`。

### 数据库配置

后端使用 MySQL 8 和 Flyway。默认连接配置如下：

```text
url: jdbc:mysql://localhost:3307/fitness
username: fitness
password: fitness
```

可以通过环境变量覆盖：

```powershell
$env:FITNESS_DATASOURCE_URL="jdbc:mysql://localhost:3307/fitness?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai"
$env:FITNESS_DATASOURCE_USERNAME="fitness"
$env:FITNESS_DATASOURCE_PASSWORD="fitness"
```

如果使用 Docker Desktop 启动 MySQL，数据库和用户会自动创建，不需要手动执行建库 SQL。

如果使用自己安装的 MySQL，启动后端前需要先创建数据库：

```sql
create database fitness default character set utf8mb4 collate utf8mb4_unicode_ci;
```

应用启动时 Flyway 会自动执行 `backend/src/main/resources/db/migration` 下的迁移脚本。

健康检查接口：

```text
GET http://localhost:8080/api/v1/health
```

## 小程序打开

使用微信开发者工具打开 `miniapp/` 目录。

当前小程序只是页面骨架，后续功能会按 `sdd/changes/` 中的变更逐步实现。

## 开发流程

本项目遵守 `sdd/SKILL.md` 和 `sdd/constitution.md`：

- 每个功能创建独立 change 目录。
- 先写 `spec.md` 和 `task.md`，再开发。
- 开发遵守红绿重构。
- 完成后更新 `test.md` 和 `review.md`。
- commit 和 push 必须由用户审核批准。
