package org.turner.opie.sha;

import org.turner.opie.utils.AbstractOPIEGenerator;

/**
 * Generator for otp-sha1 OPIE one-time passwords.
 *
 * @author turner
 * @since 1.0
 */
public class SHAGenerator extends AbstractOPIEGenerator {

  /** The expected input size to foldTo64Bits. */
  private static final int SHA1_OUTPUT_SIZE_BYTES = 20;

  /** A short stride on the input byte array. */
  private static final int SHORT_STRIDE = 4;

  /** A long stride on the input byte array. */
  private static final int LONG_STRIDE = 8;


  @Override
  public final byte[] foldTo64Bits(final byte[] input) {
    assert input != null;
    assert input.length == SHA1_OUTPUT_SIZE_BYTES;
    byte[] result = new byte[EXPECTED_OUTPUT_LENGTH_BYTES];

    // First, we perform the simultaneous long and short stride.
    for (int i = 0; i < EXPECTED_OUTPUT_LENGTH_BYTES / 2; i++) {
      result[i] = (byte) (input[i] ^ input[SHORT_STRIDE] ^ input[LONG_STRIDE]);
    }

    // Next, we perform the lone long stride.
    for (
        int i = EXPECTED_OUTPUT_LENGTH_BYTES / 2;
        i < EXPECTED_OUTPUT_LENGTH_BYTES;
        i++) {
      result[i] = (byte) (input[i] ^ input[LONG_STRIDE]);
    }

    return result;
  }
}
