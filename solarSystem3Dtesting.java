package solarSystem;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.media.j3d.Alpha;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Material;
import javax.media.j3d.RotPosPathInterpolator;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Switch;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JApplet;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.picking.behaviors.PickTranslateBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;

import solarSystem.SimpleModelView.InterpolatorData;
import solarSystem.testing.MovingFighter;

public class solarSystem3Dtesting extends JApplet {

	float scaleEarth = 0.5f;
	boolean pause = false;
	int timeScale = 40;
	BoundingSphere bounds;
	RotationInterpolator Rotator0;
	RotationInterpolator Rotator1;
	Canvas3D canvas;
	BranchGroup root = new BranchGroup();

	public solarSystem3Dtesting() throws IOException {
		
		Container container = getContentPane();
		container.setLayout(new BorderLayout());
		canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
		container.add("Center", canvas);
		BranchGroup scene = buildSceneGraph();
		SimpleUniverse universe = new SimpleUniverse(canvas);
		universe.getViewingPlatform().setNominalViewingTransform();
		universe.addBranchGraph(scene);

		TransformGroup cameraTransformGroup = universe.getViewingPlatform().getViewPlatformTransform();
		
		Vector3f translate = new Vector3f();
		Transform3D T3D = new Transform3D();
		// move along z axis by 10.0f ("move away from the screen")
		translate.set(0.0f,2.0f, 20.0f);
		T3D.setTranslation(translate);
		cameraTransformGroup.setTransform(T3D);
	}
	
	/**	
	    loading a scene from a .obj model
	 	 
		@param location the path of the model
		@return Scene the scene containing the model
		@throws IOException if an error occurs
	*/
	public static Scene getSceneFromFile(String location) throws IOException {
        ObjectFile file = new ObjectFile(ObjectFile.RESIZE);
        return file.load(new FileReader(location));
	}
	
	 /**
        loading a model from disk and assign the root node of the scene
        @throws IOException if it's impossible to find the 3D model
     */
	public void addModelToUniverse() throws IOException {
        Scene scene = getSceneFromFile("C:\\Users\\Mihai\\eclipse-workspace\\solarSystem\\src\\models\\cassini.obj"); 
        root = scene.getSceneGroup();
	}
	
	 private void addLightsToUniverse() {
	        Bounds influenceRegion = new BoundingSphere();
	        Color3f lightColor = new Color3f(Color.BLUE);
	        Vector3f lightDirection = new Vector3f(-1F, -1F, -1F);
	        DirectionalLight light = new DirectionalLight(lightColor, lightDirection);
	        light.setInfluencingBounds(influenceRegion);
	        root.addChild(light);
	    }

	
	void listSceneNamedObjects(Scene root) {
	    Map<String, Shape3D> nameMap = root.getNamedObjects(); 

	    for (String name : nameMap.keySet()) {
	        System.out.printf("Name: %s\n", name); 
	    }
	}
        
	public BranchGroup buildSceneGraph() throws IOException {
		//universe bounds
		bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 10.0);

		//creating the transform group for the main branchgroup
		TransformGroup mainTransformGroup = new TransformGroup();
		mainTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		mainTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

		//creating different branchgroups for each element of the universe
		
		BranchGroup sunBranchGroup = new BranchGroup();
		BranchGroup earthBranchGroup = new BranchGroup();
		BranchGroup marsBranchGroup = new BranchGroup();
		BranchGroup jupiterBranchGroup = new BranchGroup();
		BranchGroup titanBranchGroup = new BranchGroup();
		BranchGroup neptuneBranchGroup = new BranchGroup();
		BranchGroup plutoBranchGroup = new BranchGroup();
		BranchGroup mercuryBranchGroup = new BranchGroup();
		BranchGroup venusBranchGroup = new BranchGroup();
		BranchGroup saturnBranchGroup = new BranchGroup();
		BranchGroup uranusBranchGroup = new BranchGroup();
		
		BranchGroup cassiniBranchGroup = new BranchGroup();
		
		
		// --------------------- SUN -----------------------------------------

		//scale
		Transform3D sunTransform1 = new Transform3D();
		sunTransform1.setScale(new Vector3d(1.0, 1.0, 1.0));
		TransformGroup sunTransformGroup1 = new TransformGroup(sunTransform1);

		//adding light to the sun
		Color3f ambientColourSun = new Color3f(0.0f, 0.0f, 0.0f);
		Color3f emissiveColourSun = new Color3f(0.0f, 0.0f, 0.0f);
		Color3f diffuseColourSun = new Color3f(0.8f, 0.4f, 0.0f);
		Color3f specularColourSun = new Color3f(0.8f, 0.8f, 0.0f);
		float shininessSun = 100.0f;
	
		//texture
		TextureLoader loader = new TextureLoader("C:\\Users\\Mihai\\eclipse-workspace\\solarSystem\\src\\sun.jpg","LUMINANCE", new Container());
		Texture texture = loader.getTexture();
		texture.setBoundaryModeS(Texture.WRAP);
		texture.setBoundaryModeT(Texture.WRAP);
		texture.setBoundaryColor(new Color4f(0.0f,1.0f,0.0f,0.0f));
		
