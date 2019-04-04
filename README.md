# hadoop
Hadoop 伪分布式部署命令

```text
sudo docker build -t hadoop_centos .

sudo docker run -it --rm --name test hadoop_centos bash

# 下面的命令需要在容器启动后运行
# /usr/sbin/sshd
# ssh localhost 注意连接上之后要Exit退出来之后再执行下面那条命令
# hdfs namenode -format

```