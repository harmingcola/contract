package org.seekay.contract.model.tools;

public class SleepTools {

  /**
   * Private constructor for utility class
   */
  private SleepTools() {
    throw new IllegalStateException("Utility classes should never be constructed");
  }

  /**
   * Wraps the thread sleep call and catches the exception
   * @param time
   */
  public static void sleep(int time) {
    try {
      Thread.sleep(time);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
