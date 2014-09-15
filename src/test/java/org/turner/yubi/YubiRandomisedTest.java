package org.turner.yubi;

import java.security.SecureRandom;
import net.java.quickcheck.generator.iterable.Iterables;
import net.java.quickcheck.generator.support.ByteArrayGenerator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.turner.yubi.utils.YubiUtils;

/**
 *
 * @author turner
 */
@RunWith(JUnit4.class)
public class YubiRandomisedTest {
  
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
    for (byte[] inputBytes : Iterables.toIterable(new ByteArrayGenerator())) {
      YubiSecretState yubiSecretState = new YubiSecretState();
      String generateOtp = YubiGenerator.generateOtp(yubiSecretState);
      Assert.assertEquals(32, generateOtp.length());
    }
  }
  
  @Test
  public void yubiKeyModHexOutput() {
    for (byte[] inputBytes : Iterables.toIterable(new ByteArrayGenerator())) {
      YubiSecretState yubiSecretState = new YubiSecretState();
      String generateOtp = YubiGenerator.generateOtp(yubiSecretState);
      Assert.assertTrue(YubiUtils.isModHex(generateOtp));
    }
  }
  
  @Test
  public void generatedOtpsValidate() {
    SecureRandom secureRandom = new SecureRandom();
    for (byte[] inputBytes : Iterables.toIterable(new ByteArrayGenerator())) {
      secureRandom.setSeed(inputBytes);
      YubiSecretState yubiSecretState = YubiStateManager.generateNewState(secureRandom);
      String generateOtp = YubiGenerator.generateOtp(yubiSecretState);
      Assert.assertTrue(YubiValidator.validateOtp(generateOtp, yubiSecretState));
    }
  }
  
  
}