		// setting up texture attribute
		TextureAttributes textAttr = new TextureAttributes();
		textAttr.setTextureMode(TextureAttributes.MODULATE);
		
		// combining the texture and lighting
		Appearance appearance = new Appearance();
		appearance.setTexture(texture);
		appearance.setTextureAttributes(textAttr);
		
		// setting up material to give it a glow
		appearance.setMaterial(new Material(ambientColourSun,emissiveColourSun, diffuseColourSun,
				specularColourSun, shininessSun));
		
		int primflags = Primitive.GENERATE_NORMALS + Primitive.GENERATE_TEXTURE_COORDS;
		
		Sphere sun = new Sphere(1.0f,primflags,550,appearance);	
				  
	    root.addChild(sun);

	   
	    
		/////////////////Sun-Lighting//////////////////////////////
		
		// Creating lighting for the sun
		Color3f light1Color = new Color3f(1.0f,1.0f,1.0f);
		
		Vector3f light1Direction = new Vector3f(4.0f,-7.0f,-12.0f);
		DirectionalLight light1 = new DirectionalLight(light1Color,light1Direction);
		light1.setInfluencingBounds(bounds);
		sunBranchGroup.addChild(light1);
		AmbientLight ambientLight = new AmbientLight(new Color3f(1.0f,1.0f,0.0f));
		ambientLight.setInfluencingBounds(bounds);
		
		Color3f light2Color = new Color3f(1.0f,1.0f,1.0f);
		
		Vector3f light2Direction = new Vector3f(-4.0f,7.0f,12.0f);
		DirectionalLight light2 = new DirectionalLight(light2Color,light2Direction);
		light2.setInfluencingBounds(bounds);
		sunBranchGroup.addChild(light2);
		AmbientLight ambientLight2 = new AmbientLight(new Color3f(1.0f,1.0f,0.0f));
		ambientLight2.setInfluencingBounds(bounds);
		
		// adding sun to object 
		root.addChild(mainTransformGroup);
		mainTransformGroup.addChild(sunBranchGroup);
		sunBranchGroup.addChild(sunTransformGroup1);
	
		
		
		////////////////////Earth//////////////////////////


		TransformGroup earthTransformGroup0 = new TransformGroup();
		earthTransformGroup0.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D earthSunTranform3D0 = new Transform3D();
		//rotation around the sun
		setRotationAroundSun(earthTransformGroup0, earthSunTranform3D0,365 * timeScale);

		Transform3D earthTranform3D1 = new Transform3D();
		//scale
		earthTranform3D1.setScale(new Vector3d(0.5, 0.5, 0.5));
		//translation
		earthTranform3D1.setTranslation(new Vector3d(0.0, 0.0, 3.5));

		//applying effect to transform group
		TransformGroup earthTransformGroup1 = new TransformGroup(earthTranform3D1);

		TransformGroup earthTransformGroup2 = new TransformGroup();
		earthTransformGroup2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D earthTransform3D2 = new Transform3D();
		//y axis rotation
		setYAxisRotation(earthTransform3D2, earthTransformGroup2);

		
		// adding light to the sun
		Color3f ambientColourEarth = new Color3f(0.3f, 0.2f, 0.4f);
		Color3f emissiveColourEarth = new Color3f(0.0f, 0.0f, 0.0f);
		Color3f diffuseColourEarth = new Color3f(0.3f, 0.6f, 0.0f);
		Color3f specularColourEarth = new Color3f(0.3f, 0.6f, 0.0f);
		float shininessEarth = 40.0f;
			
		// texture
		TextureLoader loaderEarth = new TextureLoader("C:\\Users\\Mihai\\eclipse-workspace\\solarSystem\\src\\solarSystem\\Images\\earth.jpg","LUMINANCE", new Container());
		Texture textureEarth = loaderEarth.getTexture();
		textureEarth.setBoundaryModeS(Texture.WRAP);
		textureEarth.setBoundaryModeT(Texture.WRAP);
		textureEarth.setBoundaryColor(new Color4f(0.0f,0.0f,1.0f,0.0f));
				
		// setting up texture attribute
		TextureAttributes textAttrEarth = new TextureAttributes();
		textAttrEarth.setTextureMode(TextureAttributes.MODULATE);
				
		// combining the texture and lighting
		Appearance appearanceEarth = new Appearance();
		appearanceEarth.setTexture(textureEarth);
		appearanceEarth.setTextureAttributes(textAttrEarth);
				
		// setting up material to give it a glow
		appearanceEarth.setMaterial(new Material(ambientColourEarth,emissiveColourEarth, diffuseColourEarth,
				specularColourEarth, shininessEarth));
				
		int primflagsEarth = Primitive.GENERATE_NORMALS + Primitive.GENERATE_TEXTURE_COORDS;
		
		Sphere earth = new Sphere(scaleEarth,primflags,300,appearance);
		/////

		mainTransformGroup.addChild(earthBranchGroup);
		earthBranchGroup.addChild(earthTransformGroup0);
		earthTransformGroup0.addChild(earthTransformGroup1);
		earthTransformGroup1.addChild(earthTransformGroup2);

		earthTransformGroup0.addChild(getYRotator());
		earthTransformGroup2.addChild(getAroundSunRotator());
		earthTransformGroup2.addChild(earth);
		
