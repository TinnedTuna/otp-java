package org.turner.yubi;

import java.security.SecureRandom;

/**
 *
 * @author turner
 */
public class YubiStateManager {
  
  public static YubiSecretState generateNewState(final SecureRandom secureRandom) {
    return new YubiSecretState();
  }
  
}
