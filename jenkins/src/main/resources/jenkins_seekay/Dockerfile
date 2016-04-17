FROM jenkins

USER root

RUN apt-get update
RUN apt-get install -y maven
RUN apt-get install -y apt-utils
RUN apt-get install -y xmlstarlet

COPY plugins.txt /usr/share/jenkins/ref/
RUN /usr/local/bin/plugins.sh /usr/share/jenkins/ref/plugins.txt

RUN git config --global user.email "grahamcolins@gmail.com"
RUN git config --global user.name "Colin Graham"
RUN git config --global push.default simple

USER jenkins