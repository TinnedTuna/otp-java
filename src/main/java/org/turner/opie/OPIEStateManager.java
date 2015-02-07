package org.turner.opie;

import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 * Utility class to manage the lifecyle of OPIE secret states.
 *
 * @author turner
 * @since 1.0
 */
public final class OPIEStateManager {

    /**
     * The default length of the secrets generated.
     */
    private static final int DEFAULT_SECRET_LENGTH = 32;

    /**
     * The default length of the seed generated.
     */
    private static final int DEFAULT_SEED_LENGTH = 3;

    /**
     * Private constructor. Never called, Always throws an
     * IllegalArgumentException.
     */
    private OPIEStateManager() {
        throw new IllegalArgumentException("Utility class, OPIEStateManager, "
                + "cannot be instantiated.");
    }

    /**
     * Generate the next state from the current state.
     *
     * @param opieSecretState The current sate for this user
     * @param opieGenerator The generator for this user's authentication style
     * @return The new state
     */
    public static OPIESecretState generateNextState(
            final OPIESecretState opieSecretState,
            final OPIEGenerator opieGenerator) {
        assert opieSecretState != null;
        assert opieGenerator != null;
        byte[] nextSecret = opieGenerator.generateOPIEBytes(opieSecretState);
        return new OPIESecretState(
                nextSecret,
                opieSecretState.getSeed(),
                opieSecretState.getHashCounts() - 1,
                opieSecretState.getMessageDigest());
    }

    /**
     * Generate a new state, usually used to enroll a new user in the system.
     *
     * @param secureRandom A CSPRNG.
     * @param messageDigest A message digest algorithm.
     * @param hashCounter How many OTPs may be generated.
     * @return The new user's state.
     */
    public static OPIESecretState generateNewState(
            final SecureRandom secureRandom,
            final MessageDigest messageDigest,
            final long hashCounter) {
        assert secureRandom != null;
        assert messageDigest != null;
        assert hashCounter > 0;
        byte[] freshSecret = secureRandom.generateSeed(DEFAULT_SECRET_LENGTH);
        byte[] freshSeed = secureRandom.generateSeed(DEFAULT_SEED_LENGTH);
        return new OPIESecretState(
                freshSecret,
                freshSeed,
                hashCounter,
                messageDigest);
    }
}
