package ventanas;

import config.AppConfig;
import controllers.CAlmacen;
import controllers.CEncargadoAlmacen;
import controllers.CMoneda;
import java.awt.event.ActionEvent;
import modelbd.EncargadoAlmacen;
import modelbd.Usuario;
import util.text.FormatoDecimal;
import ventanas.paneles.EncargadoAlmacenPanel;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class DLogin extends javax.swing.JDialog {

    private final DefaultComboBoxModel mMoneda;
    private final DefaultComboBoxModel mAlmacen;

    public DLogin(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        CMoneda controllerMoneda = new CMoneda();
        mMoneda = new DefaultComboBoxModel(controllerMoneda.getRegistros().toArray());
        this.cmbMoneda.setModel(mMoneda);
        this.cmbMoneda.setSelectedItem(controllerMoneda.getPredeterminado());

        CAlmacen controllerAlmacen = new CAlmacen();
        mAlmacen = new DefaultComboBoxModel(controllerAlmacen.getRegistros(new Object[]{1}).toArray());
        this.cmbAlmacen.setModel(mAlmacen);
        this.bntAceptar.setVisible(false);
        this.bntCancelarConfig.setVisible(false);
        this.txtUsuario.requestFocus();
        this.setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panelImage1 = new elaprendiz.gui.panel.PanelImage();
        panelCurves1 = new elaprendiz.gui.panel.PanelCurves();
        pnlConfig = new elaprendiz.gui.panel.PanelTranslucido();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        clockDigital1 = new elaprendiz.gui.varios.ClockDigital();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        tftTipoCambio = new javax.swing.JFormattedTextField();
        dcFecha = new com.toedter.calendar.JDateChooser();
        cmbMoneda = new javax.swing.JComboBox();
        cmbAlmacen = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        bntAceptar = new elaprendiz.gui.button.ButtonRect();
        bntCancelarConfig = new elaprendiz.gui.button.ButtonRect();
        pnlLogin = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtUsuario = new elaprendiz.gui.textField.TextFieldRectIcon();
        ptxtPass = new elaprendiz.gui.passwordField.PasswordFieldRectIcon();
        bntCancelar = new javax.swing.JButton();
        bntIngresar = new javax.swing.JButton();
        panelImage2 = new elaprendiz.gui.panel.PanelImage();
        lblLogo = new elaprendiz.gui.label.LabelHeader();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Formulario de Ingreso");
        setLocationByPlatform(true);
        setModal(true);
        setResizable(false);

        panelImage1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/fondoazulceleste.jpg"))); // NOI18N
        panelImage1.setLayout(new java.awt.BorderLayout());

        pnlConfig.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Configuración Predeterminada", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 12), java.awt.Color.white)); // NOI18N
        pnlConfig.setColorPrimario(new java.awt.Color(0, 153, 255));
        pnlConfig.setLayout(new java.awt.GridBagLayout());

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 153, 255));
        jLabel3.setText("Tipo de Cambio:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(3, 8, 0, 0);
        pnlConfig.add(jLabel3, gridBagConstraints);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 153, 255));
        jLabel1.setText("Fecha:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 0, 0);
        pnlConfig.add(jLabel1, gridBagConstraints);

        clockDigital1.setForeground(new java.awt.Color(255, 255, 255));
        clockDigital1.setPreferredSize(new java.awt.Dimension(120, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 10);
        pnlConfig.add(clockDigital1, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 153, 255));
        jLabel2.setText("Hora:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(3, 8, 0, 0);
        pnlConfig.add(jLabel2, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 153, 255));
        jLabel4.setText("Almacen:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(3, 8, 0, 0);
        pnlConfig.add(jLabel4, gridBagConstraints);

        tftTipoCambio.setEditable(false);
        tftTipoCambio.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new FormatoDecimal("#,##0.00",true))));
        tftTipoCambio.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tftTipoCambio.setPreferredSize(new java.awt.Dimension(60, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 10);
        pnlConfig.add(tftTipoCambio, gridBagConstraints);

        dcFecha.setDate(Calendar.getInstance().getTime());
        dcFecha.setEnabled(false);
        dcFecha.setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 0, 10);
        pnlConfig.add(dcFecha, gridBagConstraints);

        cmbMoneda.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbMoneda.setEnabled(false);
        cmbMoneda.setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 8, 10);
        pnlConfig.add(cmbMoneda, gridBagConstraints);

        cmbAlmacen.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbAlmacen.setEnabled(false);
        cmbAlmacen.setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 10);
        pnlConfig.add(cmbAlmacen, gridBagConstraints);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 153, 255));
        jLabel5.setText("Moneda:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(3, 8, 8, 0);
        pnlConfig.add(jLabel5, gridBagConstraints);

        bntAceptar.setBackground(new java.awt.Color(51, 153, 255));
        bntAceptar.setText("Aceptar");
        bntAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntAceptarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        pnlConfig.add(bntAceptar, gridBagConstraints);

        bntCancelarConfig.setBackground(new java.awt.Color(51, 153, 255));
        bntCancelarConfig.setText("Continuar");
        bntCancelarConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntCancelarConfigActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        pnlConfig.add(bntCancelarConfig, gridBagConstraints);

        panelCurves1.add(pnlConfig, java.awt.BorderLayout.WEST);

        pnlLogin.setOpaque(false);
        pnlLogin.setLayout(new java.awt.GridBagLayout());

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Usuario:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 1, 0);
        pnlLogin.add(jLabel6, gridBagConstraints);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Contraseña:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 4, 0);
        pnlLogin.add(jLabel7, gridBagConstraints);

        txtUsuario.setText("codigolite");
        txtUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/user.png"))); // NOI18N
        txtUsuario.setPreferredSize(new java.awt.Dimension(150, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 1, 7);
        pnlLogin.add(txtUsuario, gridBagConstraints);

        ptxtPass.setText("123456");
        ptxtPass.setPreferredSize(new java.awt.Dimension(150, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 4, 7);
        pnlLogin.add(ptxtPass, gridBagConstraints);

        bntCancelar.setText("Cancelar");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        pnlLogin.add(bntCancelar, gridBagConstraints);

        bntIngresar.setText("Ingresar");
        bntIngresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntIngresarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        pnlLogin.add(bntIngresar, gridBagConstraints);

        panelCurves1.add(pnlLogin, java.awt.BorderLayout.CENTER);

        panelImage2.setLayout(new java.awt.BorderLayout());

        lblLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLogo.setText("SISTEMA DE ALMACÉN Y VENTAS");
        lblLogo.setColor(new java.awt.Color(51, 255, 255));
        lblLogo.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        panelImage2.add(lblLogo, java.awt.BorderLayout.CENTER);

        panelCurves1.add(panelImage2, java.awt.BorderLayout.PAGE_START);

        panelImage1.add(panelCurves1, java.awt.BorderLayout.CENTER);

        getContentPane().add(panelImage1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bntIngresarActionPerformed(ActionEvent evt) {//GEN-FIRST:event_bntIngresarActionPerformed
        if (!this.txtUsuario.getText().isEmpty() && !String.copyValueOf(this.ptxtPass.getPassword()).isEmpty()) {
            String msg = "";
            //login usuario
            AppConfig.Estado configUsuario = AppConfig.configUsuario(this.txtUsuario.getText(), String.copyValueOf(this.ptxtPass.getPassword()));
            if (null == configUsuario) {
                CEncargadoAlmacen controllerEa = new CEncargadoAlmacen();
                ArrayList<EncargadoAlmacen> rEa = controllerEa.getRegistrosPorEncargado(AppConfig.getUsuario().getPrimaryKey());
                if (!rEa.isEmpty()) {
                    this.cmbAlmacen.setSelectedItem(rEa.get(0).getAlmacen());
                    AppConfig.setAlmacen(rEa.get(0).getAlmacen());
                } else {
                    asignarAlmacen(AppConfig.getUsuario());
                }
                int op = JOptionPane.showConfirmDialog(null, "Acceso Concedido.\n¿Desea cambiar la configuración actual?",
                        "Bienvenido: " + this.txtUsuario.getText(), JOptionPane.YES_NO_OPTION);
                if (op == JOptionPane.OK_OPTION) {
                    this.pnlLogin.setVisible(false);
                    this.dcFecha.setEnabled(true);
                    this.tftTipoCambio.setEditable(true);
                    this.cmbAlmacen.setEnabled(true);
                    this.cmbMoneda.setEnabled(true);
                    lblLogo.setText("SistemaVenta");
                    this.setSize(pnlConfig.getWidth(), this.getHeight() + bntAceptar.getHeight());
                    this.setLocationRelativeTo(null);
                    this.bntAceptar.setVisible(true);
                    this.bntCancelarConfig.setVisible(true);
                } else {
                    initMainWindow();
                    this.dispose();
                }
                return;
            } else {
                switch (configUsuario) {
                    case NO_EXISTE:
                        msg = "El usuario: " + this.txtUsuario.getText() + " no existe.";
                        break;
                    case ERROR_CLAVE:
                        msg = "Contraseña Incorrecta!";
                        break;
                    default:
                        CEncargadoAlmacen controllerEa = new CEncargadoAlmacen();
                        ArrayList<EncargadoAlmacen> rEa = controllerEa.getRegistrosPorEncargado(AppConfig.getUsuario().getPrimaryKey());
                        if (!rEa.isEmpty()) {
                            this.cmbAlmacen.setSelectedItem(rEa.get(0).getAlmacen());
                            AppConfig.setAlmacen(rEa.get(0).getAlmacen());
                        } else {
                            asignarAlmacen(AppConfig.getUsuario());
                        }
                        int op = JOptionPane.showConfirmDialog(null, "Acceso Concedido.\n¿Desea cambiar la configuración actual?",
                                "Bienvenido: " + this.txtUsuario.getText(), JOptionPane.YES_NO_OPTION);
                        if (op == JOptionPane.OK_OPTION) {
                            this.pnlLogin.setVisible(false);
                            this.dcFecha.setEnabled(true);
                            this.tftTipoCambio.setEditable(true);
                            this.cmbAlmacen.setEnabled(true);
                            this.cmbMoneda.setEnabled(true);
                            lblLogo.setText("SistemaVenta");
                            this.setSize(pnlConfig.getWidth(), this.getHeight() + bntAceptar.getHeight());
                            this.setLocationRelativeTo(null);
                            this.bntAceptar.setVisible(true);
                            this.bntCancelarConfig.setVisible(true);
                        } else {
                            initMainWindow();
                            this.dispose();
                        }
                        return;
                }
            }
            JOptionPane.showMessageDialog(null, msg, "Error: no se pudo conectar.", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Ingrese un nombre de usuario y su contraseña", "Error: Datos vacios.", JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_bntIngresarActionPerformed

    private void bntAceptarActionPerformed(ActionEvent evt) {//GEN-FIRST:event_bntAceptarActionPerformed
        initMainWindow();
        this.dispose();
    }//GEN-LAST:event_bntAceptarActionPerformed

    private void bntCancelarConfigActionPerformed(ActionEvent evt) {//GEN-FIRST:event_bntCancelarConfigActionPerformed
        initMainWindow();
        this.dispose();
    }//GEN-LAST:event_bntCancelarConfigActionPerformed

    private void asignarAlmacen(Usuario user) {
        EncargadoAlmacenPanel pvc = new EncargadoAlmacenPanel(user);
        pvc.desabilitarCancelar();
        JLabel aviso = new JLabel();
        aviso.setVisible(false);

        JOptionPane.showOptionDialog(this, pvc, "Debe asignar un almacen a este usuario.", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, new Object[]{aviso}, null);

        if (pvc.EsAceptado()) {
            pvc.guardar();
        }
    }

    private void initMainWindow() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MainWindow mw = new MainWindow();
                mw.setVisible(true);
                mw.requestFocus();
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private elaprendiz.gui.button.ButtonRect bntAceptar;
    private javax.swing.JButton bntCancelar;
    private elaprendiz.gui.button.ButtonRect bntCancelarConfig;
    private javax.swing.JButton bntIngresar;
    private elaprendiz.gui.varios.ClockDigital clockDigital1;
    private javax.swing.JComboBox cmbAlmacen;
    private javax.swing.JComboBox cmbMoneda;
    private com.toedter.calendar.JDateChooser dcFecha;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private elaprendiz.gui.label.LabelHeader lblLogo;
    private elaprendiz.gui.panel.PanelCurves panelCurves1;
    private elaprendiz.gui.panel.PanelImage panelImage1;
    private elaprendiz.gui.panel.PanelImage panelImage2;
    private elaprendiz.gui.panel.PanelTranslucido pnlConfig;
    private javax.swing.JPanel pnlLogin;
    private elaprendiz.gui.passwordField.PasswordFieldRectIcon ptxtPass;
    private javax.swing.JFormattedTextField tftTipoCambio;
    private elaprendiz.gui.textField.TextFieldRectIcon txtUsuario;
    // End of variables declaration//GEN-END:variables
}
