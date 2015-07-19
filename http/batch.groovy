import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method

@Grapes([
        @Grab(group = 'ch.qos.logback', module = 'logback-classic', version = '1.1.3'),
        @Grab(group = 'org.codehaus.groovy.modules.http-builder', module = 'http-builder', version = '0.7.1')
])

@Slf4j
class HttpHelper {
    static String request(options, cookies = []) {
        def method = (options.method == "post") ? Method.POST : Method.GET
        def contentType
        switch (options.contentType) {
            case "urlenc":
                contentType = ContentType.URLENC
                break
            case "text":
                contentType = ContentType.TEXT
                break
            case "binary":
                contentType = ContentType.BINARY
                break
            default:
                log.error("No content type defined")
                return
        }
        def http = new HTTPBuilder(options.url)
        def ret

        log.info("Requesting...method=[$method],url=[$options.url],path=[$options.path],query=[$options.query]")
        http.request(method, contentType) {
            uri.path = options.path
            if (method == Method.POST) {
                body = options.query
            } else {
                uri.query = options.query
            }
            headers.'User-Agent' = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36'
            headers.'Cookie' = cookies.join(';')
            response.success = { resp, reader ->

                if (contentType == ContentType.URLENC) {
                    ret = reader
                    log.debug("ret=[$ret]")
                } else if (contentType == ContentType.BINARY) {
                    File file = new File(options.response.filename)
                    if (file.parentFile.exists() == false) {
                        file.parentFile.mkdirs()
                    }
                    def fos = new FileOutputStream(file)
                    int readBytes
                    byte[] buf = new byte[1024 * 8]
                    while ((readBytes = reader.read(buf, 0, buf.length)) != -1) {
                        log.debug("readBytes=[$readBytes]")
                        fos.write(buf, 0, readBytes)
                    }
                    fos.close()
                } else {
                    ret = reader.text
                }

//                log.debug("response status: ${resp.statusLine}")
//                log.debug('Headers: -----------')
//                resp.headers.each { h ->
//                    log.debug(" ${h.name} : ${h.value}")
//                }

                if (options.useCookie == true) {
                    resp.getHeaders('Set-Cookie').each {
                        String cookie = it.value.split(';')[0]
//                        log.debug("cookie=[$cookie]")
                        cookies.add(cookie)
                    }
                }

            } // response.success

            response.failure = { resp ->
                log.error("failed")
            }

        } // http.request

        return ret
    }
}

@Slf4j
class BatchHttpClient {
    static void main(String[] args) {
        def configRoot = new JsonSlurper().parseText(new File("batch.config.json").text);
        def config = configRoot.groups[configRoot.selected]
        log.debug("requests=[$config.requests]")

        def cookies = []
        config.requests.each { req ->
            log.debug("req=[$req]")
            def ret = HttpHelper.request(req, cookies)
//    log.debug("ret=[$ret]")
            if (ret != null) {
                log.debug("ret.len=[${ret.length()}]")
            } else {
                log.debug("ret is null")
            }
            log.debug("req.response.expected=[$req.response.expected]")
            def success = false
            if (req.response.expected.isEmpty() == false) {
                if (ret != null && ret.indexOf(req.response.expected) > -1) {
                    log.info("Found expectation")
                    success = true
                } else {
                    log.warn("Not matched expectation: : expected=[$req.response.expected], actual=[$ret]")
                }
            } else {
                log.info("No expectation")
                success = true
            }

            if (success == true) {
                log.info("OK")
            } else {
                log.warn("Fail")
                if (req.response.stopOnFail == true) {
                    log.warn("Stopped")
                    return
                }
            }
        }
    }
}

BatchHttpClient.main()
