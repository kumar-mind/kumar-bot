
package ai.kumar.server;

import org.json.JSONObject;

import ai.kumar.json.JsonObjectWithDefault;

import javax.annotation.Nonnull;

public class UserRole {

    private String name;
    private JSONObject json;
    private JSONObject permissions;
    private UserRole parent;
    private BaseUserRole baseUserRole;

    /**
     *
     * @param name          the key in the sourceObject
     * @param baseUserRole
     * @param parent        if a parent user role exists
     * @param sourceObject  JSONObject in some storage file. Write changes here if exist
     */
    public UserRole(@Nonnull String name, @Nonnull BaseUserRole baseUserRole, UserRole parent, JSONObject sourceObject){
        this.baseUserRole = baseUserRole;
        this.name = name;

        if(sourceObject != null) json = sourceObject;
        else json = new JSONObject();

        if(!json.has("display-name")) json.put("display-name", name);
        if(!json.has("permissions")) json.put("permissions", new JSONObject());

        permissions = json.getJSONObject("permissions");
        setParent(parent);
    }


    public BaseUserRole getBaseUserRole(){
        return this.baseUserRole;
    }

    public String getName(){
        return this.name;
    }

    public String getDisplayName() { return json.getString("display-name"); }

    public void setDisplayName(String name) { json.put("display-name", name); }

    public UserRole getParent(){
        return parent;
    }

    public void setParent(UserRole parent){
        if(parent == null){
            json.put("parent", baseUserRole.name());
        }
        else{
            json.put("parent", parent.getName());
        }
        this.parent = parent;
    }

    public JsonObjectWithDefault getPermissions(APIHandler servlet){
        JsonObjectWithDefault permissions;

        // get upstream permission from default values or from parent
        if(parent == null){
            permissions = new JsonObjectWithDefault(servlet.getDefaultPermissions(baseUserRole));
            if(permissions == null) permissions = new JsonObjectWithDefault();
        }
        else{
            permissions =  parent.getPermissions(servlet);
        }

        // override of permissions
        if(this.permissions.has(servlet.getClass().getCanonicalName())){
            permissions.putAll(this.permissions.getJSONObject(servlet.getClass().getCanonicalName()));
        }

        return permissions;
    }

    public JSONObject getPermissionOverrides(){
        return permissions;
    }

    public JSONObject getPermissionOverrides(String servletCanonicalName){
        if(permissions.has(servletCanonicalName)) return permissions.getJSONObject(servletCanonicalName);
        return null;
    }

    public JSONObject getPermissionOverrides(APIHandler servlet){
        return getPermissionOverrides(servlet.getClass().getCanonicalName());
    }

    public void setPermission(String servletCanonicalName, String key, JSONObject value){
        if(!permissions.has(servletCanonicalName)) permissions.put(servletCanonicalName, new JSONObject());

        permissions.getJSONObject(servletCanonicalName).put(key, value);
    }

    public void setPermission(APIHandler servlet, String key, JSONObject value){
        setPermission(servlet.getClass().getCanonicalName(), key, value);
    }
}
