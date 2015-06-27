import org.telegram.api.*
import org.telegram.mtproto.*
import org.telegram.tl.*
import org.telegram.api.engine.*;
import org.telegram.bot.engine.*;

@Grapes([
        @Grab(group = 'org.telegram-s', module = 'mtproto', version = '1.5.0'),
        @Grab(group = 'org.telegram-s', module = 'tl-core', version = '1.2.0')
])

def a
print a

// TODO set up AbsApiState, AppInfo and ApiCallback objects
def useTest = true
def state = new MemoryApiState(useTest)
def appInfo = new AppInfo(5, "console", "???", "???", "en")
def apiCallback = new ApiCallback() {

    @Override
    public void onAuthCancelled(TelegramApi api) {

    }

    @Override
    public void onUpdatesInvalidated(TelegramApi api) {

    }

    @Override
    public void onUpdate(TLAbsUpdates updates) {
        if (updates instanceof TLUpdateShortMessage) {
            onIncomingMessageUser(((TLUpdateShortMessage) updates).getFromId(), ((TLUpdateShortMessage) updates).getMessage());
        } else if (updates instanceof TLUpdateShortChatMessage) {
            onIncomingMessageChat(((TLUpdateShortChatMessage) updates).getChatId(), ((TLUpdateShortChatMessage) updates).getMessage());
        }
    }
}
TelegramApi api = new TelegramApi(state, appInfo, apiCallback);
