FROM rabbitmq:3.8.1

RUN rabbitmq-plugins enable --offline rabbitmq_management
RUN rabbitmq-plugins enable --offline rabbitmq_mqtt
RUN rabbitmq-plugins enable --offline rabbitmq_web_mqtt

EXPOSE 15672
EXPOSE 1883
EXPOSE 15675
