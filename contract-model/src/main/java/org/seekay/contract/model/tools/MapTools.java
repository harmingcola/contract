package org.seekay.contract.model.tools;

import java.util.Map;

public class MapTools {

  /**
   * Private constructor for utility class
   */
  private MapTools() {
    throw new IllegalStateException("Utility classes should never be constructed");
  }

  public static boolean isSubMap(Map<String, String> subMap, Map<String, String> superMap) {
    if(subMap == null || subMap.isEmpty()) {
      return true;
    }
    for(Map.Entry<String, String> subMapEntry : subMap.entrySet()) {
      if(!containsEntry(subMapEntry, superMap)) {
        return false;
      }
    }
    return true;
  }

  public static boolean containsEntry(Map.Entry<String, String> entry, Map<String, String> map) {
    if(map == null) {
      return false;
    }
    return map.containsKey(entry.getKey()) && map.get(entry.getKey()).equals(entry.getValue());
  }
}
