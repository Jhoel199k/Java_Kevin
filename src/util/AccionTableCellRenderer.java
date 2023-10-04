package util;

import ventanas.paneles.PanelAccion;
import java.awt.Component;
import javax.swing.JInternalFrame;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class AccionTableCellRenderer implements TableCellRenderer {

    private PanelAccion test;

    public AccionTableCellRenderer(final JInternalFrame ifr) {
        test = new PanelAccion(ifr);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        test.setTabla(table);
        return test;
    }

}
