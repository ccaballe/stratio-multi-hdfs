FROM qa.stratio.com/stratio/stratio-base:2.0.0

ENV KMS_UTILS 0.2.1

RUN rm -rf /etc/service/confd

RUN apt-get -y update && apt-get install -y jq curl openjdk-8-jdk \
    && apt-get -qq clean \
    && rm -rf /var/lib/apt/lists/*


RUN mkdir -p /etc/sds/multihdfs/secrets

ADD docker/* /etc/sds/multihdfs/

COPY target/*.jar /etc/sds/multihdfs/
COPY docker/docker-entrypoint.sh /etc/service/multihdfs/

ADD http://sodio.stratio.com/repository/paas/kms_utils/${KMS_UTILS}/kms_utils-${KMS_UTILS}.sh /kms_utils.sh

RUN chmod +x /etc/service/multihdfs/docker-entrypoint.sh

EXPOSE 8443
EXPOSE 18080

ENTRYPOINT ["bash", "/etc/sds/multihdfs/docker-entrypoint.sh" ]