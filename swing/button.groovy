"""
http://groovy.codehaus.org/SwingBuilder.button
"""


import groovy.swing.SwingBuilder

import javax.swing.*
import java.awt.*

println 'hello, world'

new SwingBuilder().frame(title: 'demo using buttons', defaultCloseOperation: JFrame.EXIT_ON_CLOSE, show: true, pack: true) {
    vbox() {
        button(action: action(name: 'basic', closure: { println "basic" }))
        button(action: action(name: 'mnemonic', mnemonic: 'P', closure: { println 'with mnemonic' }))
        button(action: action(name: 'default', defaultButton: true, closure: { println 'this is the default button' }))
        button(borderPainted: false, action: action(name: 'unpainted border', closure: { println 'unpainted border' }))
        button(contentAreaFilled: false, action: action(name: 'unfilled content area', closure: {
            println 'unfilled content area'
        }))
        button(focusPainted: false, action: action(name: 'unpainted focus (select it and you\'ll see)', closure: {
            println 'unpainted focus'
        }))
        button(margin: new Insets(5, 10, 15, 20), action: action(name: 'margins', closure: { println 'margins' }))
    }
}

