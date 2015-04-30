package org.turner.opie.utils;

/**
 * Encodes a series of bytes into a Hex-encoded string.
 *
 * @author turner
 * @since 1.0
 */
public final class HexEncoder {

  /** All possible hexits, ordered. */
  private static final char[] HEX_CHARS = {'0', '1', '2', '3',
                                           '4', '5', '6', '7',
                                           '8', '9', 'a', 'b',
                                           'c', 'd', 'e', 'f'};

  /** A byte with only the lowest 4 bits set. */
  private static final byte LOWER_4_BITS_SET = 0x0F;

  /** The number of bits represented per hexit. */
  private static final int BITS_PER_HEXIT = 4;

  /**
   * Private constructor, never called.
   */
  private HexEncoder() {
    throw new IllegalArgumentException("Cannot instantiate HexEncoder.");
  }

  /**
   * Encode a byte array as a hex string.
   *
   * @param inputBytes The bytes to encode.
   * @return The byte array encoded as a string of hex digits (hexits).
   */
  public static String encode(final byte[] inputBytes) {
    assert inputBytes != null;
    StringBuilder outputBuffer = new StringBuilder(inputBytes.length * 2);
    for (byte b : inputBytes) {
      char lsbChar = HEX_CHARS[(b & LOWER_4_BITS_SET)];
      b >>= BITS_PER_HEXIT;
      char msbChar = HEX_CHARS[(b & LOWER_4_BITS_SET)];
      outputBuffer.append(new char[] {msbChar, lsbChar});
    }
    return outputBuffer.toString();
  }
}
