# hadoop
Hadoop 伪分布式部署命令

```text
sudo docker build -t hadoop_centos .

sudo docker run -it --rm -p 50070:50070 --name test hadoop_centos bash

# 下面的命令需要在容器启动后运行

start-all.sh

```