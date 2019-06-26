package solarSystem;


import java.awt.*;
import java.awt.List;

import javax.media.j3d.*;
import javax.swing.JFrame;
import javax.vecmath.*;

import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.PlatformGeometry;
import com.sun.j3d.utils.behaviors.keyboard.*;
import com.sun.j3d.utils.geometry.Text2D;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import java.io.*;

import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.KeyEvent;

import java.util.*;

import com.sun.j3d.utils.image.TextureLoader;

public class testing1 extends JFrame implements KeyListener {
	    
 Wind wind;

 long startTime = System.nanoTime();
 public static int count = 0;
 private SimpleUniverse universe = null;
 private Canvas3D canvas = null;
 private TransformGroup viewtrans = null;
 public boolean landing = false;
 
 private TransformGroup tg = null;
 private Transform3D t3d = null;
 private Transform3D t3dstep = new Transform3D();
 private Matrix4d matrix = new Matrix4d();

 private MovingProbe probe = null;

private static titanLanderDUO l;
 
 public testing1(double time) throws IOException {
	 
  
  setLayout(new BorderLayout());
  GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();

  canvas = new Canvas3D(config);
  add("Center", canvas);
  universe = new SimpleUniverse(canvas);
  
  BranchGroup scene = createScene();
  
  universe.getViewingPlatform().setNominalViewingTransform();

  universe.getViewer().getView().setBackClipDistance(100.0);

  canvas.addKeyListener(this);

  universe.addBranchGraph(scene);
 }

 private BranchGroup createScene() throws IOException {
  
  BranchGroup root = new BranchGroup();

  BoundingSphere bounds = new BoundingSphere(new Point3d(), 10000.0);

  viewtrans = universe.getViewingPlatform().getViewPlatformTransform();

  KeyNavigatorBehavior keyNavBeh = new KeyNavigatorBehavior(viewtrans);
  keyNavBeh.setSchedulingBounds(bounds);
  PlatformGeometry platformGeom = new PlatformGeometry();
  platformGeom.addChild(keyNavBeh);
  universe.getViewingPlatform().setPlatformGeometry(platformGeom);
  
  // keep getting IIOException when importing the image using the relative path
  TextureLoader backgroundTexture = new TextureLoader("C:\\Users\\Mihai\\eclipse-workspace\\solarSystem\\src\\solarSystem\\textureUltraHD.jpg", this);
  
  Background background1 = new Background(backgroundTexture.getImage());
  background1.setApplicationBounds(bounds);
  
  root.addChild(createProbe());
  root.addChild(background1);
  
  return root;
  }
 

 private BranchGroup createProbe() throws IOException {
 
  BranchGroup root = new BranchGroup();
  tg = new TransformGroup();
  t3d = new Transform3D();

  t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
  t3d.setRotation(new AxisAngle4f(0.0f, 1.0f, 0.0f, 3.0f));
  t3d.setScale(1.0);

  tg.setTransform(t3d);

  // for some reason, the relative path doesn't work when importing, just the absolute paths
  probe = new MovingProbe("C:\\Users\\Mihai\\eclipse-workspace\\solarSystem\\src\\solarSystem\\objects\\cassini.obj");
  tg.addChild(probe.tg);

  tg.addChild(probe);

  root.addChild(tg);
  
  root.addChild(createLight());

  root.compile();

  return root;

 }
 
 
 private Light createLight() {
  DirectionalLight light = new DirectionalLight(true, new Color3f(1.0f,1.0f, 1.0f), new Vector3f(-0.3f, 0.2f, -1.0f));

  light.setInfluencingBounds(new BoundingSphere(new Point3d(), 10000.0));

  return light;
 }

 public static void main(String[] args) throws InterruptedException, IOException {
	 

  testing1 l = new testing1(titanLanderDUO.airTime);
  l.setSize(800,600);
  l.setVisible(true);
  l.setLayout(new FlowLayout());
  l.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  
  
  
 }
 
 ///////////Manual landing section///////////////
 
 public void keyTyped(KeyEvent e) {
  char key = e.getKeyChar();

  if (key == 's') {

   t3dstep.rotY(Math.PI / 32);
   probe.tg.getTransform(probe.t3d);
   probe.t3d.get(matrix);
   probe.t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
   probe.t3d.mul(t3dstep);
   probe.t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13,matrix.m23));
   
