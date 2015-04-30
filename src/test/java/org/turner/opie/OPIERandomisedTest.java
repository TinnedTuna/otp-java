package org.turner.opie;

import net.java.quickcheck.generator.iterable.Iterables;
import net.java.quickcheck.generator.support.ByteArrayGenerator;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.turner.opie.md5.MD5Generator;
import org.turner.opie.sha.SHAGenerator;
import org.turner.opie.utils.OPIEUtils;
import org.turner.opie.utils.WordEncoder;
import org.turner.otp.generators.FixedLengthByteArrayGenerator;


/**
 *
 * @author turner
 */
@RunWith(JUnit4.class)
public class OPIERandomisedTest {
  
  @Test
  public void wordEncoder() {
    for (byte[] bytes : Iterables.toIterable(new ByteArrayGenerator())) {
      Assert.assertTrue(
              obviousEquals(
                  bytes,
                  WordEncoder.decode(WordEncoder.encode(bytes))));
    }
  }

// Commented out until we have a better way for generating OPIEs.
//  @Test
//  public void sizeOfSHAOTP() throws NoSuchAlgorithmException {
//    OPIEGenerator opieGenerator = new SHAGenerator();
//    for (OPIESecretState state : Iterables.toIterable(new OPIEStateGenerator(MessageDigest.getInstance("SHA-1")))) {
//      String generatedOTP = opieGenerator.generateOPIEString(state);
//      Assert.assertTrue(generatedOTP.length() <= 30);
//      String[] split = generatedOTP.split(" ");
//      Assert.assertEquals(6, split.length);
//    }
//  }
//
//  @Test
//  public void sizeOfMD5OTP() throws NoSuchAlgorithmException {
//    OPIEGenerator opieGenerator = new MD5Generator();
//    for (OPIESecretState state : Iterables.toIterable(new OPIEStateGenerator(MessageDigest.getInstance("MD5")))) {
//      String generatedOTP = opieGenerator.generateOPIEString(state);
//      Assert.assertTrue(generatedOTP.length() <= 30);
//      String[] split = generatedOTP.split(" ");
//      Assert.assertEquals(6, split.length);
//    }
//  }
//
  @Test
  public void foldTo64BitsSHA1() {
    SHAGenerator shaGenerator = new SHAGenerator();
    for (byte[] inputBytes : Iterables.toIterable(new FixedLengthByteArrayGenerator(20))) {
      byte[] foldTo64Bits = shaGenerator.foldTo64Bits(inputBytes);
      Assert.assertEquals(8, foldTo64Bits.length);
    }
  }
  
  @Test
  public void foldTo64BitsMD5() {
    MD5Generator md5Generator = new MD5Generator();
    for (byte[] inputBytes : Iterables.toIterable(new FixedLengthByteArrayGenerator(16))) {
      byte[] foldTo64Bits = md5Generator.foldTo64Bits(inputBytes);
      Assert.assertEquals(8, foldTo64Bits.length);
    }
  }
  
  @Test
  public void constantTimeEquals() {
    for (byte[] left : Iterables.toIterable(new ByteArrayGenerator())) {
      for (byte[] right : Iterables.toIterable(new ByteArrayGenerator())) {
        if (obviousEquals(left, right)) {
          Assert.assertTrue(OPIEUtils.constantTimeEquals(left, right));
        } else {
          Assert.assertFalse(OPIEUtils.constantTimeEquals(left, right));
        }
      }
    }
  }
  
  private static boolean obviousEquals(final byte[] left, final byte[] right) {
    assert left != null;
    assert right != null;
    
    if (left.length != right.length) {
      return false;
    }
    
    for (int i = 0; i < left.length; i++) {
      if (left[i] != right[i]) {
        return false;
      }
    }
    return true;
  }
}
