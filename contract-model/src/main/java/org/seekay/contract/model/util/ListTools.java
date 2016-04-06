package org.seekay.contract.model.util;

import java.util.List;

public class ListTools {

    private ListTools() {
        throw new IllegalStateException("Utility classes should never be constructed");
    }

    public static <T> T first(List<T> list) {
        if(list.size() < 1) {
            return null;
        }
        return list.get(0);
    }
}
