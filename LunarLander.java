import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import java.lang.*;

import java.util.ArrayList;
import java.util.Scanner;


public class LunarLander {

    private double thrust = 0;
    private double leftThrust;
    private double rightThrust;

    private final double GRAVITY = 1.352;
    private final double MAX_THRUST = 20.0;        //maximum engine thrust in m/sec^2
    private final double MAX_BURN_RATE = 10.0;    //fuel burn rate at max thrust kg/sec
    private final double TIME_SLICE = 1;        //time between thrust choices in secs

    private final double ORIGINAL_HEIGHT = 4000;        //Starting height in meters
    private final double ORIGINAL_VELOCITY = 9.6;    //Starting descent rate (m/sec)
    private final double ORIGINAL_FUEL = 1200.0;        //Starting fuel in kg

    private final double INFINITY = 1E10;    //a time very far in the future
    private final double SMALL = 0.001;    //acceleration below this will be called 0

    private double TERMINAL_VELOCITY = 9.2; // terminal velocity cassini earth 50,2 m/s terminal velocity is about 5,5 times less on titan
    private boolean touchdown = false;        //set to true on landing
    private boolean fuel_out = false;        //set to true when fuel used up
    private double time_touchdown = 0;    //secs from now to touchdown
    private double time_no_fuel = 0;

    private double a = 0; //acceleration caused by thrust plus gravity
    private double l = 0; // acceleration caused by left thrust
    private double r = 0; // acceleration caused by right thrust


    // Set up the initial configuration at time t=0

    private double height = ORIGINAL_HEIGHT;
    private double velocity = ORIGINAL_VELOCITY;
    private double fuel = ORIGINAL_FUEL;
    private double t = 0;//secs from now to fuel gone
    private double x = 2;
    private double oldH;
    private double almostH;
    boolean escape = false;


    Wind wind;

    ArrayList<Rover> list = new ArrayList<Rover>();


    public LunarLander() {

        list = new ArrayList<Rover>();
        Rover LunarRover = new Rover("LunarRover", 200, 0, 1000000, 0, 0);
        list.add(LunarRover);

    }


    double timestep = 0;

    void freefall() {
    if(!escape) {

        Scanner in = new Scanner(System.in);
        System.out.println("please enter 1 for feedback controler");
        System.out.println("please enter 2 for Openloop controler");
        System.out.println("please enter 3 for game controler");

        int k = in.nextInt();
        if (k == 1) {
            feedback();
        } else if (k == 2) {
            Openloop();
        } else if (k == 3) {
            GameControler();
        } else System.out.println("invalid number entered");
    }
    }

