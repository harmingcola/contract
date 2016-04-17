FROM jenkins

USER root

RUN apt-get update
RUN apt-get install -y maven

COPY plugins.txt /usr/share/jenkins/ref/
RUN /usr/local/bin/plugins.sh /usr/share/jenkins/ref/plugins.txt

USER jenkins