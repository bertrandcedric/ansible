FROM centos
ARG pwd

#ENV http_proxy=http://CB1791DN@pcyvipncp2n.edf.fr:3128/
#ENV https_proxy=https://CB1791DN@pcyvipncp2n.edf.fr:3128/
#ENV no_proxy="localhost,192.168.99.100,127.0.0.1,.local,.edf.fr,.erdf.fr"

RUN yum -y update
RUN yum install -y initscripts openssh-server openssh-clients sudo nc

ADD sshd_config /etc/ssh/sshd_config

RUN /usr/sbin/sshd-keygen

RUN echo "root:${pwd}" | chpasswd

EXPOSE 22
EXPOSE 8080
CMD    ["/usr/sbin/sshd", "-D"]
