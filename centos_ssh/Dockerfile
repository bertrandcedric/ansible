FROM centos

ENV http_proxy=http://CB1791DN@noevipncp2n.edf.fr:3128/
ENV https_proxy=https://CB1791DN@noevipncp2n.edf.fr:3128/
ENV no_proxy="localhost,192.168.99.100,127.0.0.1,.local,.edf.fr,.erdf.fr"

RUN yum -y update
RUN yum install -y initscripts openssh-server

RUN /usr/sbin/sshd-keygen

ADD sshd_config /etc/ssh/sshd_config

RUN echo "root:password1" | chpasswd

EXPOSE 22
CMD    ["/usr/sbin/sshd", "-D"]
