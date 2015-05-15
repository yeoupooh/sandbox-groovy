package swing

import groovy.swing.SwingBuilder

import javax.swing.*
import java.awt.*

def swing = new SwingBuilder();
swing.frame(title: 'MyDialog', defaultCloseOperation: JFrame.EXIT_ON_CLOSE, size: [400, 300], show: true,
        locationRelativeTo: null) {
    lookAndFeel('system')
    borderLayout()
    panel(constraints: BorderLayout.CENTER){
        borderLayout()
        list(constraints: BorderLayout.CENTER) {
        }
    }
    panel(constraints: BorderLayout.SOUTH) {
        hbox() {
            button(text: 'test1', actionPerformed: {
                println 'test1 clicked'
            })
            button(text: 'test2', actionPerformed: {
                println 'test2 clicked'
            })
        }
    }
}