		///////////////////End-Earth-test//////////////////
		
		// sphere <<earth>> without using a TextureAttribute
		// Making "fancy" glowy looking spheres like the one we used for the Sun
		// uses tons of resources
		// can't even run the program anymore since I get a <<out of heap memory>> exception
		
		/*
		TransformGroup earthTransformGroup0 = new TransformGroup();
		earthTransformGroup0.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D earthSunTranform3D0 = new Transform3D();
		//rotation around the sun
		setRotationAroundSun(earthTransformGroup0, earthSunTranform3D0,365 * timeScale);

		Transform3D earthTranform3D1 = new Transform3D();
		//scale
		earthTranform3D1.setScale(new Vector3d(0.5, 0.5, 0.5));
		//translation
		earthTranform3D1.setTranslation(new Vector3d(0.0, 0.0, 3.5));

		//applying effect to transform group
		TransformGroup earthTransformGroup1 = new TransformGroup(earthTranform3D1);

		TransformGroup earthTransformGroup2 = new TransformGroup();
		earthTransformGroup2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D earthTransform3D2 = new Transform3D();
		//y axis rotation
		setYAxisRotation(earthTransform3D2, earthTransformGroup2);

		//creating planet with image texture
		Sphere earth = new Sphere(0.5f, getPrimitiveFlag(), 550,getTexture("C:\\Users\\Mihai\\eclipse-workspace\\solarSystem\\src\\solarSystem\\Images\\earth.jpg"));


		mainTransformGroup.addChild(earthBranchGroup);
		earthBranchGroup.addChild(earthTransformGroup0);
		earthTransformGroup0.addChild(earthTransformGroup1);
		earthTransformGroup1.addChild(earthTransformGroup2);

		earthTransformGroup0.addChild(getYRotator());
		earthTransformGroup2.addChild(getAroundSunRotator());
		earthTransformGroup2.addChild(earth);
		
		*/
		
		/////////////////////Moon////////////////////////////////
		TransformGroup moonTransformGroup0 = new TransformGroup();
		moonTransformGroup0.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D moonTranform3D0 = new Transform3D();
		// setting rotation around the sun
		setRotationAroundSun(moonTransformGroup0, moonTranform3D0,15000);

		Transform3D moonTranform3D1 = new Transform3D();
		// setting the scale of moon, later, when creating
		// the actual object, we approximate it to 1/4 of Earth's size
		moonTranform3D1.setScale(new Vector3d(1f, 1f, 1f));
		// setting translation - where the distance from Earth to moon has been approximated
		// to 400k km 
		moonTranform3D1.setTranslation(new Vector3d(0.0, 0.0, 0.4));

		// applying effect to transform group
		TransformGroup moonTransformGroup1 = new TransformGroup(moonTranform3D1);

		TransformGroup moonTransformGroup2 = new TransformGroup();
		moonTransformGroup2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D moonTransform3D2 = new Transform3D();
		// setting y axis rotation
		setYAxisRotation(moonTransform3D2, moonTransformGroup2);

		// creating planet with image texture - a quarter of Earth's diameter
		Sphere moon = new Sphere(0.25f * scaleEarth, getPrimitiveFlag(), 550,getTexture("C:\\Users\\Mihai\\eclipse-workspace\\solarSystem\\src\\solarSystem\\Images\\moon.jpg"));

		// adding moon to earth transform group, so that Earth's <<TransformGroup>> 
		// will rotate both objects, Earth and moon, around the sun
		earthTransformGroup2.addChild(moonTransformGroup0);

		moonTransformGroup0.addChild(moonTransformGroup1);
		moonTransformGroup1.addChild(moonTransformGroup2);

		moonTransformGroup0.addChild(getAroundSunRotator());
		moonTransformGroup2.addChild(getYRotator());
		moonTransformGroup2.addChild(moon);

		//////////////////Mars//////////////////////////////
		
		TransformGroup marsTransformGroup0 = new TransformGroup();
		marsTransformGroup0.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D marsSunTranform3D0 = new Transform3D();
		// rotation around the sun where 687 represents the Earth days for a full rotation
		// around sun 
		setRotationAroundSun(marsTransformGroup0, marsSunTranform3D0,687 * timeScale);

		Transform3D marsTranform3D1 = new Transform3D();
		//scale
		marsTranform3D1.setScale(new Vector3d(1f, 1f, 1f));
		//translation
		marsTranform3D1.setTranslation(new Vector3d(0.0, 0.0, 4.5));

		//applying effect to transform group
		TransformGroup marsTransformGroup1 = new TransformGroup(marsTranform3D1);

		TransformGroup marsTransformGroup2 = new TransformGroup();
		marsTransformGroup2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D marsTransform3D2 = new Transform3D();
		//y axis rotation
		setYAxisRotation(marsTransform3D2, marsTransformGroup2);

		//creating planet with image texture, where the size of Sphere <<mars>> has been
		//estimated to half of <<earth>>
		Sphere mars = new Sphere(0.5f * scaleEarth, getPrimitiveFlag(), 550,getTexture("C:\\Users\\Mihai\\eclipse-workspace\\solarSystem\\src\\solarSystem\\Images\\mars.jpg"));


