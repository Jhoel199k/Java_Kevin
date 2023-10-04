package core;

import managerbd.BaseConexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class JAbstractModelBD {

    protected Integer primaryKey;
    private String campoExistencial;
    private String nombreTabla;
    private String CampoClavePrimaria;
    public Object[] valoresGrabar; // valores que se insertaran si es un registro nuevo
    public String camposTabla; // campos de la tabla de la base de datos

    public String getCampoClavePrimaria() {
        return CampoClavePrimaria;
    }

    public void setCampoClavePrimaria(String CampoClavePrimaria) {
        this.CampoClavePrimaria = CampoClavePrimaria;
    }

    public String getNombreTabla() {
        return nombreTabla;
    }

    public void setNombreTabla(String nombreTabla) {
        this.nombreTabla = nombreTabla;
    }

    public Integer getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Integer pk) {
        primaryKey = pk;
    }

    public String getCampoExistencial() {
        return campoExistencial;
    }

    public void setCampoExistencial(String campoExistencial) {
        this.campoExistencial = campoExistencial;
    }

    public String getCamposTabla() {
        return camposTabla;
    }

    public void setCamposTabla(String camposTabla) {
        this.camposTabla = camposTabla;
    }

    public void setCamposTabla(String[] camposTabla) {
        this.camposTabla = generarArrayAString(camposTabla);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.primaryKey != null ? this.primaryKey.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final JAbstractModelBD other = (JAbstractModelBD) obj;
        if (!Objects.equals(this.primaryKey, other.primaryKey) && (this.primaryKey == null || !this.primaryKey.equals(other.primaryKey))) {
            return false;
        }
        return true;
    }

    public Object encuentra(CriterioSQL criterio) {
        JAbstractModelBD sm = null;
        try {
            ResultSet rs = BaseConexion.getResultSet(criterio.getConsultaSQL());
            while (rs.next()) {
                sm = llenarObjeto(rs);
            }
            rs.close();
        } catch (SQLException ex) {
        }
        return sm;
    }

    public List<JAbstractModelBD> encuentra() {
        List<JAbstractModelBD> listaAP = new ArrayList();
        try {
            CriterioSQL criterio = new CriterioSQL(nombreTabla);
            try (ResultSet rs = BaseConexion.getResultSet(criterio.getConsultaSQL())) {
                JAbstractModelBD sm = null;
                while (rs.next()) {
                    sm = llenarObjeto(rs);
                    listaAP.add(sm);
                }
            }
        } catch (SQLException ex) {
        }
        return listaAP;
    }

    public List<JAbstractModelBD> encuentraTodos(CriterioSQL criterio) {
        List<JAbstractModelBD> listaAP = new ArrayList();
        try {
            ResultSet rs = BaseConexion.getResultSet(criterio.getConsultaSQL());
            JAbstractModelBD sm = null;
            while (rs.next()) {
                sm = llenarObjeto(rs);
                listaAP.add(sm);
            }
            rs.close();
        } catch (SQLException ex) {
        }
        return listaAP;
    }

    public List<JAbstractModelBD> encuentraTodos() {
        CriterioSQL criterio = new CriterioSQL(nombreTabla);
        return encuentraTodos(criterio);
    }

    public Object encuentraPorPk(Object pk, CriterioSQL criterio) {
        JAbstractModelBD sm = null;
        try {
            criterio.addCondicion(CampoClavePrimaria, (Number) pk);
            try (ResultSet rs = BaseConexion.getResultSet(criterio.getConsultaSQL())) {
                while (rs.next()) {
                    sm = llenarObjeto(rs);
                }
            }
        } catch (SQLException ex) {
        }
        return sm;
    }

    public Object encuentraPorPk(Object pk) {
        JAbstractModelBD sm = null;
        try {
            CriterioSQL criterio = new CriterioSQL(nombreTabla);
            criterio.addCondicion(CampoClavePrimaria, pk, "=");
            try (ResultSet rs = BaseConexion.getResultSet(criterio.getConsultaSQL())) {
                while (rs.next()) {
                    sm = llenarObjeto(rs);
                }
            }
        } catch (SQLException ex) {
        }
        return sm;

    }

    public List<JAbstractModelBD> encuentraTodosPorPk(Object pk, CriterioSQL criterio) {
        List<JAbstractModelBD> listaAP = new ArrayList();
        try {
            criterio.addCondicion(CampoClavePrimaria, pk, "=");
            try (ResultSet rs = BaseConexion.getResultSet(criterio.getConsultaSQL())) {
                JAbstractModelBD sm = null;
                while (rs.next()) {
                    sm = llenarObjeto(rs);
                    listaAP.add(sm);
                }
            }
        } catch (SQLException ex) {
        }
        return listaAP;
    }

    public List<JAbstractModelBD> encuentraTodosPorPk(Object pk) {

        List<JAbstractModelBD> listaAP = new ArrayList();
        try {
            CriterioSQL criterio = new CriterioSQL(nombreTabla);
            criterio.addCondicion(CampoClavePrimaria, pk, "=");
            try (ResultSet rs = BaseConexion.getResultSet(criterio.getConsultaSQL())) {
                JAbstractModelBD sm = null;
                while (rs.next()) {
                    sm = llenarObjeto(rs);
                    listaAP.add(sm);
                }
            }
        } catch (SQLException ex) {
        }
        return listaAP;
    }

    public int contar() {
        CriterioSQL criterio = new CriterioSQL();
        return contar(criterio);
    }

    public int contar(CriterioSQL criterio2) {
        CriterioSQL criterio = criterio2;
        criterio.setSelect(" count(*) as contar");
        criterio.setTabla(this.nombreTabla);
        int numRegistros = 0;
        try {
            ResultSet rs = BaseConexion.getResultSet(criterio.getConsultaSQL());
            while (rs.next()) {
                numRegistros = rs.getInt(1);
                break;
            }
            rs.close();
        } catch (SQLException ex) {
        }
        return numRegistros;
    }

    public boolean existe(CriterioSQL criterio) {
        boolean existe = false;
        try {
            ResultSet rs = BaseConexion.getResultSet(criterio.getConsultaSQL());
            while (rs.next()) {
                existe = true;
                break;
            }
            rs.close();
        } catch (SQLException ex) {
        }
        return existe;
    }

    public JAbstractModelBD getSiguiente(Object pk, CriterioSQL criterio) {
        JAbstractModelBD sm = null;
        try {
            criterio.setTabla(nombreTabla);
            criterio.addCondicion(CampoClavePrimaria, pk, " > ");
            criterio.setLimit(1);
            try (ResultSet rs = BaseConexion.getResultSet(criterio.getConsultaSQL())) {
                while (rs.next()) {
                    sm = llenarObjeto(rs);
                }
            }
        } catch (SQLException ex) {
        }
        return sm;
    }

    public JAbstractModelBD getSiguiente(CriterioSQL criterio) {
        return getSiguiente(this.getPrimaryKey(), criterio);
    }

    public JAbstractModelBD getSiguiente(Object pk) {
        CriterioSQL criterio = new CriterioSQL();
        return getSiguiente(pk, criterio);
    }

    public JAbstractModelBD getSiguiente() {
        CriterioSQL criterio = new CriterioSQL();
        return getSiguiente(criterio);
    }

    public JAbstractModelBD getAnterior(Object pk, CriterioSQL criterio) {
        JAbstractModelBD sm = null;
        try {
            criterio.setTabla(nombreTabla);
            criterio.addCondicion(CampoClavePrimaria, pk, " < ");
            criterio.setOrden(CampoClavePrimaria, "DESC");
            criterio.setLimit(1);
            try (ResultSet rs = BaseConexion.getResultSet(criterio.getConsultaSQL())) {
                while (rs.next()) {
                    sm = llenarObjeto(rs);
                }
            }
        } catch (SQLException ex) {
        }
        return sm;
    }

    public JAbstractModelBD getAnterior(Object pk) {
        CriterioSQL criterio = new CriterioSQL();
        return getAnterior(pk, criterio);
    }

    public JAbstractModelBD getAnterior(CriterioSQL criterio) {
        return getAnterior(this.getPrimaryKey(), criterio);
    }

    public JAbstractModelBD getAnterior() {
        CriterioSQL criterio = new CriterioSQL();
        return getAnterior(criterio);
    }

    public JAbstractModelBD getPrimero(Object pk, CriterioSQL criterio) {
        JAbstractModelBD sm = null;
        try {
            criterio.setTabla(nombreTabla);
            criterio.setLimit(1);
            criterio.setOrden(this.CampoClavePrimaria, "ASC");
            try (ResultSet rs = BaseConexion.getResultSet(criterio.getConsultaSQL())) {
                while (rs.next()) {
                    sm = llenarObjeto(rs);
                }
            }
        } catch (SQLException ex) {
        }
        return sm;
    }

    public JAbstractModelBD getPrimero(CriterioSQL criterio) {
        return getPrimero(this.getPrimaryKey(), criterio);

    }

    public JAbstractModelBD getPrimero(Object pk) {
        CriterioSQL criterio = new CriterioSQL();
        return getPrimero(pk, criterio);

    }

    public JAbstractModelBD getPrimero() {
        return getPrimero(this.getPrimaryKey());
    }

    public JAbstractModelBD getUltimo(Object pk, CriterioSQL criterio) {
        JAbstractModelBD sm = null;
        try {
            criterio.setTabla(nombreTabla);
            criterio.setLimit(1);
            criterio.setOrden(this.CampoClavePrimaria, "DESC");
            try (ResultSet rs = BaseConexion.getResultSet(criterio.getConsultaSQL())) {
                while (rs.next()) {
                    sm = llenarObjeto(rs);
                }
            }
        } catch (SQLException ex) {
        }
        return sm;

    }

    public JAbstractModelBD getUltimo(CriterioSQL criterio) {
        return getUltimo(this.getPrimaryKey(), criterio);

    }

    public JAbstractModelBD getUltimo(Object pk) {
        CriterioSQL criterio = new CriterioSQL();
        return getUltimo(pk, criterio);

    }

    public JAbstractModelBD getUltimo() {
        return getUltimo(this.getPrimaryKey());
    }

    public boolean grabar(String[] campos) {
        return grabar(generarArrayAString(campos));
    }

    public boolean grabar() {
        return grabar(this.camposTabla);
    }

    public boolean grabar(String campos) {
        String sql = "insert into " + nombreTabla + "(" + campos + ") values("
                + formatearValores(this.valoresGrabar.length) + ")";
        System.out.println(sql);
        int op = 0;
        try {
            PreparedStatement ps = BaseConexion.getPreparedStatement(sql);
            setValores(ps, this.valoresGrabar);
            op = ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
        }

        return op != 0;
    }

    //mejorar estos metodos 
    //implementar el metodo para pasar cadena a array y viceversa en CriteriaSQL
    public boolean actualizar(String[] campos) {
        return actualizar(generarArrayAString(campos));
    }

    public boolean actualizar() {
        return actualizar(this.camposTabla);
    }

    public boolean actualizar(String campos) {
        String sql = "insert into " + nombreTabla + "(" + campos + ") values("
                + formatearValores(this.valoresGrabar.length) + ")";
        System.out.println(sql);
        int op = 0;
        try {
            PreparedStatement ps = BaseConexion.getPreparedStatement(sql);
            setValores(ps, this.valoresGrabar);
            op = ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
        }

        return op != 0;
    }

    public String generarArrayAString(String[] campos) {
        String cad = "* ";
        if (campos != null) {
            if (campos.length > 0) {
                cad = "";
                if (campos.length == 1) {
                    return cad + campos[0];
                }
                for (int i = 0; i < campos.length; i++) {
                    cad += (i == 0 ? " " : ", ") + campos[i];
                }
            }
        }
        return cad;
    }

    /**
     * Este metodo genera una cadena con simbolos "?" separada por comas <br/>
     *
     * @param num cantidad de repeticiones
     *
     * @return
     */
    public String formatearValores(int num) {
        String cad = "";
        if (num > 0) {
            cad = "";
            for (int i = 0; i < num; i++) {
                cad += (i == 0 ? " " : ", ") + "?";
            }
        }
        return cad;
    }

    private void setValores(PreparedStatement ps, Object[] valores) {
        try {
            for (int i = 0; i < valores.length; i++) {
                if (getInt(valores[i]) != null) {
                    ps.setInt(i + 1, getInt(valores[i]));
                } else if (getDouble(valores[i]) != null) {
                    ps.setDouble(i + 1, getDouble(valores[i]));
                } else if (getFloat(valores[i]) != null) {
                    ps.setFloat(i + 1, getFloat(valores[i]));
                } else if (valores[i] instanceof DatoArchivo) {
                    DatoArchivo da = (DatoArchivo) valores[i];
                    ps.setBinaryStream(i + 1, da.getFis(), da.getLongitud());
                } else {
                    if (valores[i] != null) {
                        ps.setString(i + 1, valores[i].toString());
                    } else {
                        ps.setString(i + 1, null);
                    }

                }
            }
        } catch (SQLException ex) {
        }
    }

    private Integer getInt(Object valor) {
        Integer vl = null;
        if (valor instanceof Integer) {
            vl = (Integer) valor;
        }
        return vl;
    }

    private Double getDouble(Object valor) {
        Double vl = null;
        if (valor instanceof Double) {
            vl = (Double) valor;
        }
        return vl;
    }

    private Float getFloat(Object valor) {
        Float vl = null;
        if (valor instanceof Float) {
            vl = (Float) valor;
        }
        return vl;
    }

    public abstract Object[] llenarValores();

    public abstract JAbstractModelBD llenarObjeto(ResultSet rs);

}
