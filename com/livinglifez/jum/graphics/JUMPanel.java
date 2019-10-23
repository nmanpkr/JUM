package com.livinglifez.jum.graphics;

import com.livinglifez.jum.JUM;
import com.livinglifez.jum.atom.*;
import com.livinglifez.jum.particles.Electron;
import com.livinglifez.jum.particles.Proton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * JUM Panel component used to draw atoms to screen and other important information.
 * In the future this will be renamed to something else as more panels are made.
 */

public class JUMPanel extends JPanel implements ActionListener, MouseListener {

    private final int PROTON_WIDTH = 64;
    private final int ELECTRON_WIDTH = 16;

    //Panel components
    private BufferedImage buffImage;
    private Graphics2D g2;

    private final int SELECTOR_STARTX = 200;
    private final int SELECTOR_STARTY = 100;
    private final int SELECTOR_COUNT = 10;
    private final int SELECTOR_MARGIN = 34;
    private int selectorIndex = 0;
    private int selectedAtom = 0;

    private boolean hasNext = false;
    private boolean hasBack = false;
    private Point backButton = new Point(0,0);
    private Point nextButton = new Point(0,0);

    private Timer timer;

    //Jum Object
    private JUM jum;

    /**
     * Instantiate the JUMPanel, width and height are arbitrary and are only used to
     * correctly declare our buffered image. One resize these settings will be overwritten.
     *
     * @param width
     * @param height
     *
     */

