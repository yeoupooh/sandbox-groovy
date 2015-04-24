"""
http://www.jroller.com/aalmiray/entry/building_rich_swing_applications_with
"""

import groovy.swing.SwingBuilder  
import static javax.swing.JFrame.EXIT_ON_CLOSE  
  
SwingBuilder.build {  
   frame( title: "Swing Sample", pack: true, show: true,  
          defaultCloseOperation: EXIT_ON_CLOSE ) {  
      gridLayout( cols: 1, rows: 3 )  
      textField( id: "textField", columns: 20 )  
      label( id: "label" )  
      button( "Update", actionPerformed: { evt ->  
         label.text = textField.text  
      })  
   }  
}  

