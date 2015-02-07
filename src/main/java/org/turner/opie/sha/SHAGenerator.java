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
   * The number of bytes required to represent a Java int.
   */
  private static final int BYTES_PER_INT = 4;

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

    /**
     * RFC2289 requires that the folding of the sha digest take the form, where
     * sha.digest is a 5-element array of 32-bit integers:
     *
     *  sha.digest[0] ^= sha.digest[2];
     *  sha.digest[1] ^= sha.digest[3];
     *  sha.digest[0] ^= sha.digest[4];
     *
     * Followed by a loop to pack it into a byte array. We receive the result of
     * the SHA-1 as a byte array (input), so we can re-do this in less code.
     *
     * We convert to a 20-element byte array, and use SSA to denote values, that
     * is, no variable is assigned to more than once.
     *
     * Represent sha.digest[0] ^= sha.digest[2]:
     *
     *   input'[0] = input[0] ^ input[8]
     *   input'[1] = input[1] ^ input[9]
     *   input'[2] = input[2] ^ input[10]
     *   input'[3] = input[3] ^ input[11]
     *
     * Represent sha.digest[1] ^= sha.digest[3]:
     *
     *   input'[4] = input[4] ^ input[12]
     *   input'[5] = input[5] ^ input[13]
     *   input'[6] = input[6] ^ input[14]
     *   input'[7] = input[7] ^ input[15]
     *
     * Represent sha.digest[0] ^= sha.digest[4]:
     *
     *   input''[0] = input'[0] ^ input[16]
     *   input''[1] = input'[1] ^ input[17]
     *   input''[2] = input'[2] ^ input[18]
     *   input''[3] = input'[3] ^ input[19]
     *
     * Eliminate the assignments to input' which get overwritten, and expand
     * any instances of input' which would be otherwise lost:
     *
     *   input'[4] = input[4] ^ input[12]
     *   input'[5] = input[5] ^ input[13]
     *   input'[6] = input[6] ^ input[14]
     *   input'[7] = input[7] ^ input[15]
     *
     *   input'[0] = input[0] ^ input[8] ^ input[16]
     *   input'[1] = input[1] ^ input[9] ^ input[17]
     *   input'[2] = input[2] ^ input[10] ^ input[18]
     *   input'[3] = input[3] ^ input[11] ^ input[19]
     *
     * We do not want/need to update the input array, so we store in an output
     * byte array of size 8. result. We also re-order at this point:
     *
     *   result[0] = input[0] ^ input[8] ^ input[16]
     *   result[1] = input[1] ^ input[9] ^ input[17]
     *   result[2] = input[2] ^ input[10] ^ input[18]
     *   result[3] = input[3] ^ input[11] ^ input[19]
     *
     *   result[4] = input[4] ^ input[12]
     *   result[5] = input[5] ^ input[13]
     *   result[6] = input[6] ^ input[14]
     *   result[7] = input[7] ^ input[15]
     *
     * Hence, we use two loops to implement this logic.
     */
    for (int i = 0; i < BYTES_PER_INT; i++) {
      result[i]
        = (byte) (input[i] ^ input[i + Byte.SIZE] ^ input[i + 2 * Byte.SIZE]);
    }

    for (int i = BYTES_PER_INT; i < 2 * BYTES_PER_INT; i++) {
      result[i] = (byte) (input[i] ^ input[i + Byte.SIZE]);
    }

    return result;
  }
}
