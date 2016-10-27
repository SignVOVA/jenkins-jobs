String jenkinsCredentialId = ""

// Each Key is a Folders
def jobDefn = 	[
					Demo	:	// Each Element is a Entry with Key being the project Name and Value being the Git URL
								[
									DemoHelloWorld	: 	"git@github.com:SignVOVA/helloword.git"
								]

				]

// Don't change anything below unless you know what you doing
jobDefn.each { entry ->
	// Create / Update Folder by Key
	folder( entry.key )
	// In each folder Create Jobs
	entry.value.each { job ->
		jobName = entry.key + "/" + job.key;
		jobVCS = job.value;
		buildMultiBranchJob(jobName, jobVCS, jenkinsCredentialId)
	}
}

// Define method to build the job
def buildMultiBranchJob(jobName, jobVCS, credentials) {
	// Create job
	multibranchPipelineJob(jobName) {
		// Define source
		branchSources {
			git {
				remote(jobVCS)
				credentialsId(credentials)
			}
		} // End source

		// Triggers
		triggers {

		  cron('H/15 * * * *')

		} // End of Triggers

		// How Many Items in the history
		orphanedItemStrategy {
			discardOldItems {
		        numToKeep(5)
		    }
		} // End Orphaned
	} // End Creating
} // End Method
