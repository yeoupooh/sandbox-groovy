import groovy.json.JsonSlurper

def config = new JsonSlurper().parseText(new File("./telegram.bot.config.json").text)
String token = config.token

def updates = TelegramBotApiHelper.getUpdates(token)
println "getUpdates:" + updates

def updatesJson = new JsonSlurper().parseText(updates)

if (updatesJson.result.size() > 0) {
    def msg = updatesJson.result[0]
    def result = TelegramBotApiHelper.sendMessage(token, msg.message.chat.id, "hello")
    println "result=[$result]"
}

