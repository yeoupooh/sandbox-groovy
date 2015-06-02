import com.taskadapter.redmineapi.RedmineManager
import com.taskadapter.redmineapi.RedmineManagerFactory
import com.taskadapter.redmineapi.bean.Project
import groovy.json.JsonSlurper

@Grapes([
        @Grab(group = 'com.taskadapter', module = 'redmine-java-api', version = '2.2.0'),
        @Grab(group = 'org.slf4j', module = 'slf4j-jdk14', version = '1.7.1'),
        @Grab(group = 'org.apache.httpcomponents', module = 'httpclient', version = '4.2'),
        @Grab(group = 'org.easytesting', module = 'fest-assert', version = '1.4'),
        @Grab(group = 'org.json', module = 'json', version = '20090211'),
        @Grab(group = 'org.slf4j', module = 'slf4j-api', version = '1.7.1'),
        @Grab(group = 'org.apache.httpcomponents', module = 'httpcore', version = '4.2'),
        @Grab(group = 'junit', module = 'junit', version = '4.10')
])

def confRes = this.getClass().getResource('/redmine.config.json')
if (confRes == null) {
    println "Can't read 'redmine.config.json' file."
    return null
}
configJsons = new JsonSlurper().parseText(confRes.text)
println configJsons

println "\nConfigurations:\n"
configJsons.eachWithIndex { config, index ->
    println "$index) $config.name ($config.userName@$config.uri)"
}

int selectedConfigIndex = 0
if (System.console() != null) {
    selectedConfigIndex = System.console().readLine("\nWhich configuration do you want to use? ").toInteger()
}
println "You choose " + configJsons[selectedConfigIndex] + "\n"

def uri = configJsons[selectedConfigIndex].uri
def apiAccessKey = configJsons[selectedConfigIndex].apiAccessKey
def userName = configJsons[selectedConfigIndex].userName
def password = configJsons[selectedConfigIndex].password

// NOTE: Checkout SSL cert issue at http://blog.nerdability.com/2013/01/tech-how-to-fix-sslpeerunverifiedexcept.html
RedmineManager mgr = RedmineManagerFactory.createWithUserAuth(uri, userName, password)
//RedmineManager mgr = RedmineManagerFactory.createWithApiKey(uri, apiAccessKey)

List<Project> projectsWithHttpBasicAuth = mgr.getProjectManager().getProjects()
for (Project project : projectsWithHttpBasicAuth) {
    System.out.println(project.toString())
}

//List<Issue> issues = mgr.getIssueManager().getIssues(projectKey, queryId)
//for (Issue issue : issues) {
//    System.out.println(issue.toString())
//}
