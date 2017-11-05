FROM openjdk:8

ADD target/mobileLogin-0.0.1-SNAPSHOT.jar /mobileLoginapp.jar

RUN sh -c 'touch /mobileLoginapp.jar'

#EXPOSE 8140
EXPOSE 80

CMD ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/mobileLoginapp.jar"]