package modelbd;

import codigolite.EncriptadorPassword;
import autorizacion.Autorizacion;
import autorizacion.AutorizacionAsignado;
import autorizacion.DbAutorizacionAdministrador;
import core.DatoArchivo;
import core.JAbstractModelBD;
import java.io.FileInputStream;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Código Lite - https://codigolite.com**
 */
public class Usuario extends JAbstractModelBD implements Serializable, IModel {

    private static final long serialVersionUID = 1L;
    public static final String nt = "gv_usuario";
    public static final String COLUMNA_PK = "idu";
    public static final String COLUMNA_ACTIVO = "activo";
    public static final String[] tCargos = {"Seleccionar Cargo", "CAJA", "COMPRAS", "VENTAS", "SERVICIO TENICO",
        "ALMACEN", "MULTIPLE"};
    public static final String[] sexos = {"Seleccionar Sexo", "Hombre", "Mujer"};

    DbAutorizacionAdministrador auth = new DbAutorizacionAdministrador();
    private Integer idu;
    private String dni;
    private String codigo;
    private String login;
    private String nombre;
    private String apellidos;
    private FileInputStream foto;
    private String clave;
    private String salt;
    private long fechaNacimiento;
    private String fono;
    private String fono2;
    private String celular;
    private String nextel;
    private String email;
    private String sexo;
    private String tipoCargo;
    private String descipcion;
    private Integer activo = 1;
    private long fecRegistro;
    private long fecBaja;
    private DatoArchivo dat;

    private boolean activoBool;

    private String tipoCargoAlmacen;

    public static final String FULL_NOM_CAMPOS = "idu,dni,codigo,login,nombre,apellidos,"
            + "foto,clave,salt, fec_nac, fono, fono2, celular, nextel, email, sexo, tipo_cargo,"
            + "descripcion, activo, fec_registro, fec_baja";
    public static final String NO_FULL_NOM_CAMPOS = "dni,codigo,login,nombre,apellidos,"
            + "foto,clave,salt, fec_nac, fono, fono2, celular, nextel, email, sexo, tipo_cargo,"
            + "descripcion, activo, fec_registro, fec_baja";
    public static final String SIN_FOTO_CLAVE = "dni,codigo,login,nombre,apellidos,"
            + "fec_nac, fono, fono2, celular, nextel, email, sexo, tipo_cargo,"
            + "descripcion, activo, fec_registro, fec_baja";
    public static final String CON_FOTO = "dni,codigo,login,nombre,apellidos,"
            + "foto,fec_nac, fono, fono2, celular, nextel, email, sexo, tipo_cargo,"
            + "descripcion, activo, fec_registro, fec_baja";

    public static final String OBLIGATORIOS_NOM_CAMPOS = "dni,codigo,login,nombre,apellidos"
            + "clave,salt,fono,sexo,tipo_cargo,fec_registro";

    public Usuario(String dni, String codigo, String login, String nombre, String apellidos, String clave, String salt, String fono, String sexo, String tipoCargo, long fecRegistro) {
        this.dni = dni;
        this.codigo = codigo;
        this.login = login;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.clave = clave;
        this.salt = salt;
        this.fono = fono;
        this.sexo = sexo;
        this.tipoCargo = tipoCargo;
        this.fecRegistro = fecRegistro;
        initBasic();
    }

    public Usuario(Integer idu, String dni, String codigo, String login, String nombre, String apellidos, String clave, String salt, String fono, String sexo, String tipoCargo, long fecRegistro) {
        this.idu = idu;
        this.dni = dni;
        this.codigo = codigo;
        this.login = login;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.clave = clave;
        this.salt = salt;
        this.fono = fono;
        this.sexo = sexo;
        this.tipoCargo = tipoCargo;
        this.fecRegistro = fecRegistro;
        initBasic();
    }