		mainTransformGroup.addChild(marsBranchGroup);
		marsBranchGroup.addChild(marsTransformGroup0);
		marsTransformGroup0.addChild(marsTransformGroup1);
		marsTransformGroup1.addChild(marsTransformGroup2);

		marsTransformGroup0.addChild(getYRotator());
		marsTransformGroup2.addChild(getAroundSunRotator());
		marsTransformGroup2.addChild(mars);
		
		///////////Mercury////////////////
		TransformGroup mercuryTransformGroup0 = new TransformGroup();
		mercuryTransformGroup0.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D mercurySunTranform3D0 = new Transform3D();
		
		//rotation around the sun
		setRotationAroundSun(mercuryTransformGroup0, mercurySunTranform3D0,88 * timeScale);

		Transform3D mercuryTranform3D1 = new Transform3D();
		
		//scale
		mercuryTranform3D1.setScale(new Vector3d(1.0f, 1.0f, 1.0f));
		
		//translation
		mercuryTranform3D1.setTranslation(new Vector3d(0.0, 0.0, 1.5));

		//applying the transformation to transformGroup
		TransformGroup mercuryTransformGroup1 = new TransformGroup(mercuryTranform3D1);

		TransformGroup mecuryTransformGroup2 = new TransformGroup();
		mecuryTransformGroup2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D mecuryTransform3D2 = new Transform3D();
		//y axis rotation
		setYAxisRotation(mecuryTransform3D2, mecuryTransformGroup2);

		//creating planet with image texture, where mercury represents 2/5 of
		//Earth's size
		Sphere mercury = new Sphere(2/5f * scaleEarth, getPrimitiveFlag(),550,getTexture("C:\\Users\\Mihai\\eclipse-workspace\\solarSystem\\src\\mercury.jpg"));

		// Mercury
		mainTransformGroup.addChild(mercuryBranchGroup);
		mercuryBranchGroup.addChild(mercuryTransformGroup0);
		mercuryTransformGroup0.addChild(mercuryTransformGroup1);
		mercuryTransformGroup1.addChild(mecuryTransformGroup2);

		mercuryTransformGroup0.addChild(getYRotator());
		mecuryTransformGroup2.addChild(getAroundSunRotator());
		mecuryTransformGroup2.addChild(mercury);

		//////////Venus/////////////////////////////

		TransformGroup venusTransformGroup0 = new TransformGroup();
		venusTransformGroup0.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D venusSunTranform3D0 = new Transform3D();
		//rotation around the sun
		setRotationAroundSun(venusTransformGroup0, venusSunTranform3D0, 225 * timeScale);

		Transform3D venusTranform3D1 = new Transform3D();
		//scale
		venusTranform3D1.setScale(new Vector3d(1.0f, 1.0f, 1.0f));
		//translation
		venusTranform3D1.setTranslation(new Vector3d(0.0, 0.0, 2.5));

		//applying effect to transform group
		TransformGroup venusTransformGroup1 = new TransformGroup(venusTranform3D1);

		TransformGroup venusTransformGroup2 = new TransformGroup();
		venusTransformGroup2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D venusTransform3D2 = new Transform3D();
		//y axis rotation
		setYAxisRotation(venusTransform3D2, venusTransformGroup2);

		//creating planet with image texture, where <<venus>>'s diameter is
		//95% of <<earth>>'s diameter
		Sphere venus = new Sphere(0.95f * scaleEarth, getPrimitiveFlag(), 550,getTexture("C:\\Users\\Mihai\\eclipse-workspace\\solarSystem\\src\\solarSystem\\Images\\venus.jpg"));

		mainTransformGroup.addChild(venusBranchGroup);
		venusBranchGroup.addChild(venusTransformGroup0);
		venusTransformGroup0.addChild(venusTransformGroup1);
		venusTransformGroup1.addChild(venusTransformGroup2);

		venusTransformGroup0.addChild(getYRotator());
		venusTransformGroup2.addChild(getAroundSunRotator());
		venusTransformGroup2.addChild(venus);

		//////////////////Jupiter/////////////////////////
		
		TransformGroup jupiterTransformGroup0 = new TransformGroup();
		jupiterTransformGroup0.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D jupiterSunTranform3D0 = new Transform3D();
		//rotation around the sun
		setRotationAroundSun(jupiterTransformGroup0, jupiterSunTranform3D0,4332 * timeScale );

		Transform3D jupitorTranform3D1 = new Transform3D();
		//scale
		jupitorTranform3D1.setScale(new Vector3d(1.0, 1.0, 1.0));
		//translation
		jupitorTranform3D1.setTranslation(new Vector3d(0.0, 0.0, 5.5));

		//applying effect to transform group
		TransformGroup jupiterTransformGroup1 = new TransformGroup(jupitorTranform3D1);

		TransformGroup jupiterTransformGroup2 = new TransformGroup();
		jupiterTransformGroup2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D jupiterTransform3D2 = new Transform3D();
		//y axis rotation
		setYAxisRotation(jupiterTransform3D2, jupiterTransformGroup2);

