FROM centos

ENV JAVA_VERSION 1.8.0
ENV SPARK_VERSION v1.5.2

RUN yum update -y
RUN yum install -y git wget java-$JAVA_VERSION-openjdk java-$JAVA_VERSION-openjdk-devel
ENV JAVA_HOME /usr/lib/jvm/java-$JAVA_VERSION-openjdk

RUN git clone --depth 1 https://github.com/apache/spark.git -b $SPARK_VERSION /spark

WORKDIR /spark

ADD settings.xml /root/.m2/settings.xml

RUN ./build/sbt -Pyarn -Phadoop-2.3 assembly

#./bin/spark-shell --master local[2]
