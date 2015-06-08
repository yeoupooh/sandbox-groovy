import groovy.json.JsonSlurper
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import org.apache.http.conn.ConnectTimeoutException
import org.apache.http.conn.HttpHostConnectException
import org.ccil.cowan.tagsoup.Parser

import java.util.regex.Matcher
import java.util.regex.Pattern

import static groovyx.net.http.Method.POST

@Grapes([
        @Grab(group = 'commons-lang', module = 'commons-lang', version = '2.6'),
        @Grab(group = 'org.codehaus.groovy.modules.http-builder', module = 'http-builder', version = '0.7.1'),
        @Grab(group = 'org.ccil.cowan.tagsoup', module = 'tagsoup', version = '1.2.1')
])

public class IptimeClient {

    def configJsons, efmSessionId

    int TENSECONDS = 10 * 1000;
    int THIRTYSECONDS = 30 * 1000;

    def parseEfmSessionId = { text ->
        def cookie = ""
        Matcher m = Pattern.compile(/setCookie\('([a-zA-Z0-9]*)'\)/).matcher(text)
        if (m.find()) {
            cookie = m.group(1)
        }

        return cookie
    }

    def iptimeLoginHandler = { configJson ->
        println "Connecting..." + configJson.baseUrl
        def http = new HTTPBuilder(configJson.baseUrl)
        println "Loading...loginHandler"
        def html;

        //HTTPBuilder has no direct methods to add timeouts.  We have to add them to the HttpParams of the underlying HttpClient
        http.getClient().getParams().setParameter("http.connection.timeout", new Integer(TENSECONDS))
        http.getClient().getParams().setParameter("http.socket.timeout", new Integer(THIRTYSECONDS))

        try {
            http.request(POST, ContentType.TEXT) {
                uri.path = '/sess-bin/login_handler.cgi'
                requestContentType = ContentType.URLENC
                body = [init_status   : 1,
                        captcha_on    : 0,
                        captcha_file  : '',
                        username      : configJson.userName,
                        passwd        : configJson.password,
                        default_passwd: configJson.defaultPassword,
                        captcha_code  : '']

                response.success = { resp, reader ->
                    //println "reader=" + reader
                    assert reader instanceof Reader
                    //println "POST response status: ${resp.statusLine}"

                    assert resp.statusLine.statusCode == 200

                    //println "resp=" + resp
                    html = reader.getText()
                    //println "loginHandlerHtml=" + html
                    //println "resp.contentType=" + resp.contentType
                    //println "resp.data=" + resp.getData()

                    def cookies = []
                    resp.getHeaders('Set-Cookie').each {
                        //[Set-Cookie: JSESSIONID=E68D4799D4D6282F0348FDB7E8B88AE9; Path=/frontoffice/; HttpOnly]
                        String cookie = it.value.split(';')[0]
                        println "Adding efmSessionId to collection: $cookie"
                        cookies.add(cookie)
                    }
                }

                response.failure = { resp, reader ->
                    throw new RuntimeException(reader.text);
                }
            } // http.request
        } catch (HttpHostConnectException e) {
            throw new RuntimeException(e);
        } catch (ConnectTimeoutException e) {
            throw new RuntimeException(e);
        }

        print "Done...loginHandler..."

        if (html.indexOf('setCookie') >= 0) {
            println "OK"
        } else {
            println "Unexpected response"
        }

        return html;
    }

    def loadConfig() {
        def confRes = this.getClass().getResource('/iptime.config.json')
        if (confRes == null) {
            return null
        }
        configJsons = new JsonSlurper().parseText(confRes.text)
        return configJsons
    }

    def getWolList(config) {
        def loginHandlerHtml = iptimeLoginHandler(config)
        efmSessionId = parseEfmSessionId(loginHandlerHtml)
        def loginHtml = iptimeLogin(config, efmSessionId)
        def wolHtml = iptimeWol(config, efmSessionId)
        def wol = parseWol(wolHtml)

        return wol
    }

    def parseWol(def html) {
        def tagsoupParser = new Parser()
        def slurper = new XmlSlurper(tagsoupParser)
        def htmlParser = slurper.parseText(html)
        def wol = []
        def pc = [:]
        htmlParser.'**'.findAll { it.@class == 'item_td' }.eachWithIndex { it, index ->
//        println "index=" + index
//        println "it=" + (it instanceof groovy.util.slurpersupport.GPathResult)
//        println "it.input.@name=" + (it.input.@name instanceof groovy.util.slurpersupport.GPathResult)
//        println "it.text()=" + it.text()
//        println "it.input.@name=" + it.input.@name
//        println "it.input.@value=" + it.input.@value
            if (index >= 5 && index % 5 == 0) {
                pc['name'] = it.text()
            }
            if (index >= 5 && index % 5 == 1) {
                pc['mac'] = it.input.@value
                wol.add([name: pc['name'], mac: pc['mac']])
            }
        }
        //println "wol=" + wol

        return wol
    }

    def iptimeWol(def configJson, def efmSessionId) {
        def http = new HTTPBuilder(configJson.baseUrl)
        def html;
        println "Loading...WOL"

        http.request(Method.GET, ContentType.TEXT) { req ->
            uri.path = '/sess-bin/timepro.cgi'
            uri.query = [tmenu: "expertconf", smenu: "remotepc"]
            headers.'Cookie' = "efm_session_id=$efmSessionId;"

            response.success = { resp, reader ->
                html = reader.getText()
            }

            response.failure = { resp, reader ->
                println "failure: statusLine=" + resp.statusLine
                println "failure: text=" + reader.text
            }
        }

        print "Done...WOL..."

        if (html.indexOf('wakeupchk') >= 0) {
            println "OK"
        } else {
            println "Unexpected response"
        }

        return html
    }

    def iptimeLogin(def configJson, def efmSessionId) {
        def http = new HTTPBuilder(configJson.baseUrl)
        def html;
        println "Loading...login"
        http.request(Method.GET, ContentType.TEXT) { req ->
            uri.path = '/sess-bin/login.cgi'
//        headers.'User-Agent' = 'Mozilla/5.0 Ubuntu/8.10 Firefox/3.0.4'
            headers.'Cookie' = "efm_session_id=$efmSessionId;"

            response.success = { resp, reader ->
                //println 'Headers: -----------'
                resp.headers.each { h ->
                    //println " ${h.name} : ${h.value}"
                }
                html = reader.getText()
            }
        }
        print "Done...login..."

        if (html.indexOf('<title>EFM Networks ipTIME') >= 0) {
            println "OK"
        } else {
            println "Unexpected response"
        }

        return html
    }

    def iptimeWakeUp(def configJson, def pc) {
        def http = new HTTPBuilder(configJson.baseUrl)
        def html;
        println "Loading...Wakeup:" + pc.toString()
        http.request(Method.GET, ContentType.TEXT) { req ->
            uri.path = '/sess-bin/timepro.cgi'
            uri.query = [tmenu: "expertconf", smenu: "remotepc", act: "wake", wakeupchk: pc.mac]
            headers.'Cookie' = "efm_session_id=$efmSessionId;"

            response.success = { resp, reader ->
                html = reader.getText()
            }

            response.failure = { resp, reader ->
                println "failure: statusLine=" + resp.statusLine
                println "failure: text=" + reader.text
            }
        }

        print "Done...Wakeup..."

        boolean success = false
        if (html.indexOf('wakeupchk') >= 0) {
            println "OK"
            success = true
        } else {
            println "Unexpected response"
            success = false
        }

        return success
    }
}
