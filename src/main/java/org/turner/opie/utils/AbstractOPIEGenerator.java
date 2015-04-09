package org.turner.opie.utils;

import java.security.MessageDigest;
import org.turner.opie.OPIEGenerator;
import org.turner.opie.OPIESecretState;

/**
 *
 * @author turner
 */
public abstract class AbstractOPIEGenerator implements OPIEGenerator {
  
  /**
   * Takes an input stream of bytes, >64b length and folds it down to 64 bits
   * 
   * @param input
   * @return a 64b array of bytes
   */
  public abstract byte[] foldTo64Bits(byte[] input);
  
  public String generateOPIEString(final OPIESecretState opieSecretState) {
    assert opieSecretState != null;
    return OPIEUtils.bytesToWords(generateOPIEBytes(opieSecretState));
  }
  
  public byte[] generateOPIEBytes(final OPIESecretState opieSecretState) {
    assert opieSecretState != null;
    
    MessageDigest messageDigest = opieSecretState.getMessageDigest();
    messageDigest.update(opieSecretState.getSeed());
    byte[] digested = messageDigest.digest(opieSecretState.getSecret());

    return foldTo64Bits(digested);
  }
}
