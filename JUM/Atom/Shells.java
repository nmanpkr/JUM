package JUM.Atom;

import JUM.Particles.Electron;

import java.util.ArrayList;
import java.util.Iterator;

public class Shells {

    private ArrayList<ArrayList<ArrayList<Electron>>> shells;
    private ArrayList<Electron> electronsArray;
    private String notation;

    public Shells(ArrayList<Electron> electrons){

        shells = new ArrayList<>();
        this.electronsArray = electrons;

        notation = "";
        fillShells();
    }

    private int getCurrentShell(){
        return shells.size() - 1;
    }

    private static int subShellMax(int sub){
        return (4*sub) + 2;
    }

    private static int getSubShells(int shell){
        if(shell <= 0)
            shell = 1;

        return shell;
    }

    public ArrayList<ArrayList<ArrayList<Electron>>> getShells() {
        return shells;
    }

    private static String getNotation(int shell, int subshell, int electrons){
        char[] shellLetters = new char[]{'K','L','M','N','O','P','Q'};
        char[] subshellLetters = new char[]{'S','P','D','F','G'};
        String notation = "";

        if(shell < shellLetters.length)
            notation += shellLetters[shell];
        else
            notation += "U";

        if(subshell < subshellLetters.length)
            notation += "" + subshellLetters[subshell] + "" + electrons + " ";
        else
            notation += "*" + electrons + " ";

        return notation;
    }

    private void fillShells(){

        notation = "";
        shells.clear();

        int electrons  = electronsArray.size();
        int electron_counter = 0;
        int eCount = 0;
        int shell = 0;
        int subshell = 0;


        while(electrons > 0){

            while(shells.size() < shell + 1)
                shells.add(new ArrayList<>()); //Add Shell


            shells.get(shell).add(new ArrayList<>()); //Add subshell

            subshell = shells.get(shell).size() - 1; //Sub Shell Index starting at 0


            //Loop till the end
            while(subshell < getSubShells(shell + 1)){

                ArrayList<Electron> subshella = shells.get(shell).get(subshell); //Array to work on current subshell

                while(subshella.size() < subShellMax(subshell) && electrons > 0){
                    subshella.add(electronsArray.get(electron_counter));

                    if(subshella.size() <= (subShellMax(subshell) / 2 )){
                        electronsArray.get(electron_counter).setSpin(0.5); //Spin up
                    }else{
                        electronsArray.get(electron_counter).setSpin(-0.5); //Spin down
                    }

                    electron_counter++;
                    electrons--;
                }

                eCount = subshella.size();

                if(eCount == 0)
                    break;

                notation += getNotation(shell, subshell, eCount);

                //check for lower levels

                int stop = 0;

                int subcolumn = subshell;
                int subrow = shell + 1;


                while((subcolumn > stop) && (electrons > 0) && (subshell + 1) < getSubShells(shell + 1)){

                    while(shells.size() < subrow + 1)
                        shells.add(new ArrayList<>());

                    while(shells.get(subrow).size() < subcolumn) {
                        shells.get(subrow).add(new ArrayList<>());
                    }

                    ArrayList<Electron> subrowshella = shells.get(subrow).get(subcolumn - 1);

                    while(subrowshella.size() < subShellMax(subcolumn - 1) && electrons > 0){
                        subrowshella.add(electronsArray.get(electron_counter));

                        if(subrowshella.size() <= (subShellMax(subcolumn - 1) / 2 )){
                            electronsArray.get(electron_counter).setSpin(0.5); //Spin up
                        }else{
                            electronsArray.get(electron_counter).setSpin(-0.5); //Spin down
                        }

                        electron_counter++;
                        electrons--;
                    }

                    eCount = subrowshella.size();
                    notation += getNotation(subrow, subcolumn - 1, eCount);


                    subrow++;
                    subcolumn--;

                }


                subshell++;

                if(subshell < getSubShells(shell + 1))
                    shells.get(shell).add(new ArrayList<>());


            }

            shell++;
        }

        Iterator shellIt = shells.iterator();
        while(shellIt.hasNext()){
            ArrayList<Electron> sub = (ArrayList<Electron>)shellIt.next();
            Iterator subIt = sub.iterator();
            while(subIt.hasNext()){
                if(((ArrayList<Electron>)subIt.next()).isEmpty()){
                    subIt.remove();
                }
            }
        }

    }

    public void stabilize(){
        //loop through sub shells
        //see if we can get half/full or full
        for(ArrayList<ArrayList<Electron>> shell : shells){

            int shellIndex = shells.indexOf(shell);

            for(ArrayList<Electron> subshell : shell){

                int subIndex = shell.indexOf(subshell);
                if(subshell.size() < subShellMax(subIndex)){
                    //lets see about borrowing an electron ;)
                    //Borrow from the next s orbital

                    //But only if it will make the shells more stable!
                    if((subshell.size() + 1 == subShellMax(subIndex)) || (subshell.size() + 1 == (subShellMax(subIndex) / 2)))
                    //more stable ok borrow
                    //Safety check
                    if(shells.size() > (shellIndex + 1)) {
                        subshell.add(shells.get(shellIndex + 1).get(0).get(1));
                        shells.get(shellIndex + 1).get(0).remove(1);
                    }
                }
            }

        }

    }

    public void fill(Electron[] electrons){
        for(Electron e : electrons){
            this.electronsArray.add(e);
        }
        fillShells();
    }

    public void fill(Electron e){
        electronsArray.add(e);
        fillShells();
    }

    public void removeElectron(Electron e){
        electronsArray.remove(e);
        fillShells();
    }

    public void removeElectrons(Electron[] electrons){
        for(Electron e : electrons){
            electronsArray.remove(e);
        }
        fillShells();
    }


    public Electron[] getValence(){
        ArrayList<Electron> valence = new ArrayList<>();

        for(ArrayList<Electron> subshell : shells.get(getCurrentShell())){
            for(Electron e : subshell){
                valence.add(e);
            }
        }

        return valence.toArray(new Electron[valence.size()]);
    }

    public String getElectronConfigurationU(){
        String note = "";

        for(ArrayList<ArrayList<Electron>> shell : shells){

            int shellIndex = shells.indexOf(shell);

            for(ArrayList<Electron> subshell : shell){

                int subIndex = shell.indexOf(subshell);

                note += getNotation(shellIndex, subIndex, subshell.size());

            }

        }

        return note.trim();
    }

    public String getElectronConfiguration(){
        return notation.trim();
    }

    @Override
    public String toString(){
        return "JUM.Atom.Shells: " + shells.size() + " Notation: " + getElectronConfiguration();
    }

}
