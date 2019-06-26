package solarSystem;

import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;

import javax.swing.*;

public class choiceMenu
{
  static JFrame frame;
  public static double thrustDouble = 5;
  public static double thrustLeft = 1;
  public static double thrustRight;
  
  public static void main(String[] args)
  {
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        displayJFrame();
      }
    });
  }

  static void displayJFrame()
  {
    frame = new JFrame("Type of Landing");

    JButton opt1 = new JButton("Open Controller");
    
    JTextField numberField1 = new JTextField("Set the level of thrust %");
    JTextField numberFieldLeft = new JTextField("Set the left thrust %");
    JTextField numberFieldRight = new JTextField("Set the right thrust %");
    
    numberField1.addFocusListener(new FocusListener() {
        public void focusGained(FocusEvent e) {
            numberField1.setText("");
        }
        
     public void focusLost(FocusEvent e) {
            // do nothing
        } });
     numberFieldRight.addFocusListener(new FocusListener() {
         public void focusGained(FocusEvent e) {
             numberFieldRight.setText("");
         }
         
      public void focusLost(FocusEvent e) {
             // do nothing
         }
     });
      numberFieldLeft.addFocusListener(new FocusListener() {
          public void focusGained(FocusEvent e) {
              numberFieldLeft.setText("");
          }
          
       public void focusLost(FocusEvent e) {
              // do nothing
          }
    });
    
    numberField1.addActionListener(new ActionListener() {
       
        public void actionPerformed(ActionEvent e) {
           thrustDouble = Double.parseDouble(numberField1.getText());
        }
    });
    
    numberFieldLeft.addActionListener(new ActionListener() {
        
        public void actionPerformed(ActionEvent e) {
           thrustLeft = Double.parseDouble(numberFieldLeft.getText());
        }
    });
    numberFieldRight.addActionListener(new ActionListener() {
        
        public void actionPerformed(ActionEvent e) {
           thrustRight = Double.parseDouble(numberFieldRight.getText());
        }
    });
    
    opt1.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
    	  
    	  titanLanderDUO landingText = new titanLanderDUO ();
    	  landingText.Openloop();

      }
    });
    
    JButton opt2 = new JButton("Feedback Controller");
    opt2.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {	  
    	  titanLanderDUO landingText = new titanLanderDUO ();
    	  landingText.feedback();
    	  
    	  
    	  valuesGUI val = new valuesGUI ();
    	  val.setVisible(true);
    
      }
    });
    
    JButton opt3 = new JButton("Game Controller");
    opt3.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
    	  titanLanderDUO landingText = new titanLanderDUO ();
    	  landingText.GameControler();
    	  
    	  valuesGUI val = new valuesGUI ();
    	  val.setVisible(true);
      }
      
    });
    
    frame.getContentPane().setLayout(new FlowLayout());
    frame.add(opt1);
    frame.add(opt2);
    frame.add(opt3);
    
    frame.add(numberFieldLeft);
    frame.add(numberField1);
    frame.add(numberFieldRight);
   
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setPreferredSize(new Dimension(500,100));
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
}