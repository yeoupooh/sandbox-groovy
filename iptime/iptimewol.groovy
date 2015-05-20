import IptimeClient

IptimeClient ic = new IptimeClient()
def configJsons = ic.loadConfig()

println "\nConfigurations:\n"
configJsons.eachWithIndex { config, index ->
    println "$index) $config.baseUrl"
}

int selectedConfigIndex = System.console().readLine("\nWhich configuration do you want to use? ").toInteger()
println "You choose " + configJsons[selectedConfigIndex] + "\n"

def wolList = ic.getWolList(configJsons[selectedConfigIndex]);

println "\nPCs:\n"
wolList.eachWithIndex { wol, index ->
    println "$index) $wol.name ($wol.mac)"
}
int selectedWolIndex = System.console().readLine("\nWhich PC do you want to wake up? ").toInteger()
println "You choose " + wolList[selectedWolIndex] + "\n"
boolean success = ic.iptimeWakeUp(configJsons[selectedConfigIndex], wolList[selectedWolIndex])

println(success == true ? "Success" : "Failed")

