package util;

import java.util.HashMap;
import java.util.Map;

public class Mapper {
    public static Map<String, String> messageToMap(String s) {
        Map<String, String> map = new HashMap<>();
        if (s.length() < 6) return map;   // No valid map can be made with less than 6 chars.
        String[] pairs = s.substring(1, s.length() - 2).split(", ");   // Create pairs, example of pair: 'KEY: "value"'

        // Put pairs in map
        for (String pair : pairs) {
            String[] split = pair.split(": ");
            map.put(split[0], split[1].substring(1, split[1].length() - 1));
        }

        return map;
    }
}
