import groovy.util.logging.Slf4j
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import groovyx.net.http.*
import org.apache.http.conn.scheme.Scheme
import org.apache.http.conn.ssl.SSLSocketFactory
import org.apache.http.entity.mime.MultipartEntity
import org.apache.http.entity.mime.content.InputStreamBody

import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate

import static groovyx.net.http.Method.HEAD

@Grapes([
        @Grab(group = 'ch.qos.logback', module = 'logback-classic', version = '1.1.3'),
        @Grab(group = 'org.codehaus.groovy.modules.http-builder', module = 'http-builder', version = '0.7.1'),
//        @Grab(group = 'org.apache.httpcomponents', module = 'httpmime', version = '4.5'),
        @Grab(group = 'org.apache.httpcomponents', module = 'httpmime', version = '4.2.1'),
        @Grab(group = 'org.apache.httpcomponents', module = 'httpclient', version = '4.2.1')
//        @Grab(group = 'org.apache.httpcomponents', module = 'httpclient', version = '4.5'),
])

/**
 *
 */
@Slf4j
public class HttpClientHelper {

    static void acceptAllCerts(HTTPBuilder http) {
// https://github.com/jgritman/httpbuilder/blob/master/src/test/groovy/groovyx/net/http/SSLTest.groovy
// http://humbleprogrammer.wikidot.com/groovy-ssl
// http://stackoverflow.com/questions/11638005/is-there-an-easier-way-to-tell-httpbuilder-to-ignore-an-invalid-cert
//import org.apache.http.conn.scheme.Scheme
//import org.apache.http.conn.ssl.SSLSocketFactory
//
//import javax.net.ssl.SSLContext
//import javax.net.ssl.TrustManager
//import javax.net.ssl.X509TrustManager
//import java.security.SecureRandom
//import java.security.cert.CertificateException
//import java.security.cert.X509Certificate
//
//import static groovyx.net.http.Method.HEAD
//=== SSL UNSECURE CERTIFICATE ===
        def sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, [new X509TrustManager() {
            void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                System.out.println("checkClientTrusted =============")
            }

            void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                System.out.println("checkServerTrusted =============")
            }

            X509Certificate[] getAcceptedIssuers() {
                System.out.println("getAcceptedIssuers =============")
                return null
            }
        }] as TrustManager[], new SecureRandom())

        def sf = new SSLSocketFactory(sslContext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
        def httpsScheme = new Scheme("https", sf, 443)
        http.client.connectionManager.schemeRegistry.register(httpsScheme)
//================================

//// https://github.com/jgritman/httpbuilder/wiki/SSL
// -----------------------------------
//import org.apache.http.conn.scheme.*
//import org.apache.http.conn.ssl.*
//import java.security.*
//import javax.net.ssl.*
//def keyStore = KeyStore.getInstance(KeyStore.defaultType)
//
//getClass().getResource(config.keystoreResource).withInputStream {
//    keyStore.load(it, config.keystorePassword.toCharArray())
//}
//
//final socketFactory = new SSLSocketFactory(keyStore)
//socketFactory.hostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER
//
//http.client.connectionManager.schemeRegistry.register(
//        new Scheme("https", socketFactory, 443))
// -----------------------------------

// HTTP Request
        println "HEAD:" + HEAD.toString()
    }

/**
 *
 * @param url
 * @return
 */
    public static String getUrl(url) {
        println "url=" + url
        def http = new HTTPBuilder(url)

        // Pass SSL
        acceptAllCerts(http)

        def result
        def status = http.request(Method.GET, ContentType.TEXT) {
            response.success = { resp, reader ->
                println "response status: ${resp.statusLine}"
                result = new String(reader.text)
            }
            response.failure = {
                println "statusCode:$it.statusLine.statusCode"
            }
        }

        return result
    }

    static String post(url, Object[] body) {
        log.debug("body=[$body]")
        MultipartEntity entity = new MultipartEntity()
        body.each { it ->
            log.debug(it)
            if (it instanceof String) {
                entity.addPart(it.split("=")[0], it.split("=")[1])
            } else if (it instanceof File) {
                File file = (File) it
                entity.addPart("file", new InputStreamBody(file.inputStream,
                        file.contentType, file.originalFilename))
            }
        }
        log.debug("entity=[$entity]")

        def http = new HTTPBuilder(url)

        // Pass SSL
        acceptAllCerts(http)

        def result
        def status = http.request(Method.POST, ContentType.TEXT) {
            response.success = { resp, reader ->
                println "response status: ${resp.statusLine}"
                result = new String(reader.text)
            }
            response.failure = {
                println "statusCode:$it.statusLine.statusCode"
            }
        }

        return result

    }
}