public class PID {

    private double Proportinal=0;
    private double Intergral=0;
    private double Derivative=0;
    private double F=0;

    private double maxIntergral=0;
    private double maxError=0;
    private double Error=0;

    private double maxOutput=0;
    private double minOutput=0;

    private double setpoint=0;

    private double lastActual=0;

    private boolean firstRun=true;
    private boolean reversed=false;

    private double outputRampRate=0;
    private double lastOutput=0;

    private double outputFilter=0;

    private double Range=0;


    /**
     * Create a PID class object.
     * See setP, setI, setD methods for more detailed parameters.
     * @param p Proportional gain. Large if large difference between setpoint and target.
     * @param i Integral gain.  Becomes large if setpoint cannot reach target quickly.
     * @param d Derivative gain. Responds quickly to large changes in error. Small values prevents P and I terms from causing overshoot.
     */
    public PID(double p, double i, double d){
        Proportinal=p; Intergral=i; Derivative=d;
        checkSigns();
    }





    public void setI(double i){
        if(Intergral!=0){
            Error=Error*Intergral/i;
        }
        if(maxIntergral!=0){
            maxError=maxIntergral/i;
        }
        Intergral=i;
        checkSigns();

    }




    /**
     * Configure the PID object.
     * See setP, setI, setD, setF methods for more detailed parameters.
     * @param p Proportional gain. Large if large difference between setpoint and target.
     * @param i Integral gain.  Becomes large if setpoint cannot reach target quickly.
     * @param d Derivative gain. Responds quickly to large changes in error. Small values prevents P and I terms from causing overshoot.
     * @param f Feed-forward gain. Open loop "best guess" for the output should be. Only useful if setpoint represents a rate.
     */
    public void setPID(double p, double i, double d,double f){
        Proportinal=p;Derivative=d;F=f;

        setI(i);
        checkSigns();
    }


    public void setMaxIOutput(double maximum){

        maxIntergral=maximum;
        if(Intergral!=0){
            maxError=maxIntergral/Intergral;
        }
    }


    public void setOutputLimits(double output){
        setOutputLimits(-output,output);
    }


    public void setOutputLimits(double minimum,double maximum){
        if(maximum<minimum)return;
        maxOutput=maximum;
        minOutput=minimum;


        if(maxIntergral==0 || maxIntergral>(maximum-minimum) ){
            setMaxIOutput(maximum-minimum);
        }
    }




    public void setSetpoint(double setpoint){
        this.setpoint=setpoint;
    }

    public double getOutput(double actual, double setpoint){
        double output;
        double Poutput;
        double Ioutput;
        double Doutput;
        double Foutput;

        this.setpoint=setpoint;


        if(Range!=0){
            setpoint=constrain(setpoint,actual-Range,actual+Range);
        }

        double error=setpoint-actual;


        Foutput=F*setpoint;

        Poutput=Proportinal*error;

        if(firstRun){
            lastActual=actual;
            lastOutput=Poutput+Foutput;
            firstRun=false;
        }


        Doutput= -Derivative*(actual-lastActual);
        lastActual=actual;


        Ioutput=Intergral*error;
        if(maxIntergral!=0){
            Ioutput=constrain(Ioutput,-maxIntergral,maxIntergral);
        }


        output=Foutput + Poutput + Ioutput + Doutput;


        if(minOutput!=maxOutput && !bounded(output, minOutput,maxOutput) ){
            Error=error;

        }
        else if(outputRampRate!=0 && !bounded(output, lastOutput-outputRampRate,lastOutput+outputRampRate) ){
            Error=error;
        }
        else if(maxIntergral!=0){
            Error=constrain(Error+error,-maxError,maxError);

        }
        else{
            Error+=error;
        }


        if(outputRampRate!=0){
            output=constrain(output, lastOutput-outputRampRate,lastOutput+outputRampRate);
        }
        if(minOutput!=maxOutput){
            output=constrain(output, minOutput,maxOutput);
        }
        if(outputFilter!=0){
            output=lastOutput*outputFilter+output*(1-outputFilter);
        }


        lastOutput=output;
        return output;
    }


    public void reset(){
        firstRun=true;
        Error=0;
    }





    private double constrain(double value, double min, double max){
        if(value > max){ return max;}
        if(value < min){ return min;}
        return value;
    }


    private boolean bounded(double value, double min, double max){

        return (min<value) && (value<max);
    }


    private void checkSigns(){
        if(reversed){  // all values should be below zero
            if(Proportinal>0) Proportinal*=-1;
            if(Intergral>0) Intergral*=-1;
            if(Derivative>0) Derivative*=-1;
            if(F>0) F*=-1;
        }
        else{  // all values should be above zero
            if(Proportinal<0) Proportinal*=-1;
            if(Intergral<0) Intergral*=-1;
            if(Derivative<0) Derivative*=-1;
            if(F<0) F*=-1;
        }
    }
}