		//creating planet with image texture, where jupiter is 11x bigger than earth
		//made it only 5x bigger than earth due to visualization purposes
		Sphere jupiter = new Sphere(scaleEarth * 5f, getPrimitiveFlag(), 550,getTexture("C:\\Users\\Mihai\\eclipse-workspace\\solarSystem\\src\\solarSystem\\Images\\jupiter.jpg"));

		mainTransformGroup.addChild(jupiterBranchGroup);
		jupiterBranchGroup.addChild(jupiterTransformGroup0);
		jupiterTransformGroup0.addChild(jupiterTransformGroup1);
		jupiterTransformGroup1.addChild(jupiterTransformGroup2);

		jupiterTransformGroup0.addChild(getYRotator());
		jupiterTransformGroup2.addChild(getAroundSunRotator());
		jupiterTransformGroup2.addChild(jupiter);

		/////////////////////Saturn////////////////////

		TransformGroup saturnTransformGroup0 = new TransformGroup();
		saturnTransformGroup0.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D saturnSunTranform3D0 = new Transform3D();
		//rotation around the sun
		setRotationAroundSun(saturnTransformGroup0, saturnSunTranform3D0, 10759 * timeScale);

		Transform3D saturnTranform3D1 = new Transform3D();
		//scale
		saturnTranform3D1.setScale(new Vector3d(1.0, 1.0, 1.0));
		//translation
		saturnTranform3D1.setTranslation(new Vector3d(0.0, 0.0, 7.5));

		//applying effect to transform group
		TransformGroup saturnTransformGroup1 = new TransformGroup(saturnTranform3D1);

		TransformGroup saturnTransformGroup2 = new TransformGroup();
		saturnTransformGroup2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D saturnTransform3D2 = new Transform3D();
		//y axis rotation
		setYAxisRotation(saturnTransform3D2, saturnTransformGroup2);

		//creating planet with image texture
		//since the volume of saturn is 764x bigger than earth's
		//a realistic 3D visualization is unreachable
		//used a 10:1 scale
		Sphere saturn = new Sphere(scaleEarth * 10f, getPrimitiveFlag(), 550,getTexture("C:\\Users\\Mihai\\eclipse-workspace\\solarSystem\\src\\solarSystem\\Images\\saturn.jpg"));

		mainTransformGroup.addChild(saturnBranchGroup);
		saturnBranchGroup.addChild(saturnTransformGroup0);
		saturnTransformGroup0.addChild(saturnTransformGroup1);
		saturnTransformGroup1.addChild(saturnTransformGroup2);

		saturnTransformGroup0.addChild(getYRotator());
		saturnTransformGroup2.addChild(getAroundSunRotator());
		saturnTransformGroup2.addChild(saturn);
		
		/////////////////////Titan////////////////////

		TransformGroup titanTransformGroup0 = new TransformGroup();
		titanTransformGroup0.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D titanTranform3D0 = new Transform3D();
		//rotation around the sun
		setRotationAroundSun(titanTransformGroup0, titanTranform3D0,15000);

		Transform3D titanTranform3D1 = new Transform3D();
		//scale
		titanTranform3D1.setScale(new Vector3d(1.0f, 1.0f, 1.0f));
		//translation, where distance between Titan and Saturn has been
		//approximated to 1.221k km
		titanTranform3D1.setTranslation(new Vector3d(0.0, 0.0, 1.221));

		//applying effect to transform group
		TransformGroup titanTransformGroup1 = new TransformGroup(titanTranform3D1);

		TransformGroup titanTransformGroup2 = new TransformGroup();
		titanTransformGroup2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D titanTransform3D2 = new Transform3D();
		//y axis rotation
		setYAxisRotation(titanTransform3D2, titanTransformGroup2);

		//creating planet with image texture
		//where titan is roughly half the size of Earth
		Sphere titan = new Sphere(0.5f * scaleEarth, getPrimitiveFlag(), 550,getTexture("C:\\Users\\Mihai\\eclipse-workspace\\solarSystem\\src\\titan.jpg"));

		saturnTransformGroup2.addChild(titanTransformGroup0);

		titanTransformGroup0.addChild(titanTransformGroup1);
		titanTransformGroup1.addChild(titanTransformGroup2);

		titanTransformGroup0.addChild(getAroundSunRotator());
		titanTransformGroup2.addChild(getYRotator());
		titanTransformGroup2.addChild(titan);	
		
		//////////////////////Uranus//////////////////////

		TransformGroup uranusTransformGroup0 = new TransformGroup();
		uranusTransformGroup0.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D uranusSunTranform3D0 = new Transform3D();
		//rotation around the sun
		setRotationAroundSun(uranusTransformGroup0, uranusSunTranform3D0,30688 * timeScale);

		Transform3D uranusTranform3D1 = new Transform3D();
		//scale
		uranusTranform3D1.setScale(new Vector3d(1.0f, 1.0f, 1.0f));
		//translation
		uranusTranform3D1.setTranslation(new Vector3d(0.0, 0.0, 9.5));

		//applying effect to transform group
		TransformGroup uranusTransformGroup1 = new TransformGroup(uranusTranform3D1);

		TransformGroup uranusTransformGroup2 = new TransformGroup();
		uranusTransformGroup2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D uranusTransform3D2 = new Transform3D();
		//y axis rotation
		setYAxisRotation(uranusTransform3D2, uranusTransformGroup2);

