
package ai.kumar.server;

import java.util.TreeMap;

import org.json.JSONObject;

/**
 * Accounting asks: what has the user done. This class holds user activities.
 */
public class UserRequests extends JSONObject {

    private static final JSONObject EMPTY_MAP = new JSONObject(new TreeMap<String, String>());
    private static final long ONE_HOUR_MILLIS = 1000 * 60 * 60;

    private static long uc = 0;
    
    public UserRequests() {
        super();
    }
    
    /**
     * cleanup deletes all old entries and frees up the memory.
     * some outside process muss call this frequently
     * @return self
     */
    public UserRequests cleanup() {
        if (!this.has("requests")) return this;
        JSONObject requests = this.getJSONObject("requests");
        for (String path: requests.keySet()) {
            JSONObject events = requests.getJSONObject(path);
            // shrink that map and delete everything which is older than now minus one hour
            long pivotTime = System.currentTimeMillis() - ONE_HOUR_MILLIS;
            while (events.length() > 0 && Long.parseLong(events.keys().next()) < pivotTime) events.remove(events.keys().next());
            if (events.length() == 0) requests.remove(path);
        }
        return this;
    }
    
    public synchronized UserRequests addRequest(String path, String query) {
        if (!this.has("requests")) this.put("requests", new JSONObject());
        JSONObject requests = this.getJSONObject("requests");
        if (!requests.has(path)) requests.put(path, new TreeMap<String, String>());
        JSONObject events = requests.getJSONObject(path);
        events.put(Long.toString(System.currentTimeMillis() + ((uc ++) % 1000)), query); // the counter is used to distinguish very fast concurrent requests
        return this;
    }
    
    public synchronized JSONObject getRequests(String path) {
        if (!this.has("requests")) return EMPTY_MAP;
        JSONObject requests = this.getJSONObject("requests");
        if (!requests.has(path)) return EMPTY_MAP;
        JSONObject events = requests.getJSONObject(path);
        return events;
    }
    
    public static void main(String[] args) {
        UserRequests a = new UserRequests();
        a.addRequest("/api/test.json", "q=test");
        JSONObject r = a.getRequests("/api/test.json");
        System.out.println(r.toString());
    }
    
}
