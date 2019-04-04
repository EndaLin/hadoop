FROM centos:7
MAINTAINER Enda Lin

# 将配置好的Hadoop文件复制在docker里面
COPY hadoop/ /root/hadoop/

# 创建相应的文件夹
RUN mkdir /root/hadoop/data \
    && mkdir /root/hadoop/data/dfs \
    && mkdir /root/hadoop/data/dfs/name \
    && mkdir /root/hadoop/data/dfs/data

# 安装JDK1.8.0
RUN yum install -y java-1.8.0-openjdk-devel.x86_64 \
    && yum install -y which

# 配置响应的环境变量
ENV JAVA_HOME /usr/lib/jvm/java-1.8.0-openjdk-1.8.0.201.b09-2.el7_6.x86_64
ENV HADOOP_HOME /root/hadoop
ENV CLASSPATH .:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
ENV PATH $PATH:$JAVA_HOME/bin:$HADOOP_HOME/bin:$HADOOP_HOME/sbin

# 配置SSH无密码登陆
RUN yum install -y openssh-server \
    && yum install -y openssh-clients \
    && ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa \
    && cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys \
    && /usr/sbin/sshd-keygen -A

EXPOSE 9000
EXPOSE 50070
