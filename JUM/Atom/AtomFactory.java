package JUM.Atom;

import JUM.Particles.Electron;
import JUM.Particles.Neutron;
import JUM.Particles.Particle;
import JUM.Particles.Proton;

import java.util.ArrayList;

public class AtomFactory {

    public AtomFactory(){

    }

    public static Atom creatAtom(String name, int protons, int neutrons, int electrons){
        ArrayList<Particle> parts = new ArrayList<>();

        for(int i = 0; i < protons; i++)
            parts.add(new Proton());

        for(int i = 0; i < neutrons; i++)
            parts.add(new Neutron());

        for(int i = 0; i < electrons; i++)
            parts.add(new Electron());

        return new Atom(name, parts.toArray(new Particle[parts.size()]));
    }

}
