FROM jenkins

USER root

RUN apt-get update
RUN apt-get install -y mvn

COPY plugins.txt /usr/share/jenkins/ref/

USER jenkins