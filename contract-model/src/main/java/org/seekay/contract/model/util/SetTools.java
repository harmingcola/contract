package org.seekay.contract.model.util;

import java.util.*;


public class SetTools {

    private SetTools() {
        throw new IllegalStateException("Utility classes should never be constructed");
    }

    public static <T> Set<T> intersectingElements(Set<T>... sets) {
        assert sets.length > 1;

        Set<T> result = new HashSet<T>();

        Set<T> head = head(sets);
        List<Set<T>> tail = tail(sets);

        for(T t: head) {
            if(containedInAll(t, tail)) {
                result.add(t);
            }
        }

        return result;
    }

    private static <T> boolean containedInAll(T t, List<Set<T>> sets) {
        boolean found = true;
        for(Set<T> set :  sets) {
            if(!set.contains(t)) {
                found = false;
            }
        }
        return found;
    }

    public static <T> T head(Set<T> set) {
        return set.iterator().next();
    }

    public static <T> Set<T> head(Set<T>... sets) {
        return Arrays.asList(sets).get(0);
    }

    public static <T> Set<T> tail(Set<T> set) {
        Set<T> result = new HashSet<T>();
        Iterator<T> iterator = set.iterator();
        iterator.next();
        while(iterator.hasNext()) {
            result.add(iterator.next());
        }

        return result;
    }

    private static <T> List<Set<T>> tail(Set<T>... sets) {
        if(sets.length < 2) {
            throw new IllegalStateException("Matching configured incorrectly");
        }
        List<Set<T>> lists =  Arrays.asList(sets);
        return lists.subList(1, lists.size());
    }

}
