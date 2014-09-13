package org.turner.oath.hotp;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collection;
import javax.crypto.Mac;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.turner.oath.OATHGenerator;
import org.turner.oath.OATHSecretState;
import org.turner.oath.OATHValidator;
import org.turner.oath.TestUtils;

/**
 *
 * @author turner
 */
@RunWith(Parameterized.class)
public class HOTPRFCTest {
  
  private static final byte[] SECRET = TestUtils.hexStringToBytes("3132333435363738393031323334353637383930");
  private static final int OTP_LENGTH = 6;
  private static final Mac macAlgorithm;
  
  static {
    try {
      macAlgorithm = Mac.getInstance("HmacSHA1");
    } catch (NoSuchAlgorithmException ex) {
      throw new IllegalStateException("Could not find HMAC-SHA-1 Algorithm");
    }
  }
  
  @Parameterized.Parameters
  public static Collection<Object[]> rfcTestVectors() {
    return Arrays.asList(new Object[][] {
      {0, "755224"},
      {1, "287082"},
      {2, "359152"},
      {3, "969429"},
      {4, "338314"},
      {5, "254676"},
      {6, "287922"},
      {7, "162583"},
      {8, "399871"},
      {9, "520489"}
    });
  }

  private final int count;
  private final String expectedOtp;
  
  public HOTPRFCTest(
          final int count,
          final String expectedOtp) {
    assert count >= 0;
    assert expectedOtp != null;
    assert expectedOtp.length() == OTP_LENGTH;
    this.count = count;
    this.expectedOtp = expectedOtp;
  }
  
  @Test
  public void rfcValidates() {
    OATHGenerator oathGenerator = new HOTPGenerator(macAlgorithm);
    OATHSecretState hotpSecretState = new HOTPSecretState(SECRET, OTP_LENGTH, count);
    Assert.assertTrue(
            OATHValidator.validateOtp(
              expectedOtp,
              hotpSecretState, 
              oathGenerator));
  }
  
  @Test
  public void canGenerateRfcValue() {
    OATHGenerator oathGenerator = new HOTPGenerator(macAlgorithm);
    OATHSecretState hotpSecretState = new HOTPSecretState(SECRET, OTP_LENGTH, count);
    String generateOtp = oathGenerator.generateOtp(hotpSecretState);
    Assert.assertEquals(expectedOtp, generateOtp);
  }
          
  
}
