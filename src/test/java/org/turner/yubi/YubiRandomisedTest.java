package org.turner.yubi;

import java.security.SecureRandom;
import net.java.quickcheck.generator.iterable.Iterables;
import net.java.quickcheck.generator.support.ByteArrayGenerator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.turner.otp.generators.YubiStateGenerator;
import org.turner.yubi.utils.YubiUtils;

/**
 *
 * @author turner
 */
@RunWith(JUnit4.class)
public class YubiRandomisedTest {
  
  final SecureRandom secureRandom = new SecureRandom();

  /**
   * Test that encoding then decoding a byte[] with modHex doesn't affect the
   * contents.
   */
  @Test
  public void modHexEncoding() {
    for (byte[] inputBytes : Iterables.toIterable(new ByteArrayGenerator())) {
      byte[] modHexToBytes = YubiUtils.modHexToBytes(YubiUtils.bytesToModHex(inputBytes));
      Assert.assertArrayEquals(inputBytes, modHexToBytes);
    }
  }
  
  @Test
  public void yubiKeyOtpSize() {
    for (YubiSecretState yubiSecretState : Iterables.toIterable(new YubiStateGenerator())) {
      String generateOtp = YubiGenerator.generateOtp(yubiSecretState);
      Assert.assertEquals(32, generateOtp.length());
    }
  }
  
  @Test
  public void yubiKeyModHexOutput() {
    for (YubiSecretState yubiSecretState : Iterables.toIterable(new YubiStateGenerator())) {
      String generateOtp = YubiGenerator.generateOtp(yubiSecretState);
      Assert.assertTrue(YubiUtils.isModHex(generateOtp));
    }
  }
  
  @Test
  public void generatedOtpsValidate() {
    for (YubiSecretState yubiSecretState : Iterables.toIterable(new YubiStateGenerator())) {
      String generateOtp = YubiGenerator.generateOtp(yubiSecretState);
      Assert.assertTrue(YubiValidator.validateOtp(generateOtp, yubiSecretState));
    }
  }
  
  
}
