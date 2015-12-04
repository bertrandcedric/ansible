FROM centos

ENV http_proxy=http://CB1791DN@noevipncp2n.edf.fr:3128/
ENV https_proxy=https://CB1791DN@noevipncp2n.edf.fr:3128/
ENV no_proxy="localhost,192.168.99.100,127.0.0.1,.local,.edf.fr,.erdf.fr"

RUN yum update -y
RUN yum install -y git, wget

ENV MAVEN_VERSION 3.3.9
ENV JAVA_VERSION 1.8.0
ENV SCALA_VERSION 2.10.4
ENV SBT_VERSION 0.13.6

#install java
RUN yum install -y java-$JAVA_VERSION-openjdk
RUN yum install -y java-$JAVA_VERSION-openjdk-devel
ENV JAVA_HOME /usr/lib/jvm/java-$JAVA_VERSION-openjdk

#install maven
RUN curl -fsSL https://archive.apache.org/dist/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz | tar xzf - -C /usr/share \
  && mv /usr/share/apache-maven-$MAVEN_VERSION /usr/share/maven \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn
ENV MAVEN_HOME /usr/share/maven
VOLUME /cygdrive/c/apps/.m2
ADD settings.xml /root/.m2/settings.xml

#install scala
RUN wget http://www.scala-lang.org/files/archive/scala-$SCALA_VERSION.tgz
RUN tar xvf scala-$SCALA_VERSION.tgz
RUN mv scala-$SCALA_VERSION /usr/lib
RUN rm scala-$SCALA_VERSION.tgz
RUN ln -s /usr/lib/scala-$SCALA_VERSION /usr/lib/scala

ENV PATH $PATH:/usr/lib/scala/bin

# install sbt
#RUN wget -O /usr/local/bin/sbt-launch.jar http://repo.typesafe.com/typesafe/ivy-releases/org.scala-sbt/sbt-launch/$SBT_VERSION/sbt-launch.jar
#ADD scripts/sbt.sh /usr/local/bin/sbt
#RUN chmod 755 /usr/local/bin/sbt

RUN git clone --depth 1 https://github.com/apache/spark.git -b v1.5.2 /spark

WORKDIR /spark
#RUN mvn dependency:resolve
#RUN mvn -DskipTests clean package
RUN ./make-distribution.sh
