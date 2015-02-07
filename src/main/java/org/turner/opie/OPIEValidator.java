package org.turner.opie;

import org.turner.opie.utils.OPIEUtils;

/**
 * Utility class which validates user-supplied OPIE OTPs.
 *
 * @author turner
 * @since 1.0
 */
public final class OPIEValidator {

    /**
     * Private constructor. Never called, Always throws an
     * IllegalArgumentException.
     */
    private OPIEValidator() {
        throw new IllegalArgumentException("Utility class, OPIEValidator, "
                + "cannot be instantiated.");
    }

    /**
     * Validate a user supplied OPIE OTP.
     *
     * @param userSuppliedOPIE The user-supplied OPIE OTP.
     * @param secretState The secret state associated with the purported user.
     * @param opieGenerator The generator representing the OPIE algorithm
     * @return True iff the user-supplied OPIE OTP is valid.
     */
    public static boolean validateOPIE(
            final String userSuppliedOPIE,
            final OPIESecretState secretState,
            final OPIEGenerator opieGenerator) {
        assert userSuppliedOPIE != null;
        assert secretState != null;
        assert opieGenerator != null;
        byte[] generatedOPIE = opieGenerator.generateOPIEBytes(secretState);
        return OPIEUtils.constantTimeEquals(
                generatedOPIE,
                OPIEUtils.wordsToBytes(userSuppliedOPIE));
  }
}