   // where m03 is the fourth element of the first row
   // m13 is the fourth element of the second row and so on
   
   probe.tg.setTransform(probe.t3d);
  }

  if (key == 'f') {

   t3dstep.rotY(-Math.PI / 32);
   probe.tg.getTransform(probe.t3d);
   probe.t3d.get(matrix);
   probe.t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
   probe.t3d.mul(t3dstep);
   probe.t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13,matrix.m23));
   probe.tg.setTransform(probe.t3d);
  }

  if (key == 'e') {

   t3dstep.rotX(-Math.PI / 128);
   probe.tg.getTransform(probe.t3d);
   probe.t3d.get(matrix);
   probe.t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
   probe.t3d.mul(t3dstep);
   probe.t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13,matrix.m23));
   probe.tg.setTransform(probe.t3d);

  }

  if (key == 'c') {

   t3dstep.rotX(Math.PI / 128);
   probe.tg.getTransform(probe.t3d);
   probe.t3d.get(matrix);
   probe.t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
   probe.t3d.mul(t3dstep);
   probe.t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13,matrix.m23));
   probe.tg.setTransform(probe.t3d);

  }

 }

 public void keyReleased(KeyEvent e) {
 }

 public void keyPressed(KeyEvent e) {
 }
 

 class MovingProbe extends Behavior {

	  public TransformGroup tg = null;
	  public Transform3D t3d = null;
	  private Transform3D t3dstep = new Transform3D();
	  private WakeupOnElapsedFrames wakeFrame = null;

	  public MovingProbe(String filename) {
	     
	   tg = new TransformGroup();
	   t3d = new Transform3D();  
	   
	   t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
	   t3d.setScale(0.5);
	   tg.setTransform(t3d);
	   
	   
	   ObjectFile loader = new ObjectFile();
	   Scene s = null;

	   File file = new java.io.File(filename);

	   try {
	    s = loader.load(file.toURI().toURL());
	   } catch (Exception e) {
	    System.err.println(e);
	    System.exit(1);
	   }

	   tg.addChild(s.getSceneGroup());

	   tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

	   BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0,0.0), 1000.0);
	   this.setSchedulingBounds(bounds);
	  }
	  
	  //The initialize method allows a Behavior object to 
	  //initialize its internal state and specify its initial wakeup condition
	  public void initialize() {
	   wakeFrame = new WakeupOnElapsedFrames(0);
	   wakeupOn(wakeFrame);
	   count++;
	  }

	  /**
	     * The processStimulus method receives and processes a behavior's ongoing messages. 
	     * The Java 3D behavior scheduler invokes a Behavior node's processStimulus 
	     * method when an active ViewPlatform's activation volume intersects 
	     * a Behavior object's scheduling region and all of that behavior's wakeup 
	     * criteria are satisfied.
	     * 
	     */
	  
	  public int index = 0;
	
	  java.util.List<Double> list1 = Arrays.asList(new Double[]{64.25644388386257, 106.16121598611984,142.81492386298038, 204.73160944839674,256.8621085555021,299.69822641890954, 356.98218422761664, 410.5592396218766,456.66412928769876
			  
			 });
	  
	  //ArrayList<Double> posList = titanLander.Xlist;
	 
	  public void processStimulus(Enumeration criteria) {
		  long timeNow = System.nanoTime();
		  long secondsCount = (timeNow - startTime)/1000000000;
		  //System.out.println(secondsCount);
		  
		  // the translation will be stopped once the secondsCount > time it takes to
		  // the land, once the probe entered the atmosphere - using the variables in titanLanderDUO
		  if(secondsCount > titanLanderDUO.airTime) // (titanLanderDUO.t + titanLanderDUO.time_to_touchdown))
			  t3dstep.set(new Vector3d(0.0, 0.0, 0.0f)); 
		  else {  
				  t3dstep.set(new Vector3d(0.0, 0.0, 0.002f * 9.2 / 10)); // 9.2 is the actual velocity
				  
				  tg.getTransform(t3d);
				  t3d.mul(t3dstep);
				  tg.setTransform(t3d);

				  wakeupOn(wakeFrame);
	      }}
	    }

	}