import groovy.json.JsonSlurper
import org.eclipse.jetty.websocket.api.Session
import org.eclipse.jetty.websocket.client.WebSocketClient

import java.util.concurrent.Future

@Grapes([
        @Grab(group = 'ch.qos.logback', module = 'logback-classic', version = '1.1.3'),
        @Grab(group = 'org.eclipse.jetty.websocket', module = 'websocket-common', version = '9.2.11.v20150529'),
        @Grab(group = 'org.eclipse.jetty.websocket', module = 'websocket-client', version = '9.2.11.v20150529'),
        @Grab(group = 'org.eclipse.jetty.websocket', module = 'websocket-server', version = '9.2.11.v20150529')
])

// NOTE: To run this, use run-script.sh due to classpath

class EventBot {

    def chatMessages = [:]
    def token
    def eventConfig
    def telegramConfig

    def updateUpdates() {
        // Find chats from updates
        def offset = 0
        def updates = TelegramBotApiHelper.getUpdates(token, offset)
        def updatesJson = new JsonSlurper().parseText(updates)
        if (updatesJson.result.size() > 0) {
            updatesJson.result.each { update ->
                println "update=[$update]"
                println "update.message.chat=[$update.message.chat]"
                println "update.message.chat.id=[$update.message.chat.id]"
                def cid = update.message.chat.id
                chatMessages[cid] = update.message.chat
            }
        } else {
            println "No updates"
        }
    }

    def notifyToChats(message) {
        if (chatMessages.size() > 0) {
            chatMessages.each { it ->
                println "chat:key=[$it.key],value=[$it.value]"
                TelegramBotApiHelper.sendMessage(token, it.value.id, message)
            }
        }
    }

    def start() {

        eventConfig = new JsonSlurper().parseText(new File("./event.config.json").text)
        telegramConfig = new JsonSlurper().parseText(new File("./telegram.bot.config.json").text)
        token = telegramConfig.token

        URI uri = URI.create(eventConfig.url);

        updateUpdates()
        notifyToChats("Starting bot...")

        WebSocketClient client = new WebSocketClient();
        try {
            try {
                client.start();
                // The socket that receives events
                EventSocket socket = new EventSocket();
                socket.listener = (IMessageListener) Class.forName(eventConfig.listener).newInstance()
                socket.listener.bot = this

                // Attempt Connect
                Future<Session> fut = client.connect(socket, uri);
                // Wait for Connect
                Session session = fut.get();

                // Send a message
                session.getRemote().sendString(eventConfig.message);

                while (true) {
                    println("Sleeping...")
                    sleep(1000);
                }
                // Close session
                session.close();
            }
            finally {
                client.stop();
            }
        }
        catch (Throwable t) {
            t.printStackTrace(System.err);
        }
    }

    static void main(String[] args) {
        EventBot bot = new EventBot()
        bot.start()
    }
}

EventBot.main()