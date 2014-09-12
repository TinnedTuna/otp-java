package org.turner.opie;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import net.java.quickcheck.generator.iterable.Iterables;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.turner.opie.md5.MD5Generator;
import org.turner.opie.sha.SHAGenerator;
import org.turner.opie.utils.OPIEUtils;
import org.turner.otp.generators.FixedLengthByteArrayGenerator;
import org.turner.otp.generators.OPIEStateGenerator;


/**
 *
 * @author turner
 */
@RunWith(JUnit4.class)
public class OPIERandomisedTest {
  
  @Test
  public void bytesToWords() {
    for (byte[] bytes : Iterables.toIterable(new FixedLengthByteArrayGenerator(8))) {
      Assert.assertEquals(bytes, 
              OPIEUtils.wordsToBytes(
              OPIEUtils.bytesToWords(bytes)
              )
              );
    }
  }
  
  @Test
  public void sizeOfSHAOTP() throws NoSuchAlgorithmException {
    OPIEGenerator opieGenerator = new SHAGenerator();
    for (OPIESecretState state : Iterables.toIterable(new OPIEStateGenerator(MessageDigest.getInstance("SHA-1")))) {
      String generatedOTP = opieGenerator.generateOPIEString(state);
      Assert.assertTrue(generatedOTP.length() <= 30);
      String[] split = generatedOTP.split(" ");
      Assert.assertEquals(6, split.length);
    }
  }
  
    
  @Test
  public void sizeOfMD5OTP() throws NoSuchAlgorithmException {
    OPIEGenerator opieGenerator = new MD5Generator();
    for (OPIESecretState state : Iterables.toIterable(new OPIEStateGenerator(MessageDigest.getInstance("MD5")))) {
      String generatedOTP = opieGenerator.generateOPIEString(state);
      Assert.assertTrue(generatedOTP.length() <= 30);
      String[] split = generatedOTP.split(" ");
      Assert.assertEquals(6, split.length);
    }
  }
}
