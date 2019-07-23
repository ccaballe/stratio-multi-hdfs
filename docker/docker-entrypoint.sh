#!/bin/bash

FQDN=${FQDN:="multihdfs.marathon.mesos"}
CERTPATH=/etc/sds/multihdfs/secrets/
ENVPATH=/etc/sds/multihdfs/multihdfs.env
CONF_PATH=/etc/sds/multihdfs/application.conf
CACERTS_PATH=/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/security/cacerts

source /kms_utils.sh

 # Secrets configuration
 # If retrieving from vault, the following env variables need to be set:
 # STRING_VAULT_HOST (comma separated string)
 # VAULT_PORT (int)

 OLD_IFS=${IFS}
 IFS=',' read -r -a VAULT_HOSTS <<< "$STRING_VAULT_HOST"
 IFS=${OLD_IFS}
 if [[ "${RETRIEVE_FROM_VAULT}" == "true" ]];
 then
   CLUSTER=${CLUSTER:="userland"}
   INSTANCE=${INSTANCE:="multihdfs"}

   if [[ ${VAULT_ROLE_ID} ]] && [[ ${VAULT_SECRET_ID} ]];
   then
     # Login to Vault to get VAULT_TOKEN using dynamic authentication
     login
   fi

   # get user/pass from marathon http basic
   getPass dcs marathon rest
   export MULTIHDFS_USER=$MARATHON_REST_USER
   export MULTIHDFS_PASS=$MARATHON_REST_PASS

   env

   getCAbundle ${CERTPATH} "JKS" "truststore.jks"
   getCert ${CLUSTER} ${INSTANCE} ${FQDN} "JKS" "${CERTPATH}"
   export MULTIHDFS_KEYSTORE_LOCATION="${CERTPATH}${FQDN}.jks"
   export KEYSTORE_PATH=${MULTIHDFS_KEYSTORE_LOCATION}

   getPass ${CLUSTER} ${INSTANCE} "keystore"
   export KEYSTORE_PASS=${MULTIHDFS_KEYSTORE_PASS}

   getPass "ca-trust" "default" "keystore"
   keytool -importkeystore -noprompt -srckeystore "${CERTPATH}truststore.jks" -srcstorepass "${DEFAULT_KEYSTORE_PASS}" -destkeystore "${CACERTS_PATH}" -deststorepass changeit

 fi

java -jar /etc/sds/multihdfs/multihdfs-*.jar
