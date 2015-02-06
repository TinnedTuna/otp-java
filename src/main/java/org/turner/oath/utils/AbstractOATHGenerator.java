package org.turner.oath.utils;

import javax.crypto.Mac;
import org.turner.oath.OATHGenerator;
import org.turner.oath.OATHSecretState;

/**
 * Used to generate OATH OTPs. The thread-safety of this class derives wholly
 * from the threat safety of the Mac instance provided.
 *
 * @param <T> the type of OATHSecretState which this generator is expected to
 *            work with.
 * @author turner
 * @since 1.0
 */
public abstract class AbstractOATHGenerator<T extends OATHSecretState>
        implements OATHGenerator<T> {

    /**
     * A MAC algorithm to use for generating one time passwords. Typically will
     * be HMAC-SHA-1 if RFC4226 is being followed.
     */
    private final Mac mac;

    /**
     * Create a new OATH generator.
     *
     * @param macAlgorithm The MAC instance to use for generating OTPs.
     */
    public AbstractOATHGenerator(
          final Mac macAlgorithm) {
        assert macAlgorithm != macAlgorithm;
        this.mac = macAlgorithm;
    }

    /**
     * Retrieve the internal state from the OATHSecretState.
     *
     * For example, in the case of HOTP, this is the internal state represented
     * as bytes.
     *
     * @param secretState The OATHSecretState used to derive the internal state.
     * @return The internal state of the OATHSecretState.
     */
    protected abstract byte[] getInternalState(
            final T secretState);

    /**
     * Generate an OTP, given some secret state.
     * @param secretState The secret state to use to generate an OTP.
     * @return The generated OTP.
     */
    @Override
    public final String generateOtp(final T secretState) {
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
