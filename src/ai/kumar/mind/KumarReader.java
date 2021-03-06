
package ai.kumar.mind;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class KumarReader {

    private final Map<String,String> synonyms; // a map from a synonym to a canonical expression
    private final Map<String,String> categories; // a map from an expression to an associated category name
    private final Set<String> filler; // a set of words that can be ignored completely
    
    public KumarReader() {
        this.synonyms = new ConcurrentHashMap<>();
        this.categories = new ConcurrentHashMap<>();
        this.filler = new HashSet<>();
    }
    
    public KumarReader learn(JSONObject json) {

        // initialize temporary json objects
        JSONObject syn = json.has("synonyms") ? json.getJSONObject("synonyms") : new JSONObject();
        JSONArray fill = json.has("filler") ? json.getJSONArray("filler") : new JSONArray();
        JSONObject cat = json.has("categories") ? json.getJSONObject("categories") : new JSONObject();
        
        // add synonyms
        for (String canonical: syn.keySet()) {
            JSONArray a = syn.getJSONArray(canonical);
            a.forEach(synonym -> synonyms.put(((String) synonym).toLowerCase(), canonical));
        }
        
        // add filler
        fill.forEach(word -> filler.add((String) word));
        
        // add categories
        for (String canonical: cat.keySet()) {
            JSONArray a = cat.getJSONArray(canonical);
            a.forEach(synonym -> categories.put(((String) synonym).toLowerCase(), canonical));
        }
        
        return this;
    }

    public static class Token {
        public final String original, canonical, categorized;
        public Token(String original, String canonical, String categorized) {
            this.original = original;
            this.canonical = canonical;
            this.categorized = categorized;
        }
        public String toString() {
            return "{" +
                    "\"original\"=\"" + original + "\"," +
                    "\"canonical\"=\"" + canonical + "\"," +
                    "\"categorized\"=\"" + categorized + "\"" +
                    "}";
        }
    }

    public Token tokenizeTerm(String term) {
        String original = term.toLowerCase();
        String s = this.synonyms.get(original);
        String canonical = s == null ? original : s;
        String c = this.categories.get(canonical);
        String categorized = c == null ? canonical : c;
        return new Token(original, canonical, categorized);
    }

    public List<Token> tokenizeSentence(String term) {
        List<Token> t = new ArrayList<>();
        term = term.replaceAll("\\?", " ?").replaceAll("\\!", " !").replaceAll("\\.", " .").replaceAll("\\,", " ,").replaceAll("\\;", " ;").replaceAll("\\:", " :").replaceAll("  ", " ");
        String[] u = term.split(" ");
        for (String v: u) {
            String original = v.toLowerCase();
            if (this.filler.contains(original)) continue;
            t.add(tokenizeTerm(original));
        }
        return t;
    }
}
