package org.turner.oath;

import org.turner.oath.utils.OATHUtils;

/**
 * Validates given OATH one-time passwords.
 *
 * @author turner
 * @since 1.0
 */
public final class OATHValidator {

    /**
     * Private constructor, never called.
     */
    private OATHValidator() {
        throw new IllegalStateException("Cannot instantiate OATHValidator.");
    }

    /**
     * Validates a given OTP, against the given state and generation strategy.
     *
     * @param userSuppliedOtp The OTP to validate.
     * @param secretState The state associated with the presumed user.
     * @param oathGenerator The generation strategy for the presumed user.
     * @return True, if the OTP was valid for the user's state.
     */
    public static boolean validateOtp(
            final String userSuppliedOtp,
            final OATHSecretState secretState,
            final OATHGenerator oathGenerator) {
        assert userSuppliedOtp != null;
        assert secretState != null;
        assert oathGenerator != null;

        if (userSuppliedOtp.length() != secretState.getLength()) {
            return false;
        }

        String generatedOtp = oathGenerator.generateOtp(secretState);

        return OATHUtils.constantTimeEquals(userSuppliedOtp, generatedOtp);
    }
}
