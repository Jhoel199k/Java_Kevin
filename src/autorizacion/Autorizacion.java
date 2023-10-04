package autorizacion;

import java.util.HashMap;
import java.util.List;

/**
 * CAuthItem representa un elemento de autorización.
 * Un elemento de autorización puede ser una operación, una tarea o un rol.
 * Forman una jerarquía de autorización. Los elementos de niveles superiores de la jerarquía
 * heredan los permisos representados por elementos en niveles inferiores.
 * Un usuario puede tener asignados uno o varios elementos de autorización (llamados {@link CAuthAssignment assignments}.
 * Sólo puede realizar una operación si se encuentra entre los elementos que tiene asignados.
 *
 * @property IAuthManager $authManager El gestor de autorizaciones.
 * @property integer $type El tipo de elemento de autorización. Puede ser 0 (operación), 1 (tarea) o 2 (rol).
 * @property string $name Nombre del elemento.
 * @property string $description Descripción del elemento.
 * @property string $bizRule La regla de negocio asociada a este elemento.
 * @property mixed $data Los datos adicionales asociados a este elemento.
 * @property array $children Todos los ítems hijos de este ítem.
 * @autor Código Lite

 */
public class Autorizacion {
    public final static int TYPE_OPERATION=0;
    public final static int TYPE_TASK=1;
    public final static int TYPE_ROLE=2;

	private IAuthManager auth;
	private int type;
	private String name;
	private String description;
	private String bizRule;
	private List<Autorizacion> data;

	/**
	 * Constructor.
	 * @param auth IAuthManager authorization manager
	 * @param name string authorization item name
	 * @param type integer authorization item type. This can be 0 (operation), 1 (task) or 2 (role).
	 * @param description string the description
	 * @param bizRule string the business rule associated with this item
	 * @param data mixed additional data for this item
	 */
	public Autorizacion(IAuthManager auth,String name,int type,String description,String bizRule,List<Autorizacion> data)
	{
		this.type=type;
		this.auth=auth;
		this.name=name;
		this.description=description;
		this.bizRule=bizRule;
		this.data=data;
	}
        
        public Autorizacion(IAuthManager auth,String name,int type,String description,String bizRule)
	{
                this(auth,name,type,description,bizRule,null);
	}
        
         public Autorizacion(IAuthManager auth,String name,int type,String description)
	{
                this(auth,name,type,description,"",null);		
	}
        
         public Autorizacion(IAuthManager auth,String name,int type)
	{
                this(auth,name,type,"","",null);
	}
        

	/**
	 * Checks to see if the specified item is within the hierarchy starting from this item.
	 * This method is expected to be internally used by the actual implementations
	 * of the {@link IAuthManager::checkAccess}.
	 * @param itemName string the name of the item to be checked
	 * @param params HashMap the parameters to be passed to business rule evaluation
	 * @return boolean whether the specified item is within the hierarchy starting from this item.
	 */
	public boolean checkAccess(String itemName,HashMap params)
	{
		//Yii::trace('Checking permission "'.$this->_name.'"','system.web.auth.CAuthItem');
		if(this.auth.executeBizRule(this.bizRule,params,this.data))
		{
			if(this.name.equals(itemName))
				return true;
                        List<Autorizacion> itemHijos = this.auth.getItemChildren(this.name);
			for(Autorizacion hijos : itemHijos)
                        {
                            if(hijos.checkAccess(itemName,params))
			        return true;
                        }			
		}
		return false;
	}

	/**
	 * @return IAuthManager the authorization manager
	 */
	public IAuthManager getAuthManager()
	{
		return this.auth;
	}

	/**
	 * @return integer the authorization item type. This could be 0 (operation), 1 (task) or 2 (role).
	 */
	public int getType()
	{
		return this.type;
	}

	/**
	 * @return string the item name
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * @param value string the item name
	 */
	public void setName(String value)
	{
		if(!this.name.equals(value))
		{
			String oldName=this.name;
			this.name=value;
			this.auth.saveAuthItem(this,oldName);
		}
	}

	/**
	 * @return string the item description
	 */
	public String getDescription()
	{
		return this.description;
	}

