public class TelegramBotApiHelper {

    /**
     *
     * @param token
     * @param botMethod
     * @param query
     * @return
     */
    public static String executeCommand(String token, String botMethod, String query) {
        String url = "https://api.telegram.org/bot" + token + "/" + botMethod + "?" + query
        return HttpClientHelper.getUrl(url);
    }

    /**
     *
     * @param token
     * @return
     */
    public static String getUpdates(String token, int offset) {
        if (offset > 0) {
            return executeCommand(token, "getUpdates", "offset=" + offset)
        } else {
            return executeCommand(token, "getUpdates", "")
        }
    }

    /**
     *
     * @param token
     * @param chatId
     * @param text
     * @return
     */
    public static String sendMessage(String token, int chatId, String text) {
        return executeCommand(token, "sendMEssage", "chat_id=" + chatId + "&text=" + text)
    }
}