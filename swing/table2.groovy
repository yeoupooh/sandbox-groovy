import groovy.swing.SwingBuilder

import javax.swing.WindowConstants
import java.awt.*

import javax.swing.event.ListSelectionListener

data = [[first: 'qwer', last: 'asdf'],
        [first: 'zxcv', last: 'tyui'],
        [first: 'ghjk', last: 'bnm']]

swing = new SwingBuilder()
frame = swing.frame(title: 'table test', defaultCloseOperation: WindowConstants.DISPOSE_ON_CLOSE) {
    def tab = table(constraints: BorderLayout.CENTER) {

        current.selectionModel.addListSelectionListener({ e ->
            println "selected: ${e.toString()}"
        } as ListSelectionListener)

        tableModel(list: data) {
            propertyColumn(header: 'First Name', propertyName: 'first')
            propertyColumn(header: 'Last Name', propertyName: 'last')
        }
    }
    widget(constraints: BorderLayout.NORTH, tab.tableHeader)
    button(constraints: BorderLayout.SOUTH, text: "Add", actionPerformed: {
        data.add([first: 'a', last: 'b']);
        frame.pack()
    })
}
frame.pack()
frame.setVisible(true)