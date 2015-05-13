"""
http://stackoverflow.com/questions/6392965/groovy-swingbuilder-button-to-change-the-color-of-a-panel
"""

import groovy.swing.SwingBuilder

import javax.swing.BoxLayout as BXL
import javax.swing.WindowConstants as WC

swing = new SwingBuilder()
frame = swing.frame(title: 'test', pack: true, visible: true, defaultCloseOperation: WC.DISPOSE_ON_CLOSE) {
    panel(id: 'mainPanel') {
        (1..6).each { num ->
            def panelID = "panel$num"
            def pane = panel(alignmentX: 0f, id: panelID, background: java.awt.Color.GREEN) {
                label('description')
                textField(id: "description$num", text: panelID, columns: 50)
                button(id: "buttonpanel$num", text: panelID, actionPerformed: {
                    swing."$panelID".background = java.awt.Color.RED
                })
            }
        }
        boxLayout(axis: BXL.Y_AXIS)

        panel(id: 'secondPanel', alignmentX: 0f) {
            button('Quit', actionPerformed: {
                frame.visible = false
                System.exit(1)
            })
        }
    }
}