    public Usuario(Integer idu, String dni, String codigo, String login, String nombre, String apellidos, FileInputStream foto, String clave, String salt, Integer fechaNacimiento, String fono, String fono2, String celular, String nextel, String email, String sexo, String tipoCargo, String descipcion, Integer activo, long fecRegistro, long fecBaja, boolean activoBool) {
        this.idu = idu;
        this.dni = dni;
        this.codigo = codigo;
        this.login = login;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.foto = foto;
        this.clave = clave;
        this.salt = salt;
        this.fechaNacimiento = fechaNacimiento;
        this.fono = fono;
        this.fono2 = fono2;
        this.celular = celular;
        this.nextel = nextel;
        this.email = email;
        this.sexo = sexo;
        this.tipoCargo = tipoCargo;
        this.descipcion = descipcion;
        this.activo = activo;
        this.fecRegistro = fecRegistro;
        this.fecBaja = fecBaja;
        this.activoBool = activoBool;
        initBasic();
    }

    public Usuario(String dni, String codigo, String login, String nombre, String apellidos, FileInputStream foto, String clave, String salt, Integer fechaNacimiento, String fono, String fono2, String celular, String nextel, String email, String sexo, String tipoCargo, String descipcion, Integer activo, long fecRegistro, long fecBaja, boolean activoBool) {
        this.dni = dni;
        this.codigo = codigo;
        this.login = login;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.foto = foto;
        this.clave = clave;
        this.salt = salt;
        this.fechaNacimiento = fechaNacimiento;
        this.fono = fono;
        this.fono2 = fono2;
        this.celular = celular;
        this.nextel = nextel;
        this.email = email;
        this.sexo = sexo;
        this.tipoCargo = tipoCargo;
        this.descipcion = descipcion;
        this.activo = activo;
        this.fecRegistro = fecRegistro;
        this.fecBaja = fecBaja;
        this.activoBool = activoBool;
        initBasic();
    }

    public Usuario() {
        initBasic();
    }

    public DbAutorizacionAdministrador getAuth() {
        return auth;
    }

    public boolean verificarAcceso(String itemName) {
        if (this.login == null) {
            throw new NullPointerException("login nulo. El login debe ser inicalizado");
        }

        return this.auth.checkAccess(itemName, this.login, null);
        //return false;
    }

    public DefaultMutableTreeNode getPermisos() {
        ArrayList<AutorizacionAsignado> roles = this.auth.getAuthAssignments(this.login);
        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("Permisos");
        for (AutorizacionAsignado au : roles) {
            Autorizacion authItem = this.auth.getAuthItem(au.getItemName());
            findChild(authItem, raiz);
        }
        if (raiz.getChildCount() == 0) {
            raiz = new DefaultMutableTreeNode(this.login + " sin permisos");
        }
        return raiz;
    }

    private void findChild(Autorizacion au, DefaultMutableTreeNode padre) {
        if (au.getType() == Autorizacion.TYPE_ROLE || au.getType() == Autorizacion.TYPE_TASK) {
            DefaultMutableTreeNode hijo = new DefaultMutableTreeNode(au.getName());
            padre.add(hijo);
            List<Autorizacion> children = au.getChildren();
            for (Autorizacion ch : children) {
                findChild(ch, hijo);
            }
        }

        if (au.getType() == Autorizacion.TYPE_OPERATION) {
            DefaultMutableTreeNode hijo = new DefaultMutableTreeNode(au.getName());
            padre.add(hijo);
        }

    }

