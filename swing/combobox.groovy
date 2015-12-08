import groovy.beans.Bindable
import groovy.swing.SwingBuilder

import javax.swing.*

swing = new SwingBuilder()

class Item {
    String name
    int value

    @Override
    String toString() {
        return String.format('name=[%s],value=[%s]', name, value)
    }
}

class ItemModel {
    ComboBoxModel model = new DefaultComboBoxModel()
    @Bindable
    Item selectedItem;
}

cmbModel = new ItemModel()

swing.frame(title: 'Combobox Demo', defaultCloseOperation: JFrame.EXIT_ON_CLOSE, location: [200, 100], size: [400, 300]) {
    vbox {
        comboBox(model: cmbModel.model, selectedItem: bind(target: cmbModel, targetProperty: 'selectedItem'))
        hbox {
            button('Add', actionPerformed: {
                addNewItem()
            })
            button('Check', actionPerformed: {
                println(cmbModel.selectedItem)
            })
        }
    }
}.setVisible(true)

def addNewItem() {
    def n = cmbModel.model.size
    cmbModel.model.addElement(new Item(name: 'test' + n, value: n))
}

new Thread({
    for (int i = 0; i < 10; i++) {
        addNewItem()
        sleep(1000)
    }
}).start()