package com.livinglifez.jum.particles;

public class Particle {

    public static final double AMU_KG = 1.66054e-27;
    public static final double PROTON_MASS = 1.0072766;
    public static final double NEUTRON_MASS = 1.0086654;
    public static final double ELECTRON_MASS = 0.000548597;

    private int type;

    /**
     * Parent class used to create particles.
     * the basic atomic particles extend this class.
     * @param type
     */

    public Particle(int type){
        this.type = type;
    }

    public int getType(){
        return type;
    }

}
