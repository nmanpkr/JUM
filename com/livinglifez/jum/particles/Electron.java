package com.livinglifez.jum.particles;

public class Electron extends Particle {

    private double spin;

    public Electron(){
        super(0);
        spin = 0.5; //Half integer
    }

    /**
     * Set electron spin. Half integer 0.5 for
     * spin up, and -0.5 for spin down.
     * @param spin
     */

    public void setSpin(double spin){
        this.spin = spin;
    }

    /**
     * Returns electrons current half integer spin.
     * @return
     */

    public double getSpin(){
        return spin;
    }

}
