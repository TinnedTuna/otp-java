package org.turner.oath.totp;

import org.turner.oath.OATHSecretState;
import org.turner.oath.utils.AbstractOATHSecretState;

/**
 * Holds TOTP-specific state for generating OATH one-time passwords.
 *
 * @author turner
 * @since 1.0
 */
public class TOTPSecretState
        extends AbstractOATHSecretState implements OATHSecretState {

    /** The time step, in seconds, to use. */
    private final long timeStep;

    /** The initial time to base all future OTPs from. */
    private final long initialUnixTime;

    /** The current time, as seconds since the unix epoch. */
    private final long currentUnixTime;

    /**
     * Constructs a TOTP secret state.
     *
     * @param secret The secret for this state.
     * @param length The ouput OTP length.
     * @param initialTimeSeconds The initial unix time, typically 0 seconds.
     * @param currentTimeSeconds The current unix time.
     * @param timeStepSeconds The number of seconds per step.
     */
    public TOTPSecretState(
            final byte[] secret,
            final int length,
            final long initialTimeSeconds,
            final long currentTimeSeconds,
            final long timeStepSeconds) {
        super(secret, length);
        assert timeStepSeconds >= 0;
        assert initialTimeSeconds >= 0;
        assert currentTimeSeconds >= 0;
        this.timeStep = timeStepSeconds;
        this.initialUnixTime = initialTimeSeconds;
        this.currentUnixTime = currentTimeSeconds;
    }

    /**
     * Calculates the current time step, as a function of the initial time,
     * the current time, and the time step.
     *
     * @return The currrent time step value.
     */
    public final long getCurrentTimeStepValue() {
        long timeStepValue = (currentUnixTime - initialUnixTime) / timeStep;
        assert timeStepValue >= 0;
        return timeStepValue;
    }

}
