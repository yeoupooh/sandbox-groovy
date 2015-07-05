import groovy.json.JsonSlurper
import groovyx.net.http.HTTPBuilder
import org.apache.http.conn.scheme.Scheme
import org.apache.http.conn.ssl.SSLSocketFactory

import java.security.KeyStore
import static groovyx.net.http.Method.HEAD

@Grapes([
        @Grab(group = 'org.codehaus.groovy.modules.http-builder', module = 'http-builder', version = '0.7.1'),
        @Grab(group = 'org.apache.httpcomponents', module = 'httpclient', version = '4.5')
])

// https://github.com/jgritman/httpbuilder/blob/master/src/test/groovy/groovyx/net/http/SSLTest.groovy
// http://humbleprogrammer.wikidot.com/groovy-ssl

def config = new JsonSlurper().parseText(new File("./telegram.bot.config.json").text)

String token = config.token
String host = "https://api.telegram.org/bot" + token + "/getUpdates"

println "host=" + host
def http = new HTTPBuilder(host)

def keyStore = KeyStore.getInstance(KeyStore.defaultType)

getClass().getResource(config.keystoreResource).withInputStream {
    keyStore.load(it, config.keystorePassword.toCharArray())
}

final socketFactory = new SSLSocketFactory(keyStore)
socketFactory.hostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER

http.client.connectionManager.schemeRegistry.register(
        new Scheme("https", socketFactory, 443))

def status = http.request(HEAD) {
    response.success = {
        println response.text
        return it.statusLine.statusCode
    }
    response.failure = {
        it.statusLine.statusCode
    }

}

println status