     void feedback() {


        wind = new Wind(2, TIME_SLICE);
        double[] windVector = new double[2];

        while (!touchdown) {
            if (velocity >= TERMINAL_VELOCITY) {
                velocity = TERMINAL_VELOCITY;
            }


            // System.out.println("Enter Thrust (%)");
            if (height <= 50) {
                thrust = getThrust(height, velocity);
            }
            leftThrust = getLeftThrust(x);
            rightThrust = getRightThrust(x);
            for (Rover p : list) {
                p.setPosition(x, height / 1000);
            }


            // Display the current situation

            System.out.println("Time: " + t + " secs");
            System.out.println("Height: " + height + " m");
            System.out.println("Descent rate: " + velocity + " m/sec");
            System.out.println("Change from initial axis: " + (x - 2) + " m");
            System.out.println("Fuel left: " + fuel + " kg");
            System.out.println("Thrust percentage: " + thrust + "%");
            System.out.println("left thrust percentage: " + leftThrust + "%");
            System.out.println("Right thrust percentage: " + rightThrust + "%");
            System.out.println();


            System.out.println();

            // Calculate the acceleration, and how long fuel will last

            a = (GRAVITY - MAX_THRUST * thrust / 100.0);

            time_no_fuel = fuel / (MAX_BURN_RATE * thrust / 100.0);

            // calculate time_to_touchdown assuming fuel never runs out

            double discrim = velocity * velocity + 2 * height * a;
            if (discrim < 0)
                time_touchdown = INFINITY;
            else if (Math.abs(a) < SMALL)
                time_touchdown = height / velocity;
            else
                time_touchdown = (velocity - Math.sqrt(discrim)) / -a;

            // Handle the case when fuel will run out first

            if (time_no_fuel < time_touchdown
                    && time_no_fuel <= TIME_SLICE) {

                System.out.println("Out of fuel! Free fall!  ");
                fuel_out = true;
                t = t + time_no_fuel;
                fuel = 0.0;
                height = height - time_no_fuel * velocity - a * time_no_fuel * time_no_fuel / 2.0;
                velocity = velocity + a * time_no_fuel;
                a = GRAVITY; //gravity never sleeps!
                time_touchdown = (velocity - Math.sqrt(velocity * velocity + 2 * height * a)) / -a;
            }
            // end if (fuel runs out case)
            boolean heighttouch = height < 1;


            touchdown = fuel_out || time_touchdown < TIME_SLICE || heighttouch;

            // If not, advance everything one time period and keep going
            l = (MAX_THRUST * leftThrust);
            r = (MAX_THRUST * rightThrust);
            if (!touchdown) {
                t = t + TIME_SLICE;
                fuel = fuel - TIME_SLICE * MAX_BURN_RATE * thrust / 100.0;
                oldH = height;
                almostH = height - TIME_SLICE * velocity - a * TIME_SLICE * TIME_SLICE / 2;
                windVector = wind.addWind(oldH, almostH);
                x += (windVector[0] - r + l);
                //height = windVector[1];
                height = height - TIME_SLICE * velocity - a * TIME_SLICE * TIME_SLICE / 2;
                velocity = velocity + TIME_SLICE * a;
            }
        }


        // We're down! Print out the final numbers.

        System.out.println("Touchdown at time " + (t + time_touchdown));
        double touchdown_velocity = velocity; //+ a * time_to_touchdown;
        System.out.println("Velocity at touchdown: " + touchdown_velocity + "m/sec");

        // Print a suitable assessment of the landing.

        if (touchdown_velocity <= 0.5)
            System.out.println("Perfect landing!");
        else if (touchdown_velocity <= 1.0)
            System.out.println("Soft landing");
        else if (touchdown_velocity <= 2.0)
            System.out.println("Good landing");
        else if (touchdown_velocity <= 5.0)
            System.out.println("Hard landing");
        else if (touchdown_velocity <= 10.0)
            System.out.println("Lander damaged");
        else if (touchdown_velocity <= 20.0)
            System.out.println("No survivors");
        else
            System.out.println("Maybe they'll name the crater after you!");

        launch();
    }


