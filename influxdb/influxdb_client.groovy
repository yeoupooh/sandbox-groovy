import groovy.json.JsonSlurper
import org.influxdb.InfluxDB
import org.influxdb.InfluxDBFactory
import org.influxdb.dto.Pong

import java.util.concurrent.TimeUnit

@Grapes([
        @Grab(group = 'org.influxdb', module = 'influxdb-java', version = '1.5')
])

def confRes = this.getClass().getResource('/influxdb.config.json')
if (confRes == null) {
    println "No config file"
    return null
}
def configJsons = new JsonSlurper().parseText(confRes.text)

InfluxDB influxDB = InfluxDBFactory.connect(configJsons.url, configJsons.username, configJsons.password);
boolean influxDBstarted = false;
def Pong response
while (true) {
    println "ping"
    try {
        response = influxDB.ping();
        println "response: " + response
        if (response.getStatus().equalsIgnoreCase("ok")) {
            influxDBstarted = true;
        }
    } catch (Exception e) {
        e.printStackTrace()
        break;
    }
    Thread.sleep(100L);
    if (influxDBstarted) {
        break;
    }
}

if (influxDBstarted) {
    pritnln "Querying..." + configJsons.query
    def result = influxDB.query(configJsons.database, configJsons.query, TimeUnit.MILLISECONDS)
    println result
}