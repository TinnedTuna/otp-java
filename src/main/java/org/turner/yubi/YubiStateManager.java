package org.turner.yubi;

import java.security.SecureRandom;

/**
 *
 * @author turner
 */
public class YubiStateManager {
  
  public static YubiSecretState generateNewState(final SecureRandom secureRandom) {
    byte[] publicId = secureRandom.generateSeed(6);
    byte[] secretId = secureRandom.generateSeed(6);
    byte[] key = secureRandom.generateSeed(16);
    return new YubiSecretState(publicId, secretId, key, new byte[2], new byte[0]);
  }
  
}
