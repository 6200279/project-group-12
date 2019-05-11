import java.util.Random;

public class Wind {
    private double[] direction;
    private double windVelocity;
    private double timeSlice;
    private int dimension;
    private double altitude;
    private double storedAltitude;

    private double[] windVector;
    private double[] roverVector;

    public Wind(int dimension, double timeSlice){
        direction = new double[dimension];
        windVector = new double[dimension];
        roverVector = new double[dimension];

        windVelocity = 0;
        storedAltitude = 0;
        this.timeSlice = timeSlice;
        this.dimension = dimension;
    }

    public void generateConstantWind(){
        if(dimension == 2) {
            direction[0] = -0.9805;
            direction[1] = -0.1961;
        }
        windVelocity = 27.8;   //Wind speed 100 km/h
    }

    //generate random direction of wind per every 1000 m of altitude and random wind speed with every step
    public void generateWindRandomly(){
        Random rd = new Random();
        if(dimension == 2) {
            if (Math.abs(altitude - storedAltitude) > 1000) {
                if (rd.nextDouble() > 0.5) {
                    direction[0] = (0.25 + (0.75 - 0.25) * rd.nextDouble())+0.5;
                    System.out.println("wind direction x: " + direction[0]);
                    direction[1] = 1 - direction[0];
                } else {
                    direction[0] = -(0.25 + (0.75 - 0.25) * rd.nextDouble())-0.5;
                    System.out.println("wind direction x: " + direction[0]);
                    direction[1] = -1 - direction[1];
                }
                storedAltitude = altitude;
            }
        }
        windVelocity = (0.18 + (0.57 - 0.18) * rd.nextDouble())*100;
        System.out.println("wind speed: " + windVelocity);
    }

    public void setWindVector(){
        //generateConstantWind();   //uncomment for constant wind
        generateWindRandomly();
        for(int i = 0; i < dimension; i++) {
            windVector[i] = direction[i] * windVelocity * timeSlice;
        }
    }


    public double[] addWind(double oldHeight, double newHeight){
        altitude = oldHeight;
        double diff = oldHeight - newHeight;

        if(dimension < 3){
            roverVector[0] = 0;
            roverVector[1] = diff;
        }else if(dimension == 3){
            roverVector[2] = 0;
        }

        setWindVector();
        double[] vector = new double[dimension];
        for(int i = 0; i < dimension; i++){
            vector[i] = windVector[i] + roverVector[i];
            //System.out.println(vector[i]);
        }

        return vector;
    }
}
