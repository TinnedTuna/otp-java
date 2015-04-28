package org.turner.oath.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.InvalidKeyException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Collection of common, low-level utilities used when generating OATH one-time
 * passwords.
 *
 * @author turner
 * @since 1.0
 */
public final class OATHUtils {

    /** Pre-computed table of 10^n, for n <- (1, ..., 8). */
    private static final int[] DIGITS_POWER = {1
            , 10
            , 100
            , 1000
            , 10000
            , 100000
            , 1000000
            , 10000000
            , 100000000
    };

    /** A byte with only the lowest 4 bits set. */
    private static final byte LOWER_4_BITS_SET = 0x0F;

    /** A byte with the sign-bit cleared. */
    private static final byte SIGN_BIT_CLEAR = 0x7F;

    /** A byte with all bits set. */
    private static final byte ALL_BITS_SET = ~0x0;

    /** The number of bytes required to represent 32 bits. */
    private static final int DYNAMIC_TRUNCATION_OUTPUT_LENGTH = 4;

    /** The number of bytes in a java.lang.Long. */
    private static final int BYTES_PER_LONG = 8;

    /**
     * Private constructor, never called.
     */
    private OATHUtils() {
        throw new IllegalStateException("Cannot instantiate OATHUtils.");
    }

    /**
     * Dynamic truncation, as specified in RFC4226. Uses the last 4 bits as an
     * offset into the provided bytes, then selects 32-bits from that point.
     *
     * @param inputBytes The bytes to truncate.
     * @return 4 bytes of output from the inputBytes.
     */
    public static byte[] truncateBytes(final byte[] inputBytes) {
        assert inputBytes != null;
        assert inputBytes.length > 1;

        int offsetLocation
                = inputBytes[inputBytes.length - 1] & LOWER_4_BITS_SET;

        assert offsetLocation >= 0;
        assert offsetLocation + DYNAMIC_TRUNCATION_OUTPUT_LENGTH
                <= inputBytes.length;

        byte[] truncated = new byte[DYNAMIC_TRUNCATION_OUTPUT_LENGTH];
        // We mas out the sign bit before continuing.
        truncated[0] = (byte) (inputBytes[offsetLocation] & SIGN_BIT_CLEAR);
        for (int i = 1; i < DYNAMIC_TRUNCATION_OUTPUT_LENGTH; i++) {
            truncated[i] = inputBytes[offsetLocation + i];
        }
        assert truncated[0] >= 0; // The sign bit must not be set.
        return truncated;
    }

    /**
     * Converts a set of input bytes into a decimal string, with the requested
     * length.
     *
     * @param inputIntBytes The bytes to convert, must have a length of 4.
     * @param numberOfOutputDigits The number of digits to output, upto 8.
     * @return The decimal interpretation of the bytes, truncated to the
     *         requested number of digits.
     */
    public static String integerify(
            final byte[] inputIntBytes,
            final int numberOfOutputDigits) {
        assert inputIntBytes != null;
        assert inputIntBytes.length == DYNAMIC_TRUNCATION_OUTPUT_LENGTH;
        assert numberOfOutputDigits != 0;
        assert numberOfOutputDigits <= DIGITS_POWER.length;
        ByteBuffer wrappedBytes = ByteBuffer.wrap(inputIntBytes);
        wrappedBytes.order(ByteOrder.BIG_ENDIAN);
        int binary = wrappedBytes.getInt();
        assert binary >= 0;
        int truncatedOtp = binary % DIGITS_POWER[numberOfOutputDigits];
        assert truncatedOtp >= 0;
        String result = Integer.toString(truncatedOtp);
        assert !result.startsWith("-");

        if (result.length() < numberOfOutputDigits) {
            return prependPad(result, '0', numberOfOutputDigits);
        } else {
            return result;
        }
    }

    /**
     * Prepends a padding character to the front of a string, until the string
     * is the requested length.
     *
     * @param value The value to pre-pend
     * @param prependedChar The character to add to the front.
     * @param size The desired size of the result.
     * @return The value, plus as many prependedChar characters added at the
     *         front to make it at least the requested size.
     */
    public static String prependPad(
            final String value,
            final char prependedChar,
            final int size) {
        assert value != null;
        assert size > value.length();

        int requiredPadSize = size - value.length();
        assert requiredPadSize > 0;

        StringBuilder paddedResult = new StringBuilder(size);
        while (requiredPadSize > 0) {
            paddedResult.append(prependedChar);
            requiredPadSize -= 1;
            assert paddedResult.length() < size;
        }
        paddedResult.append(value);

        assert paddedResult.length() == size;
        return paddedResult.toString();
    }

    /**
     * Converts a given long into an array of bytes.
     *
     * @param inputLong The long to convert.
     * @return The representation of the long as a byte array.
     */
    public static byte[] longBytes(final long inputLong) {
        byte[] longBytes = new byte[BYTES_PER_LONG];
        long temporaryLong = inputLong;
        /*
         * Go "backwards", strip off the lower order bits, then shift our
         * temporary variable to throw them away.
         */
        for (int i = BYTES_PER_LONG - 1; i >= 0; i--) {
            longBytes[i] = (byte) (temporaryLong & ALL_BITS_SET);
            temporaryLong >>= Byte.SIZE;
        }
        assert temporaryLong == 0;
        return longBytes;
    }

    /**
     * Apply the given MAC algorithm to the given message, under the given key.
     *
     * @param mac The MAC algorithm to use.
     * @param secretKey The key to use,
     * @param message The message to MAC.
     * @return The bytes of output from the MAC.
     */
    public static byte[] macBytes(
            final Mac mac,
            final byte[] secretKey,
            final byte[] message) {
        assert secretKey != null;
        assert secretKey.length != 0;

        assert message != null;
        assert message.length != 0;

        assert mac != null;

        try {
            mac.init(new SecretKeySpec(secretKey, "RAW"));
            return mac.doFinal(message);
        } catch (InvalidKeyException ex) {
            throw new IllegalStateException(ex);
        } finally {
            mac.reset();
        }
    }

    /**
     * Compare two strings in constant time.
     *
     * @param left A string to compare.
     * @param right A string to compare.
     * @return True, iff each string has the same characters in the same order.
     */
    public static boolean constantTimeEquals(
            final String left,
            final String right) {
        assert left != null;
        assert right != null;

        if (left.length() != right.length()) {
            return false;
        }

        int result = 0;
        for (int i = 0; i < left.length(); i++) {
            result |= left.charAt(i) ^ right.charAt(i);
        }
        return result == 0;
    }
}
