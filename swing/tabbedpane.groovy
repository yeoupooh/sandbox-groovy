// http://stackoverflow.com/questions/6483261/groovy-swingbuilder-controlling-the-style-of-the-tabs-titles-in-jtabbedpane

import groovy.swing.SwingBuilder

import javax.swing.*
import javax.swing.WindowConstants as WC
import java.awt.*

Font font = new Font("Serif", Font.BOLD, 13)
int numPanels = 20

swing = new SwingBuilder()

swing.edt {
    lookAndFeel 'nimbus'
}

frame = swing.frame(title:'test', pack:true,   visible:true, defaultCloseOperation:WC.DISPOSE_ON_CLOSE) {
    vbox {
        tabbedPane(id: 'tabs', tabLayoutPolicy:JTabbedPane.SCROLL_TAB_LAYOUT) {
            panel( name:'Tab 1', background:java.awt.Color.WHITE ) {
                vbox {
                    panel( background:java.awt.Color.WHITE ){
                        label ( 'Label 1', preferredSize: [104, 24]).setFont(font)
                        label ( 'Label 2', preferredSize: [104, 24]).setFont(font)
                        label ( 'Label 3', preferredSize: [104, 24]).setFont(font)
                    }
                    scrollPane( verticalScrollBarPolicy:JScrollPane.VERTICAL_SCROLLBAR_ALWAYS) {
                        vbox {
                            (1..numPanels).each { num ->
                                def panelID = "panel$num"
                                def pane = panel( alignmentX:0f, id:panelID, background:java.awt.Color.GREEN ) {
                                    label('description')
                                    textField( id: "description$num", text:panelID, columns: 70 )
                                    button( id: "buttonpanel$num", text:panelID, actionPerformed:{
                                        swing."$panelID".background = java.awt.Color.RED
                                    } )
                                }
                            }
                        }
                    }
                }
            }
            panel(name: 'Tab 2', background:java.awt.Color.WHITE) {
                textField(text: 'Some text', columns: 15)
                scrollPane() {
                    textArea(text: 'Some text', columns: 15, rows: 4)
                }
            }
        }
        panel(id:'secondPanel', background:java.awt.Color.WHITE){
            button('Quit', actionPerformed:{
                dispose()
            })
        }
    }
}

// Define a list of labels for our tabs
def tabComponents = [
        swing.label( text:'Tab 1', font:font.deriveFont( Font.ITALIC ) ),
        swing.label( text:'Tab 2', font:font.deriveFont( 20.0f ) )
]
// Set the tab components to our labels
tabComponents.eachWithIndex { lbl, idx ->
    swing.tabs.setTabComponentAt idx, lbl
}

frame.size = [ frame.width, 600 ]