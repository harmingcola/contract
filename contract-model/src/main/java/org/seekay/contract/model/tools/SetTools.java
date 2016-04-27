package org.seekay.contract.model.tools;

import java.util.*;


public class SetTools {

  /**
   * Private constructor for utility class
   */
  private SetTools() {
    throw new IllegalStateException("Utility classes should never be constructed");
  }

  /**
   * For multiple sets, only the elements contained in all will be returned
   *
   * @param sets
   * @param <T>
   * @return
   */
  public static <T> Set<T> intersectingElements(Set<T>... sets) {
    assert sets.length > 1;

    Set<T> result = new HashSet<T>();

    Set<T> head = head(sets);
    List<Set<T>> tail = tail(sets);

    for (T t : head) {
      if (containedInAll(t, tail)) {
        result.add(t);
      }
    }

    return result;
  }

  /**
   * Returns the first element in a set
   *
   * @param set
   * @param <T>
   * @return
   */
  public static <T> T head(Set<T> set) {
    return set.iterator().next();
  }

  /**
   * Returns the first set in a vararg of sets
   *
   * @param sets
   * @param <T>
   * @return
   */
  public static <T> Set<T> head(Set<T>... sets) {
    return Arrays.asList(sets).get(0);
  }

  /**
   * Returns all but the first element of a set
   *
   * @param set
   * @param <T>
   * @return
   */
  public static <T> Set<T> tail(Set<T> set) {
    Set<T> result = new HashSet<T>();
    Iterator<T> iterator = set.iterator();
    iterator.next();
    while (iterator.hasNext()) {
      result.add(iterator.next());
    }

    return result;
  }

  /**
   * Converts a set of strings to an array of strings
   *
   * @param input
   * @return
   */
  public static String[] toArray(Set<String> input) {
    if(input == null) {
      return new String[0];
    }
    String[] output = new String[input.size()];
    int i = 0;
    for (String inputString : input) {
      output[i] = inputString;
      i++;
    }
    return output;
  }

  /**
   * Converts an array of strings to a set of strings
   * @param input
   * @return
   */
  public static Set<String> toSet(String[] input) {
    Set<String> output = new LinkedHashSet<String>();
    if(input == null) {
      return output;
    }
    for(String inputString : input) {
      output.add(inputString);
    }
    return output;
  }

  private static <T> List<Set<T>> tail(Set<T>... sets) {
    if (sets.length < 2) {
      throw new IllegalStateException("Matching configured incorrectly");
    }
    List<Set<T>> lists = Arrays.asList(sets);
    return lists.subList(1, lists.size());
  }

  private static <T> boolean containedInAll(T t, List<Set<T>> sets) {
    boolean found = true;
    for (Set<T> set : sets) {
      if (!set.contains(t)) {
        found = false;
      }
    }
    return found;
  }


}