		//creating planet with image texture
		//where uranus is roughly 4x the size of earth
		Sphere uranus = new Sphere(4.0f * scaleEarth, getPrimitiveFlag(), 550,getTexture("C:\\Users\\Mihai\\eclipse-workspace\\solarSystem\\src\\solarSystem\\Images\\uranus.jpg"));

		mainTransformGroup.addChild(uranusBranchGroup);
		uranusBranchGroup.addChild(uranusTransformGroup0);
		uranusTransformGroup0.addChild(uranusTransformGroup1);
		uranusTransformGroup1.addChild(uranusTransformGroup2);

		uranusTransformGroup0.addChild(getYRotator());
		uranusTransformGroup2.addChild(getAroundSunRotator());
		uranusTransformGroup2.addChild(uranus);
		
		//////////////////////Neptune//////////////////////
		
		TransformGroup neptuneTransformGroup0 = new TransformGroup();
		neptuneTransformGroup0.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D neptuneSunTranform3D0 = new Transform3D();
		//rotation around the sun
		setRotationAroundSun(neptuneTransformGroup0, neptuneSunTranform3D0, 60182 * timeScale );

		Transform3D neptuneTranform3D1 = new Transform3D();
		//scale
		neptuneTranform3D1.setScale(new Vector3d(1.0f, 1.0f, 1.0f));
		//translation
		neptuneTranform3D1.setTranslation(new Vector3d(0.0, 0.0, 10.5));

		//applying effect to transform group
		TransformGroup neptuneTransformGroup1 = new TransformGroup(neptuneTranform3D1);

		TransformGroup neptuneTransformGroup2 = new TransformGroup();
		neptuneTransformGroup2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D neptuneTransform3D2 = new Transform3D();
		//y axis rotation
		setYAxisRotation(neptuneTransform3D2, neptuneTransformGroup2);

		//creating planet with image texture
		//where neptune is 3.9x bigger than earth
		Sphere neptune = new Sphere(3.9f * scaleEarth, getPrimitiveFlag(), 550,getTexture("C:\\Users\\Mihai\\eclipse-workspace\\solarSystem\\src\\solarSystem\\Images\\neptune.jpg"));

		mainTransformGroup.addChild(neptuneBranchGroup);
		neptuneBranchGroup.addChild(neptuneTransformGroup0);
		neptuneTransformGroup0.addChild(neptuneTransformGroup1);
		neptuneTransformGroup1.addChild(neptuneTransformGroup2);

		neptuneTransformGroup0.addChild(getYRotator());
		neptuneTransformGroup2.addChild(getAroundSunRotator());
		neptuneTransformGroup2.addChild(neptune);

		//////////////////////Pluto////////////////////////////////////
		
		TransformGroup plutoTransformGroup0 = new TransformGroup();
		plutoTransformGroup0.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D plutoSunTranform3D0 = new Transform3D();
		//rotation around the sun
		setRotationAroundSun(plutoTransformGroup0, plutoSunTranform3D0,160000);

		Transform3D plutoTranform3D1 = new Transform3D();
		//scale
		plutoTranform3D1.setScale(new Vector3d(1.0f, 1.0f, 1.0f));
		//translation
		plutoTranform3D1.setTranslation(new Vector3d(0.0, 0.0, 12.0));

		//applying effect to transform group
		TransformGroup plutoTransformGroup1 = new TransformGroup(plutoTranform3D1);

		TransformGroup plutoTransformGroup2 = new TransformGroup();
		plutoTransformGroup2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D plutoTransform3D2 = new Transform3D();
		//y axis rotation
		setYAxisRotation(plutoTransform3D2, plutoTransformGroup2);

		//creating planet with image texture
		//where the shpere <<pluto>> has been estimated to be around 1/6 the size 
		//of earth
		Sphere pluto = new Sphere(1/6f * scaleEarth, getPrimitiveFlag(),550,getTexture("C:\\Users\\Mihai\\eclipse-workspace\\solarSystem\\src\\solarSystem\\Images\\pluto.jpg"));

		//adding transform to mainTG and correct transform group
		mainTransformGroup.addChild(plutoBranchGroup);
		plutoBranchGroup.addChild(plutoTransformGroup0);
		plutoTransformGroup0.addChild(plutoTransformGroup1);
		plutoTransformGroup1.addChild(plutoTransformGroup2);

		plutoTransformGroup0.addChild(getYRotator());
		plutoTransformGroup2.addChild(getAroundSunRotator());
		plutoTransformGroup2.addChild(pluto);
		
		
		/////////////////////////CASSINI////////////////////////////
		
		// creating lighting and texture
		Color3f ambientColourCassini = new Color3f(1.0f, 1.0f, 1.1f);
		Color3f emissiveColourCassini = new Color3f(0.0f, 0.0f, 0.0f);
		Color3f diffuseColourCassini = new Color3f(1.0f, 1.0f, 1.1f);
		Color3f specularColourCassini = new Color3f(1.0f, 0.0f, 1.1f);
		float shininessCassini = 90.0f;
			
		Appearance cassiniAppearance = new Appearance();
			
		// material for cassini
		cassiniAppearance.setMaterial(new Material(ambientColourCassini,
			emissiveColourCassini, diffuseColourCassini,
			specularColourCassini, shininessCassini));
			
