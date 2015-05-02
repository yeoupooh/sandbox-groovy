import groovy.json.JsonSlurper

def load() {
	def inputFile = new File("conf/server.config.json")
	def InputJSON = new JsonSlurper().parseText(inputFile.text)
	//InputJSON.each{ println it }

	println "basePath="+InputJSON.basePath
	InputJSON.serverPaths.each { println "script=" + it.script + ", server=" + it.server }

	return InputJSON
}