	/**
	 * @param value the string item description
	 */
	public void setDescription(String value)
	{
		if(!this.description.equals(value))
		{
			this.description=value;
			this.auth.saveAuthItem(this,null);
		}
	}

	/**
	 * @return string the business rule associated with this item
	 */
	public String getBizRule()
	{
		return this.bizRule;
	}

	/**
	 * @param value string the business rule associated with this item
	 */
	public void setBizRule(String value)
	{
		if(!this.bizRule.equals(value))
		{
			this.bizRule=value;
			this.auth.saveAuthItem(this,null);
		}
	}

	/**
	 * @return mixed the additional data associated with this item
	 */
	public List<Autorizacion> getData()
	{
		return this.data;
	}

	/**
	 * @param value List<Autorizacion> the additional data associated with this item
	 */
	public void setData(List<Autorizacion> value)
	{
		if(!this.data.equals(value))
		{
			this.data=value;
			this.auth.saveAuthItem(this,null);
		}
	}

	/**
	 * Adds a child item.
	 * @param name string the name of the child item
	 * @return boolean whether the item is added successfully
	 * throws CException if either parent or child doesn't exist or if a loop has been detected.
	 * @see IAuthManager::addItemChild
	 */
	public boolean addChild(String name)
	{
		return this.auth.addItemChild(this.name,name);
	}

	/**
	 * Removes a child item.
	 * Note, the child item is not deleted. Only the parent-child relationship is removed.
	 * @param name string the child item name
	 * @return boolean whether the removal is successful
	 * @see IAuthManager::removeItemChild
	 */
	public boolean removeChild(String name)
	{
		return this.auth.removeItemChild(this.name,name);
	}

	/**
	 * Returns a value indicating whether a child exists
	 * @param name string the child item name
	 * @return boolean whether the child exists
	 * @see IAuthManager::hasItemChild
	 */
	public boolean hasChild(String name)
	{
		return this.auth.hasItemChild(this.name,name);
	}

	/**
	 * Returns the children of this item.
	 * @return array all child items of this item.
	 * @see IAuthManager::getItemChildren
	 */
	public List<Autorizacion> getChildren()
	{
		return this.auth.getItemChildren(this.name);
	}

	/**
	 * Assigns this item to a user.
	 * @param userId Object the user ID (see {@link IWebUser::getId})
	 * @param bizRule string the business rule to be executed when {@link checkAccess} is called
	 * for this particular authorization item.
	 * @param data String additional data associated with this assignment
	 * @return CAuthAssignment the authorization assignment information.
	 * throws CException if the item has already been assigned to the user
	 * @see IAuthManager::assign
	 */
	public AutorizacionAsignado assign(Object userId,String bizRule,String data)
	{
		return this.auth.assign(this.name,userId,bizRule,data);
	}
        
        public AutorizacionAsignado assign(Object userId,String bizRule)
	{
		return this.auth.assign(this.name,userId,bizRule,null);
	}
        
        public AutorizacionAsignado assign(Object userId)
	{
		return this.auth.assign(this.name,userId,null,null);
	}

	/**
	 * Revokes an authorization assignment from a user.
	 * @param userId Object the user ID (see {@link IWebUser::getId})
	 * @return boolean whether removal is successful
	 * @see IAuthManager::revoke
	 */
	public boolean revoke(Object userId)
	{
		return this.auth.revoke(this.name,userId);
	}

	/**
	 * Returns a value indicating whether this item has been assigned to the user.
	 * @param userId Object the user ID (see {@link IWebUser::getId})
	 * @return boolean whether the item has been assigned to the user.
	 * @see IAuthManager::isAssigned
	 */
	public boolean isAssigned(Object userId)
	{
		return this.auth.isAssigned(this.name,userId);
	}

	/**
	 * Returns the item assignment information.
	 * @param userId Object the user ID (see {@link IWebUser::getId})
	 * @return CAuthAssignment the item assignment information. Null is returned if
	 * this item is not assigned to the user.
	 * @see IAuthManager::getAuthAssignment
	 */
	public AutorizacionAsignado getAssignment(Object userId)
	{
		return this.auth.getAuthAssignment(this.name,userId);
	}
}
