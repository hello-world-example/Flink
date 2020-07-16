# 获取 Github 源码

## 获取代码

```bash
# clone 国内镜像仓库，速度会快一点
$ git clone https://gitee.com/mirrors/apache-flink.git flink

$ cd flink

# 设置远程仓库为自己 clone 的仓库
$ git remote set-url origin https://github.com/o-fork/flink.git
# 更新一下代码
$ git pull -p


# Alibaba Blink 分支
$ git checkout blink

# 官方 1.8 版本分支
$ git checkout release-1.8
# 从官方 1.8 版本分支，创建一个自己的分支
$ git checkout -b dev_kail_v1.8
# 推送同步一下远程
$ git push --set-upstream origin dev_kail_v1.8
```

## 打包

### 添加 pom.xml 仓库

`vim pom.xml` 添加 阿里的 Maven 仓库，这样打包(下载 jar 包)时会稍微块一点

```xml
<repositories>
	<repository>
		<id>aliyun</id>
		<url>https://maven.aliyun.com/repository/public</url>
	</repository>
</repositories>

<pluginRepositories>
	<pluginRepository>
		<id>aliyun</id>
		<url>https://maven.aliyun.com/repository/public</url>
	</pluginRepository>
</pluginRepositories>
```

### mvn package

```
# 第一次可能需要 10分钟
$ mvn clean package -DskipTests
```



## 先关注以下模块

- `flink-examples` 示例程序
  - `flink-examples-streaming` 流处理代码示例
    - wordcount
    - windowing
    - join
    - statemachine
  - `flink-examples-batch` 批处理代码示例
  - `flink-examples-table`
- `docs`
  - `./docs/build_docs.sh` 启动本地文档



## Read More

- [Importing Flink into an IDE](https://ci.apache.org/projects/flink/flink-docs-release-1.8/flinkDev/ide_setup.html)
- [Building Flink from Source](https://ci.apache.org/projects/flink/flink-docs-release-1.8/flinkDev/building.html)

