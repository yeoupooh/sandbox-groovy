import groovy.swing.SwingBuilder

import static javax.swing.JFrame.EXIT_ON_CLOSE

SwingBuilder.build {
    frame(title: "Swing Sample", pack: true, show: true,
            defaultCloseOperation: EXIT_ON_CLOSE) {
        gridLayout(cols: 1, rows: 4)
        textField(id: "textField", columns: 20)
        label(id: "label")
        button("Update", actionPerformed: { evt ->
            label.text = textField.text
        })
        button("Run", actionPerformed: { evt ->
            println "Run"
            label.text = textField.text
        })
    }
}  

