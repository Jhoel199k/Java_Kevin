package util;

import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Component;
import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JViewport;
import javax.swing.text.JTextComponent;

public class ECampos {

    public final static int VERIFICAR = 1;
    public final static int LIMPIAR = 2;
    public final static int HABILITAR_DESABILITAR = 3;
    public final static int HABILITAR_TODO = 4;
    public final static int HABILITAR_POR_NOMBRE = 5;

    /**
     * metedo que buscas los botonoes que tiene contiene un contenedor y los
     * habilias o desabihilitas.
     *
     * @param cm contenedor (JPanel o JFrame)
     * @param habilitar boolean true para habilitar, false para lo contrario
     * @param excepcion componentes que seran ignorados: pasar null si no se
     * desea que ningun objeto sea ignorado
     */
    public static void buscarBotones(Component cm, boolean habilitar, Component[] excepcion) {
        if (cm instanceof JPanel) {
            habilitaBotones(((JPanel) cm).getComponents(), habilitar, excepcion);
        } else {
            habilitaBotones(new Component[]{cm}, habilitar, excepcion);
        }
    }

    private static void habilitaBotones(Component[] cmps, boolean habilitar, Component[] excepcion) {

        for (Component cmp : cmps) {
            if (cmp instanceof JPanel) {
                habilitaBotones(((JPanel) cmp).getComponents(), habilitar, excepcion);
                continue;
            }
            if (excepcion != null) {
                if (excepcion.length > 0) {
                    boolean iguales = false;
                    for (Component excepcion1 : excepcion) {
                        if (cmp.equals(excepcion1)) {
                            iguales = true;
                            break;
                        }
                    }
                    if (iguales) {
                        continue;
                    }
                }
            }
            if (cmp instanceof AbstractButton) {
                cmp.setEnabled(habilitar);
                if (cmp instanceof JToggleButton) {
                    JToggleButton tmp = (JToggleButton) cmp;
                    tmp.setSelected(false);
                }
            }
        }
    }

    public static void setEditableTexto(Component cm, boolean habilitar, Component[] excepcion, boolean limpiar, String valor) {
        if (cm instanceof JPanel) {
            habilitarTexto(((JPanel) cm).getComponents(), habilitar, excepcion, limpiar, valor);
        } else if (cm instanceof JScrollPane) {
            habilitarTexto(((JScrollPane) cm).getComponents(), habilitar, excepcion, limpiar, valor);
        } else if (cm instanceof JViewport) {
            habilitarTexto(((JViewport) cm).getComponents(), habilitar, excepcion, limpiar, valor);
        } else {
            habilitarTexto(new Component[]{cm}, habilitar, excepcion, limpiar, valor);
        }
    }

    private static void habilitarTexto(Component[] cmps, boolean habilitar, Component[] excepcion, boolean limpiar, String valor) {
        for (Component cmp : cmps) {
            if (cmp instanceof JDateChooser) {
                continue;
            }
            if (cmp instanceof JPanel || cmp instanceof JScrollPane || cmp instanceof JViewport) {
                setEditableTexto(cmp, habilitar, excepcion, limpiar, valor);
                continue;
            }
            if (excepcion != null) {
                if (excepcion.length > 0) {
                    boolean iguales = false;
                    for (Component excepcion1 : excepcion) {
                        if (cmp.equals(excepcion1)) {
                            iguales = true;
                            break;
                        }
                    }
                    if (iguales) {
                        continue;
                    }
                }
            }
            if (cmp instanceof JTextComponent) {
                JTextComponent tmp = (JTextComponent) cmp;
                tmp.setEditable(habilitar);
                if (limpiar) {
                    if (cmp instanceof JFormattedTextField) {
                        ((JFormattedTextField) cmp).setValue(0);
                        if (cmp.getName() != null) {
                            if (cmp.getName().equals("igv")) {
                                ((JFormattedTextField) cmp).setValue(18);
                            }
                        }
                    } else if (cmp instanceof JTextField) {
                        tmp.setText(valor);
                    }
                }
            }
        }
    }

    public static boolean esObligatorio(Component cm, boolean opcion) {
        if (cm instanceof JPanel) {
            return marcarCamposObligatorios(((JPanel) cm).getComponents(), opcion);
        } else if (cm instanceof JScrollPane) {
            return marcarCamposObligatorios(((JScrollPane) cm).getComponents(), opcion);
        } else if (cm instanceof JViewport) {
            return marcarCamposObligatorios(((JViewport) cm).getComponents(), opcion);
        } else {
            return marcarCamposObligatorios(new Component[]{cm}, opcion);
        }
    }

    private static boolean marcarCamposObligatorios(Component[] cm, boolean opcion) {
        boolean existen = false;
        for (Component cm1 : cm) {
            if (cm1 instanceof JDateChooser) {
                continue;
            }
            if (cm1 instanceof JPanel || cm1 instanceof JScrollPane || cm1 instanceof JViewport) {
                esObligatorio(cm1, opcion);
                continue;
            }
            if (cm1.getName() != null) {
                if (cm1 instanceof JTextComponent) {
                    if (((JTextComponent) cm1).getText().isEmpty()) {
                        if (opcion) {
                            cm1.setBackground(Color.red);
                            cm1.setForeground(Color.WHITE);
                        } else {
                            cm1.setBackground(Color.white);
                            cm1.setForeground(Color.BLACK);
                        }
                        existen = true;
                    } else if (!((JTextComponent) cm1).getText().isEmpty()) {
                        cm1.setBackground(Color.white);
                        cm1.setForeground(Color.BLACK);
                    }
                } else if (cm1 instanceof JComboBox) {
                    if (((JComboBox) cm1).getSelectedIndex() == -1) {
                        if (opcion) {
                            cm1.setBackground(Color.red);
                            cm1.setForeground(Color.WHITE);
                        } else {
                            cm1.setBackground(Color.white);
                            cm1.setForeground(Color.BLACK);
                        }
                        existen = true;
                    } else if (((JComboBox) cm1).getSelectedIndex() != -1) {
                        if (((JComboBox) cm1).getSelectedIndex() == 0) {
                            if (opcion) {
                                cm1.setBackground(Color.red);
                                cm1.setForeground(Color.WHITE);
                            } else {
                                cm1.setBackground(Color.white);
                                cm1.setForeground(Color.BLACK);
                            }
                            existen = true;
                        }
                    } else {
                        cm1.setBackground(Color.white);
                        cm1.setForeground(Color.BLACK);
                    }
                }
            }
        }

        return existen;
    }

}
