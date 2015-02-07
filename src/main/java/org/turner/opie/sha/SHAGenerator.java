package org.turner.opie.sha;

import org.turner.opie.utils.AbstractOPIEGenerator;

/**
 * The SHA-1 generator for OPIE OTPs.
 *
 * @author turner
 * @since 1.0
 */
public class SHAGenerator extends AbstractOPIEGenerator {

  /**
   * The number of bytes output by the SHA-1 algorithm.
   */
  private static final int SHA_OUTPUT_LENGTH = 20;

  /**
   * Take the output of the SHA-1 algorithm and fold it down to 64 bits.
   *
   * @param input The output of a hashing algorithm.
   * @return A byte array of length 8.
   */
  @Override
  public final byte[] foldTo64Bits(final byte[] input) {
    assert input != null;
    assert input.length == SHA_OUTPUT_LENGTH;
    byte[] result = new byte[OUTPUT_BYTES_COUNT];

    result[0] = (byte) (input[0]  ^ input[4]);
    result[1] = (byte) (input[1]  ^ input[5]);
    result[2] = (byte) (input[2]  ^ input[6]);
    result[3] = (byte) (input[3]  ^ input[7]);

    result[4] = (byte) (input[12]  ^ input[4]);
    result[5] = (byte) (input[13]  ^ input[5]);
    result[6] = (byte) (input[14]  ^ input[6]);
    result[7] = (byte) (input[15]  ^ input[7]);

    result[0] ^= input[16];
    result[1] ^= input[17];
    result[2] ^= input[18];
    result[3] ^= input[19];

    return result;
  }
}
