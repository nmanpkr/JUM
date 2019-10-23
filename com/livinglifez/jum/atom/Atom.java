package com.livinglifez.jum.atom;

import com.livinglifez.jum.particles.Electron;
import com.livinglifez.jum.particles.Neutron;
import com.livinglifez.jum.particles.Particle;
import com.livinglifez.jum.particles.Proton;

import java.util.ArrayList;

public class Atom {

    private String name;

    private ArrayList<Proton> protons;
    private ArrayList<Neutron> neutrons;
    private ArrayList<Electron> electrons;

    private Shells shells;


    private double mass;
    private int charge;
    private int valance;

    private boolean stabilized;

    /**
     * Atom class which hold all of our particles, calculates
     * mass, and holds shells. Atomic interaction are done using
     * this class.
     *
     * @param name
     * @param parts
     */

    public Atom(String name, Particle[] parts){

        protons = new ArrayList<>();
        neutrons = new ArrayList<>();
        electrons = new ArrayList<>();

        this.name = name;
        mass = 0;
        charge = 0;
        valance = 0;
        stabilized = false;

        for(Particle p : parts){
            //Sort particles
            switch(p.getType()){
                case 0: //com.livinglifez.jum.particles.Electron
                    electrons.add((Electron)p);
                    break;
                case 1: //com.livinglifez.jum.particles.Proton
                    protons.add((Proton)p);
                    break;
                case 2: //com.livinglifez.jum.particles.Neutron
                    neutrons.add((Neutron)p);
                    break;
            }
        }
            //Set AM
            calculateMass();
            //Set Charge
            calculateCharge();
            //Setup shells
            calculateShells();

    }

    /**
     * Method calls upon Shells constructor(electrons) -> fill()
     * and sets atom to not stabilized.
     */

    public void calculateShells(){
        shells = new Shells(electrons);
        stabilized = false;
    }

    /**
     * Calls upon shells.stabilize to stabilize atoms electrons.
     * Also sets atom to stabilized.
     */

    public void stabilize(){
        shells.stabilize();
        stabilized = true;
    }

    /**
     * Returns atoms current stabilization state.
     * @return
     */

    public boolean isStabilized(){
        return stabilized;
    }

    /**
     * Returns shells.getElectronConfigurationU() which
     * will return electron configuration for atom from left to right.
     * @return
     */

    public String getNotationU(){
        return shells.getElectronConfigurationU();
    }

    /**
     * Calculates atoms mass in AMU.
     */

    private void calculateMass(){
        mass = (protons.size() * Particle.PROTON_MASS ) + (neutrons.size() * Particle.NEUTRON_MASS) + (electrons.size() * Particle.ELECTRON_MASS);
    }


    /**
     * Determines atoms charge. protons - electrons.
     */

    private void calculateCharge(){
        charge = (protons.size() - electrons.size());
    }

    /**
     * Return atoms given name.
     * @return
     */

    public String getName(){
        return name;
    }

    /**
     * calculates and returns atoms calculated mass.
     * @return
     */

    public double getMass(){
        calculateMass();
        return mass;
    }

    /**
     * Calculates and returns atoms charge.
     * @return
     */

    public int getCharge(){
        calculateCharge();
        return charge;
    }

    /**
     * Count number of electrons in atom spinning a given
     * direction. dir 0 == spin up, otherwise spin down.
     *
     * @param dir
     * @return
     */

    public int getSpin(int dir){

        int spinup = 0;
        int spindown = 0;

        for(Electron e : electrons){

            if(e.getSpin() > 0)
                spinup++;
            else
                spindown++;
        }

        if(dir == 0)
            return spinup;

        return spindown;
    }

    /**
     * Returns protons ArrayList.
     * @return
     */

    public ArrayList<Proton> getProtons(){
        return protons;
    }

    /**
     * Returns neutrons ArrayList.
     * @return
     */

    public ArrayList<Neutron> getNuetrons(){
        return neutrons;
    }

    /**
     * Returns electrons ArrayList.
     * @return
     */

    public ArrayList<Electron> getElectrons(){
        return electrons;
    }

    /**
     * Returns Shells object shells.
     * @return
     */

    public Shells getShells(){
        return shells;
    }

    /**
     * Prints out atom configuration
     * @return
     */
    @Override
    public String toString(){
        String out = "Atom: " + name + " Atomic Mass: " + mass + " Charge: " + charge + " " + shells;
        out += "\n Spins: Spin up: " + getSpin(0) + " Spin down: " + getSpin(1);
        return out;
    }

}
