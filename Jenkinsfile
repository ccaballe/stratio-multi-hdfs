@Library('libpipelines@master') _

hose {
    EMAIL = 'architecture'
    DEVTIMEOUT = 30
    RELEASETIMEOUT = 20
    ACCEPTSNAPSHOTSONRELEASES = false
    NEW_VERSIONING = true
    KMS_UTILS='0.2.1'
    BUILDTOOLVERSION = '3.5.0'


    DEV = { config ->        
        doCompile(config)
        doUT(config)
        doPackage(config)
        doStaticAnalysis(config)
        doDocker(config)
     }
}
