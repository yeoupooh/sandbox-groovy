class SimpleMessageListener implements IMessageListener {
    def bot

    void onMessage(msg) {
        println "msg=[$msg]"
        bot.updateUpdates()
        bot.notifyToChats(msg)
    }
}