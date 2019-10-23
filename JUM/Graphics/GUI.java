package JUM.Graphics;

import JUM.JUM;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class GUI extends JFrame {

    private JUM jum;
    private JUMPanel panel;

    public GUI(JUM jum){
        this.jum = jum;

        setTitle("JUM.JUM library atom example");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(640,640));

        panel = new JUMPanel(640,640);
        panel.setJUM(jum);
        panel.setDoubleBuffered(true);

        add(panel);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                panel.resize();
            }
        });

        setResizable(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        revalidate();
        pack();

        setVisible(true);

    }

}