    void Openloop() {

        wind = new Wind(2, TIME_SLICE);
        double[] windVector = new double[2];


        while (!touchdown) {
            if (velocity >= TERMINAL_VELOCITY) {
                velocity = TERMINAL_VELOCITY;
            }

            // System.out.println("Enter Thrust (%)");
            thrust = openloopthrust(t);
            leftThrust = getLeftThrust(x);
            rightThrust = getRightThrust(x);

            for (Rover p : list) {
                p.setPosition(x, height / 1000);

            }


            // Display the current situation

            System.out.println("Time: " + t + " secs");
            System.out.println("Height: " + height + " m");
            System.out.println("Descent rate: " + velocity + " m/sec");
            System.out.println("Change from initial axis: " + (x - 2) + " m");
            System.out.println("Fuel left: " + fuel + " kg");
            System.out.println("Thrust percentage: " + thrust + "%");
            System.out.println("left thrust precentage: " + leftThrust + "%");
            System.out.println("right thrust precentage: " + rightThrust + "%");
            System.out.println();


            System.out.println();

            // Calculate the acceleration, and how long fuel will last

            a = (GRAVITY - MAX_THRUST * thrust / 100.0);

            time_no_fuel = fuel / (MAX_BURN_RATE * thrust / 100.0);

            // calculate time_to_touchdown assuming fuel never runs out

            double discrim = velocity * velocity + 2 * height * a;
            if (discrim < 0)
                time_touchdown = INFINITY;
            else if (Math.abs(a) < SMALL)
                time_touchdown = height / velocity;
            else
                time_touchdown = (velocity - Math.sqrt(discrim)) / -a;

            // Handle the case when fuel will run out first

            if (time_no_fuel < time_touchdown
                    && time_no_fuel <= TIME_SLICE) {

                System.out.println("Out of fuel! Free fall! ");
                fuel_out = true;
                t = t + time_no_fuel;
                fuel = 0.0;
                height = height - time_no_fuel * velocity - a * time_no_fuel * time_no_fuel / 2.0;
                velocity = velocity + a * time_no_fuel;
                a = GRAVITY; //gravity never sleeps!
                time_touchdown = (velocity - Math.sqrt(velocity * velocity + 2 * height * a)) / -a;
            }
            // end if (fuel runs out case)


            touchdown = fuel_out || time_no_fuel < TIME_SLICE;
            l = (MAX_THRUST * leftThrust);
            r = (MAX_THRUST * rightThrust);
            // If not, advance everything one time period and keep going
            if (!touchdown) {
                t = t + TIME_SLICE;
                fuel = fuel - TIME_SLICE * MAX_BURN_RATE * thrust / 100.0;
                oldH = height;
                almostH = height - TIME_SLICE * velocity - a * TIME_SLICE * TIME_SLICE / 2;
                windVector = wind.addWind(oldH, almostH);
                x += (windVector[0] - r + l);
                height = height - TIME_SLICE * velocity - a * TIME_SLICE * TIME_SLICE / 2.0;
                velocity = velocity + TIME_SLICE * a;
            }


        }

        // We're down! Print out the final numbers.

        System.out.println("Touchdown at time " + (t + time_touchdown));
        double touchdown_velocity = velocity + a * time_touchdown;
        System.out.println("Velocity at touchdown: " + touchdown_velocity + "m/sec");

        // Print a suitable assessment of the landing.

        if (touchdown_velocity <= 0.5)
            System.out.println("Perfect landing! ");
        else if (touchdown_velocity <= 1.0)
            System.out.println("Soft landing ");
        else if (touchdown_velocity <= 2.0)
            System.out.println("Good landing  ");
        else if (touchdown_velocity <= 5.0)
            System.out.println("Hard landing ");
        else if (touchdown_velocity <= 10.0)
            System.out.println("Lander damaged ");
        else if (touchdown_velocity <= 20.0)
            System.out.println("No survivors ");
        else
            System.out.println("Maybe they'll name the crater after you! ");

        System.out.println(" ");
        System.out.println(" ");
        System.out.println(" ");


        launch();
    }

