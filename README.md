# Multihdfs

Api to get hdfs config from multiple HDFS schedulers

### Pre-requisites

#### Secrets

The following secrets must exist in Vault:


- Certificate and private key of the API:
><u>Path</u>: /userland/certificates/multihdfs
```
    {"multihdfs.marathon.mesos_crt": "-----BEGIN CERTIFICATE-----MIIEqDCCApCgA...-----END CERTIFICATE-----",
    "multihdfs.marathon.mesos_key": "-----BEGIN RSA PRIVATE KEY-----MIIEogIBAAK....-----END RSA PRIVATE KEY-----"
    }
```
- Keystore password:
><u>Path</u>: /userland/passwords/multihdfs/keystore 
```
    {"pass": "randompassword"}
```
- Marathon http basic user/pass
><u>Path</u>: /dcs/passwords/marathon/rest 
```
{
    "pass": "0eTXqRiL2u11suNV4L94RhKqDTxbob6bSGQjBi4U",
    "user": "marathon"
  }
```

To upload secrets using deploy api:

```
curl -X POST --header 'Content-Type: application/json' --header 'Accept: */*' 'https://multiaz.labs.stratio.com/service/deploy-api/secrets/certificate?path=%2Fuserland%2Fcertificates%2Fmultihdfs&name=multihdfs.marathon.mesos&cn=multihdfs.marathon.mesos'

curl -X POST --header 'Content-Type: application/json' --header 'Accept: */*' 'https://multiaz.labs.stratio.com/service/deploy-api/secrets/password?path=%2Fuserland%2Fpasswords%2Fmultihdfs&name=keystore&password=randompassword'
```


### How to use it:

```
curl https://multihdfs.marathon.mesos:8443/config/hdfs-example/hdfs-example-2/hdfs-site.xml
```

