public class Wind {
    private double[] direction;
    private double windVelocity;
    private double timeSlice;
    private int dimension;
    private double altitude;

    private double[] windVector;
    private double[] roverVector;

    public Wind(int dimension, double timeSlice){
        direction = new double[dimension];
        windVector = new double[dimension];
        roverVector = new double[dimension];

        windVelocity = 0;
        this.timeSlice = timeSlice;
        this.dimension = dimension;
    }

    public void generateWind(){
        //generate direction using unit vector
        if(dimension == 2) {    
            direction[0] = -1.0;
            direction[1] = 0.0;
        }
        windVelocity = 27.8;   //Wind speed in m/s
    }

    public void setWindVector(){
        generateWind();
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
