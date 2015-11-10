# sandbox-groovy (simple examples)

* examples/: read file, run process, http client
* h2/: h2 database client example
* http/: httpbuilder example
* influxdb/: influxdb example with configuration
* iptime/: WOL tool for ipTime AP. About product, see http://iptime.com/iptime/?page_id=11&pf=3
* jaudiotagger/: read mp3 tag info using jaudiotagger library, http://www.jthink.net/jaudiotagger/
* javafx/: javafx using groovy
* json/: reading json file example
* mindwiki/: freemind mind map file to redmine wiki syntaxed text conversion tool
* mongo/: Mongo DB client example
* redmine/: Redmine client example
* sigar/: Sigar example
* swing/: Bunch of swing examples
* telegram/: telegram bot api example
* undertow/: undertow example
* webserver/: webserver examples using jetty 8, 9, as an example, Minecraft Server Launcher service is included.  
* websocket/: websocket examples

## Minecraft Server Launcher (webserver/)

* This allows you to start/stop minecraft server on the web.

### Folder structure

```
+- mc-server-root/ : includes minecraft server jar file and start script (=$MC_SERVER_ROOT)
    +- my-mc-server1/ : mc server files with configurations
    +- my-mc-server2/ : mc server files with configurations
```

### Preparation

* Copy server.config.json.sample to server.config.json in webserver/conf/ folder.
* Download minecraft server jar file into $MC_SERVER_ROOT
* Create starting script such as start.sh. as below example.
```
#!/bin/bash

java -jar minecraft_server.jar $1
```

### Run

* Execute ./jetty.groovy and go http://localhost:9090/runmc.groovy
* Enjoy it.

![alt text](https://raw.githubusercontent.com/yeoupooh/sandbox-groovy/master/webserver/screenshot-runmc.png "Screenshot of runmc.groovy")

[![Join the chat at https://gitter.im/yeoupooh/sandbox-groovy](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/yeoupooh/sandbox-groovy?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
Sandbox for groovy

