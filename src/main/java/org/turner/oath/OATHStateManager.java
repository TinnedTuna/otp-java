package org.turner.oath;

import org.turner.oath.hotp.HOTPSecretState;
import org.turner.oath.totp.TOTPSecretState;

/**
 * Utility class to manage the lifecycle of OATHSecretStates.
 *
 * @author turner
 * @since 1.0
 */
public final class OATHStateManager {

    /**
     * Private constructor. Never called, Always throws an
     * IllegalArgumentException.
     */
    private OATHStateManager() {
        throw new IllegalArgumentException("Utility class, OATHStateManager, "
                + "cannot be instantiated.");
    }


    /**
     * Provides a new state for the HOTP algorithm. Simply increments an
     * internal counter. The supplied state is unchanged. Throws an
     * IllegalStateException if the counter would wrap-around.
     *
     * @param secretState The HOTP state to update.
     * @return A new secret state with it's internal counter incremented.
     */
    public static OATHSecretState generateNextState(
            final HOTPSecretState secretState) {
        assert secretState != null;
        if (secretState.getCounter() == Long.MAX_VALUE) {
            throw new IllegalStateException("Cannot generate a new state from "
                    + "this state. Counter size exceeded.");
        }

        return new HOTPSecretState(
                secretState.getSecret(),
                secretState.getLength(),
                secretState.getCounter() + 1);
    }

    /**
     * Provides the new OATH state for a TOTP token. Does nothing, just provided
     * for completeness.
     *
     * @param secretState The secret state to update. No change will be made.
     * @return The same secret state (it is constant for TOTP).
     */
    public static OATHSecretState generateNextState(
            final TOTPSecretState secretState) {
      assert secretState != null;
      return secretState;
    }
}
