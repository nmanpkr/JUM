package JUM.Atom;

import JUM.Particles.Electron;
import JUM.Particles.Neutron;
import JUM.Particles.Particle;
import JUM.Particles.Proton;

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
                case 0: //JUM.Particles.Electron
                    electrons.add((Electron)p);
                    break;
                case 1: //JUM.Particles.Proton
                    protons.add((Proton)p);
                    break;
                case 2: //JUM.Particles.Neutron
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

    public void calculateShells(){
        shells = new Shells(electrons);
        stabilized = false;
    }

    public void stabilize(){
        shells.stabilize();
        stabilized = true;
    }

    public boolean isStabilized(){
        return stabilized;
    }

    public String getNotationU(){
        return shells.getElectronConfigurationU();
    }

    private void calculateMass(){
        mass = (protons.size() * Particle.PROTON_MASS ) + (neutrons.size() * Particle.NEUTRON_MASS) + (electrons.size() * Particle.ELECTRON_MASS);
    }

    private void calculateCharge(){
        charge = (protons.size() - electrons.size());
    }

    /**
     * Returns the Atoms mass in AMU
     * @return
     */

    public String getName(){
        return name;
    }

    public double getMass(){
        calculateMass();
        return mass;
    }

    public int getCharge(){
        calculateCharge();
        return charge;
    }

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

    public ArrayList<Proton> getProtons(){
        return protons;
    }

    public ArrayList<Neutron> getNuetrons(){
        return neutrons;
    }

    public ArrayList<Electron> getElectrons(){
        return electrons;
    }

    public Shells getShells(){
        return shells;
    }

    /**
     * Prints out atom configuration
     * @return
     */
    @Override
    public String toString(){
        String out = "JUM.Atom.JUM.Atom: " + name + " Atomic Mass: " + mass + " Charge: " + charge + " " + shells;
        out += "\n Spins: Spin up: " + getSpin(0) + " Spin down: " + getSpin(1);
        return out;
    }

}
