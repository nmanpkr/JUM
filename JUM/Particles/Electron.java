package JUM.Particles;

public class Electron extends Particle {

    private double spin;

    public Electron(){
        super(0);
        spin = 0.5; //Half integer
    }

    public void setSpin(double spin){
        this.spin = spin;
    }

    public double getSpin(){
        return spin;
    }

}
