#!/usr/bin/env groovy

import groovy.swing.SwingBuilder

import javax.swing.*
import java.awt.*

def wolModel = new DefaultListModel()

def ic = new IptimeClient()

def configJsons, cmb, frame, swing
JList uiList

def loadConfig = {
    println "loadConfig"
    //println "wolModel=" + wolModel
    //wolModel.add(0, "test")
    //wolModel.add("test")
    configJsons = ic.loadConfig()
    if (configJsons == null) {
        swing.optionPane().showMessageDialog(null,
            "Can't load config file.",
            "Wakeup On LAN",
            JOptionPane.ERROR_MESSAGE)
        return
    }
    println configJsons
    cmb.clear()
    configJsons.each { config ->
        cmb.addItem(config.baseUrl)
    }
    frame.pack()
}

def wakeUp = {
    println uiList
    println uiList.getSelectedValue()
    //ic.connect()
    def success = ic.iptimeWakeUp(configJsons[cmb.selectedIndex], uiList.getSelectedValue())
    //println html

    def msg = (success) ? "Success" : "Failed"

    swing.optionPane().showMessageDialog(null,
            msg,
            "Wakeup On LAN",
            JOptionPane.INFORMATION_MESSAGE)
}

def getWolList = {
    try {
        def wolList = ic.getWolList(configJsons[cmb.selectedIndex]);

        println wolList
        wolModel.clear()
        wolList.each { wol ->
            wolModel.addElement(wol)
        }
    } catch (RuntimeException e) {
        swing.optionPane().showMessageDialog(null,
                e.getMessage(),
                "Wakeup On LAN",
                JOptionPane.ERROR_MESSAGE)
    }
}

swing = new SwingBuilder();
frame = swing.frame(title: 'ipTime WOL', defaultCloseOperation: JFrame.DISPOSE_ON_CLOSE,
        size: [400, 200], show: true, locationRelativeTo: null) {
    lookAndFeel("nimbus")
    borderLayout()
    panel(constraints: BorderLayout.NORTH) {
        button(label: 'Load config', actionPerformed: {
            loadConfig()
            println "load config pressed"
        })
        cmb = comboBox(items: [], itemStateChanged: {
            //comboitemStateChanged()
        })
        button(label: 'Get WOL list', actionPerformed: {
            println "wake up pressed"
            getWolList()
        })
        button(label: 'Wake Up', actionPerformed: {
            println "wake up pressed"
            wakeUp()
        })
    }
    scrollPane(constraints: BorderLayout.CENTER) {
        uiList = list(visibleRowCount: 5, visible: true, model: wolModel) {
        }
    }
}

frame.pack()

//wolModel.load();

println "frame is done"