		// cassini shell 
		Transform3D cassiniTF = new Transform3D();
		
		TransformGroup cassiniTransformGroup1 = new TransformGroup(cassiniTF);
		Cylinder cassiniShell = new Cylinder(0.1f,0.50f,cassiniAppearance);
			
		// cassini probe - nose (cone)
		// was supposed to implement the idea: the probe detaching itself
		// from the actual rocket once it enters orbit
		// way too sci-fi to implement without a game engine
		// like most of this 3d simulation...
		
		Transform3D cassiniNoseTF = new Transform3D();
		cassiniNoseTF.setTranslation(new Vector3f(0.0f,0.30f,0.0f));
		//TransformGroup rocketTG2 = new TransformGroup(rocketNoseTF);
		Cone rocketNose = new Cone(0.1f,0.1f,cassiniAppearance);
			
			
		Transform3D cassiniTranform3D0 = new Transform3D();
			
		TransformGroup cassiniTransformGroup0 = new TransformGroup(cassiniTranform3D0);
		cassiniTransformGroup0.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
			
		/////////////
			
		//Alpha alphaNave = new Alpha( -1, Alpha.INCREASING_ENABLE, 0,0,6000,0,0,0,0,0 );
		TransformGroup target = new TransformGroup();
		//Transform3D axisOfRotPos = new Transform3D();
		//float[] alphas = {0.0f, 0.25f, 0.50f, 0.75f, 1.0f};
		Quat4f[] quats = new Quat4f[5];
		Point3f[] positions = new Point3f[5];

		target.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		// quaternion values
		quats[0] = new Quat4f(0.0f, 1.0f, 0.0f, 0.0f);
		quats[1] = new Quat4f(0.0f, 1.0f, 0.0f, 0.0f);
		quats[2] = new Quat4f(0.0f, 1.0f, 0.0f, 0.0f);
		quats[3] = new Quat4f(0.0f, 1.0f, 0.0f, 0.0f);
		quats[4] = quats[0];
		
		positions[0]= new Point3f( -20.0f,  0.0f, 20.0f);
		positions[1]= new Point3f( -20.0f, 0.0f, -20.0f);
		positions[2]= new Point3f( 20.0f,  0.0f, -20.0f);
		positions[3]= new Point3f( 20.0f,  0.0f, 20.0f);
		positions[4]= positions[0];
			
		////////////
			
		Vector3f positionTitan = new Vector3f();
		titan.getLocalToVworld(titanTranform3D1); 
		titanTranform3D1.get(positionTitan);
		
		Vector3f positionEarth = new Vector3f();
		venus.getLocalToVworld(mercuryTranform3D1); 
		venusTranform3D1.get(positionEarth);
			
		///////////
		
		Transform3D axisOfTransform = new Transform3D();
	    
		InterpolatorData i = new InterpolatorData();
	  
	    i.add(new Point3f(positionEarth), 0.0f);
	    i.add(new Point3f(positionTitan), 0.0f);
	    i.add(new Point3f(positionEarth), 0.0f);
	    
	    // in theory, the 3d simulation of probe's path to Titan and back is cyclical
	    // unless it's stopped by the timer telling us it got to Titan
	       
	     Alpha alpha = new Alpha(-1, (long)titanLanderDUO.t);
	        
	     /**
	      * RotRosPathInterpolator class class defines a behavior that modifies
	      * the rotational and translational components of its target 
	      * TransformGroup by linearly interpolating among a series of predefined
	      * knot/positon and knot/orientation pairs (using the value generated
	      * by the specified Alpha object - aka speed). The interpolated position and
	      * orientationare used to generate a transform in the local coordinate system of this interpolator.
	      * 
	      */
	     
	     RotPosPathInterpolator interpolator = new RotPosPathInterpolator(
	         alpha, cassiniTransformGroup0, axisOfTransform, 
	         i.getAlphas(), i.getOrientations(), i.getPositions());        
	     interpolator.setSchedulingBounds(
	     new BoundingSphere(new Point3d(), 100.0));
			
		mainTransformGroup.addChild(cassiniBranchGroup);
		cassiniBranchGroup.addChild(cassiniTransformGroup0);
		cassiniTransformGroup0.addChild(cassiniTransformGroup1);
		cassiniTransformGroup0.addChild(interpolator);
		cassiniTransformGroup1.addChild(cassiniShell);
		
			
	    //////////////////////BACKGROUND//////////////////////////////////
		
	    //setting the background
		TextureLoader loader1 = new TextureLoader("C:\\Users\\Mihai\\eclipse-workspace\\solarSystem\\src\\solarSystem\\Images\\bg.jpg", this);
		ImageComponent2D image = loader1.getImage();
		Background bg = new Background();
		bg.setImage(image);
		bg.setApplicationBounds(bounds);
		bg.setCapability(Background.ALLOW_IMAGE_WRITE);
		root.addChild(bg);
		
		//In order to allow navigation through the scene
	    //everything must be collected in a separate transformation group to which 
	    //the KeyNavigatorBehavior is applied to modify the view platform transformation
		
