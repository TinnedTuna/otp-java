package org.turner.opie.sha;

import org.turner.opie.utils.AbstractOPIEGenerator;

/**
 *
 * @author turner
 */
public class SHAGenerator extends AbstractOPIEGenerator {

  @Override
  public byte[] foldTo64Bits(final byte[] input) {
    assert input != null;
    assert input.length == 5;
    byte[] result = new byte[2];
    result[0] = (byte) (input[0] ^ input[2]);
    result[1] = (byte) (input[1] ^ input[3]);
    result[0] = (byte) (input[0] ^ input[4]);
    return result;
  }

  
}
