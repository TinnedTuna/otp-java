package org.turner.opie.utils;

import java.util.regex.Pattern;

/**
 * Encodes and decodes byte arrays as hex-encoded strings.
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

  /** A regex which matches valid hexadecimal strings. */
  private static final Pattern HEX_DIGIT_PATTERN
          = Pattern.compile("\\p{XDigit}*");

  /** The radix used for decoding. */
  private static final int DECODE_RADIX = 16;

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

  /**
   * Decode a hex-encoded string into a byte array.
   *
   * @param hexString The encoded string.
   * @return The decoded byte array.
   */
  public static byte[] decode(final String hexString) {
    assert hexString != null;
    assert hexString.length() % 2 == 0;
    assert HEX_DIGIT_PATTERN.matcher(hexString).matches();
    final byte[] result = new byte[hexString.length() / 2];
    final char[] hexStringChars = hexString.toCharArray();
    for (int i = 0; i < hexString.length() - 1; i += 2) {
      int resultIndex = i / 2;
      result[resultIndex] = findHexit(hexString.charAt(i));
      result[resultIndex] <<= BITS_PER_HEXIT;
      result[resultIndex] |= findHexit(hexString.charAt(i + 1));
    }
    return result;
  }

  /**
   * Search for a given hexit in the HEX_CHARS table.
   *
   * @param hexit The hexit to search for.
   * @return The location of the hexit in the table.
   */
  private static byte findHexit(final char hexit) {
    for (byte i = 0; i < HEX_CHARS.length; i++) {
      if (HEX_CHARS[i] == hexit) {
        return i;
      }
    }
    assert false;
    return -1;
  }
}