	    KeyNavigatorBehavior knb = new KeyNavigatorBehavior(mainTransformGroup);
	    knb.setSchedulingBounds(bounds);
	    mainTransformGroup.addChild(knb);
	    
	    PickTranslateBehavior pickTrans = new PickTranslateBehavior(root,canvas,bounds);
	    root.addChild(pickTrans); 

		MouseRotate behavior = new MouseRotate();
		behavior.setTransformGroup(mainTransformGroup);
		root.addChild(behavior);
		behavior.setSchedulingBounds(bounds);

		MouseZoom behavior2 = new MouseZoom();
		behavior2.setTransformGroup(mainTransformGroup);
		root.addChild(behavior2);
		behavior2.setSchedulingBounds(bounds);

		root.compile();
		return root;
	}  
	
	/**
     * metod sets the rotation around the sun, adding the Rotation
     * Interpolator to the transformation group
     * 
     * @param transformGroup
     * @param transform3D
     * @param speed
     * 
     */
	
	public void setRotationAroundSun(TransformGroup transformGroup,
			Transform3D transform3D,int speed) {
		Rotator0 = new RotationInterpolator(new Alpha(-1, speed),
				transformGroup, transform3D, 0.0f, (float) Math.PI * 2);
		Rotator0.setSchedulingBounds(bounds);
	}

	public static void main(String[] args) throws IOException {
		new MainFrame(new solarSystem3Dtesting(), 512, 512);
	}
	
	
	private static class InterpolatorData
    {
        private final List<Point3f> positions = new ArrayList<Point3f>();
        private final List<Quat4f> orientations = new ArrayList<Quat4f>();

        void add(Point3f p, float angleDeg)
        {
            positions.add(p);

            AxisAngle4f a = new AxisAngle4f(
                0.0f, 1.0f, 0.0f, (float) Math.toRadians(angleDeg));
            Quat4f q = new Quat4f();
            q.set(a);
            orientations.add(q);
        }

        Point3f[] getPositions()
        {
            return positions.toArray(new Point3f[0]);
        }

        Quat4f[] getOrientations()
        {
            return orientations.toArray(new Quat4f[0]);
        }

        float[] getAlphas()
        {
            float alphas[] = new float[positions.size()];
            float delta = 1.0f / (alphas.length - 1);
            for (int i = 0; i < alphas.length; i++)
            {
                alphas[i] = i * delta;
            }
            return alphas;
        }
              
   }
    	
	/**
     * getter for the Y Axis rotation
     * @return Rorator0 of type RotationInterpolator
     */
	public RotationInterpolator getYRotator() {
		return Rotator0;
	}
	
	/**
     * setter for the Y Axis rotation
     * adding the Rotation Interpolator to the transformation group 
     */
	public void setYAxisRotation(Transform3D transform3D,
			TransformGroup transformGroup) {
		
		// creating RotationInterpolator with Alpha (speed) 
		Rotator1 = new RotationInterpolator(new Alpha(-1, 1900),
				transformGroup, transform3D, 0.0f, (float) Math.PI * (2.0f));
		Rotator1.setSchedulingBounds(bounds);
	}
	
	/**
     * getter for RotationInterpolator for the effect of rotation around sun 
     * @return Rorator1 of type RotationInterpolator
     */
	
	public RotationInterpolator getAroundSunRotator() {
		return Rotator1;
	}
	
	/**
     * method creates a image texture
     * @return a appear which can be used in all primitive objects
     * the appearance object defines all rendering state that can be set 
     * as a component object of a Shape3D node
     */
	public Appearance getTexture(String imgPath) {
		TextureLoader TextureLoader0 = new TextureLoader(imgPath,new Container());
		Texture Texture0 = TextureLoader0.getTexture();
		Texture0.setBoundaryModeS(Texture.WRAP);
		Texture0.setBoundaryModeT(Texture.WRAP);
		Texture0.setBoundaryColor(new Color4f(0.0f, 1.0f, 0.0f, 0.0f));
		TextureAttributes TextureAttribute0 = new TextureAttributes();

		Appearance appearance = new Appearance();
		appearance.setTexture(Texture0);
		appearance.setTextureAttributes(TextureAttribute0);

		return appearance;
	}

	/**
     * getter for the current primitive's flag 
     * @return int
     */
	public int getPrimitiveFlag() {
		int primFlag = Primitive.GENERATE_NORMALS + Primitive.GENERATE_TEXTURE_COORDS;
		return primFlag;
	}
 
	/**
     * method to add the light to the branch
     */
	protected void addLights(BranchGroup branch) {
		
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),100.0);
		
		// Set up the ambient light
		Color3f ambientColour = new Color3f(0.2f, 0.2f, 0.2f);
		AmbientLight ambientLight = new AmbientLight(ambientColour);
		ambientLight.setInfluencingBounds(bounds);
		
		// Set up the directional light
		Color3f lightColour = new Color3f(1.0f, 1.0f, 1.0f);
		Vector3f lightDir = new Vector3f(-1.0f, -1.0f, -1.0f);
		DirectionalLight light = new DirectionalLight(lightColour, lightDir);
		light.setInfluencingBounds(bounds);
		
		// Add the lights to the BranchGroup
		branch.addChild(ambientLight);
		branch.addChild(light);
	}

}