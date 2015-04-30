package org.turner.opie;

/**
 * Generates OPIE one time passwords, as a string or array of bytes.
 *
 * @author turner
 * @since 1.0
 */
public interface OPIEGenerator {

  /**
   * Generate an OPIE OTP as an array of bytes, given the secret state.
   *
   * @param opieSecretState The secret state of this OPIE OTP.
   * @return The OTP.
   */
  byte[] generateOPIEBytes(OPIESecretState opieSecretState);
}
