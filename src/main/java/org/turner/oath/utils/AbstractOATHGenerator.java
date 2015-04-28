package org.turner.oath.utils;

import javax.crypto.Mac;

import org.turner.oath.OATHGenerator;
import org.turner.oath.OATHSecretState;

/**
 * Provides common logic for generating OTAP OTPs.
 *
 * @author turner
 * @since 1.0
 */
public abstract class AbstractOATHGenerator implements OATHGenerator {

    /** The MAC algorithm which is to be used to generate OTPs. */
    private final Mac mac;

    /**
     * Creates a generator which uses the MAC algorithm given.
     *
     * @param macAlgo The MAC algorithm used to generate OTPs.
     */
    public AbstractOATHGenerator(final Mac macAlgo) {
        assert macAlgo != null;
        this.mac = macAlgo;
    }

    /**
     * Retrieves the one-time internal state from the given state, such as a
     * counter, or current time-step.
     *
     * @param secretState The state to extract internal state from.
     * @return The one-time internal state.
     */
    protected abstract byte[] getInternalState(
            final OATHSecretState secretState);

    @Override
    public final String generateOtp(final OATHSecretState secretState) {
        assert secretState != null;

        byte[] macOutput = OATHUtils.macBytes(
                mac,
                secretState.getSecret(),
                getInternalState(secretState));
        return OATHUtils.integerify(
                OATHUtils.truncateBytes(macOutput),
                secretState.getLength());
    }

}
