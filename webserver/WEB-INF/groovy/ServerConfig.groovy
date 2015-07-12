import groovy.json.JsonSlurper

def load() {
    def inputFile = new File("conf/server.config.json")
    def InputJSON = new JsonSlurper().parseText(inputFile.text)
    //InputJSON.each{ println it }

    println "ServerConfig.load: basePath=[$InputJSON.basePath]"
    InputJSON.serverPaths.each {
        println "ServerConfig.load: script=[$it.script], server=[$it.server]"
    }

    return InputJSON
}
