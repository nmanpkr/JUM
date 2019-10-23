package com.livinglifez.jum.atom;

import com.livinglifez.jum.particles.Electron;
import com.livinglifez.jum.particles.Neutron;
import com.livinglifez.jum.particles.Particle;
import com.livinglifez.jum.particles.Proton;

import java.util.ArrayList;

public class AtomFactory {

    public AtomFactory(){

    }

    /**
     * Used to create atoms in a easier fashion.
     * Given name and amount of each particle, will
     * fill a parts array for you and return an Atom.
     *
     * @param name
     * @param protons
     * @param neutrons
     * @param electrons
     * @return
     */
    public static Atom createAtom(String name, int protons, int neutrons, int electrons){
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
