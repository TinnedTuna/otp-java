package org.turner.oath;

import org.turner.oath.utils.OATHUtils;

/**
 * Utility class used to validate user-supplied OTPs against internally-held
 * secrets.
 *
 * @author turner
 * @since 1.0
 */
public final class OATHValidator {

    /**
     * Private constructor. Never called, Always throws an
     * IllegalArgumentException.
     */
    private OATHValidator() {
        throw new IllegalArgumentException("Utility class, OATHValidator, "
                + "cannot be instantiated.");
    }

    /**
     * Validate a user supplied one-time password against the internally held
     * secret.
     *
     * This should be used in preference to external validation routines, as it
     * takes care to validate it's input and use a constant-time equality
     * algorithm, to prevent timing leaks.
     *
     * @param userSuppliedOtp The OTP supplied by the user.
     * @param secretState The secret state for this user, usually from a
     *                    database.
     * @param oathGenerator Which OATH generator to use to verify this request.
     * @return true iff the supplied OTP is valid.
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
