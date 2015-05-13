import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent

def panel = new JPanel(new BorderLayout())
panel.add(new JLabel("Header"), BorderLayout.PAGE_START)
panel.add(new JList(*(1..10)), BorderLayout.CENTER)

def button = new JButton(new AbstractAction("Footer") {
    {
        putValue(ACCELERATOR_KEY, "Ctrl+K");
    }

    @Override
    void actionPerformed(ActionEvent actionEvent) {
        println "Footer pressed"
    }
})
panel.add(button, BorderLayout.PAGE_END)

def frame = new JFrame()
frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
frame.contentPane = panel
frame.pack()
frame.visible = true