    void GameControler() {
        wind = new Wind(2, TIME_SLICE);
        double[] windVector = new double[2];

        Scanner in = new Scanner(System.in);

        while (!touchdown) {

            System.out.println("Enter Thrust (%)");
            thrust = in.nextDouble();

            if (velocity >= TERMINAL_VELOCITY) {
                velocity = TERMINAL_VELOCITY;
            }

            for (Rover p : list) {
                p.setPosition(x, height / 1000);
            }


            // Display the current situation

            System.out.println("Time: " + t + " secs");
            System.out.println("Height: " + height + " m");
            System.out.println("Descent rate: " + velocity + " m/sec");
            System.out.println("Fuel left: " + fuel + " kg");
            System.out.println("Thrust percentage: " + thrust + "%");
            System.out.println();


            boolean validResponse = false;
            while (!validResponse) {                    //TODO valid reponse check feedback controller
                System.out.println("Thrust: " + thrust + "%");

                if (thrust >= 0.0 && thrust <= 100.0)
                    validResponse = true;
                else
                    System.out.println("Only values 0 to 100 will work.");
                break;
            }
            System.out.println();

            // Calculate the acceleration, and how long fuel will last

            a = (GRAVITY - MAX_THRUST * thrust / 100.0);

            time_no_fuel = fuel / (MAX_BURN_RATE * thrust / 100.0);

            // calculate time_to_touchdown assuming fuel never runs out

            double discrim = velocity * velocity + 2 * height * a;
            if (discrim < 0)
                time_touchdown = INFINITY;
            else if (Math.abs(a) < SMALL)
                time_touchdown = height / velocity;
            else
                time_touchdown = (velocity - Math.sqrt(discrim)) / -a;

            // Handle the case when fuel will run out first

            if (time_no_fuel < time_touchdown
                    && time_no_fuel <= TIME_SLICE) {

                System.out.println("Out of fuel! Free fall!");
                fuel_out = true;
                t = t + time_no_fuel;
                fuel = 0.0;
                height = height - time_no_fuel * velocity - a * time_no_fuel * time_no_fuel / 2.0;
                velocity = velocity + a * time_no_fuel;
                a = GRAVITY; //gravity never sleeps!
                time_touchdown = (velocity - Math.sqrt(velocity * velocity + 2 * height * a)) / -a;
            }
            // end if (fuel runs out case)


            touchdown = fuel_out || time_touchdown < TIME_SLICE;

            // If not, advance everything one time period and keep going

            if (!touchdown) {
                t = t + TIME_SLICE;
                fuel = fuel - TIME_SLICE * MAX_BURN_RATE * thrust / 100.0;
                oldH = height;
                almostH = height - TIME_SLICE * velocity - a * TIME_SLICE * TIME_SLICE / 2;
                windVector = wind.addWind(oldH, almostH);
                x += windVector[0];
                height = windVector[1];
                velocity = velocity + TIME_SLICE * a;
            }


        }

        // We're down! Print out the final numbers.

        System.out.println("Touchdown at time " + (t + time_touchdown));
        double touchdown_velocity = velocity + a * time_touchdown;
        System.out.println("Velocity at touchdown: " + touchdown_velocity + "m/sec");

        // Print a suitable assessment of the landing.

        if (touchdown_velocity <= 0.5)
            System.out.println("Perfect landing!");
        else if (touchdown_velocity <= 1.0)
            System.out.println("Soft landing   ");
        else if (touchdown_velocity <= 2.0)
            System.out.println("Good landing   ");
        else if (touchdown_velocity <= 5.0)
            System.out.println("Hard landing   ");
        else if (touchdown_velocity <= 10.0)
            System.out.println("Lander damaged   ");
        else if (touchdown_velocity <= 20.0)
            System.out.println("No survivors");
        else
            System.out.println("Maybe they'll name the crater after you!");


    }


    public double openloopthrust(double t) {


        while (velocity >= 0 && height <= 100) {

            if (height < 0)
                return 0;
            else if (height >= 100) {
                return 0;
            } else if (height >= 50) {
                return 5;
            } else if (height >= 25) {
                return 10;


            } else if (height >= 15) {
                return 15;
            } else if (height >= 10) {
                return 20;
            } else if (height >= 5) {
                return 15;
            } else if (height >= 2) {
                return 9;
            } else if (height == 0) {
                return 0;
            }

        }


        return 0;
    }
      /*  if (t >=395 && t<= 400){
            return 5;
        }
        else if (t>=400 && t<= 403){
            return 10;
        }
        else if (t == 404){
            return 15;
        }
        else if (t == 405){
            return 20;
        }
        else if (t == 406 || t == 407){
            return 15;
        }
        else if (t>= 409 && t<= 411){
            return 9;
        }
        else if (t == 413 || t== 414){
            return 9;
        }
        else return 0;
*/


    public double getThrust(double height, double velocity) {
        PID PID;

        PID = new PID(5, 0.01, 0.4);
        PID.setOutputLimits(100);
        //miniPID.setMaxIOutput(2);
        //miniPID.setOutputRampRate(3);
        //miniPID.setOutputFilter(.3);
        //  miniPID.setSetpointRange();

        double target = 0;

        double actual = velocity;
        double output = 0;

        // miniPID.setSetpoint(0);
        PID.setSetpoint(target);

        System.err.printf("Target \t velocity \tOutput \tError\n");
        //System.err.printf("Output\tP\tI\tD\n");
        if (!(height > 0 && height < 1)) {
            // Position based test code
            for (int i = 0; i < 100; i++) {


                output = PID.getOutput(actual, target);
                actual = actual + output;

                //System.out.printf("Current: %3.2f , Actual: %3.2f, Error: %3.2f\n",actual, output, (target-actual));
                System.err.printf("%3.2f\t%3.2f\t%3.2f\t%3.2f\n", target, actual, output, (target - actual));
                return (output * -1);

            }
        }


        return 0;
    }

