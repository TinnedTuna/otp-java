package org.turner.oath;

/**
 * Data transfer object containing the secret state needed to validate or
 * generate OATH one-time passwords.
 *
 * This must never be disclosed.
 *
 * @author turner
 * @since 1.0
 */
public interface OATHSecretState {

  /**
   * The secret which backs this OATHSecretState.
   *
   * @return The secret.
   */
  byte[] getSecret();

  /**
   * The length of the OTPs which may be generated using this secret.
   *
   * @return The length.
   */
  int getLength();

}
