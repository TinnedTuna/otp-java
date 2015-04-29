package org.turner.opie.md5;

import org.turner.opie.utils.AbstractOPIEGenerator;

/**
 * Generator for otp-md5 OPIE one-time passwords.
 *
 * @author turner
 * @since 1.0
 */
public class MD5Generator extends AbstractOPIEGenerator {

  /** The expected input size to foldTo64Bits. */
  private static final int MD5_OUTPUT_LENGTH_BYTES = 16;

  /** The number of bytes to "stride" over when folding. */
  private static final int STRIDE = 8;

  @Override
  public final byte[] foldTo64Bits(final byte[] input) {
    assert input != null;
    assert input.length == MD5_OUTPUT_LENGTH_BYTES;

    byte[] results = new byte[EXPECTED_OUTPUT_LENGTH_BYTES];
    for (int i = 0; i < EXPECTED_OUTPUT_LENGTH_BYTES; i++) {
      results[i] = (byte) (input[i] ^ input[i + STRIDE]);
    }

    return results;
  }
}
