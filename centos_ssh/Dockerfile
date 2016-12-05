FROM centos:7

ARG pwd

RUN yum -y update
RUN yum install -y epel-release initscripts openssh-clients openssh-server sudo nc python-pip gcc

ADD sshd_config /etc/ssh/sshd_config

RUN /usr/sbin/sshd-keygen

RUN echo "root:${pwd}" | chpasswd

EXPOSE 22
CMD    ["/usr/sbin/sshd", "-D"]
