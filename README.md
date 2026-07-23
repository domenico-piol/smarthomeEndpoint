# Smarthome Endpoint

This is an REST Endpoint for various automation tasks in my smarthome/homelab environment.

## Build and Run
### How to build and run in a Podman container
Build the binary with:

```
mvn clean package -Dnative
```

Create the container image:

```
podman build -t smarthome-endpoint:v2 .
```

Run the container in Podman:

```
podman run -d --name smarthome-endpoint --network host -e smarthome.wakeup.endpoints='HEARTOFGOLD=08:BF:B8:01:33:17,IMAC=10:DD:B1:BD:FE:C2' -e smarthome.homelab.ilopwd='MYILOPASSWD' localhost/smarthome-endpoint:v2
```

### Access the REST endpoints
I use `httpie` for accessing the endpoints, e.g.:

```
http --form POST http://localhost:8080/smarthome/wakeup/MYHOST
```

## Configuration
There are a few config-parameters required.

| Parameter      | Example Value |
| ----------- | ----------- |
| smarthome.wakeup.endpoints      | HEARTOFGOLD=08:BF:B8:01:33:17,IMAC=10:DD:B1:BD:FE:C2       |
| smarthome.homelab.ilopwd   | MYPASSWD        |