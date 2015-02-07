package org.turner.opie;

/**
 * Generate OPIE OTPs, as both strings and arrays of bytes.
 *
 * @author turner
 * @since 1.0
 */
public interface OPIEGenerator {

  /**
   * Generate a one-time password, as a series of space-separated words drawn
   * from the RFC2289 dictionary.
   *
   * @param opieSecretState The state used to generate the password.
   * @return The one-time password.
   */
  String generateOPIEString(OPIESecretState opieSecretState);

  /**
   * Generate a one-tome password, as an array of bytes, with length 8.
   *
   * @param opieSecretState The state used to generate the password.
   * @return The one-time password.
   */
  byte[] generateOPIEBytes(OPIESecretState opieSecretState);

}
