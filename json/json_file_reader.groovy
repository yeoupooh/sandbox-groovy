"""
http://stackoverflow.com/questions/19522919/reading-json-object-from-txt-file-in-groovy
"""

import groovy.json.JsonSlurper

File f = new File( '.', 'jsonObject.txt' )
if( f.exists() ) {
    def game = f.withReader { r ->
        new JsonSlurper().parse( r )
    }
    println game
    println game.name
    println game.value
}