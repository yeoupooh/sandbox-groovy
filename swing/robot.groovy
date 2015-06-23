// http://www.javaprogrammingforums.com/java-se-api-tutorials/214-java-program-contol-computer-mouse-using-awt-robot-events.html

import java.awt.*
import java.awt.event.*

Robot robot = new Robot();

// SET THE MOUSE X Y POSITION
robot.mouseMove(300, 550);

// LEFT CLICK
robot.mousePress(InputEvent.BUTTON1_MASK);
robot.mouseRelease(InputEvent.BUTTON1_MASK);