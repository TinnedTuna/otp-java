package org.turner.oath.totp;

import org.turner.oath.utils.AbstractOATHSecretState;

/**
 * Represents an OATH TOTP state at an exact moment in time.
 *
 * @author turner
 * @since 1.0
 */
public final class TOTPSecretState extends AbstractOATHSecretState {

    /**
     * What the initial unix time was when this counter was initialised. This is
     * almost always 0.
     */
    private final long initialUnixTime;

    /**
     * What the current time was, when this TOTP was requested.
     */
    private final long currentUnixTime;

    /**
     * How many seconds to use per sub-division of time.
     */
    private final long timeStep;

    /**
     * Create a TOTP secret state representing an exact moment in time.
     *
     * @param providedSecret The secret for this TOTP OTP
     * @param providedLength The length of the OTPs created and accepted.
     * @param providedInitialUnixTime The initial unix time, usually 0.
     * @param providedCurrentUnixTime The current unix time.
     * @param providedTimeStep The time step, in seconds. Often 30.
     */
    public TOTPSecretState(
            final byte[] providedSecret,
            final int providedLength,
            final long providedInitialUnixTime,
            final long providedCurrentUnixTime,
            final long providedTimeStep) {
        super(providedSecret, providedLength);
        assert providedInitialUnixTime >= 0;
        assert providedCurrentUnixTime >= 0;
        assert providedTimeStep >= 0;
        this.initialUnixTime = providedInitialUnixTime;
        this.currentUnixTime = providedCurrentUnixTime;
        this.timeStep = providedTimeStep;

    }

    /**
     * Get the current time step value, given the current time and initial time.
     *
     * @return The time step value.
     */
    public long getCurrentTimeStepValue() {
        return (currentUnixTime - initialUnixTime) / timeStep;
    }
}
