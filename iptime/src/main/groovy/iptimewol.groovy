


import groovy.json.JsonSlurper
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method

import java.util.regex.Matcher
import java.util.regex.Pattern

import static groovyx.net.http.Method.POST

@Grab(group = 'org.codehaus.groovy.modules.http-builder', module = 'http-builder', version = '0.6')
@Grab(group = 'org.ccil.cowan.tagsoup', module = 'tagsoup', version = '1.2')

def iptimeLoginHandler = { configJson ->
    def http = new HTTPBuilder(configJson.baseUrl)
    println "Loading...loginHandler"
    def html;
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
    } // http.request

    print "Done...loginHandler..."

    if (html.indexOf('setCookie') >= 0) {
        println "OK"
    } else {
        println "Unexpected response"
    }

    return html;
}

def loadConfig = {
    def configFile = new File("iptime.config.json")
    def configJson = new JsonSlurper().parseText(configFile.text)
    return configJson
}

def parseEfmSessionId = { text ->
    def cookie = "";
    Matcher m = Pattern.compile(/setCookie\('([a-zA-Z0-9]*)'\)/).matcher(text)
    if (m.find()) {
        cookie = m.group(1)
    }

    return cookie
}

def iptimeLogin = { configJson, efmSessionId ->
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

def iptimeWol = { configJson, efmSessionId ->
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

def parseWol = { html ->
    def tagsoupParser = new org.ccil.cowan.tagsoup.Parser()
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

def iptimeWakeup = { configJson, efmSessionId, pc ->
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

    if (html.indexOf('wakeupchk') >= 0) {
        println "OK"
    } else {
        println "Unexpected response"
    }

    return html
}

def config = loadConfig()
def loginHandlerHtml = iptimeLoginHandler(config)
def efmSessionId = parseEfmSessionId(loginHandlerHtml)
//println "efmSessionId=" + efmSessionId
def loginHtml = iptimeLogin(config, efmSessionId)
//println loginHtml
def wolHtml = iptimeWol(config, efmSessionId)
//println wolHtml
def wol = parseWol(wolHtml)
wol.eachWithIndex { pc, index ->
    println "$index) $pc.name ($pc.mac)"
}
def select = System.console().readLine "Which one do you want to wake up? "
def wakeupHtml = iptimeWakeup(config, efmSessionId, wol[select.toInteger()])
