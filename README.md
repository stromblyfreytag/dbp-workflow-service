<h1>Flowable POC</h1>
<h2>Description</h2>
This is a POC for a replacement of the job-service using Flowable / Camunda / Activitii
See JIRA https://trustwave.atlassian.net/browse/DBP-4093

<h2>Resources</h2>
<ul>
  <li>This is the workflow being modeled:  https://trustwave.atlassian.net/wiki/spaces/eng/pages/210469682/DBP+-+Audit+Job+Workflow</li>
  <li>This is the POC description: https://trustwave.atlassian.net/wiki/spaces/eng/pages/703397926/DBP+-+Flowable+POC</li>
  <li>To get the source code: git clone git@github.com:stromblyfreytag/dbp-workflow-service.git</li>
</ul>

<h2>Startup</h2>
Compile and begin the process, then follow the instructions that are printed after it starts up.  Output should be something like the following:
> Open for business!
>
> Begin by calling scan: http://localhost:8080/scan
> Or for a failing case, by calling scan: http://localhost:8080/scan?assetToFail=asset2</code>
