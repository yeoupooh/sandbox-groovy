import java.awt.*

while(true){
    Thread.sleep(100);
    System.out.println("("+MouseInfo.getPointerInfo().getLocation().x+", "+MouseInfo.getPointerInfo().getLocation().y+")");
}