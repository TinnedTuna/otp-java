package org.turner.opie.utils;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Test;

/**
 * Ensures that the HexEncoder can encode and decode data correctly.
 */
@RunWith(JUnit4.class)
public class HexEncoderTest {

    @Test
    public void testEncodeSimple() {
        String encode = HexEncoder.encode(new byte[]{~0x00, 0x00, 0x01});
        Assert.assertEquals(encode, "ff0001");
    }

    @Test
    public void testDecodeSimple() {
        byte[] encode = HexEncoder.decode("ff0001");
        Assert.assertArrayEquals(encode, new byte[]{~0x00, 0x00, 0x01});
    }
}
