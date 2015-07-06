import groovy.json.JsonSlurper

def config = new JsonSlurper().parseText(new File("./telegram.bot.config.json").text)
String token = config.token

def prevLastMsg
def offset = 0
String R_CMD = "/r "
Thread.start {
    while (true) {
        def updates = TelegramBotApiHelper.getUpdates(token, offset)
        println "getUpdates:" + updates

        def updatesJson = new JsonSlurper().parseText(updates)
        if (updatesJson.result.size() > 0) {
            def lastMsg = updatesJson.result[updatesJson.result.size() - 1]
            if (prevLastMsg == null) {
                prevLastMsg = lastMsg
            }
            if (lastMsg.message.message_id > prevLastMsg.message.message_id) {
                println "update lastMsg:$lastMsg.text"
                if (lastMsg.message.text.startsWith(R_CMD) == true) {
                    def keyword = lastMsg.message.text.substring(R_CMD.length())
                    println "keyword:$keyword"
                    TelegramBotApiHelper.sendMessage(token, lastMsg.message.chat.id, "http://www.reddit.com/r/" + keyword)
                }
            } else {
                println "no new message"
            }
            prevLastMsg = lastMsg
            offset = prevLastMsg.update_id
        }

        sleep(2000)
    }
}
