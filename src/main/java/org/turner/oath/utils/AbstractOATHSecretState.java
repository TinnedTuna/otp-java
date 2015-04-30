package org.turner.oath.utils;

import org.turner.oath.OATHSecretState;

import java.util.Arrays;

/**
 * State which is common to both TOTP and HOTP.
 *
 * @author turner
 * @since 1.0
 */
public class AbstractOATHSecretState implements OATHSecretState {

    /** The secret for this OTP. */
    private final byte[] secret;

    /** The length of the generated OTPs. */
    private final int length;

    /**
     * Create an OTP state with the given bytes and output length.
     *
     * @param secretBytes The secret of this OTP.
     * @param outputLength The output length of this OTP.
     */
    public AbstractOATHSecretState(
            final byte[] secretBytes,
            final int outputLength) {
        assert secretBytes != null;
        assert outputLength > 0;
        this.secret = Arrays.copyOf(secretBytes, secretBytes.length);
        this.length = outputLength;
    }

    @Override
    public final byte[] getSecret() {
        return Arrays.copyOf(secret, secret.length);
    }

    @Override
    public final int getLength() {
        return length;
    }
}