    public JUMPanel(int width, int height)
    {
        buffImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        timer = new Timer(100, this);
        timer.start();

        JButton stabilize = new JButton("Stabilize");
        add(stabilize);

        stabilize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jum.getAtoms().get(selectedAtom).stabilize();
            }
        });

        JButton destabilize = new JButton("De-Stabilize");
        add(destabilize);

        destabilize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jum.getAtoms().get(selectedAtom).calculateShells();
            }
        });

        this.addMouseListener(this);

    }

    /**
     * Called to correct buffered images dimensions on frame resize.
     */

    public void resize(){
        buffImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
    }

    /**
     * Sets the reference for our main JUM class.
     * We call back on this to get important information about the state.
     *
     * @param jum
     */

    public void setJUM(JUM jum){
        this.jum = jum;
    }


    /**
     * Clears the buffered image, readying it for painting.
     * @param g
     */

    private void clearImageBuff(Graphics2D g){
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(0, 0, buffImage.getWidth(), buffImage.getHeight());
        g.setComposite(AlphaComposite.SrcOver);
    }

    /**
     * Our paint override that does the actual drawing.
     * First we call super method then we grab the buffered image graphics.
     * Before actually drawing the buffered image is cleared using clearImageBuff(g2);
     *
     * @param g
     */

    @Override
    public void paintComponent(Graphics g){

        super.paintComponent(g);

        g2 = (Graphics2D)buffImage.getGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //Clear our current buffer
        clearImageBuff(g2);

        drawAtoms(g2);
        drawKey(g2);
        drawAtomSelector(g2);

        g.drawImage(buffImage, 0, 0, null);

    }

    /**
     * Returns a color to draw the electrons depending on which sub-shell they are in.
     *
     * @param subshell
     * @return
     */

    public Color subShellColor(int subshell){
        switch(subshell){

            case 0:
                return Color.GREEN;

            case 1:
                return Color.magenta;

            case 2:
                return Color.orange;

            case 3:
                return Color.red;

            default:
                return Color.GRAY;
        }
    }

    /**
     * Gives us a point [X,Y] to draw the current electron.
     * This divides a circle into equal pieces then chooses
     * the right spot based on the current index.
     *
     * @param r
     * @param h
     * @param k
     * @param index
     * @param count
     * @return
     */

    public Point getElectronPoint(int r, int h, int k, int index, int count){

        //Degrees to radians conversion
        double t = ((360 / count) * index ) * (Math.PI / 180);

        int x = (int)(h + (r * Math.cos(t)));
        int y = (int)(k + (r * Math.sin(t)));

        return new Point(x,y);
    }

    /**
     * Draws our atom selector if we have atoms present.
     * Handles drawing next and previous buttons, and the atoms in the selector.
     *
     * @param g
     *
     */
    private void drawAtomSelector(Graphics2D g){
        ArrayList<Atom> atoms = jum.getAtoms();

        if(atoms.size() == 0)
            return;

        if(selectorIndex < 0)
            selectorIndex = 0;

        //Get atom range (Index)
        int startAtom = selectorIndex * SELECTOR_COUNT;

        //Safety check
        if(atoms.size() < (startAtom + 1))
            selectorIndex = 0;

        //Get subset of atoms start -> next 10 if available otherwise start -> size
        List<Atom> drawing = atoms.subList(startAtom, (startAtom + (SELECTOR_COUNT)) < atoms.size() ? startAtom + (SELECTOR_COUNT) : atoms.size());

        //Used to get our x,y based on index
        int drawingIndex = 0;
        int drawX = 0;
        int drawY = 0;

        Color prev = g.getColor();

        for(Atom a : drawing){
            g.setColor(Color.BLUE);

            drawX = ((drawingIndex % (SELECTOR_COUNT / 2)) * SELECTOR_MARGIN) + SELECTOR_STARTX;
            drawY = ((drawingIndex / (SELECTOR_COUNT / 2)) * SELECTOR_MARGIN) + SELECTOR_STARTY;
            Shape s = new Ellipse2D.Double(drawX, drawY, 30, 30);
            g.fill(s);

            g.setColor(Color.WHITE);
            //Label com.livinglifez.atom.com.livinglifez.atom
            g.drawString(a.getName().substring(0, a.getName().length() > 1 ? 2 : 1), drawX + 8, drawY + 18);

            drawingIndex++;
        }

        //Need to draw selectors?

        int buttonXOffset = 0;

        if(selectorIndex > 0){

            g.setColor(Color.lightGray);

            //Back button
            g.fillRoundRect(SELECTOR_STARTX, drawY + SELECTOR_MARGIN + 8, 40, 24, 8, 8);
            g.setColor(Color.black);
            g.drawString("BACK", SELECTOR_STARTX + 4, drawY + SELECTOR_MARGIN + 24);
            backButton.setLocation(SELECTOR_STARTX, drawY + SELECTOR_MARGIN + 8);

            buttonXOffset = 48;
            hasBack = true;

        }else{
            hasBack = false;
        }

        if(atoms.size() > (startAtom + SELECTOR_COUNT)){

            g.setColor(Color.lightGray);

            //Next button
            g.fillRoundRect(SELECTOR_STARTX + buttonXOffset, drawY + SELECTOR_MARGIN + 8, 40, 24, 8, 8);
            g.setColor(Color.black);
            g.drawString("NEXT",SELECTOR_STARTX + buttonXOffset + 4, drawY + SELECTOR_MARGIN + 24);
            nextButton.setLocation(SELECTOR_STARTX + buttonXOffset, drawY + SELECTOR_MARGIN + 8);

            hasNext = true;
        }else{
            hasNext = false;
        }

        g.setColor(prev);

    }

    /**
     * Responsible for drawing the currently selected atom.
     * This will draw the nucleus electron shells, and electrons.
     * This will also draw the atoms information in the top left panel,
     * as well as draw the electron configuration.
     *
     * @param g
     */

    private void drawAtoms(Graphics2D g){


        if(jum.getAtoms().size() == 0)
            return;

        //Safety check
        if(selectedAtom >= jum.getAtoms().size()){
            selectedAtom = 0;
        }

            Atom atom = jum.getAtoms().get(selectedAtom); //Draw first atom available

            ArrayList<Proton> protons = atom.getProtons();
            int electrons = atom.getElectrons().size();
            Shells shells = atom.getShells();


            Color previous = g.getColor();
            g.setColor(Color.BLUE);



                //int nuclues_radius = 32;

                int centerX = (getWidth() - PROTON_WIDTH) / 2;
                int centerY = (getHeight() - PROTON_WIDTH) / 2;

                //I know
                if(atom.getProtons().size() > 0) {
                    Shape protonShape = new Ellipse2D.Double(centerX, centerY, PROTON_WIDTH, PROTON_WIDTH);
                    g.fill(protonShape);

                    g.setColor(Color.white);

                    String pTxt = "P: " + String.valueOf(atom.getProtons().size())
                            ;
                    int ptxtX = (g.getFontMetrics().stringWidth(pTxt) / 2) + centerX;
                    int ptxtY = centerY + (PROTON_WIDTH / 4) - (0) + 8;

                    g.drawString(pTxt, ptxtX, ptxtY);

                        String nTxt = "N: " + String.valueOf(atom.getNuetrons().size());
                        g.drawString(nTxt, ptxtX, ptxtY + 18);
                }

            int shellX = 0, shellY = 0;
            int shell_radius = 96;

            for(ArrayList<ArrayList<Electron>> shell : atom.getShells().getShells()){
                g.setColor(Color.white);

                shellX = (getWidth() - (shell_radius * 2)) / 2;
                shellY = (getHeight() - (shell_radius * 2)) / 2;

                Shape shellShape = new Ellipse2D.Double(shellX, shellY, shell_radius * 2, shell_radius * 2);
                g.draw(shellShape);

                int subshellIndex = 0;
                int electronIndex = 0;
                int shellsize = 0;

                for(ArrayList<Electron> subshell : shell){
                    shellsize += subshell.size();
                }

                //Draw electrons on shell
                for(ArrayList<Electron> subshell : shell){

                    g.setColor(subShellColor(subshellIndex));

                    for(Electron e : subshell){

                       Point ePoint = getElectronPoint(shell_radius, shellX + shell_radius, shellY + shell_radius, electronIndex, shellsize);

                       Shape electronShape = new Ellipse2D.Double(ePoint.getX() - (ELECTRON_WIDTH / 2), ePoint.getY() - (ELECTRON_WIDTH / 2), ELECTRON_WIDTH, ELECTRON_WIDTH);
                       g.fill(electronShape);

                       electronIndex++;
                    }

                    subshellIndex++;

                }

                shell_radius += 32;

            }

            String conf = "";

            if(atom.isStabilized())
                conf = "Electron Configuration(U): " + atom.getShells().getElectronConfigurationU();
            else
                conf = "Electron Configuration: " + atom.getShells().getElectronConfiguration();

            int stringWidth = g.getFontMetrics().stringWidth(conf);

            g.setColor(Color.cyan);
            g.drawString(conf, shellX + (((shell_radius*2) - (stringWidth)) / 2), shellY + (shell_radius * 2));

            g.setColor(Color.white);
            g.drawString(atom.getName(), 10, 20);
            g.drawString("Mass (AMU): " + String.valueOf(atom.getMass()),10,40);

            g.drawString("Protons: " + String.valueOf(atom.getProtons().size()), 10, 60);
            g.drawString("Neutrons: " + String.valueOf(atom.getNuetrons().size()), 10, 80);
            g.drawString("Electrons: " + String.valueOf(atom.getElectrons().size()), 10, 100);

            g.drawString("Charge: " + String.valueOf(atom.getCharge()), 10, 140);
            g.drawString("Spin(UP): " + String.valueOf(atom.getSpin(0)), 10, 160);
            g.drawString("Spin(DOWN): " + String.valueOf(atom.getSpin(1)), 10, 180);

            g.setColor(previous);



    }

    /**
     * Draws the legend for the sub-shells,
     * {s, p, d, f}
     * @param g
     */

    private void drawKey(Graphics2D g){

        int keySphereX = getWidth() - 200;
        int keySphereY = 50;

        Color prev = g.getColor();

        g.drawString("Subshell Key", keySphereX, keySphereY - 12);

        char subshells[] = new char[]{'s','p','d','f'};

        for(int i = 0; i < 4; i++){
            g.setColor(subShellColor(i));

            Shape keySphere = new Ellipse2D.Double(keySphereX, keySphereY, 24, 24);
            g.fill(keySphere);

            g.drawString(String.valueOf(subshells[i]), keySphereX + 32, keySphereY + 18);

            keySphereY += 28;
        }
        g.setColor(prev);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == timer){
            repaint();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    Rectangle mouse = new Rectangle(e.getX(), e.getY(), 2,2);

        if(hasBack){
            if(mouse.intersects(new Rectangle((int)backButton.getX(),(int)backButton.getY(), 40, 24))) {
                selectorIndex--;
            }
        }

        if(hasNext){
            if(mouse.intersects(new Rectangle((int)nextButton.getX(),(int)nextButton.getY(), 40, 24))) {
                selectorIndex++;
            }
        }

        //Check if selected atom
        if(jum.getAtoms().size() > 0){

            for(int i = 0; i < 10; i++) {
                int ix = ((i % (SELECTOR_COUNT / 2)) * SELECTOR_MARGIN) + SELECTOR_STARTX;
                int iy = ((i / (SELECTOR_COUNT / 2)) * SELECTOR_MARGIN) + SELECTOR_STARTY;

                if(mouse.intersects(new Rectangle(ix, iy, 30, 30))){
                    selectedAtom = (selectorIndex * SELECTOR_COUNT) + i;
                }
            }

        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}