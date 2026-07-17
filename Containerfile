FROM fedora-minimal:latest

RUN dnf install wol -y

WORKDIR /app/
COPY target/smarthomeEndpoint*-runner /app/smarthomeEndpoint
RUN chmod 775 /app

EXPOSE 8080

CMD ["./smarthomeEndpoint", "-Dquarkus.http.host=0.0.0.0"]
