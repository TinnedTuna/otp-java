package org.turner.oath.totp;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collection;
import javax.crypto.Mac;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.turner.oath.OATHGenerator;
import org.turner.oath.OATHSecretState;
import org.turner.oath.TestUtils;
import org.turner.oath.utils.OATHUtils;

/**
 *
 * @author turner
 */
@RunWith(Parameterized.class)
public class TOTPRFCTest {
  
  private static final byte[] SHA1_SEED = TestUtils.hexStringToBytes("3132333435363738393031323334353637383930");
  private static final byte[] SHA256_SEED = TestUtils.hexStringToBytes("3132333435363738393031323334353637383930313233343536373839303132");
  private static final byte[] SHA512_SEED = TestUtils.hexStringToBytes("31323334353637383930313233343536373839303132333435363738393031323334353637383930313233343536373839303132333435363738393031323334");
  
  @Parameters
  public static Collection<Object[]> rfcTestVectors() {
    return Arrays.asList(new Object[][] {
      {"HmacSHA1", SHA1_SEED, 59L, "94287082" },
      {"HmacSHA256", SHA256_SEED, 59L, "46119246"},
      {"HmacSHA512", SHA512_SEED, 59L, "90693936"},
      {"HmacSHA1", SHA1_SEED, 1111111109L, "07081804"},
      {"HmacSHA256", SHA256_SEED, 1111111109L, "68084774"},
      {"HmacSHA512", SHA512_SEED, 1111111109L, "25091201"},
      {"HmacSHA1", SHA1_SEED, 1111111111L, "14050471"},
      {"HmacSHA256", SHA256_SEED, 1111111111L, "67062674"},
      {"HmacSHA512", SHA512_SEED, 1111111111L, "99943326"},
      {"HmacSHA1", SHA1_SEED, 1234567890L, "89005924"},
      {"HmacSHA256", SHA256_SEED, 1234567890L, "91819424"},
      {"HmacSHA512", SHA512_SEED, 1234567890L, "93441116"},
      {"HmacSHA1", SHA1_SEED, 2000000000L, "69279037"},
      {"HmacSHA256", SHA256_SEED, 2000000000L, "90698825"},
      {"HmacSHA512", SHA512_SEED, 2000000000L, "38618901"},
      {"HmacSHA1", SHA1_SEED, 20000000000L, "65353130"},
      {"HmacSHA256", SHA256_SEED, 20000000000L, "77737706"},
      {"HmacSHA512", SHA512_SEED, 20000000000L, "47863826"}
    });
  }
  
  private String algorithmName;
  private byte[] secret;
  private long currentUnixTime;
  private String expectedOtp;
  
  public TOTPRFCTest(String algorithmName, byte[] secret, long currentUnixTime, String expectedOtp) {
    this.algorithmName = algorithmName;
    assert secret != null;
    assert secret.length != 0;
    this.secret = secret;
    this.currentUnixTime = currentUnixTime;
    this.expectedOtp = expectedOtp;
  }
  
  @Test
  public void rfcVector() throws NoSuchAlgorithmException {
    OATHGenerator totpGenerator = getTOTPGenerator(algorithmName);
    String generateOtp = totpGenerator.generateOtp(getOATHSecretState(secret, 0, currentUnixTime, 30));
    Assert.assertEquals(expectedOtp, generateOtp);
  }
  
  private static OATHGenerator getTOTPGenerator(String algorithmName) throws NoSuchAlgorithmException {
    Mac sha1Mac = Mac.getInstance(algorithmName);
    return new TOTPGenerator(sha1Mac);
  }
  
  private static OATHSecretState getOATHSecretState(
          byte[] seed, 
          long initialUnixTime,
          long currentTime,
          long timeStep) {
    return new TOTPSecretState(seed, 8, initialUnixTime, currentTime, timeStep);
  }
}
