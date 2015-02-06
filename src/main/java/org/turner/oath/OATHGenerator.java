package org.turner.oath;

/**
 * Used to generate one-time passwords, given a secret state.
 *
 * @param <T> The type of OATHSecretState to generate OTPs from.
 * @author turner
 * @since 1.0
 */
public interface OATHGenerator<T extends OATHSecretState> {

  /**
   * Generate a one-time password.
   *
   * @param secretState The secret state used to generate the one time password.
   * @return The one-time password,
   */
  String generateOtp(T secretState);
  
}
