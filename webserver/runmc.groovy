import groovy.json.*

def json = new JsonBuilder()

def startServer = { config, index ->
	def cmd = config.basePath + config.serverPaths[index].script + " " + config.basePath + config.serverPaths[index].server
	// Using ".text" will wait until server is down
	//def res = cmd.execute().text
	
	//println "cmd=" + cmd
	
	cmd.execute()
	return "OK"
}

def res = "OK"

def config = new ServerConfig().load()
//println config

def cmd = request.getParameter("cmd")
//println cmd

if (cmd == "start") {
	startServer(config, request.getParameter("index").toInteger())
}

//response.contentType = 'application/json'
//json.result res
//out << json

html.html {
	body {
		p { a(href:"/runmc.groovy", "Home") }
		table {
			thead {
				tr {
					td { yield "Actions" }
					td { yield "Server Name" }
					td { yield "Status" }
					td { yield "Minecraft Version" }
					td { yield "Port#" }
					td { yield "Required Mods" }
					td { yield "Required Resource Packs" }
				} // tr
			} // thead
			config.serverPaths.eachWithIndex { server, i ->
				tr {			
					td { a(href:"/runmc.groovy?cmd=start&index=" + i, "Start") } // td
					td { yield "$server.name" }
					td { yield "(TODO)" }
					td { yield "$server.minecraftVersion" }
					td { yield "$server.port" }
					td { yield "$server.requiredMods" } // td
					td { yield "$server.requiredResourcePacks" } // td
				} // tr
			} // each
		} // table
	} // body
} // html
