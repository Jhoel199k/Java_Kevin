package app;


import javax.swing.*;
import java.awt.*;

public class JStatusBar extends JPanel {

    public JStatusBar() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(10, 23));

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JLabel(new AngledLinesWindowsCornerIcon()), BorderLayout.SOUTH);
        rightPanel.setOpaque(false);

        add(rightPanel, BorderLayout.EAST);
        setBackground(new Color(236, 233, 216));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int valueY = 0;
        g.setColor(new Color(156, 154, 140));
        g.drawLine(0, valueY, getWidth(), valueY);
        valueY++;
        g.setColor(new Color(196, 194, 183));
        g.drawLine(0, valueY, getWidth(), valueY);
        valueY++;
        g.setColor(new Color(218, 215, 201));
        g.drawLine(0, valueY, getWidth(), valueY);
        valueY++;
        g.setColor(new Color(233, 231, 217));
        g.drawLine(0, valueY, getWidth(), valueY);

        valueY = getHeight() - 3;
        g.setColor(new Color(233, 232, 218));
        g.drawLine(0, valueY, getWidth(), valueY);
        valueY++;
        g.setColor(new Color(233, 231, 216));
        g.drawLine(0, valueY, getWidth(), valueY);
        valueY = getHeight() - 1;
        g.setColor(new Color(221, 221, 220));
        g.drawLine(0, valueY, getWidth(), valueY);

    }

}
