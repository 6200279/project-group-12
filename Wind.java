import java.util.Random;

/**
 * The Wind class generates wind vectors randomly or
 * from a wind model, and calculates vector of a flying
 * object when it's influenced by wind
 *
 * @author  Mateusz Wiza
 * @version 1.2
 * @since   2019-05-08
 */

public class Wind {
    private boolean PRINT_VALUES = false;   //set TRUE to see the printouts
    
    private double[] direction;
    private double windVelocity;

    private double timeSlice;
    private int dimension;  //are we in 2 or 3 dimensions?

    private double altitude;
    private double storedAltitude;

    private double[] windVector;
    private double[] roverVector;

    /**
     * Instance method of the class
     * @param dimension If we're in 2 or 3 dimensions
     * @param timeSlice Timestep in seconds of the main program
     */
    public Wind(int dimension, double timeSlice){
        direction = new double[dimension];
        windVector = new double[dimension];
        roverVector = new double[dimension];

        windVelocity = 0;
        storedAltitude = 0;
        this.timeSlice = timeSlice;
        this.dimension = dimension;
    }

    /**
     * Generates always the same direction and speed of wind
     */
    public void generateConstantWind(){
        if(dimension == 2) {
            direction[0] = -1.0;    //constant wind from east due west
            direction[1] = 0.0;
        }
        windVelocity = 27.8;   //Wind speed 100 km/h
    }

    /**
     * Generates random direction of wind per every 1000 m of altitude
     * and random wind speed with every step
     */
    public void generateWindRandomly(){
        Random rd = new Random();
        if(dimension == 2) {
            if (Math.abs(altitude - storedAltitude) > 1000) {
                if (rd.nextDouble() > 0.5) {
                    direction[0] = (0.25 + (0.75 - 0.25) * rd.nextDouble())+0.5;
                    if(PRINT_VALUES) System.out.println("wind direction x: " + direction[0]);
                    direction[1] = 1 - direction[0];
                } else {
                    direction[0] = -(0.25 + (0.75 - 0.25) * rd.nextDouble())-0.5;
                    if(PRINT_VALUES) System.out.println("wind direction x: " + direction[0]);
                    direction[1] = -1 - direction[1];
                }
                storedAltitude = altitude;
            }
        }
        windVelocity = (0.18 + (0.57 - 0.18) * rd.nextDouble())*100;
        if(PRINT_VALUES) System.out.println("wind speed: " + windVelocity);
    }

    /**
     * Generates wind vector by combining wind speed and direction vector
     * from generateConstantWind() or generateWindRandomly() methods
     */
    public void setWindVector(){
        //generateConstantWind();   //uncomment for constant wind
        generateWindRandomly();
        for(int i = 0; i < dimension; i++) {
            windVector[i] = direction[i] * windVelocity * timeSlice;
        }
    }


    /**
     * Creates a vector of a flying object based on changing altitude,
     * then calls setWindVector() to generate wind and create wind vector,
     * finally performs addition of wind and object vectors
     * 
     * @param oldHeight altitude of the object at the previous timestep
     * @param newHeight altitude of the object at the current timestep
     * @return array of doubles, being a 2 or 3 dimensional vector of 
     * the flying object, after it's influenced by wind
     */
    public double[] addWind(double oldHeight, double newHeight){
        altitude = oldHeight;
        double diff = oldHeight - newHeight;

        //set rover vector
        if(dimension < 3){
            roverVector[0] = 0;
            roverVector[1] = diff;
        }else if(dimension == 3){
            roverVector[2] = 0;
        }

        //generate wind and create wind vector
        setWindVector();

        //add rover and wind vectors together
        double[] vector = new double[dimension];
        for(int i = 0; i < dimension; i++){
            vector[i] = windVector[i] + roverVector[i];
            if(PRINT_VALUES) System.out.println(vector[i]);
        }

        return vector;
    }
}
