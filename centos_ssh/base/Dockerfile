FROM centos_proxy
#FROM centos

ARG pwd

RUN yum -y update
RUN yum install -y initscripts openssh-clients openssh-server sudo nc

ADD sshd_config /etc/ssh/sshd_config

RUN /usr/sbin/sshd-keygen

RUN echo "root:${pwd}" | chpasswd

EXPOSE 22
CMD    ["/usr/sbin/sshd", "-D"]
