import java.util.LinkedList;


class Point{
    double x,y;
    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }
}

public class Planet {String name;
    double mass, x, y, velX, velY,accX,accY,old_accX,old_accY;
    LinkedList<Point> positions = new LinkedList<Point>();
    final int MAX_NUM_POINTS = 100000;

    public Planet(String name, double mass, double x, double y, double velX, double velY){
        this.name = name;
        this.mass = mass;
        this.x = x;
        this.y = y;
        this.velX =velX;
        this.velY = velY;
        this.accX=  0;
        this.accY=0;
        this.old_accX=0;
        this.old_accY=0;
        positions.add(new Point(x,y));
    }


    public Planet(String name, double mass, double x, double y){
        this.name = name;
        this.mass = mass;
        this.x = x;
        this.y = y;
        this.velX =0;
        this.velY = 0;
        this.accX=  0;
        this.accY=0;
        this.old_accX=0;
        this.old_accY=0;
    }
    public void setPosition(double x, double y){
        this.x = x;
        this.y = y;
        positions.addFirst(new Point(x,y));
        if(positions.size()>MAX_NUM_POINTS)
            positions.removeLast();
    }


    }

