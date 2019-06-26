package solarSystem;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;

public class valuesGUI extends JFrame
{
    JLabel time,height,velocity,change,thrust,axis,fuel,leftThrust,rightThrust;
    
    public valuesGUI()
    {
        super("");

        ActionListener listener = new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                time.setText("Time:" + Double.toString(titanLanderDUO.t) + " sec");
                height.setText("Height:  " + Double.toString(titanLanderDUO.height) + " m");
                velocity.setText("Descent rate: " + Double.toString(titanLanderDUO.velocity) + "m/sec");
                axis.setText("Change from initial axis: " + Double.toString(titanLanderDUO.x - 2) + " m");
                
                fuel.setText("Fuel left: " + Double.toString(titanLanderDUO.fuel) + " kg");
                thrust.setText("Thrust percentage + " + Double.toString(titanLanderDUO.thrust) + "%");
               
                leftThrust.setText("Thrust percentage + " + Double.toString(titanLanderDUO.leftThrust) + "%");
                rightThrust.setText("Thrust percentage + " + Double.toString(titanLanderDUO.rightThrust) + "%");
            }
        };
        velocity = new JLabel();
        time = new JLabel();
        height = new JLabel();
        fuel = new JLabel();
        thrust = new JLabel();
        rightThrust = new JLabel();
        leftThrust = new JLabel();
        axis = new JLabel();

        getContentPane().setBackground(Color.black); 
        setLayout(new FlowLayout());
        setSize(500,600);
        setResizable(false);
        
        int x = 800;
        int y = 0;
        this.setLocation(x, y);
        
        setVisible(true);
           

        add(time);
        add(height);
        add(velocity);
        add(axis);
        add(fuel);
        add(thrust);
        add(leftThrust);
        add(rightThrust);

        time.setFont(new Font("Arial", Font.BOLD, 20));
        time.setForeground(Color.green);
        height.setFont(new Font("Arial", Font.BOLD, 20));
        height.setForeground(Color.green);
        velocity.setFont(new Font("Arial", Font.BOLD, 20));
        velocity.setForeground(Color.green);
        axis.setFont(new Font("Arial", Font.BOLD, 20));
        axis.setForeground(Color.green);
        fuel.setFont(new Font("Arial", Font.BOLD, 20));
        fuel.setForeground(Color.green);
        thrust.setFont(new Font("Arial", Font.BOLD, 20));
        thrust.setForeground(Color.green);
        leftThrust.setFont(new Font("Arial", Font.BOLD, 20));
        leftThrust.setForeground(Color.green);
        rightThrust.setFont(new Font("Arial", Font.BOLD, 20));
        rightThrust.setForeground(Color.green);
        

        javax.swing.Timer timer = new javax.swing.Timer(500, listener);
        timer.setInitialDelay(0);
        timer.start();
    }

    public static void main(String args[])
    {
        valuesGUI c = new valuesGUI();
        c.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}