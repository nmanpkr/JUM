package JUM;

import JUM.Atom.*;
import JUM.Graphics.GUI;
import JUM.Particles.Particle;

import java.util.ArrayList;

/**
 * RUM created by Noah Schweibinz
 * Params: int Dimensions, long particles
 */

public class JUM implements Runnable{

    private ArrayList<Atom> atoms;

    public JUM(){
        atoms = new ArrayList<>();
    }

    @Override
    public void run() {
        testPart();
        GUI gui = new GUI(this);
    }

    public ArrayList<Atom> getAtoms(){
        return atoms;
    }

    public void testPart(){
        ArrayList<Particle> parts = new ArrayList<>();

        //H - Hydrogen to AU - Gold
        String elemName[] = new String[]{"H", "He", "Li", "Be", "B", "C", "N", "O", "F", "Ne", "Na", "Mg", "Al", "Si", "P", "S", "Cl", "Ar", "K", "Ca", "Sc", "Ti", "V", "Cr", "Mn", "Fe", "Co", "Ni", "Cu", "Zn", "Ga", "Ge", "As", "Se", "Br", "Kr", "Rb", "Sr", "Y", "Zr", "Nb", "Mo", "Tc", "Ru", "Rh", "Pd", "Ag", "Cd", "In", "Sn", "Sb", "Te", "I", "Xe", "Cs", "Ba", "La", "Ce", "Pr", "Nd", "Pm", "Sm", "Eu", "Gd", "Tb", "Dy", "Ho", "Er", "Tm", "Yb", "Lu", "Hf", "Ta", "W", "Re", "Os", "Ir", "Pt", "Au", "Hg"};

        for(int i = 1; i < 80; i++) {
            String name = "NA";

            if(elemName.length > (i - 1)){
                name = elemName[i - 1];
            }

            atoms.add(AtomFactory.creatAtom(name, i, 0, i));
        }

    }

    public static void main(String args[]){

        JUM jum = new JUM();

        Thread jumThread = new Thread(jum);
        jumThread.start();

    }

}