    public long getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(long fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    private void initBasic() {
        this.setNombreTabla(nt);
        this.setCampoClavePrimaria("idu");
        this.setCampoExistencial("activo");
    }

    public Integer getActivo() {
        return activo;
    }

    public void setActivo(Integer activo) {
        this.activo = activo;
    }

    public boolean isActivoBool() {
        return activoBool;
    }

    public void setActivoBool(boolean activoBool) {
        setActivo(activoBool ? 1 : 0);
        this.activoBool = activoBool;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescipcion() {
        return descipcion;
    }

    public void setDescipcion(String descipcion) {
        this.descipcion = descipcion;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getFecBaja() {
        return fecBaja;
    }

    public void setFecBaja(long fecBaja) {
        this.fecBaja = fecBaja;
    }

    public long getFecRegistro() {
        return fecRegistro;
    }

    public void setFecRegistro(long fecRegistro) {
        this.fecRegistro = fecRegistro;
    }

    public String getFono() {
        return fono;
    }

    public void setFono(String fono) {
        this.fono = fono;
    }

    public String getFono2() {
        return fono2;
    }

    public void setFono2(String fono2) {
        this.fono2 = fono2;
    }

    public FileInputStream getFoto() {
        return foto;
    }

    public void setFoto(FileInputStream foto) {
        this.foto = foto;
    }

    public void setFotoDA(FileInputStream foto, int longitud) {
        dat = new DatoArchivo(foto, longitud);
        this.foto = foto;
    }

    public void setFotoDA(DatoArchivo dat) {
        this.dat = dat;
        this.foto = dat.getFis();
    }

    public DatoArchivo getFoto(String s) {
        return dat;
    }

    public Integer getIdu() {
        return idu;
    }

    public void setIdu(Integer idu) {
        this.idu = idu;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNextel() {
        return nextel;
    }

    public void setNextel(String nextel) {
        this.nextel = nextel;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getSexo(Object obj) {
        if (obj == null) {
            return sexo;
        } else {
            if (sexo.equalsIgnoreCase("h")) {
                return "Hombre";
            } else {
                return "Mujer";
            }
        }

    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
        if (sexo.length() > 1) {
            if (sexo.equalsIgnoreCase("hombre")) {
                this.sexo = "H";
            } else {
                this.sexo = "M";
            }
        }

    }

    public void encriptarPass() {
        StringBuilder sb = new StringBuilder(this.dni + this.nombre.length()
                + this.codigo);
        this.salt = sb.reverse().append(this.codigo).toString();
        EncriptadorPassword ep = new EncriptadorPassword(this.salt);
        this.clave = ep.encrypt(this.clave);
    }

    public String desencriptarPass() {
        EncriptadorPassword ep = new EncriptadorPassword(this.salt);
        String desEncrypted = ep.decrypt(this.clave);
        return desEncrypted;
    }

    public String getTipoCargo() {
        return tipoCargo;
    }

    public void setTipoCargo(String tipoCargo) {
        this.tipoCargo = tipoCargo;
    }

    public String getTipoCargoAlmacen() {
        return tipoCargoAlmacen;
    }

    public void setTipoCargoAlmacen(String tipoCargoAlmacen) {
        this.tipoCargoAlmacen = tipoCargoAlmacen;
    }

    @Override
    public String toString() {
        return this.login;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idu != null ? idu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.idu == null && other.idu != null) || (this.idu != null && !this.idu.equals(other.idu))) {
            return false;
        }
        return true;
    }

    @Override
    public JAbstractModelBD llenarObjeto(ResultSet rs) {
        Usuario user = null;
        try {
            user = new Usuario();
            user.setPrimaryKey(rs.getInt(1));
            user.setIdu(rs.getInt(1));
            user.setDni(rs.getString(2));
            user.setCodigo(rs.getString(3));
            user.setLogin(rs.getString(4));
            user.setNombre(rs.getString(5));
            user.setApellidos(rs.getString(6));
            user.setClave(rs.getString(8));
            user.setSalt(rs.getString(9));
            user.setFechaNacimiento(rs.getInt(10));
            user.setFono(rs.getString(11));
            user.setFono2(rs.getString(12));
            user.setCelular(rs.getString(13));
            user.setNextel(rs.getString(14));
            user.setEmail(rs.getString(15));
            user.setSexo(rs.getString(16));
            user.setTipoCargo(rs.getString(17));
            user.setDescipcion(rs.getString(18));
            user.setActivo(rs.getInt(19));
            user.setFecRegistro(rs.getLong(20));
            user.setFecBaja(rs.getLong(21));

        } catch (SQLException ex) {
        }
        return user;
    }

    @Override
    public Object[] llenarValores() {
        Object[] valores = {this.getDni(), this.getCodigo(),
            this.getLogin(),
            this.getNombre(),
            this.getApellidos(),
            this.getFechaNacimiento(),
            this.getFono(),
            this.getFono2(),
            this.getCelular(),
            this.getNextel(),
            this.getEmail(),
            this.getSexo(null),
            this.getTipoCargo(),
            this.getDescipcion(),
            this.getActivo(),
            this.getFecRegistro(),
            this.getFecBaja(), this.getPrimaryKey()};
        return valores;
    }

}
