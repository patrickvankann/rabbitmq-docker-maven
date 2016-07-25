FROM rabbitmq:3.6.3

RUN apt-get update

RUN apt-get install -y curl

RUN curl http://www.rabbitmq.com/community-plugins/v3.6.x/rabbitmq_web_mqtt-3.6.x-3b6a09bb.ez > $RABBITMQ_HOME/plugins/rabbitmq_web_mqtt-3.6.x-3b6a09bb.ez

RUN rabbitmq-plugins enable --offline rabbitmq_management
RUN rabbitmq-plugins enable --offline rabbitmq_mqtt
RUN rabbitmq-plugins enable --offline rabbitmq_web_mqtt

EXPOSE 15672
EXPOSE 1883
EXPOSE 15675
