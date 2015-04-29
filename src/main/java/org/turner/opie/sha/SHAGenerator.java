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

  @Override
  public final byte[] foldTo64Bits(final byte[] input) {
    assert input != null;
    assert input.length == SHA1_OUTPUT_SIZE_BYTES;
    byte[] result = new byte[EXPECTED_OUTPUT_LENGTH_BYTES];
    
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
