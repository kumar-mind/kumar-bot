
package ai.kumar.json;

import ai.kumar.json.JsonRandomAccessFile.JsonHandle;

public interface JsonReader extends Runnable {

    public final static JsonFactory POISON_JSON_MAP = new JsonHandle(null, -1, -1);
    
    public int getConcurrency();
    
    public JsonFactory take() throws InterruptedException;
    
    public String getName();
    
}
