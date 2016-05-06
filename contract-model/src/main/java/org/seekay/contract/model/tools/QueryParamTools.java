package org.seekay.contract.model.tools;

import java.util.HashMap;
import java.util.Map;

public class QueryParamTools {

  /**
   * Private constructor for utility class
   */
  private QueryParamTools() {
    throw new IllegalStateException("Utility classes should never be constructed");
  }

  public static Map<String, String> extractParameters(String path) {
    Map<String, String> result = new HashMap<String, String>();
    String[] pathChunks = path.split("\\?");
    if(pathChunks.length != 2) {
      return result;
    }
    String[] parameterChunks = pathChunks[1].split("&");
    for(String parameterChunk : parameterChunks) {
      String[] parameters = parameterChunk.split("=");
      if(parameters.length == 2) {
        result.put(parameters[0], parameters[1]);
      }
    }
    return result;
  }
}
