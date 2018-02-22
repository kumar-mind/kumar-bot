
package ai.kumar.tools;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MapTools {
    
    /**
     * sort a given map by the value
     * @param map
     * @return a map with the same keys where the key with the highest value is first
     */
    public static <K, V extends Comparable<? super V>> LinkedHashMap<K, V> sortByValue(Map<K, V> map) {
        return map
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                      ));
    }

    public static <K> Map<K, Integer> deatomize(Map<K, AtomicInteger> map) {
        final Map<K, Integer> a = new HashMap<>();
        map.forEach((k, v) -> a.put(k, v.intValue()));
        return a;
    }

    public static <K> void incCounter(Map<K, AtomicInteger> map, K key) {
        incCounter(map, key, 1);
    }
    
    public static <K> void incCounter(Map<K, AtomicInteger> map, K key, int inc) {
        AtomicInteger c = map.get(key);
        if (c == null) {
            c = new AtomicInteger(0);
            map.put(key, c);
        }
        c.addAndGet(inc);
    }
}
