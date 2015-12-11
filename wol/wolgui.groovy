import groovy.json.JsonSlurper
import groovy.swing.SwingBuilder

import javax.swing.*
import javax.swing.table.TableCellEditor
import javax.swing.table.TableCellRenderer
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

swing = new SwingBuilder()
swing.edt {
    lookAndFeel 'nimbus'
}

def model = []

class ButtonRenderer extends JButton implements TableCellRenderer {
    @Override
    Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText('Send')
        setOpaque(true)
        return this
    }
}

class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
    int row
    int col
    protected JButton button
    private ITableCellClickListener actionListener

    ButtonEditor(actionListener) {
        this.actionListener = actionListener
        button = new JButton()
        button.setOpaque(true)
        button.addActionListener(new ActionListener() {
            @Override
            void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        stopCellEditing();
                        if (actionListener != null) {
                            actionListener.onClick(row, col)
                        }

                    }
                });
            }
        })
    }

    @Override
    Object getCellEditorValue() {
        return ""
    }

    @Override
    public boolean isCellEditable(EventObject e) {
        return true
    }

    @Override
    Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        button.setText('Send')
        this.row = row
        this.col = column
        return button
    }
}

interface ITableCellClickListener {
    void onClick(int row, int col)
}

swing.frame(title: 'Wake On Lan', size: [600, 300], defaultCloseOperation: JFrame.EXIT_ON_CLOSE) {
    scrollPane {
        t = table(rowHeight: 30) {
            tm = tableModel(list: model) {
                closureColumn(header: 'Name', read: { row -> return row.name })
                closureColumn(header: 'IP Address', read: { row -> return row.ip })
                closureColumn(header: 'Mac Address', read: { row -> return row.mac })
                propertyColumn(
                        header: "Action",
                        propertyName: 'action',
                        cellRenderer: new ButtonRenderer(),
                        cellEditor: new ButtonEditor(new ITableCellClickListener() {
                            @Override
                            void onClick(int row, int col) {
                                wakeUp(model[row])
                                JOptionPane.showMessageDialog(t, 'WOL Packet Sent!')
                            }
                        })

                )
            }

        }
    }

}.setVisible(true)

def wakeUp(host) {
    new GroovyShell().parse(new File('wol.groovy')).with { wol ->
        wol.wakeUp(host.ip, host.mac)
    }
}

new Thread({
    def config = new JsonSlurper().parse(new File("wol.config.json"))
    println config
    config.hosts.each { host ->
        model.add(host)
    }
    SwingUtilities.invokeLater(new Runnable() {
        @Override
        void run() {
            tm.fireTableDataChanged()
        }
    })
}).start()
