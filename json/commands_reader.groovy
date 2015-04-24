import groovy.swing.SwingBuilder
import javax.swing.WindowConstants as WC
import javax.swing.BoxLayout as BXL

import groovy.json.JsonSlurper

File f = new File( '.', 'commands.json' )
if( f.exists() ) {
    def json = f.withReader { r ->
        new JsonSlurper().parse( r )
    }
    println json.commands.size();
    for (i = 0; i < json.commands.size(); i++) {
        println json.commands[i].name
        println json.commands[i].command
    }
    
swing = new SwingBuilder()
frame = swing.frame(title:'test', pack:true, visible:true, defaultCloseOperation:WC.DISPOSE_ON_CLOSE) {
    panel(id:'mainPanel') {
        (1..json.commands.size()).each { num ->
            def panelID = "panel$num"
            def pane = panel( id:panelID ) {
            	println json.commands[num-1].name
                label(json.commands[num-1].name) 
                textField(id: "description$num", columns: 50, text:json.commands[num-1].command )
                button(id: "buttonpanel$num", text:"Execute", actionPerformed : {
                	println "buttonpanel$num"
                })
            }
        }
        boxLayout(axis: BXL.Y_AXIS)
    }
}    
}

