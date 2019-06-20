import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Scanner; 
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

    double landingTime = 0;
    boolean landed = false;
    boolean launchtime = false;
    boolean beforeSlingshot = true;

    double probeVel;
    double probeVelRT;

    public SolarSystem() {

        //Planet Sun = new Planet("Sun", 19.89, 0.368, 0.0008963);
        Planet Sun = new Planet("Sun", 19.89, 0.3678221137960299, 0.000506513009907229);
        //Planet Earth = new Planet("Earth", 0.00005974, -14.5, 0.578, 0.001727, 0.029896);
        Planet Earth = new Planet("Earth", 0.00005974, -3.8643164599613815, 14.054622219886541, 0.02906675203296486, 0.008754256468909554);
        //Planet Moon = new Planet("Moon", 0.0000007342, -14.5 + 0.040550, 0.578, 0.001727, 0.029896);
        Planet Moon = new Planet("Moon", 0.0000007342, -3.8643164599613815 + 0.040550, 14.054622219886541, 0.02906675203296486, 0.008754256468909554);


        //Planet Mercury = new Planet("Mercury", 0.00000330, -2.67, -6.03, -0.0369, 0.015334);
        Planet Mercury = new Planet("Mercury", 0.00000330, 2.8186014603299507, -5.365631121439562, -0.03769011210531946, -0.027289247708475663);
        //Planet Venus = new Planet("Venus", 0.0000487, -0.931, -10.8, -0.0346, 0.0042);
        Planet Venus = new Planet("Venus", 0.0000487, -9.130083718500613, 5.165041503105213, 0.016849259889191028, 0.030757533047872876);
        //Planet Mars = new Planet("Mars", 0.00000642, 2.3, 23.337, 0.023155, -0.00405);
        Planet Mars = new Planet("Mars", 0.00000642, 14.989634377488617, 16.529880378105624, 0.01716276922149229, -0.018088952769549457);
        //Planet Jupiter = new Planet("Jupiter", 0.01899, -23.487, -75.996, -0.0123, 0.003305);
        Planet Jupiter = new Planet("Jupiter", 0.01899, -30.802542064225953, -73.63632750376833, -0.011865198931079722, 0.004487067873690785);
       

        //Planet Titan = new Planet("Titan", 0.0000013452, 35.754 - 0.125, -146.157, -0.00887, -0.00224);
        Planet Titan = new Planet("Titan", 0.0000013452, 30.36350217953142 - 0.125, -147.40816905813315, -0.008947758155843083, -0.0018930382514829032);
        //Planet Saturn = new Planet("Saturn", 0.00568, 35.754, -146.157, -0.00887, -0.00224);
        Planet Saturn = new Planet("Saturn", 0.00568, 30.36350217953142, -147.40816905813315, -0.008947758155843083, -0.0018930382514829032);
        

//        Planet Uranus = new Planet("Uranus", 0.000866, +287.2, 0);
//        Planet Neptune = new Planet("Neptune", 0.00103, +449.9, 0);


        Planet Probe = new Planet("Probe", 0.00000000009, -3.8643164599613815 + 0.01, 14.054622219886541, 0.02906675203296486 - 0.0042, 0.008754256468909554 - 0.0015);
        //Planet Probe = new Planet("Probe", 0.00000000009, -14.5 + 0.01, 0.578, 0.001727 - 0.004, 0.029896 - 0.0034);


//For timestep = 20:
	   // second accuracy stage reached at velX = -0.00006 / velY = -0.072521
        // third accuracy stage reached at velx = -0.000061 / velY = -0.072513
        // fourth accuracy stage reachted at velX = -0.0000616 / velY = -0.072512469
       

	   list2 = new ArrayList<Probe>();
        


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






        Moon.velY = Earth.velY + 0.000964;
        Titan.velY = Saturn.velY - 0.00558;
        list.add(Moon);
        list.add(Titan);
        getAccelerations();

    }
    void print(){
        for (Planet p : list) {
            if(p.name == "Probe") System.out.println("Probe: x = " + p.x + " ,y = " + p.y);
            if(p.name == "Earth") System.out.println("Earth: x = " + p.x + " ,y = " + p.y);
            if(p.name == "Venus") System.out.println("Venus: x = " + p.x + " ,y = " + p.y);
            if(p.name == "Jupiter") System.out.println("Jupiter: x = " + p.x + " ,y = " + p.y);
            if(p.name == "Saturn") System.out.println("Saturn: x = " + p.x + " ,y = " + p.y);
            if(p.name == "Titan") System.out.println("Titan: x = " + p.x + " ,y = " + p.y);
        }
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

            if (p1.name == "Probe") {
                probeVel = Math.abs(p1.velX) + Math.abs(p1.velY);
            }
            if (p1.name == "ProbeRT") {
                probeVelRT = Math.abs(p1.velX) + Math.abs(p1.velY);
            }
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
        if(beforeSlingshot) addSlingshot();
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

    void addSlingshot(){
        for(Planet p1 : list){
            for(Planet p2 : list) {

                if (p1.name == "Venus" && p2.name == "Probe") {
                    if(Math.abs(p1.y-p2.y) < 0.01) {
                        p2.velX = p2.velX + 0.6 * p1.velX + 0.0135427;  //to improve proximity, change this number
                        p2.velY = p2.velY + 0.6 * p1.velY;
                        System.out.println("Added sligshot");
                        beforeSlingshot = false;
                    }
                }
            }
        }
    }

    public double calculateAngle(){
        for (Planet p1 : list){
            for (Planet p2 : list){
                if(p1.name == "Earth" && p2.name == "Venus"){
                    double angle1 = Math.atan2(p1.y - 0, p1.x - 0);
                    double angle2 = Math.atan2(p2.y - 0, p2.x - 0);
                    double angle = Math.toDegrees(angle2 - angle1);
                    if(angle < 0){
                        angle += 360;
                    }
                    return angle;
                }


            }
        }


        return 0;
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
                            //double probeVel = p1.velX + p1.velY;
                            System.out.println("Probe velocity: " + probeVel);


                            // System.out.println("Probe has crashed into titan ");
                            if (p1.x >= (p2.x - 0.025) && p1.x <= (p2.x + 0.025) && p1.y >= (p2.y - 0.025) && p1.y <= (p2.y + 0.025)) {
                                System.out.println(" accuracy stage 2 reached");
                                if (p1.x >= (p2.x - 0.0025) && p1.x <= (p2.x + 0.0025) && p1.y >= (p2.y - 0.0025) && p1.y <= (p2.y + 0.0025)) {
                                    System.out.println(" accuracy stage 3 reached");
                                    if (p1.x >= (p2.x - 0.00025) && p1.x <= (p2.x + 0.00025) && p1.y >= (p2.y - 0.00025) && p1.y <= (p2.y + 0.00025)) {
                                        System.out.println(" accuracy stage 4 reached ");
                                        landed = true;
                                        double stopTime = System.currentTimeMillis();
                                        landingTime = (stopTime - startTime) / (1000 * (316 / timestep)) * 365;
                                        System.out.println(" you have killed titan after " + landingTime + " days");
                                    }

                                }

                            }
                        }
                    }
                }
            }
        }


        Planet ProbeRT = new Planet("ProbeRT", 0.00000000000000009, -17.968158239818496, -149.36182268798163, -0.00006275, 0.072585040);


        launchtime = ((System.currentTimeMillis() - startTime) / (1000 * (316 / timestep)) * 365) >= 700 && ((System.currentTimeMillis() - startTime) / (1000 * (316 / timestep)) * 365) <= 705;
            if (launchtime== true){
                for (Planet p : list){
                    if (p.name == "Titan + Probe"){
                        System.out.println(" X = " +p.getX());
                        System.out.println("Y = " + p.getY());
                    }
                }
            }
        Scanner in = new Scanner(System.in);
        if (launchtime == true) {
            System.out.println("enter 1 to return to earth");
            int StartReturn = in.nextInt();
            if (StartReturn != 1) {
                System.out.println("you have enterd an invalid number");
            }
            if (StartReturn == 1) {

                list.add(ProbeRT);

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
    double startTime = System.currentTimeMillis();
    double elapsedTime = 0;
    static double daysToPass;

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
        Font myFont = new Font ("Arial", 1, 14);
        g.setFont(myFont);
        g.setColor(Color.WHITE);
        g.drawString(timer(), 50, 50);

        if(s.landed){
            g.drawString(landingTimer(), 50, 100);
        }

        g.drawString(probeSpeed(), 50, 75);
        g.drawString(returnProbeSpeed(), 50,125);

    }

    public String timer(){
        double currentTime = System.currentTimeMillis();
        elapsedTime = (currentTime - startTime) / (1000 * (316 / s.timestep)) * 365;

        return "Time: " + (int)(elapsedTime) + " days";
    }

    public void countTime(){
        double currentTime = System.currentTimeMillis();
        elapsedTime = (currentTime - startTime) / (1000 * (316 / s.timestep)) * 365;
    }


    public String landingTimer(){
        return "Landed on Titan after: " + (int)(s.landingTime) + " days";
    }
    public String returnProbeSpeed(){
        double probeVelo = s.probeVelRT * 1000;

            return "Probe speed: " + (int)(probeVelo) + " km/s";
    }

    public String probeSpeed(){
        double probeVelo = s.probeVel * 1000;
        if(s.landed){
            return "Probe's final speed: " + (int)(probeVelo) + " km/s" ;

        }

        else{
            return "Probe speed: " + (int)(probeVelo) + " km/s";
        }
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub

        while (elapsedTime < daysToPass) {
            s.timestep = 5;
            s.updatePositions(startTime);
            repaint();
            countTime();
            setBackground(Color.BLACK);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } s.print();
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
        System.out.println("After how many days should I stop?");
        Scanner scanner = new Scanner(System.in);
        daysToPass = scanner.nextDouble();


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
        if (e.getKeyCode() == 38) ASSY += 10;
        if (e.getKeyCode() == 39) ASSX -= 10;
        if (e.getKeyCode() == 40) ASSY -= 10;
        if (e.getKeyCode() == 37) ASSX += 10;
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