    public double getLeftThrust(double x) {

        PID PID;

        PID = new PID(0.099, 0.01, 0.4);
        PID.setOutputLimits(10);
        //miniPID.setMaxIOutput(2);
        //miniPID.setOutputRampRate(3);
        //miniPID.setOutputFilter(.3);
        //  miniPID.setSetpointRange();

        double target = 0;

        double actual = x;
        double output = 0;

        // miniPID.setSetpoint(0);
        PID.setSetpoint(target);

        // System.err.printf("Target\tActual\tOutput\tError\n");
        //System.err.printf("Output\tP\tI\tD\n");
        if (!(height > 0 && height < 1)) {
            // Position based test code
            for (int i = 0; i < 100; i++) {


                output = PID.getOutput(actual, target);
                actual = actual + output;

                //System.out.printf("Current: %3.2f , Actual: %3.2f, Error: %3.2f\n",actual, output, (target-actual));
                //System.err.printf("%3.2f\t%3.2f\t%3.2f\t%3.2f\n", target, actual, output, (target - actual));
                return (output * 1);

            }

        }


        return 0;
    }

    public double getRightThrust(double x) {

        PID PID;

        PID = new PID(0.048, 0.01, 0.4);
        PID.setOutputLimits(100);
        //miniPID.setMaxIOutput(2);
        //miniPID.setOutputRampRate(3);
        //miniPID.setOutputFilter(.3);
        //  miniPID.setSetpointRange();

        double target = 0;

        double actual = velocity;
        double output = 0;

        // miniPID.setSetpoint(0);
        PID.setSetpoint(target);

        //  System.err.printf("Target\tActual\tOutput\tError\n");
        //System.err.printf("Output\tP\tI\tD\n");
        if (!(height > 0 && height < 1)) {
            // Position based test code
            for (int i = 0; i < 100; i++) {


                output = PID.getOutput(actual, target);
                actual = actual + output;

                //System.out.printf("Current: %3.2f , Actual: %3.2f, Error: %3.2f\n",actual, output, (target-actual));
                //        System.err.printf("%3.2f\t%3.2f\t%3.2f\t%3.2f\n", target, actual, output, (target - actual));
                //    return (output * -1);

            }
        }

        return 0;
    }


    public void setThrust(double thrust) {
        this.thrust = thrust;

    }

