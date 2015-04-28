package org.turner.oath;

/**
 * Interface which captures state common to TOTP and HOTP one-time passwords.
 *
 * @author turner
 * @since 1.0
 */
public interface OATHSecretState {

    /**
     * The secret for this OTP.
     *
     * @return The secret.
     */
    byte[] getSecret();

    /**
     * The length of OTP. Typically between 4 and 8.
     *
     * @return The length.
     */
    int getLength();

}
