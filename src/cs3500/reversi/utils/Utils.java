package cs3500.reversi.utils;

/**
 * Contains utility methods for better abstract out code
 * and decrease code duplication in Reversi.
 */
public class Utils {
  /**
   * Check values in between the given upper and lower bound
   * (inclusively, meaning it's checking whether the value is larger or equal to the lower bound
   * and smaller or equal to the upper bound).
   * @param value the value to be checked
   * @param lowerBound the lower bound (inclusive)
   * @param upperBound the upper bound (inclusive)
   * @return true if the value is in between, false otherwise
   */
  public static boolean checkValuesInBetween(int value, int lowerBound, int upperBound) {
    return value >= lowerBound && value <= upperBound;
  }
}
