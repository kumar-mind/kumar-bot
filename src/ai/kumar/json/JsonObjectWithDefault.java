
package ai.kumar.json;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This class extends JSONObjects with additional get-methods that accept default values. It catches all possible errors and exceptions,
 * thus always returning a valid output. It's intention is to provide a saver way to acquire values in security/stability sensitive environments
 */
public class JsonObjectWithDefault extends JSONObject {

    public JsonObjectWithDefault(){
        super();
    }

    public JsonObjectWithDefault(JSONObject src){
        this();
        if(src != null) putAll(src);
    }

    public boolean getBoolean(String key, boolean dftval){
        try{
            return getBoolean(key);
        }
        catch (Throwable e){
            return dftval;
        }
    }

    public double getDouble(String key, double dftval){
        try{
            return getDouble(key);
        }
        catch (Throwable e){
            return dftval;
        }
    }

    public int getInt(String key, int dftval){
        try{
            return getInt(key);
        }
        catch (Throwable e){
            return dftval;
        }
    }
    public JSONArray getJSONArray(String key, JSONArray dftval){
        try{
            return getJSONArray(key);
        }
        catch (Throwable e){
            return dftval;
        }
    }
    public JSONObject getJSONObject(String key, JSONObject dftval){
        try{
            return getJSONObject(key);
        }
        catch (Throwable e){
            return dftval;
        }
    }

    public JsonObjectWithDefault getJSONObjectWithDefault(String key, JsonObjectWithDefault dftval){
        try{
            return new JsonObjectWithDefault(getJSONObject(key));
        }
        catch (Throwable e){
            return dftval;
        }
    }
    public long getLong(String key, long dftval){
        try{
            return getLong(key);
        }
        catch (Throwable e){
            return dftval;
        }
    }
    public String getString(String key, String dftval){
        try{
            return getString(key);
        }
        catch (Throwable e){
            return dftval;
        }
    }
}
