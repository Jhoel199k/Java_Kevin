package util.filechooser;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.border.BevelBorder;

/**
 *
 * @author Código Lite - https://codigolite.com
 */
public class PanelVistaPrevia extends JComponent implements PropertyChangeListener {

    ImageIcon thumbnail = null;

    public PanelVistaPrevia(JFileChooser fc) {
        setPreferredSize(new Dimension(240, 180));
        fc.addPropertyChangeListener(this);
        setBorder(new BevelBorder(BevelBorder.LOWERED));
    }

    public void loadImage(File f) {
        if (f == null) {
            thumbnail = null;
        } else {
            ImageIcon tmpIcon = new ImageIcon(f.getPath());
            if (tmpIcon.getIconWidth() > 240 || tmpIcon.getIconWidth() < 240) {
                thumbnail = new ImageIcon(
                        tmpIcon.getImage().getScaledInstance(240, 180, Image.SCALE_DEFAULT));
            } else {
                thumbnail = tmpIcon;
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();
        if (prop == null ? JFileChooser.SELECTED_FILE_CHANGED_PROPERTY == null : prop.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
            if (isShowing()) {
                loadImage((File) e.getNewValue());
                repaint();
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (thumbnail != null) {
            int x = getWidth() / 2 - thumbnail.getIconWidth() / 2;
            int y = getHeight() / 2 - thumbnail.getIconHeight() / 2;
            if (y < 0) {
                y = 0;
            }

            if (x < 5) {
                x = 5;
            }
            thumbnail.paintIcon(this, g, 0, 0);
        }
    }
}
