package org.turner.oath.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.InvalidKeyException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Utility class to provide common functions used when working with OATH OTPs.
 *
 * @author turner
 * @since 1.0
 */
public final class OATHUtils {

  /**
   * Various powers of ten. We don't need many, so we statically store a few.
   */
  private static final int[] DIGITS_POWER
          = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000};

  /**
   * The number of bytes required to represent a Java int.
   */
  private static final int BYTES_PER_INT = 4;

  /**
   * The number of bytes required to represent a Java long.
   */
  private static final int BYTES_PER_LONG = 8;

  /**
   * A byte with only the last four bits set.
   */
  private static final byte NIBBLE_SET = (byte) 0x0F;

  /**
   * A byte with all but the first bit set.
   */
  private static final byte FIRST_BIT_CLEAR = (byte) 0x7F;

  /**
   * A byte with sign bit set.
   */
  private static final byte SIGN_BIT = (byte) 0x80;

  /**
   * Private constructor. Never called, Always throws an
   * IllegalArgumentException.
   */
  private OATHUtils() {
    throw new IllegalArgumentException("Utility class, OATHUtils, cannot be "
            + "instantiated.");
  }

  /**
   * Truncate a given set of bytes length 4, using the dynamic truncation
   * algorithm specified in RFC4226.
   *
   * @param inputBytes The bytes to truncate, must be of length greater than 4.
   * @return An array of bytes with length = 4.
   */
  public static byte[] truncateBytes(final byte[] inputBytes) {
    assert inputBytes != null;
    // We must have at least enough bytes to pack into an int representation.
    assert inputBytes.length > BYTES_PER_INT;

    int offsetLocation = inputBytes[inputBytes.length - 1] & NIBBLE_SET;

    assert offsetLocation >= 0;
    // The entirety of the int representation can be retrieved from the input.
    assert offsetLocation + (BYTES_PER_INT - 1) < inputBytes.length;

    byte[] truncated = new byte[BYTES_PER_INT];
    truncated[0] = (byte) (inputBytes[offsetLocation] & FIRST_BIT_CLEAR);
    for (int i = 1; i < BYTES_PER_INT; i++) {
      truncated[i] = inputBytes[offsetLocation + i];
    }

    // The result must not represent a negative number,
    assert (truncated[0] & SIGN_BIT) != SIGN_BIT;
    return truncated;
  }

  /**
   * Converts some input bytes representing a Java int into a string, possibly
   * padding on the left to ensure that the length is exactly
   * numberOfOutputDigits.
   *
   * @param inputIntBytes The bytes to convert to an int.
   * @param numberOfOutputDigits The number of digits required on the output,
   * @return The string representation of inputIntBytes, with desired length.
   */
  public static String integerify(
          final byte[] inputIntBytes,
          final int numberOfOutputDigits) {
    assert inputIntBytes != null;
    assert inputIntBytes.length == BYTES_PER_INT;
    assert numberOfOutputDigits != 0;
    assert numberOfOutputDigits <= DIGITS_POWER.length;
    ByteBuffer wrappedBytes = ByteBuffer.wrap(inputIntBytes);
    wrappedBytes.order(ByteOrder.BIG_ENDIAN);
    int binary = wrappedBytes.getInt();
    assert binary >= 0;
    int truncatedOtp = binary % DIGITS_POWER[numberOfOutputDigits];
    assert truncatedOtp >= 0;
    String result = Integer.toString(truncatedOtp);
    assert !result.startsWith("-");

    if (result.length() < numberOfOutputDigits) {
      return prependPad(result, '0', numberOfOutputDigits);
    } else {
      return result;
    }
  }

  /**
   * Pad a string on the left to the given size with the given character.
   *
   * @param value The string to pad, with length less than size.
   * @param prependedChar The character to use for padding.
   * @param size The final length desired.
   * @return A string padded on the left with enough prependedChar to ensure
   *         that the length = size.
   */
  public static String prependPad(
          final String value,
          final char prependedChar,
          final int size) {
    assert value != null;
    assert size > value.length();

    int requiredPadSize = size - value.length();
    assert requiredPadSize > 0;

    StringBuilder paddedResult = new StringBuilder(size);
    while (requiredPadSize > 0) {
      paddedResult.append(prependedChar);
      requiredPadSize -= 1;
      assert paddedResult.length() < size;
    }
    paddedResult.append(value);

    assert paddedResult.length() == size;
    return paddedResult.toString();
  }

  /**
   * Convert a long to bytes.
   *
   * @param inputLong The long to convert.
   * @return A byte array representation of a long, with length 8.
   */
  public static byte[] longBytes(final long inputLong) {
    byte[] longBytes = new byte[BYTES_PER_LONG];
    for (int i = 0; i < BYTES_PER_LONG; i++) {
      longBytes[i] = (byte) ((inputLong >> (BYTES_PER_LONG - i)));
    }
    return longBytes;
  }

  /**
   * Compute a message authentication code on a message, given a secret key. If
   * the key is not valid, an IllegalSateException is thrown, and mac.reset() is
   * called.
   *
   * @param mac The MAC algorithm to use.
   * @param secretKey The secret key for the MAC.
   * @param message The message to generate a MAC for.
   * @return The MAC bytes.
   */
  public static byte[] macBytes(
          final Mac mac,
          final byte[] secretKey,
          final byte[] message) {
    assert secretKey != null;
    assert secretKey.length != 0;

    assert message != null;
    assert mac != null;

    try {
      mac.reset(); // Prevent un-expected data entering the MAC.
      mac.init(new SecretKeySpec(secretKey, "RAW"));
      byte[] timeStepBytes = message;
      return mac.doFinal(timeStepBytes);
    } catch (InvalidKeyException ex) {
      throw new IllegalStateException(ex);
    } finally {
      mac.reset();
    }
  }

  /**
   * Constant time equality for strings.
   *
   * @param left A string to compare.
   * @param right Another string.
   * @return true iff the strings contain all equal characters.
   */
  public static boolean constantTimeEquals(
          final String left,
          final String right) {
    assert left != null;
    assert right != null;

    if (left.length() != right.length()) {
      return false;
    }

    int result = 0;
    for (int i = 0; i < left.length(); i++) {
      result |= left.charAt(i) ^ right.charAt(i);
    }
    return result == 0;
  }
}
