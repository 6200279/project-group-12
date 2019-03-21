import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import java.io.File;
import java.util.ArrayList;
import java.lang.Math;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SolarSystem {

    private final double probespeed = 2;
    static final double G = 0.000667;           //universal gravitational constant
    ArrayList<Planet> list = new ArrayList<Planet>();

    ArrayList<Probe> list2 = new ArrayList<Probe>();

    double timestep = 0;

    public SolarSystem() {

        Planet Sun = new Planet("Sun", 19.89, 0.368, 0.0008963);
        Planet Earth = new Planet("Earth", 0.00005974, -14.5, 0.578, 0.001727, 0.029896);
        Planet Moon = new Planet("Moon", 0.0000007342, -14.5 + 0.040550, 0.578, 0.001727, 0.029896);


        Planet Mercury = new Planet("Mercury", 0.00000330, -2.67, -6.03, -0.0369, 0.015334);
        Planet Venus = new Planet("Venus", 0.0000487, -0.931, -10.8, -0.0346, 0.0042);
//		Planet Earth = new Planet("Earth",0.00005974, 14.9,0);
        Planet Mars = new Planet("Mars", 0.00000642, 2.3, 23.337, 0.023155, -0.00405);
        Planet Jupiter = new Planet("Jupiter", 0.01899, -23.487, -75.996, -0.0123, 0.003305);
        //   Planet Titan2= new Planet( "Titan2",0.0000013452,-142.4 + 0.125,0,0,   )

        Planet Titan = new Planet("Titan", 0.0000013452, 35.754 - 0.125, -146.157, -0.00887, -0.00224);
        Planet Saturn = new Planet("Saturn", 0.00568, 35.754, -146.157, -0.00887, -0.00224);
        // Planet Titan2 = new Planet("Titan",0.00568, 35.754, -146.157, -0.00887, -0.00224);

//        Planet Uranus = new Planet("Uranus", 0.000866, +287.2, 0);
//        Planet Neptune = new Planet("Neptune", 0.00103, +449.9, 0);


        Planet Probe = new Planet("Probe", 0.00000000000000009, -14.6, 0.578, -0.0000616, -0.072512467);
        // second accuracy stage reached at velX = -0.00006 / velY = -0.072521
        // third accuracy stage reached at velx = -0.000061 / velY = -0.072513
        // fourth accuracy stage reachted at velX = -0.0000616 / velY = -0.072512469
        list2 = new ArrayList<Probe>();
        ;


        list = new ArrayList<Planet>();
        list.add(Sun);
        list.add(Mercury);
        list.add(Venus);
        list.add(Earth);
        list.add(Mars);
        list.add(Jupiter);
        list.add(Saturn);
        list.add(Probe);
//        list.add(Uranus);
        //       list.add(Neptune);
        //  list.add(Titan);



/*        for (Planet p : list) {
            if (p.name != "Sun")
                p.velY = Math.sqrt(G * Sun.mass / Math.abs(p.x - Sun.x));
            System.out.println(p.name+ " = " + p.velY);
            if (p.x > Sun.x)
                p.velY = -p.velY;
        }*/


//		list.add(Earth);
        Moon.velY = Earth.velY + 0.000964;
        Titan.velY = Saturn.velY - 0.00558;
        list.add(Moon);
        list.add(Titan);
        getAccelerations();

    }


    void move() {

        for (Planet p1 : list) {

            // TODO movement of probe still needs to be done correctly (20km/s)
        /*    if (p1.name == "Probe"){
               p1.x += timestep * (probespeed + timestep * p1.accX/2);
               p1.y += timestep * (probespeed + timestep * p1.accY/2);


                p1.setPosition(p1.x,p1.y);

            }
            else{*/
            // update position according to dr = v·dt + a·dt²/2

            p1.x += timestep * (p1.velX + timestep * p1.accX / 2);
            p1.y += timestep * (p1.velY + timestep * p1.accY / 2);


            // keep accelerations for velocity calculation
            p1.old_accY = p1.accY;
            p1.old_accX = p1.accX;
            p1.setPosition(p1.x, p1.y);
            //}


        }
    }

    void getAccelerations() {

        double dx, dy, dz, D, A;
        for (Planet p1 : list) {
            if (p1.name != "Probes") {
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
    }

    void accelerate() {
        for (Planet p1 : list) {
            if (p1.name != "Probes") {
                // update velocity according to dv=(a+old a) * dt/2
                p1.velX += (p1.accX + p1.old_accX) * timestep / 2;
                p1.velY += (p1.accY + p1.old_accY) * timestep / 2;
            }
        }
    }


    void updatePositions(double startTime) {
        move();
        getAccelerations();
        accelerate();
        //moveprobe();
        collion(startTime);
        if (collionstop() == true) {

            ArrayList<Planet> toRemove = new ArrayList<Planet>();
            for (Planet p1 : list) {
                if (p1.name == "Titan") {
                    p1.name = "Titan + Probe";
                }

                if (p1.name == "Probe") {
                    toRemove.add(p1);
                }
            }
            list.removeAll(toRemove);


        }
    }


    boolean collionstop() {
        for (Planet p1 : list) {
            if (p1.name == "Probe") {
                //   System.out.println(p1.x +"/"+ p1.y);
                for (Planet p2 : list) {
                    if (p2.name == "Titan") {

                        if (p1.x >= (p2.x - 0.00025) && p1.x <= (p2.x + 0.00025) && p1.y >= (p2.y - 0.00025) && p1.y <= (p2.y + 0.00025)) {
                            return true;


                        }
                    }
                }
            }
        }
        return false;
    }

    void collion(double startTime) {
        for (Planet p1 : list) {
            if (p1.name == "Probe") {
                //   System.out.println(p1.x +"/"+ p1.y);
                for (Planet p2 : list) {
                    if (p2.name == "Titan") {
                        if (p1.x >= (p2.x - 0.25) && p1.x <= (p2.x + 0.25) && p1.y >= (p2.y - 0.25) && p1.y <= (p2.y + 0.25)) {
                            System.out.println("accuracy stage 1 reached  ");
                            System.out.println("Coordinates Probe : " + p1.x + "/" + p1.y);
                            System.out.println("Coordinates Titan : " + p2.x + "/" + p2.y);
                            System.out.println("delta X : " + Math.abs((p1.x - p2.x)));
                            System.out.println("delta Y : " + Math.abs((p1.y - p2.y)));


                            // System.out.println("Probe has crashed into titan ");
                            if (p1.x >= (p2.x - 0.025) && p1.x <= (p2.x + 0.025) && p1.y >= (p2.y - 0.025) && p1.y <= (p2.y + 0.025)) {
                                System.out.println(" accuracy stage 2 reached");
                                if (p1.x >= (p2.x - 0.0025) && p1.x <= (p2.x + 0.0025) && p1.y >= (p2.y - 0.0025) && p1.y <= (p2.y + 0.0025)) {
                                    System.out.println(" accuracy stage 3 reached");
                                    if (p1.x >= (p2.x - 0.00025) && p1.x <= (p2.x + 0.00025) && p1.y >= (p2.y - 0.00025) && p1.y <= (p2.y + 0.00025)) {
                                        System.out.println(" accuracy stage 4 reached ");
                                        double stopTime = System.currentTimeMillis();
                                        double elapsedTime = (stopTime - startTime) / (1000 * (316 / timestep)) * 365;
                                        System.out.println(" you have killed titan after " + elapsedTime + " days");
                                    }

                                }

                            }
                        }
                    }
                }
            }
        }
    }
}


    class SolarGUI extends JPanel implements Runnable, MouseMotionListener, MouseListener, KeyListener {
        int ASSX = 400, ASSY = 400;
        int oldASSX = 40, oldASSY = 40;
        double scale = 3;
        int fstX, fstY;
        SolarSystem s;

        public SolarGUI() {
            s = new SolarSystem();
            setFocusable(true);
            requestFocus();
            addMouseMotionListener(this);
            addMouseListener(this);
            addKeyListener(this);
        }

        public void paintComponent(Graphics g) {
            int i = 0;
            int diameter = 5000;
//		double centraY = s.list.get(0).y;
//		double centraX = s.list.get(0).x;
            for (Planet p : s.list) {
                if (i != 0) diameter = 5;
                int x = (int) (ASSX - diameter / 2 + p.x * scale);
                int y = (int) (ASSY - diameter / 2 + p.y * scale);
                g.fillOval(x, y, diameter, diameter);

                if (p.name == "Moon" || p.name == "Titan" || p.name == "Titan + Probe") {
                    g.setColor(Color.YELLOW);
                } else if (p.name == "Probe") {
                    g.setColor(Color.MAGENTA);

                } else {
                    g.setColor(Color.cyan);
                }
                g.drawString(p.name, x, y);


                i++;


                for (int k = 1; k < p.positions.size(); k++) {
                    Point pt0 = p.positions.get(k - 1);
                    Point pt1 = p.positions.get(k);
                    //  TODO line is not being drawn fix later (fixed)1
                    g.drawLine((int) (ASSX + pt0.x * scale), (int) (ASSY + pt0.y * scale), (int) (ASSX + pt1.x * scale), (int) (ASSY + pt1.y * scale));
                }
            }
            for (Probe u : s.list2) {
                if (i != 0) diameter = 5;
                int a = (int) (ASSX - diameter / 2 + u.x * scale);
                int b = (int) (ASSY - diameter / 2 + u.y * scale);
                g.fillOval(a, b, diameter, diameter);

                if (u.name == "Probe") {
                    g.setColor(Color.MAGENTA);

                }

            }
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            double startTime = System.currentTimeMillis();
            while (true) {
                s.timestep = 20;
                s.updatePositions(startTime);
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

        /*  private Image backgroundImage = ImageIO.read(new File("spacebackground.jpeg"));

          public void paint( Graphics g ) {
              super.paint(g);
        *///      g.drawImage(backgroundImage, 0, 0, null);
        //}
        public static void main(String[] args) {
            JFrame f = new JFrame("Solar System");
            f.setBounds(0, 0, 800, 800);
            // f.setBackground(b);


            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            SolarGUI p = new SolarGUI();
            f.add(p);
            new Thread(p).start();
            f.setVisible(true);

        }

        @Override
        public void mouseDragged(MouseEvent e) {
            ASSX = oldASSX + e.getX() - fstX;
            ASSY = oldASSY + e.getY() - fstY;
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            fstX = e.getX();
            fstY = e.getY();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            oldASSX = ASSX;
            oldASSY = ASSY;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyChar() == 'q') scale += 1;
            if (e.getKeyChar() == 'a') scale -= 1;
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

    }

