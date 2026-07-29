# Smarthome Endpoint

This is an REST Endpoint for various automation tasks in my smarthome/homelab environment.

## Build and Run
### How to build and run in a Podman container
Build the binary with:

```
task build
```

Create the container image:

```
task create-image
```

Run the container in Podman:

```
task run
```
and use
```
task down
```
to stop it.
### Access the REST endpoints
I use `httpie` for accessing the endpoints, e.g.:

```
http --form POST http://localhost:8080/smarthome/wakeup/MYHOST
```
```
http --form GET http://localhost:8080/smarthome/homelab/powerstate
```


## Configuration
There are a few config-parameters required.

| Parameter      | Example Value |
| ----------- | ----------- |
| smarthome.wakeup.endpoints      | HEARTOFGOLD=08:BF:B8:01:33:17,IMAC=10:DD:B1:BD:FE:C2       |
| smarthome.homelab.ilopwd   | MYPASSWD        |

The `smarthome.homelab.ilopwd` variable is a secret and is stored in the local file `.env` during runtime. Podman compose (or docker compose) reads it from that file.