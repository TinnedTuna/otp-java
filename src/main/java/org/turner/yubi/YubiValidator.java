package org.turner.yubi;

/**
 *
 * @author turner
 */
public class YubiValidator {

  static boolean validateOtp(
          final String userOtp, 
          final YubiSecretState yubiSecretState) {
    assert userOtp != null;
    assert yubiSecretState != null;
    throw new UnsupportedOperationException("Not yet implemented");
  }
  
}
