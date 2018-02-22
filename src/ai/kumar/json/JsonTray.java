
package ai.kumar.json;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import ai.kumar.tools.CacheMap;

public class JsonTray {
    
    private JsonFile per;
    private CacheMap<String, JSONObject> vol;
    private File file_volatile;
    
    public JsonTray(File file_persistent, File file_volatile, int cachesize) throws IOException {
        this.per = new JsonFile(file_persistent);
        this.vol = new CacheMap<String, JSONObject>(cachesize);
        this.file_volatile = file_volatile;
        if (file_volatile != null && file_volatile.exists()) try {
            JSONObject j = JsonFile.readJson(file_volatile);
            for (String key: j.keySet()) this.vol.put(key, j.getJSONObject(key));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        JSONObject j = new JSONObject(true);
        for (Map.Entry<String, JSONObject> entry: this.vol.getMap().entrySet()) {
            j.put(entry.getKey(), entry.getValue());
        }
        if (this.file_volatile != null) try {
            JsonFile.writeJson(this.file_volatile, j);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public boolean has(String key) {
        synchronized(this.vol) {
            if (this.vol.exist(key)) return true;
        }
        return this.per.has(key);
    }
    
    public JsonTray put(String key, JSONObject value, boolean persistent) {
        if (persistent) putPersistent(key, value); else putVolatile(key, value);
        return this;
    }
    
    private JsonTray putPersistent(String key, JSONObject value) {
        this.per.put(key, value);
        return this;
    }
    
    private JsonTray putVolatile(String key, JSONObject value) {
        synchronized (this.vol) {
            this.vol.put(key, value);
        }
        return this;
    }
    
    public JsonTray remove(String key){
    	synchronized(this.vol) {
            if (this.vol.exist(key)){
            	this.vol.remove(key);
            	return this;
            }
        }
    	if(this.per.has(key)){
    		this.per.remove(key);
    	}
    	return this;
    }
    
    public JsonTray commit() {
        this.per.commit();
        return this;
    }
    
    public JSONObject getJSONObject(String key) {
        synchronized(this.vol) {
            JSONObject value = this.vol.get(key);
            if (value != null) return value;
        }
        return this.per.getJSONObject(key);
    }
    
    public JSONObject toJSON() {
    	JSONObject j = new JSONObject();
    	for (String key : this.per.keySet()){
    		j.put(key, this.per.get(key));
    	}
    	synchronized (this.vol) {
    		LinkedHashMap<String,JSONObject> map = this.vol.getMap();
	    	for(String key : map.keySet()){
	    		j.put(key, map.get(key));
	    	}
    	}
    	return j;
    }
    
    public Collection<String> keys() {
    	ArrayList<String> keys = new ArrayList<>();
    	keys.addAll(this.per.keySet());
    	synchronized (this.vol) {
    		keys.addAll(this.vol.getMap().keySet());
    	}
    	return keys;
    }
}
