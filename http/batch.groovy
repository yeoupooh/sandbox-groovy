import groovy.json.JsonSlurper
import groovyx.net.http.ContentType
import groovyx.net.http.Method
import groovyx.net.http.HTTPBuilder

@Grab(group = 'org.codehaus.groovy.modules.http-builder', module = 'http-builder', version = '0.7.2')

class HttpHelper {
    static String request(options, cookies = []) {
        def method = (options.method == "post") ? Method.POST : Method.GET
        def url = options.url
        def path = options.path
        def query = options.query

        def http = new HTTPBuilder(url)
        def ret

        println "Requesting...[$url],path=[$path],query=[$query]"
        http.request(method, ContentType.TEXT) {
            uri.path = path
            uri.query = query
            headers.'User-Agent' = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36'
            headers.'Cookie' = cookies.join(';')
            response.success = { resp, reader ->

                ret = reader.text
                println "ret=[$ret]"

                println "response status: ${resp.statusLine}"
                println 'Headers: -----------'
                resp.headers.each { h ->
                    println " ${h.name} : ${h.value}"
                }

                if (options.useCookie == true) {
                    resp.getHeaders('Set-Cookie').each {
                        String cookie = it.value.split(';')[0]
                        println "cookie=[$cookie]"
                        cookies.add(cookie)
                    }
                }

            } // response.success

            response.failure = { resp, reader ->
                println "failed $reader.text"
            }

        } // http.request

        return ret
    }
}

def config = new JsonSlurper().parseText(new File("batch.config.json").text);
println "requests=[$config.requests]"

def cookies = []
config.requests.each { req ->
    println "req=[$req]"
    def ret = HttpHelper.request(req, cookies)
    println "ret=[$ret]"
}
