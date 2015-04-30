package org.turner.opie.utils;

/**
 * Collection of common, low-level utilities used when generating OPIE one-time
 * passwords.
 *
 * @author turner
 * @since 1.0
 */
public final class OPIEUtils {

  /**
   * Private constructor, never called.
   */
  private OPIEUtils() {
    throw new IllegalArgumentException("Cannot instantiate OPIEUtils.");
  }

  /**
   * Compare two byte arrays in constant time.
   *
   * @param left A byte array to compare.
   * @param right A byte array to compare.
   * @return True, iff left contains the same elements as right, in the same
   *         order.
   */
  public static boolean constantTimeEquals(
          final byte[] left,
          final byte[] right) {
    assert left != null;
    assert right != null;

    if (left.length != right.length) {
      return false;
    }

    int result = 0;
    for (int i = 0; i < left.length; i++) {
      result |= left[i] ^ right[i];
    }
    return result == 0;
  }
}
