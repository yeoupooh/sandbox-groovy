@Grapes([
        @Grab('org.codehaus.groovyfx:groovyfx:0.4.0')
])

import static groovyx.javafx.GroovyFX.start

start {
    stage(title: 'GroovyFX Hello World', visible: true) {
        scene(fill: BLACK, width: 600, height: 250) {
            rectangle(width: 1, height: 1, fill: GREEN)
            hbox(padding: 60) {
                text(text: 'Groovy', font: '80pt sanserif') {
                    fill linearGradient(endX: 0, stops: [PALEGREEN, SEAGREEN])
                }
                text(text: 'FX', font: '80pt sanserif') {
                    fill linearGradient(endX: 0, stops: [CYAN, DODGERBLUE])
                    effect dropShadow(color: DODGERBLUE, radius: 25, spread: 0.25)
                }
                def wv = webView()
                def we = wv.getEngine()
                we.load('http://google.com')
            }
        }
    }
}
