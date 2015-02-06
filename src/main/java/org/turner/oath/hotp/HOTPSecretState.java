package org.turner.oath.hotp;

import org.turner.oath.utils.AbstractOATHSecretState;

/**
 * Contains data necessary to implement HOTP, such as the counter.
 *
 * @author turner
 * @since 1.0
 */
public class HOTPSecretState extends AbstractOATHSecretState {

    /**
     * A counter which keeps track of the number of usages which this HOTP key
     * has been used for.
     */
    private final long counter;

    /**
     * Construct an HOTP key.
     *
     * @param secret The secret data necessary to create the HOTP.
     * @param length The desired length of any OTPs produced.
     * @param providedCounter The number of times this HOTP key has been used.
     */
    public HOTPSecretState(
            final byte[] secret,
            final int length,
            final long providedCounter) {
        super(secret, length);
        assert providedCounter >= 0;
        this.counter = providedCounter;
    }

    /**
     * Get the number of times that this HOTP key has been validated.
     *
     * @return The internal counter.
     */
     public final long getCounter() {
         return this.counter;
     }
}
