import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class SolarSystem {
    static final double G = 0.000667;           //universal gravitational constant
    ArrayList<Planet> list = new ArrayList<Planet>();
    double timestep = 0;

    public SolarSystem() {

        Planet Sun = new Planet("Sun", 19.89, 0, 0);
        Planet Earth = new Planet("Earth", 0.00005974, 15.2, 0, 0, -0.029291);
        Planet Moon = new Planet("Moon", 0.0000007342, 15.2 + 0.040550, 0, 0, -0.029291 + 0.000964);


        Planet Mercury = new Planet("Mercury", 0.00000330, -5.7, 0);
        Planet Venus = new Planet("Venus", 0.0000487, -10.8, 0);
//		Planet Earth = new Planet("Earth",0.00005974, 14.9,0);
        Planet Mars = new Planet("Mars", 0.00000642, +22.8, 0);
        Planet Jupiter = new Planet("Jupiter", 0.01899, -81.7, 0, 0, -0.01307);
        Planet Titan = new Planet("Titan", 0.0000013452,-142.4 +0.125, 0,0, -0.1352- 0.00558 );
        Planet Saturn = new Planet("Saturn", 0.00568, -142.4, 0, 0, -0.1352 );

        Planet Uranus = new Planet("Uranus", 0.000866, +287.2, 0);
        Planet Neptune = new Planet("Neptune", 0.00103, +449.9, 0);


        list = new ArrayList<Planet>();
        list.add(Sun);
        list.add(Mercury);
        list.add(Venus);
        list.add(Earth);
        list.add(Mars);
        list.add(Jupiter);
        list.add(Saturn);
        list.add(Uranus);
        list.add(Neptune);
        list.add(Titan);

        for (Planet p : list) {
            if (p.name != "Sun")
                p.velY = Math.sqrt(G * Sun.mass / Math.abs(p.x - Sun.x));
            System.out.println("v = " + p.velY);
            if (p.x > Sun.x)
                p.velY = -p.velY;
        }


//		list.add(Earth);
        Moon.velY = Earth.velY + 0.000964;
        Titan.velY= Saturn.velY + 0.00000558;
        list.add(Moon);
        list.add(Titan);
        getAccelerations();
    }

    void move() {
        for (Planet p1 : list) {
            // update position according to dr = v·dt + a·dt²/2

            p1.x += timestep * (p1.velX + timestep * p1.accX / 2);
            p1.y += timestep * (p1.velY + timestep * p1.accY / 2);

            // keep accelerations for velocity calculation
            p1.old_accY = p1.accY;
            p1.old_accX = p1.accX;
            p1.setPosition(p1.x, p1.y);



        }
    }

    void getAccelerations() {
        double dx, dy, dz, D, A;
        for (Planet p1 : list) {
            p1.accX = 0;
            p1.accY = 0;
            for (Planet p2 : list) {
                if (p1 != p2) {
                    dx = p2.x - p1.x;
                    dy = p2.y - p1.y;
                    D = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
                    A = G * p2.mass / Math.pow(D, 2);
                    p1.accX += dx * A / D;
                    p1.accY += dy * A / D;
                }
            }
        }
    }

    void accelerate() {
        for (Planet p1 : list) {
            // update velocity according to dv=(a+old a) * dt/2
            p1.velX += (p1.accX + p1.old_accX) * timestep / 2;
            p1.velY += (p1.accY + p1.old_accY) * timestep / 2;
        }
    }

    void updatePositions() {
        move();
        getAccelerations();
        accelerate();
    }
}

class SolarGUI extends JPanel implements Runnable, MouseMotionListener, MouseListener, KeyListener {
    int offX=400, offY=400;
    int oldOffX=40, oldOffY=40;
    double scale=5;
    int fstClickX, fstClickY;
    SolarSystem s;
    public SolarGUI(){
        s = new SolarSystem();
        setFocusable(true);
        requestFocus();
        addMouseMotionListener(this);
        addMouseListener(this);
        addKeyListener(this);
    }

    public void paintComponent(Graphics g) {
        int i=0;
        int diameter=10;
//		double centraY = s.list.get(0).y;
//		double centraX = s.list.get(0).x;
        for(Planet p : s.list){
            if(i!=0) diameter=5;
            int x = (int)(offX-diameter/2+p.x*scale);
            int y = (int)(offY-diameter/2+p.y*scale);
            g.fillOval(x, y,diameter,diameter);

            if (p.name == "Moon"|| p.name == "Titan"){
                g.setColor(Color.YELLOW);
            }
            else {
                g.setColor(Color.cyan);
            }
            g.drawString(p.name, x, y);



            i++;
            for(int k=1; k<p.positions.size(); k++){
                Point pt0 = p.positions.get(k-1);
                Point pt1 = p.positions.get(k);
              //  TODO line is not being drawn fix later (fixed)
                g.drawLine((int)(offX+pt0.x*scale), (int)(offY+pt0.y*scale), (int)(offX+pt1.x*scale), (int)(offY+pt1.y*scale));
            }
        }
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while(true){
            s.timestep = 5;
            s.updatePositions();
            repaint();
            setBackground(Color.BLACK);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        JFrame f = new JFrame("Solar System");
        f.setBounds(0, 0, 800, 800);
        f.setBackground(Color.BLACK);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SolarGUI p = new SolarGUI();
        f.add(p);
        new Thread(p).start();
        f.setVisible(true);

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        offX=oldOffX+e.getX()-fstClickX;
        offY=oldOffY+e.getY()-fstClickY;
    }

    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        fstClickX=e.getX();
        fstClickY=e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        oldOffX=offX;
        oldOffY=offY;
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyChar()=='q') scale+=1;
        if(e.getKeyChar()=='a') scale-=1;
    }

    @Override
    public void keyReleased(KeyEvent e) {}

}


