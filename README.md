# Rabbitmq-Docker-Maven
Creates a Rabbitmq Docker image with the rabbitmq_web_mqtt plugin enabled using the Fabric8 Docker Maven Plugin.

## Rabbitmq configuration
Rabbitmq Dockerfile is based on the standard rabbitmq:3.8.1 tag. It enables the rabbitmq_web_mqtt plugin as well as the standard rabbitmq_mqtt and rabbitmq_management using the rabbitmq-plugins CLI.

The TCP ports that are exposed are:
1. 15672: The web UI for the management console 
2. 1883: For the TCP-based MQTT broker
3. 15675: For the HTTP websocket based MQTT broker

## Maven configuration
The build uses the Fabric8 Maven Docker plugin to build from a Dockerfile in src/main/docker/rabbitmq. To build the image, use
> mvn docker:build

The build is bound to the "package" phase of the Maven build.

To start the image, use
> mvn docker:start

This is bound to the pre-integration-test phase in the Maven build so that Rabbitmq is available for the integration tests.

To stop the image, use
> mvn docker:stop

This is bound to the post-integration-test phase to shut the image down after the tests.

Maven Failsafe plugin is used to execute a basic pub/sub scenario over TCP and websocket to verify that the broker is working. The tests are in scr/test/java/net/eusashead/rabbitmq/SyncPubSubITCase.java.

##Docker Hub
The image is uploaded to [Dockerhub](https://hub.docker.com/r/eusashead/rabbitmq/).

It should be possible to pull the image directly using 
> docker pull eusashead/rabbitmq
