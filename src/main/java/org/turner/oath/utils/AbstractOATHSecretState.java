package org.turner.oath.utils;

import org.turner.oath.OATHSecretState;

/**
 * Abstract class for the secret state relating to OATH OTPs. Contains a secret
 * key that must never be disclosed.
 *
 * @author turner
 * @since 1.0
 */
public class AbstractOATHSecretState implements OATHSecretState {

  /**
   * The secret that is used for OTP generation and validation.
   */
  private final byte[] secret;

  /**
   * How many decimal digits are to be output from this OATH state.
   */
  private final int length;

  /**
   * Create a new, immutable secret state.
   *
   * @param providedSecret This OATH OTP's secret.
   * @param providedLength The number of decimal digits to be output for OTPs.
   */
  public AbstractOATHSecretState(
          final byte[] providedSecret,
          final int providedLength) {
    assert providedSecret != null;
    assert providedLength > 0;
    this.secret = providedSecret;
    this.length = providedLength;
  }

  /**
   * Get the secret used for OTP generation and validation.
   *
   * @return The secret.
   */
  public final byte[] getSecret() {
    return secret;
  }

  /**
   * Get the desired length of OTPs.
   *
   * @return The length. Always greater than 0.
   */
  public final int getLength() {
    return length;
  }
}