    void launch() {
        System.out.println(" ");
        System.out.println(" ");
        boolean titanorbit = false;
        double orbitheight = 10000;
        height = 0;
        velocity = 0;
        t = 0;
        x = 0;
        thrust = 0;


        double[] windVector = new double[2];
        Scanner in = new Scanner(System.in);
        System.out.println("Enter 1 when Ready for take off?");

        int tt = in.nextInt();
        if (tt != 1) {
            System.out.println("Invalid Number");
        }

        while (!titanorbit) {
            if (tt == 1) {

                if (thrust != 100) {
                    thrust += 1;
                } else
                    thrust = 100;
                leftThrust = getLeftThrust(x);
                rightThrust = getRightThrust(x);

                System.out.println("Time: " + t + " secs");
                System.out.println("Height: " + height + " m");
                System.out.println("take off velocity : " + (velocity * -1) + " m/sec");
                System.out.println("Change from initial axis: " + (x - 2) + " m");
                System.out.println("Fuel left: " + fuel + " kg");
                System.out.println("Thrust percentage: " + thrust + "%");
                System.out.println("left thrust precentage: " + leftThrust + "%");
                System.out.println("right thrust precentage: " + rightThrust + "%");
                System.out.println();


                System.out.println();

                // Calculate the acceleration, and how long fuel will last

                a = (GRAVITY - MAX_THRUST * thrust / 100.0);

                time_no_fuel = fuel / (MAX_BURN_RATE * thrust / 100.0);

                // calculate time_to_touchdown assuming fuel never runs out

                double discrim = velocity * velocity + 2 * height * a;
                if (discrim < 0)
                    time_touchdown = INFINITY;
                else if (Math.abs(a) < SMALL)
                    time_touchdown = height / velocity;
                else
                    time_touchdown = (velocity - Math.sqrt(discrim)) / -a;

                // Handle the case when fuel will run out first

             /*   if (time_to_fuel_out < time_to_touchdown
                        && time_to_fuel_out <= TIME_SLICE) {

                    System.out.println("Out of fuel! Free fall! ");
                    fuel_out = true;
                    t = t + time_to_fuel_out;
                    fuel = 0.0;
                    height = height - time_to_fuel_out * velocity - a * time_to_fuel_out * time_to_fuel_out / 2.0;
                    velocity = velocity + a * time_to_fuel_out;
                    a = GRAVITY; //gravity never sleeps!
                    time_to_touchdown = (velocity - Math.sqrt(velocity * velocity + 2 * height * a)) / -a;
                }
                // end if (fuel runs out case)
*/

                l = (MAX_THRUST * leftThrust);
                r = (MAX_THRUST * rightThrust);
                if (height < 0) {
                    height = 0;
                }
                // If not, advance everything one time period and keep going

                t = t + TIME_SLICE;
                fuel = fuel - TIME_SLICE * MAX_BURN_RATE * thrust / 100.0;
                oldH = height;
                almostH = height - TIME_SLICE * velocity - a * TIME_SLICE * TIME_SLICE / 2;
                windVector = wind.addWind(oldH, almostH);
                x += (windVector[0] - r + l);
                height = height - TIME_SLICE * velocity - a * TIME_SLICE * TIME_SLICE / 2.0;
                velocity = velocity + TIME_SLICE * a;
                if (height > orbitheight) {
                    titanorbit = true;
                }


            }

        }

        System.out.println(" ");
        System.out.println(" ");
        System.out.println(" Congrats you have escaped titans gravity ");
        escape = true;
    }



    /*public static void main(String[] args) throws IOException {
        JFrame p = new JFrame("TitanLander");
        p.setBounds(0, 0, 800, 800);
        p.setBackground(Color.black);


        p.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        LanderGUI m = new LanderGUI();
        p.add(m);

        new Thread(m).start();
       p.setVisible(true);
        System.out.println("please enter 1 for feedback controler");
        System.out.println("please enter 2 for Openloop controler");
        System.out.println("please enter 3 for game controler");

    }
*/
}



    class LanderGUI extends JPanel implements Runnable, MouseMotionListener, MouseListener, KeyListener {
        int ASSX = 400, ASSY = 400;
        int oldASSX = 40, oldASSY = 40;
        double scale = 3, timestep;
        int fstX, fstY;
        LunarLander s;
        double startTime = System.currentTimeMillis();

        public LanderGUI() {
            s = new LunarLander();
            setFocusable(true);
            requestFocus();
            addMouseMotionListener(this);
            addMouseListener(this);
            addKeyListener(this);
        }

        public void paintComponent(Graphics g) {
            int i = 0;
            int diameter = 5000;

            for (Rover p : s.list) {
                if (i != 0) diameter = 5;
                int x = (int) (ASSX - diameter / 2 + p.x * scale);
                int y = (int) (ASSY - diameter / 2 + p.y * scale);
                g.fillOval(x, y, diameter, diameter);

                if (p.name == "LunarRover") {
                    g.setColor(Color.YELLOW);
                }

                g.drawString(p.name, x, y);


                i++;

                for (int k = 1; k < p.positions.size(); k++) {
                    Point pt0 = p.positions.get(k - 1);
                    Point pt1 = p.positions.get(k);
                    g.drawLine((int) (ASSX + pt0.x * scale), (int) (ASSY + pt0.y * scale), (int) (ASSX + pt1.x * scale), (int) (ASSY + pt1.y * scale));

                }
            }
        }


        @Override
        public void run() {

            // TODO Auto-generated method stub
            while (true) {

                s.timestep = 1;
                s.freefall();
                ;

                repaint();
                setBackground(Color.black);

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
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
