package org.turner.oath;

/**
 * OATH Generation strategies should implement this strategy.
 *
 * @author turner
 * @since 1.0
 */
public interface OATHGenerator {

    /**
     * Generate a one-time password from the given state.
     *
     * @param secretState The state used to generate the one-time password.
     * @return A one-time password.
     */
    String generateOtp(OATHSecretState secretState);

}
