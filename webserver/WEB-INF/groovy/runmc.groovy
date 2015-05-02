import groovy.io.FileType

def startServer = { config, index ->
    def cmd = config.basePath + config.serverPaths[index].script + " " + config.basePath + config.serverPaths[index].server
    // Using ".text" will wait until server is down
    //def res = cmd.execute().text

    cmd.execute()
    return "OK"
}

def config = new ServerConfig().load()

def thisScript = "/runmc.groovy"

def cmd = request.getParameter("cmd")

if (cmd == "start") {
    startServer(config, request.getParameter("index").toInteger())
    redirect(thisScript)
}

ProcessList pl = new ProcessList()
def ps = pl.getCurrent()

html.html {
    head {
        title("Minecraft Server Web Launcher")

        link(href: "//maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css", rel: "stylesheet")
    }
    body {
        p { button(class: "button btn btn-primary", onclick: "location.href='" + thisScript + "'", "Home") }
        table(class: "table") {
            thead {
                tr {
                    td { yield "Actions" }
                    td { yield "Status" }
                    td { yield "Server Name" }
                    td { yield "Port#" }
                    td { yield "Server Description" }
                    td { yield "Minecraft Version" }
                    td { yield "Installed Mods" }
                    td { yield "Required Resource Packs" }
                } // tr
            } // thead
            config.serverPaths.eachWithIndex { server, i ->
                def status = pl.findCommand(ps, server.server)
                tr {
                    td {
                        button(class: "button btn btn-primary", onclick: "location.href='" + thisScript + "?cmd=start&index=" + i + "'", "Start")
                    } // td
                    td {
                        span(class: status == true ? "btn btn-success" : "btn btn-danger", status == true ? "Running" : "Stopped")
                    }
                    td { yield "$server.name" }
                    td { yield "$server.port" }
                    td { yield "$server.description" }
                    td { yield "$server.minecraftVersion" }
                    td {
                        try {
                            File dir = new File(config.basePath.toString() + server.server.toString() + "/mods")
                            dir.eachFile(FileType.FILES) { file ->
                                p {
                                    a(href: config.download.baseUrl + file.getName(), file.getName())
                                }
                            }
                        } catch (FileNotFoundException) {
                            // ignore this error
                        }
                    } // td
                    td {
                        server.requiredResourcePacks.each { rp ->
                            a(href: config.download.baseUrl + rp.name, rp.name)
                        }
                    } // td
                } // tr
            } // each
        } // table
    } // body
} // html
