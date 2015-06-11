package org.turner.opie.utils;

import net.java.quickcheck.Generator;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.Test;
import org.turner.otp.generators.RandomLengthByteArrayGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Performs property-based testing on the HexEncoder.
 *
 * @author turner
 * @since 1.0
 */
@RunWith(Parameterized.class)
public class RandomisedHexEncoderTest {

    private static final int TEST_SIZE = 200;
    private static final Generator<byte[]> BYTE_ARRAY_GENERATOR
            = new RandomLengthByteArrayGenerator(TEST_SIZE);
    private static final Pattern HEXIT_PATTERN
            = Pattern.compile("[0-9a-zA-Z]*");

    private byte[] testArray;

    @Parameterized.Parameters
    public static List<Object[]> parameters() {
        Object[][] testParams = new Object[TEST_SIZE][1];
        for (int i = 0; i < TEST_SIZE; i++) {
            testParams[i] = new Object[] { BYTE_ARRAY_GENERATOR.next() };
        }
        return Arrays.asList(testParams);
    }

    public RandomisedHexEncoderTest(final byte[] testArray) {
        this.testArray = testArray;
    }

    @Test
    public void encodeLengthTest() {
        String encode = HexEncoder.encode(testArray);
        Assert.assertNotNull(encode);
        Assert.assertEquals(testArray.length * 2, encode.length());
    }

    @Test
    public void encodeDecodeInversesTest() {
        Assert.assertArrayEquals(
                testArray,
                HexEncoder.decode(HexEncoder.encode(testArray)));
    }

    @Test
    public void encodeProducesHexits() {
        Assert.assertTrue(
                HEXIT_PATTERN.matcher(HexEncoder.encode(testArray)).matches());
    }

    @Test
    public void encodeProducesEvenLengthStrings() {
        Assert.assertEquals(0, HexEncoder.encode(testArray).length() % 2);
    }
}
