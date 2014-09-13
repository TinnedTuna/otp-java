package org.turner.oath.totp;

import java.math.BigInteger;
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

/**
 *
 * @author turner
 */
@RunWith(Parameterized.class)
public class TOTPRFCTest {
  
  private static final long TIME_STEP = 30;
  private static final long INITIAL_UNIX_TIME = 0;
  
  @Parameters
  public static Collection<Object[]> rfcTestVectors() {
    return Arrays.asList(new Object[][] {
      {"HmacSHA1", "3132333435363738393031323334353637383930", 59L, "94287082" },
      {"HmacSHA256", "3132333435363738393031323334353637383930704313233343536373839303132", 59L, "46119246"},
      {"HmacSHA512", "31323334353637383930313233343536373839307073132333435363738393031323334353637383930708313233343536373839303132333435363738393070931323334", 59L, "90693936"},
      {"HmacSHA1", "3132333435363738393031323334353637383930", 1111111109L, "07081804"},
      {"HmacSHA256", "3132333435363738393031323334353637383930704313233343536373839303132", 1111111109L, "68084774"},
      {"HmacSHA512", "31323334353637383930313233343536373839307073132333435363738393031323334353637383930708313233343536373839303132333435363738393070931323334", 1111111109L, "25091201"},
      {"HmacSHA1", "3132333435363738393031323334353637383930", 1111111111L, "14050471"},
      {"HmacSHA256", "3132333435363738393031323334353637383930704313233343536373839303132", 1111111111L, "67062674"},
      {"HmacSHA512", "31323334353637383930313233343536373839307073132333435363738393031323334353637383930708313233343536373839303132333435363738393070931323334", 1111111111L, "99943326"},
      {"HmacSHA1", "3132333435363738393031323334353637383930", 1234567890L, "89005924"},
      {"HmacSHA256", "3132333435363738393031323334353637383930704313233343536373839303132", 1234567890L, "91819424"},
      {"HmacSHA512", "31323334353637383930313233343536373839307073132333435363738393031323334353637383930708313233343536373839303132333435363738393070931323334", 1234567890L, "93441116"},
      {"HmacSHA1", "3132333435363738393031323334353637383930", 2000000000L, "69279037"},
      {"HmacSHA256", "3132333435363738393031323334353637383930704313233343536373839303132", 2000000000L, "90698825"},
      {"HmacSHA512", "31323334353637383930313233343536373839307073132333435363738393031323334353637383930708313233343536373839303132333435363738393070931323334", 2000000000L, "38618901"},
      {"HmacSHA1", "3132333435363738393031323334353637383930", 20000000000L, "65353130"},
      {"HmacSHA256", "3132333435363738393031323334353637383930704313233343536373839303132", 20000000000L, "77737706"},
      {"HmacSHA512", "31323334353637383930313233343536373839307073132333435363738393031323334353637383930708313233343536373839303132333435363738393070931323334", 20000000000L, "47863826"}
    });
  }
  
  private String algorithmName;
  private byte[] seed;
  private long currentUnixTime;
  private String expectedOtp;
  
  public TOTPRFCTest(String algorithmName, String seed, long currentUnixTime, String expectedOtp) {
    this.algorithmName = algorithmName;
    this.seed = hexStr2Bytes(seed);
    this.currentUnixTime = currentUnixTime;
    this.expectedOtp = expectedOtp;
  }
  
  @Test
  public void rfcVector() throws NoSuchAlgorithmException {
    OATHGenerator totpGenerator = getTOTPGenerator(algorithmName);
    String generateOtp = totpGenerator.generateOtp(getOATHSecretState(seed, currentUnixTime));
    Assert.assertEquals(expectedOtp, generateOtp);
  }
  
  private static OATHGenerator getTOTPGenerator(String algorithmName) throws NoSuchAlgorithmException {
    Mac sha1Mac = Mac.getInstance(algorithmName);
    return new TOTPGenerator(sha1Mac);
  }
  
  private static OATHSecretState getOATHSecretState(byte[] seed, long currentTime) {
    return new TOTPSecretState(seed, 8, TIME_STEP, INITIAL_UNIX_TIME, currentTime);
  }
  
  /**
   * As in RFC6238, under a BSD-style license.
   * 
   * @param hex
   * @return 
   */
  private static byte[] hexStr2Bytes(String hex) {
    // Adding one byte to get the right conversion
    // Values starting with "0" can be converted
    byte[] bArray = new BigInteger("10" + hex, 16).toByteArray();

    // Copy all the REAL bytes, not the "first"
    byte[] ret = new byte[bArray.length - 1];
    for (int i = 0; i < ret.length; i++) {
      ret[i] = bArray[i + 1];
    }
    return ret;
  }
}
