FROM centos:7
MAINTAINER Enda Lin

RUN yum -y --setopt=tsflags=nodocs update && \
    yum -y --setopt=tsflags=nodocs install httpd && \
    yum clean all

RUN mkdir hadoop
COPY hadoop/ hadoop/

# download jdk1.80 and set the path of JAVA_HOME
RUN yum -y install java-1.8.0-openjdk-devel.x86_64 \
    && echo "export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.171-8.b10.el6_9.x86_64" >> /etc/profile \
    && echo "export HADOOP_HOME=/hadoop" >> /etc/profile \
    && echo "export CLASSPATH=.:$JAVA_HOME/jre/lib/rt.jar:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar" >> /etc/profile \
    && echo "export PATH=$PATH:$JAVA_HOME/bin:$HADOOP_HOME/bin:$HADOOP_HOME/sbin" >> /etc/profile \
    && source /etc/profile

RUN yum install -y openssh-server \


EXPOSE 80
