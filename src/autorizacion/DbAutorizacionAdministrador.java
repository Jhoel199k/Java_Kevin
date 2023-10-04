package autorizacion;

import core.CriterioSQL;
import managerbd.BaseConexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DbAutorizacionAdministrador extends AutorizacionAdministrador {

    public String connectionID = "db";
    /**
     * @var string the name of the table storing authorization items. Defaults
     * to 'AuthItem'.
     */
    public String itemTable = "gv_authitem";
    /**
     * @var string the name of the table storing authorization item hierarchy.
     * Defaults to 'AuthItemChild'.
     */
    public String itemChildTable = "gv_authitemchild";
    /**
     * @var string the name of the table storing authorization item assignments.
     * Defaults to 'AuthAssignment'.
     */
    public String assignmentTable = "gv_asignarauth";
    /**
     * @var CDbConnection the database connection. By default, this is
     * initialized automatically as the application component whose ID is
     * indicated as {@link connectionID}.
     */
    public BaseConexion db;

    private boolean usingSqlite;

    public static HashMap getPermisosPorDefecto() {
        HashMap roles = new HashMap();
        ArrayList<String> operacionesVentas = new ArrayList();
        operacionesVentas.add("registrarVenta");
        operacionesVentas.add("imprimirVenta");
        operacionesVentas.add("buscarProducto");
        operacionesVentas.add("verStock");
        operacionesVentas.add("registrarCliente");
        operacionesVentas.add("actualizarCliente");
        operacionesVentas.add("eliminarCliente");
        operacionesVentas.add("buscarCliente");

        roles.put("vendedor", operacionesVentas);

        ArrayList<String> operacionesAlmacenero = new ArrayList();
        operacionesAlmacenero.add("buscarProducto");
        operacionesAlmacenero.add("moverProducto");
        operacionesAlmacenero.add("actualizarStock");
        operacionesAlmacenero.add("registraProducto");
        operacionesAlmacenero.add("actualizarProducto");
        operacionesAlmacenero.add("eliminarProducto");
        operacionesAlmacenero.add("imprimirStock");
        roles.put("almacenero", operacionesAlmacenero);

        ArrayList<String> permisosHeredados = new ArrayList();
        permisosHeredados.add("vendedor");//hereda permisoos de vendedor
        permisosHeredados.add("almacenero");//hereda permisoos de almacenero
        permisosHeredados.add("registrarUsuario");
        permisosHeredados.add("eliminarUsuario");
        permisosHeredados.add("actualizarUsuario");
        permisosHeredados.add("buscarUsuario");
        roles.put("administrador", permisosHeredados);
        return roles;
    }

    @Override
    public boolean checkAccess(String itemName, Object userId, HashMap params) {
        List<AutorizacionAsignado> assignments = this.getAuthAssignments(userId);
        return this.checkAccessRecursive(itemName, userId, params, assignments);
    }
    protected boolean checkAccessRecursive(String itemName, Object userId, HashMap params, List<AutorizacionAsignado> assignments) {
        Autorizacion item = this.getAuthItem(itemName);
        if (item == null) {
            return false;
        }
        //Yii::trace('Checking permission "'.item.getName().'"','system.web.auth.CDbAuthManager');
        if (params == null) {
            params = new HashMap();
            params.put("userId", userId);
        } else {
            if (!params.containsKey("userId")) {
                params.put("userId", userId);
            }
        }
        if (this.executeBizRule(item.getBizRule(), params, item.getData())) {
            if (this.defaultRoles.contains(itemName)) {
                return true;
            }
            AutorizacionAsignado aa = null;
            for (AutorizacionAsignado tmp : assignments) {
                if (tmp.getItemName().equals(itemName)) {
                    aa = tmp;
                    break;
                }
            }

            if (aa != null) {
                if (assignments.contains(aa)) {
                    int index = assignments.indexOf(aa);
                    AutorizacionAsignado assignment = assignments.get(index);
                    if (this.executeBizRule(assignment.getBizRule(), params, assignment.getData())) {
                        return true;
                    }
                }
            }

            CriterioSQL criterio = new CriterioSQL(this.itemChildTable);
            criterio.addCondicion("hijo", "'" + itemName + "'", "=");
            List<String> parents = BaseConexion.getLista(criterio);
            for (String parent : parents) {
                if (this.checkAccessRecursive(parent, userId, params, assignments)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean addItemChild(String itemName, String childName) {
        if (itemName.equals(childName)) {
            System.out.println("error");
            return false;
        }

        CriterioSQL criterio = new CriterioSQL(this.itemTable);
        criterio.addCompareTexto("nombre", itemName, null, "=");
        criterio.addCompareTexto("nombre", childName, "or", "=");
        HashMap rows = BaseConexion.getItems(criterio);
        if (rows.size() == 2) {
            ArrayList reg = (ArrayList) rows.get(0);
            ArrayList reg1 = (ArrayList) rows.get(1);
            int parentType;
            int childType;
            if (reg.get(0).equals(itemName)) {
                //obtener el valor del campo tipo
                parentType = Integer.parseInt(reg.get(1).toString());
                childType = Integer.parseInt(reg1.get(1).toString());
            } else {
                parentType = Integer.parseInt(reg.get(1).toString());
                childType = Integer.parseInt(reg1.get(1).toString());
            }
            this.checkItemChildType(parentType, childType);
            if (this.detectLoop(itemName, childName)) {
                System.out.println("No se se puede agregar como hijo");
                return false;
            }
//				throw new CException(Yii::t('yii','Cannot add "{child}" as a child of "{name}". A loop has been detected.',
//					array('{child}'=>childName,'{name}'=>itemName)));
            Statement st = BaseConexion.getStatement();
            try {
                System.out.println();
                st.execute("insert into " + this.itemChildTable + "(padre, hijo) values('" + itemName + "','" + childName + "')");
//			this.db.createCommand()
//				.insert(this.itemChildTable, array(
//					'parent'=>itemName,
//					'child'=>childName,
//				));
            } catch (SQLException ex) {
                Logger.getLogger(DbAutorizacionAdministrador.class.getName()).log(Level.SEVERE, null, ex);
            }

            return true;
        } else {
            System.out.println("No existe padre ni hijo");
            return false;
        }

        //throw new CException(Yii::t('yii','Either "{parent}" or "{child}" does not exist.',array('{child}'=>childName,'{parent}'=>itemName)));
    }

    /**
     * Removes a child from its parent. Note, the child item is not deleted.
     * Only the parent-child relationship is removed.
     *
     * @param itemName string the parent item name
     * @param childName string the child item name
     * @return boolean whether the removal is successful
     */
    @Override
    public boolean removeItemChild(String itemName, String childName) {
        boolean delete = false;
        Statement st = BaseConexion.getStatement();
        try {
            delete = st.executeUpdate("delete from " + this.itemChildTable + " where padre ='" + itemName + "' and hijo='" + childName + "'") > 0;
        } catch (SQLException ex) {
            Logger.getLogger(DbAutorizacionAdministrador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return delete;
//		return this.db.createCommand()
//			.delete(this.itemChildTable, 'parent=:parent AND child=:child', array(
//				':parent'=>itemName,
//				':child'=>childName
//			)) > 0;
    }

    /**
     * Returns a value indicating whether a child exists within a parent.
     *
     * @param itemName string the parent item name
     * @param childName string the child item name
     * @return boolean whether the child exists
     */
    @Override
    public boolean hasItemChild(String itemName, String childName) {
        boolean encontrado = false;
        Statement st = BaseConexion.getStatement();
        try {
            ResultSet rs = st.executeQuery("select padre from " + this.itemChildTable + " where padre ='" + itemName + "' and hijo='" + childName + "'");
            while (rs.next()) {
                encontrado = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbAutorizacionAdministrador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return encontrado;

    }

    @Override
    public List<Autorizacion> getItemChildren(Object names) {
        String condition = "";
        if (names instanceof String) {
            condition = "padre='" + names + "'";
        } else if (names instanceof List) {
            List ns = (List) names;
            String unirNombres = "";
            for (Object name : ns) {
                if (ns.indexOf(name) == 0) {
                    unirNombres += "'" + name + "'";
                } else {
                    unirNombres += ",'" + name + "'";
                }
            }
            condition = "padre IN (" + unirNombres + ")";
//			foreach(names as &name)
//				name=this.db.quoteValue(name);
//			condition='parent IN ('.implode(', ',names).')';
        }

        String sql = "select nombre, tipo, descripcion, bizrule, dato from ";
        sql += this.itemTable + ", " + this.itemChildTable + " where " + condition;
        sql += " AND nombre=hijo";
        System.out.println(sql);
        ArrayList<HashMap> rows = BaseConexion.getItemChildrens(sql);

        List<Autorizacion> children = new ArrayList();
        Autorizacion auth;
        for (HashMap row : rows) {
            auth = new Autorizacion(this, row.get("nombre").toString(), (Integer) row.get("tipo"), String.valueOf(row.get("descripcion")), String.valueOf(row.get("bizrule")), null);
            children.add(auth);
        }

        return children;
    }

    @Override
    public AutorizacionAsignado assign(String itemName, Object userId, String bizRule, Object data) {
        if (this.usingSqlite() && this.getAuthItem(itemName) == null) {
            System.out.println("Error......");
            return null;
            //throw new CException(Yii::t('yii','The item "{name}" does not exist.',array('{name}'=>itemName)));
        }

        Statement st = BaseConexion.getStatement();
        try {
            st.execute("insert into " + this.assignmentTable + "(nombreitem,iduser,bizrule,dato) values('" + itemName + "','" + userId + "','" + bizRule + "','" + data + "')");
//			
        } catch (SQLException ex) {
            Logger.getLogger(DbAutorizacionAdministrador.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new AutorizacionAsignado(this, itemName, userId, bizRule, null);
    }

    public AutorizacionAsignado assign(String itemName, Object userId, String bizRule) {
        return assign(itemName, userId, bizRule, "");
    }

    public AutorizacionAsignado assign(String itemName, Object userId) {
        return assign(itemName, userId, "", "");
    }

    /**
     * Revokes an authorization assignment from a user.
     *
     * @param itemName string the item name
     * @param userId Objetc the user ID (see {@link IWebUser::getId})
     * @return boolean whether removal is successful
     */
    @Override
    public boolean revoke(String itemName, Object userId) {
        boolean revocado = false;

        Statement st = BaseConexion.getStatement();
        try {
            revocado = st.executeUpdate("delete from " + this.assignmentTable + " where nombreitem='" + itemName + "' and iduser='" + userId + "'") > 0;
//			
        } catch (SQLException ex) {
            Logger.getLogger(DbAutorizacionAdministrador.class.getName()).log(Level.SEVERE, null, ex);
        }
//		return this.db.createCommand()
//			.delete(this.assignmentTable, 'itemname=:itemname AND userid=:userid', array(
//				':itemname'=>itemName,
//				':userid'=>userId
//			)) > 0;
        return revocado;

    }

    /**
     * Returns a value indicating whether the item has been assigned to the
     * user.
     *
     * @param string itemName the item name
     * @param mixed userId the user ID (see {@link IWebUser::getId})
     * @return boolean whether the item has been assigned to the user.
     */
    @Override
    public boolean isAssigned(String itemName, Object userId) {
        boolean encontrado = false;
        Statement st = BaseConexion.getStatement();
        try {
            ResultSet rs = st.executeQuery("select nombreitem from " + this.assignmentTable + " where nombreitem ='" + itemName + "' and iduser='" + userId + "'");
            while (rs.next()) {
                encontrado = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbAutorizacionAdministrador.class.getName()).log(Level.SEVERE, null, ex);
        }
//		return this.db.createCommand()
//			.select('itemname')
//			.from(this.assignmentTable)
//			.where('itemname=:itemname AND userid=:userid', array(
//				':itemname'=>itemName,
//				':userid'=>userId))
//			.queryScalar() !== false;
        return encontrado;
    }

    /**
     * Returns the item assignment information.
     *
     * @param string itemName the item name
     * @param mixed userId the user ID (see {@link IWebUser::getId})
     * @return CAuthAssignment the item assignment information. Null is returned
     * if the item is not assigned to the user.
     */
    @Override
    public AutorizacionAsignado getAuthAssignment(String itemName, Object userId) {

        Statement st = BaseConexion.getStatement();
        HashMap row = new HashMap();
        try {
            ResultSet rs = st.executeQuery("select * from " + this.assignmentTable + " where nombreitem ='" + itemName + "' and iduser='" + userId + "'");
            while (rs.next()) {
                row.put("nombreitem", rs.getString(1));
                row.put("iduser", rs.getInt(2));
                row.put("bizrule", rs.getString(3));
                row.put("dato", rs.getString(4));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbAutorizacionAdministrador.class.getName()).log(Level.SEVERE, null, ex);
        }
//		row=this.db.createCommand()
//			.select()
//			.from(this.assignmentTable)
//			.where('itemname=:itemname AND userid=:userid', array(
//				':itemname'=>itemName,
//				':userid'=>userId))
//			.queryRow();
        if (!row.isEmpty()) {
            return new AutorizacionAsignado(this, row.get("nombreitem").toString(), row.get("iduser"), String.valueOf(row.get("bizrule")), null);
        } else {
            return null;
        }
//		if(row!==false)
//		{
//			if((data=@unserialize(row['data']))===false)
//				data=null;
//			return new CAuthAssignment(this,row['itemname'],row['userid'],row['bizrule'],data);
//		}
//		else
//			return null;
    }

    /**
     * Returns the item assignments for the specified user.
     *
     * @param userId Object the user ID (see {@link IWebUser::getId})
     * @return array the item assignment information for the user. An empty
     * array will be returned if there is no item assigned to the user.
     */
    @Override
    public ArrayList<AutorizacionAsignado> getAuthAssignments(Object userId) {
        Statement st = BaseConexion.getStatement();
        HashMap row;
        ArrayList<HashMap> rows = new ArrayList();
        try {
            ResultSet rs = st.executeQuery("select * from " + this.assignmentTable + " where iduser='" + userId + "'");
            while (rs.next()) {
                row = new HashMap();
                row.put("nombreitem", rs.getString(1));
                row.put("iduser", rs.getString(2));
                row.put("bizrule", rs.getString(3));
                row.put("dato", rs.getString(4));
                rows.add(row);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbAutorizacionAdministrador.class.getName()).log(Level.SEVERE, null, ex);
        }
//		rows=this.db.createCommand()
//			.select()
//			.from(this.assignmentTable)
//			.where('userid=:userid', array(':userid'=>userId))
//			.queryAll();

        ArrayList<AutorizacionAsignado> assignments = new ArrayList();
//		foreach(rows as row)
//		{
//			if((data=@unserialize(row['data']))===false)
//				data=null;
//			assignments[row['itemname']]=new CAuthAssignment(this,row['itemname'],row['userid'],row['bizrule'],data);
//		}
        for (HashMap row1 : rows) {
            AutorizacionAsignado aa = new AutorizacionAsignado(this, row1.get("nombreitem").toString(), row1.get("iduser"), String.valueOf(row1.get("bizrule")), null);
            assignments.add(aa);
        }

        return assignments;
    }

    /**
     * Saves the changes to an authorization assignment.
     *
     * @param assignment AutorizacionAsignado the assignment that has been
     * changed.
     */
    @Override
    public void saveAuthAssignment(AutorizacionAsignado assignment) {

        try {
            Statement st = BaseConexion.getStatement();
            st.executeUpdate("update " + this.assignmentTable + " set bizrule='" + assignment.getBizRule() + "', dato = '" + assignment.getData() + "' where nombreitem='" + assignment.getItemName() + "' and iduser='" + assignment.getUserId() + "'");

        } catch (SQLException ex) {
            Logger.getLogger(DbAutorizacionAdministrador.class.getName()).log(Level.SEVERE, null, ex);
        }
//            
//		this.db.createCommand()
//			.update(this.assignmentTable, array(
//				'bizrule'=>assignment.getBizRule(),
//				'data'=>serialize(assignment.getData()),
//			), 'itemname=:itemname AND userid=:userid', array(
//				'itemname'=>assignment.getItemName(),
//				'userid'=>assignment.getUserId()
//			));
    }

    /**
     * Returns the authorization items of the specific type and user.
     *
     * @param type integer the item type (0: operation, 1: task, 2: role).
     * Defaults to null, meaning returning all items regardless of their type.
     * @param userId Objetc the user ID. Defaults to null, meaning returning all
     * items even if they are not assigned to a user.
     * @return array the authorization items of the specific type.
     */
    @Override
    public ArrayList<Autorizacion> getAuthItems(Integer type, Object userId) {
        String command = "";
        if (type == null && userId == null) {
            command = "select * from " + this.itemTable;
//			command=this.db.createCommand()
//				.select()
//				.from(this.itemTable);
        } else if (userId == null) {
            command = "select * from " + this.itemTable + " where tipo=" + type;
//			command=this.db.createCommand()
//				.select()
//				.from(this.itemTable)
//				.where('type=:type', array(':type'=>type));
        } else if (type == null) {
            command = "select nombre,tipo,descripcion,t1.bizrule,t1.dato from " + this.itemTable + " as t1," + this.assignmentTable + " as t2  where nombre=nombreitem and iduser='" + userId + "'";
//			command=this.db.createCommand()
//				.select('name,type,description,t1.bizrule,t1.data')
//				.from(array(
//					this.itemTable.' t1',
//					this.assignmentTable.' t2'
//				))
//				.where('name=itemname AND userid=:userid', array(':userid'=>userId));
        } else {

            command = "select nombre,tipo,descripcion,t1.bizrule,t1.dato from " + this.itemTable + " as t1," + this.assignmentTable + " as t2  where nombre=nombreitem and tipo=" + type + " and iduser='" + userId + "'";

        }

        ArrayList<Autorizacion> items = new ArrayList();
        //items=array();

        Statement st = BaseConexion.getStatement();
        HashMap row;
        ArrayList<HashMap> rows = new ArrayList();
        try {
            ResultSet rs = st.executeQuery(command);
            while (rs.next()) {
                row = new HashMap();
                row.put("nombre", rs.getString(1));
                row.put("tipo", rs.getInt(2));
                row.put("descripcion", rs.getString(3));
                row.put("bizrule", rs.getString(4));
                row.put("dato", rs.getString(5));
                rows.add(row);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbAutorizacionAdministrador.class.getName()).log(Level.SEVERE, null, ex);
        }

//		foreach(command.queryAll() as row)
//		{
//			if((data=@unserialize(row['data']))===false)
//				data=null;
//			items[row['name']]=new CAuthItem(this,row['name'],row['type'],row['description'],row['bizrule'],data);
//		}
        Autorizacion nauth;
        for (HashMap row1 : rows) {
            nauth = new Autorizacion(this, row1.get("nombre").toString(), (Integer) row1.get("tipo"), String.valueOf(row1.get("descripcion")), String.valueOf(row1.get("bizrule")), null);
            items.add(nauth);
        }
        return items;
    }

    public ArrayList<Autorizacion> getAuthItems(int type) {
        return getAuthItems(type, null);
    }

    public ArrayList<Autorizacion> getAuthItems() {
        return getAuthItems(null, null);
    }

   
    @Override
    public Autorizacion createAuthItem(String name, int type, String description, String bizRule, Object data) {

        System.out.println("insert into " + this.itemTable + "(nombre,tipo,descripcion,bizrule,dato) values('" + name + "'," + type + ",'" + description + "','" + bizRule + "','" + data + "')");
        Statement st = BaseConexion.getStatement();
//
//		this.db.createCommand()
//			.insert(this.itemTable, array(
//				'name'=>name,
//				'type'=>type,
//				'description'=>description,
//				'bizrule'=>bizRule,
//				'data'=>serialize(data)
//			));
        return new Autorizacion(this, name, type, description, bizRule, null);
    }

    public Autorizacion createAuthItem(String name, int type, String description, String bizRule) {
        return createAuthItem(name, type, description, bizRule, null);
    }

    public Autorizacion createAuthItem(String name, int type, String description) {
        return createAuthItem(name, type, description, null, null);
    }

    public Autorizacion createAuthItem(String name, int type) {
        return createAuthItem(name, type, "", null, null);
    }

    /**
     * Removes the specified authorization item.
     *
     * @param name string the name of the item to be removed
     * @return boolean whether the item exists in the storage and has been
     * removed
     */
    @Override
    public boolean removeAuthItem(String name) {

        boolean revocado = false;

        Statement st = BaseConexion.getStatement();
        try {
            revocado = st.executeUpdate("delete from " + this.itemTable + " where nombre='" + name + "'") > 0;
//			
        } catch (SQLException ex) {
            Logger.getLogger(DbAutorizacionAdministrador.class.getName()).log(Level.SEVERE, null, ex);
        }
//		return this.db.createCommand()
//			.delete(this.itemTable, 'name=:name', array(
//				':name'=>name
//			)) > 0;
        return revocado;
    }

    /**
     * Returns the authorization item with the specified name.
     *
     * @param name string the name of the item
     * @return CAuthItem the authorization item. Null if the item cannot be
     * found.
     */
    @Override
    public Autorizacion getAuthItem(String name) {

        Statement st = BaseConexion.getStatement();
        HashMap row = new HashMap();
        try {
            ResultSet rs = st.executeQuery("select * from " + this.itemTable + " where nombre ='" + name + "'");
            while (rs.next()) {
                row.put("nombre", rs.getString(1));
                row.put("tipo", rs.getInt(2));
                row.put("descripcion", rs.getString(3));
                row.put("bizrule", rs.getString(4));
                row.put("dato", rs.getObject(5));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbAutorizacionAdministrador.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (!row.isEmpty()) {
            return new Autorizacion(this, row.get("nombre").toString(), (Integer) row.get("tipo"), String.valueOf(row.get("descripcion")), String.valueOf(row.get("bizrule")), null);
        } else {
            return null;
        }

    }

    /**
     * Saves an authorization item to persistent storage.
     *
     * @param item Autorizacion the item to be saved.
     * @param oldName string the old item name. If null, it means the item name
     * is not changed.
     */
    @Override
    public void saveAuthItem(Autorizacion item, String oldName) {

        try {
            Statement st = BaseConexion.getStatement();
            st.executeUpdate("update " + this.itemTable + " set nombre='" + item.getName() + "', tipo = " + item.getType() + ", descripcion = '"
                    + item.getDescription() + "', bizrule='" + item.getBizRule() + "', dato = '" + item.getData() + "'where nombre='" + (oldName == null ? item.getName() : oldName) + "'");

        } catch (SQLException ex) {
            Logger.getLogger(DbAutorizacionAdministrador.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void saveAuthItem(Autorizacion item) {
        saveAuthItem(item, null);
    }

    /**
     * Saves the authorization data to persistent storage.
     */
    @Override
    public void save() {
    }

    /**
     * Removes all authorization data.
     */
    @Override
    public void clearAll() {
        this.clearAuthAssignments();
//		this.db.createCommand().delete(this.itemChildTable);
//		this.db.createCommand().delete(this.itemTable);

        //boolean revocado = false;            
        Statement st = BaseConexion.getStatement();
        try {
            st.executeUpdate("delete from " + this.itemChildTable);
            st.executeUpdate("delete from " + this.itemTable);
//			
        } catch (SQLException ex) {
            Logger.getLogger(DbAutorizacionAdministrador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Removes all authorization assignments.
     */
    @Override
    public void clearAuthAssignments() {
//		this.db.createCommand().delete(this.assignmentTable);
        Statement st = BaseConexion.getStatement();
        try {
            st.executeUpdate("delete from " + this.assignmentTable);
//			
        } catch (SQLException ex) {
            Logger.getLogger(DbAutorizacionAdministrador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Checks whether there is a loop in the authorization item hierarchy.
     *
     * @param itemName string parent item name
     * @param childName string the name of the child item that is to be added to
     * the hierarchy
     * @return boolean whether a loop exists
     */
    protected boolean detectLoop(String itemName, String childName) {
        if (childName.equals(itemName)) {
            return true;
        }
        List<Autorizacion> itemChildren = this.getItemChildren(childName);
        for (Autorizacion child : itemChildren) {
            if (this.detectLoop(itemName, child.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return BaseConexion the DB connection instance CException if
     * {@link connectionID} does not point to a valid application component.
     */
    protected BaseConexion getDbConnection() {
        if (this.db != null) {
            return this.db;
        }
        return null;
    }

    /**
     * @return boolean whether the database is a SQLite database
     */
    protected boolean usingSqlite() {
        return this.usingSqlite;
    }

}
