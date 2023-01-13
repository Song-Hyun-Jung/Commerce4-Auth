FROM gradle:7.1.0-jdk11
USER root

RUN useradd -ms /bin/bash wasadm
RUN rm -rf /app
RUN mkdir -p /app

RUN chown -R wasadm:wasadm /app

RUN cd /app
RUN git clone https://github.com/Song-Hyun-Jung/Commerce4-Common.git /app/Commerce4-Common
RUN git clone https://github.com/Song-Hyun-Jung/Commerce4-Auth.git /app/Commerce4-Auth
RUN git clone https://github.com/Song-Hyun-Jung/jenniferAgent.git /app/jenniferAgent

WORKDIR /app/Commerce4-Auth

RUN cd /app/Commerce4-Auth

RUN chmod -R 777 /app/*

RUN gradle build

CMD ["java", "-javaagent:/app/jenniferAgent/jennifer.jar","-Djennifer.config=/app/jenniferAgent/conf/auth.conf", "-jar", "/app/Commerce4-Auth/build/libs/Commerce4-Auth-0.0.1-SNAPSHOT.jar"]


