import groovyx.net.http.*

import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*

@Grab(group = 'org.codehaus.groovy.modules.http-builder', module = 'http-builder', version = '0.6')

def testMirrorRest = {

    def http = new HTTPBuilder('http://restmirror.appspot.com/')
    http.request(POST) {
        uri.path = '/'
        requestContentType = ContentType.URLENC
        body = [name: 'bob', title: 'construction worker']

        response.success = { resp, html ->
            println "POST response status: ${resp.statusLine}"

            assert resp.statusLine.statusCode == 201

            println html.text

        }
    }
}

testMirrorRest()