import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j

@Grapes([
        @Grab(group = 'ch.qos.logback', module = 'logback-classic', version = '1.1.3'),
        @Grab(group = 'org.codehaus.groovy.modules.http-builder', module = 'http-builder', version = '0.7.1')
])

@Slf4j
class SendPhoto {
    static void main(String[] args) {
        def config = new JsonSlurper().parseText(new File("./telegram.bot.config.json").text)
        def token = config.token
        def offset = 0
        def updates = new JsonSlurper().parseText(TelegramBotApiHelper.getUpdates(token, offset))
        log.debug("updates=[$updates]")

        def lastChatId
        updates.result.each { msg ->
            lastChatId = msg.message.chat.id
        }
        log.debug("lastChatId=[$lastChatId]")

        log.debug("config.send.fliename=[$config.send.fliename]")
        def file = new File(config.send.filename)
        log.debug("file.exists=[${file.exists()}]")
        TelegramBotApiHelper.sendPhoto(token, lastChatId, file)
    }
}

SendPhoto.main()