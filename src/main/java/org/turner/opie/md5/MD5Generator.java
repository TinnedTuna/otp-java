package org.turner.opie.md5;

import org.turner.opie.utils.AbstractOPIEGenerator;

/**
 *
 * @author turner
 */
public class MD5Generator extends AbstractOPIEGenerator {

  @Override
  public byte[] foldTo64Bits(final byte[] input) {
    assert input != null;
    assert input.length == 16;
    
    byte[] results = new byte[8];
    results[0] = (byte) (input[0] ^ input[8]);
    results[1] = (byte) (input[1] ^ input[9]);
    results[2] = (byte) (input[2] ^ input[10]);
    results[3] = (byte) (input[3] ^ input[11]);
    
    results[4] = (byte) (input[4] ^ input[12]);
    results[5] = (byte) (input[5] ^ input[13]);
    results[6] = (byte) (input[6] ^ input[14]);
    results[7] = (byte) (input[7] ^ input[15]);
    
    return results;
  }
  
}
