# 使用 Maven 构建阶段
FROM maven:3.8.5-openjdk-8 AS builder
WORKDIR /app

# 将 Maven 配置文件和源代码复制到容器中
COPY . .
# 使用 Maven 构建项目
RUN mvn clean package -DskipTests
# 基础镜像
FROM  openjdk:8-jre
WORKDIR /app
# 复制jar文件到路径
COPY --from=builder /app/ruoyi-admin/target/ruoyi-admin.jar ruoyi-admin.jar
# 启动文件服务
ENTRYPOINT ["java","-jar","ruoyi-admin.jar"]
