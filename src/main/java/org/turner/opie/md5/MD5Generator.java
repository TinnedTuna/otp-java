package org.turner.opie.md5;

import org.turner.opie.utils.AbstractOPIEGenerator;

/**
 * The MD5 generator for OPIE OTPs.
 *
 * @author turner
 * @since 1.0
 */
public class MD5Generator extends AbstractOPIEGenerator {

  /**
   * The expected digest length of MD5, in bytes.
   */
  private static final int MD5_OUTPUT_LENGTH = 16;

  /**
   * Take the output of the MD5 algorithm, and fold it down to 64 bits.
   *
   * @param input The input byte array, of length 16 bytes.
   * @return The folded byte array, of length 8 bytes.
   */
  @Override
  public final byte[] foldTo64Bits(final byte[] input) {
    assert input != null;
    assert input.length == MD5_OUTPUT_LENGTH;

    byte[] results = new byte[OUTPUT_BYTES_COUNT];
    for (int i = 0; i < OUTPUT_BYTES_COUNT; i++) {
      results[i] = (byte) (input[i] ^ input[i + OUTPUT_BYTES_COUNT]);
    }
    return results;
  }
}
