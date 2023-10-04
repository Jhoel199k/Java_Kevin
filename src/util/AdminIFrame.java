package util;

import java.beans.PropertyVetoException;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;

public class AdminIFrame {

    public static void mostrarVentanaFactura(JDesktopPane dp, JInternalFrame vnt) {
        if (vnt != null && !vnt.isShowing()) {
            vnt.show();
            dp.remove(vnt);
            try {
                dp.add(vnt, JLayeredPane.DEFAULT_LAYER);
            } catch (IllegalArgumentException ex) {
                dp.add(vnt, JLayeredPane.DEFAULT_LAYER);
            }
        }

        if (vnt == null) {
            dp.add(vnt, JLayeredPane.DEFAULT_LAYER);
        }
        activarVentana(dp, vnt);
    }

    private static void activarVentana(JDesktopPane dp, JInternalFrame vnt) {
        try {
            vnt.setSelected(true);
        } catch (PropertyVetoException ex) {
        }
        dp.setPosition(vnt, 0);
    }

}
