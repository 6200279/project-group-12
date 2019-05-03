import java.util.LinkedList;

class Point{      
    double x,y;
    public Point(double x, double y){ //
        this.x = x;
        this.y = y;


    }
}
public class Rover {


    String name;
    double mass, x, y,accX,accY,old_accX,old_accY,velX, velY;
    LinkedList<Point> positions = new LinkedList<Point>();
    final int MAX_NUM_POINTS = 100000;

    public Rover(String name, double mass, double x, double y,double velX, double velY) {
        this.name = name;
        this.mass = mass;
        this.x = x;
        this.y = y;
        this.velX = velX;
        this.velY = velY;



    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
        positions.addFirst(new Point(x,y));
        if(positions.size()>MAX_NUM_POINTS)
            positions.removeLast();

    }
}
