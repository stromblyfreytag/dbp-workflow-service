# Flowable POC
## Description
This is a POC for a replacement of the job-service using Flowable / Camunda / Activitii
See JIRA https://trustwave.atlassian.net/browse/DBP-4093

## Resources
<ul>
  <li>This is the workflow being modeled:  https://trustwave.atlassian.net/wiki/spaces/eng/pages/210469682/DBP+-+Audit+Job+Workflow</li>
  <li>This is the POC description: https://trustwave.atlassian.net/wiki/spaces/eng/pages/703397926/DBP+-+Flowable+POC</li>
</ul>

To get the source code: 
```
git clone git@github.com:stromblyfreytag/dbp-workflow-service.git
```


## Startup
Compile and begin the process, then follow the instructions that are printed after it starts up.  Output should be something like the following:

<table><tr><td>
Open for business!<br><br>
Begin by calling scan: http://localhost:8080/scan
Or for a failing case, by calling scan: http://localhost:8080/scan?assetToFail=asset2
</td></tr></table>

To start the process, you can use the following command-line command:
```
java -jar ./dbp-workflow-service-0.0.1-SNAPSHOT.jar 
```
or if you want to be able to connect to it with the JMX and/or VisualVM
```
java -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9010 -Dcom.sun.management.jmxremote.rmi.port=9010 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=localhost -jar ./dbp-workflow-service-0.0.1-SNAPSHOT.jar
```
