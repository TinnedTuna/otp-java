package org.turner.opie;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.turner.opie.md5.MD5Generator;
import org.turner.opie.sha.SHAGenerator;
import org.turner.opie.utils.HexEncoder;
import org.turner.opie.utils.WordEncoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author turner
 */
@RunWith(Parameterized.class)
public class OPIERFCTest {

  private final OPIEGenerator opieGenerator;
  private final OPIESecretState opieSecretState;
  private final String expectedHexOutput;
  private final String expectedPasswordOutput;

  @Parameterized.Parameters
  public static Collection<Object[]>  rfcTestVectors() {
    return Arrays.asList(new Object[][] {
        // A list of test vectors, with format:
        // Algo, passphrase, seed, count, expected hex output, expected OTP.
        {"MD5", "This is a test.", "TeSt", 0, "9E876134D90499DD", "INCH SEA ANNE LONG AHEM TOUR"},
        {"MD5", "This is a test.", "TeSt", 1, "7965E05436F5029F", "EASE OIL FUM CURE AWRY AVIS"},
        {"MD5", "This is a test.", "TeSt", 99, "50FE1962C4965880", "BAIL TUFT BITS GANG CHEF THY"},
        {"MD5", "AbCdEfGhIjK", "alpha1", 0 ,"87066DD9644BF206", "FULL PEW DOWN ONCE MORT ARC"},
        {"MD5", "AbCdEfGhIjK", "alpha1", 1, "7CD34C1040ADD14B", "FACT HOOF AT FIST SITE KENT"},
        {"MD5", "AbCdEfGhIjK", "alpha1", 99, "5AA37A81F212146C", "BODE HOP JAKE STOW JUT RAP"}
    });
  }

  public OPIERFCTest(String hashAlgorithm, String password, String seed, int count,
                     String expectedHexOutput, String expectedPasswordOutput)
      throws NoSuchAlgorithmException, UnsupportedEncodingException {
    MessageDigest messageDigest;
    this.expectedHexOutput = expectedHexOutput;
    this.expectedPasswordOutput = expectedPasswordOutput;
    switch (hashAlgorithm) {
      case "MD5":
        this.opieGenerator = new MD5Generator();
        messageDigest = MessageDigest.getInstance(hashAlgorithm);
        break;
      case "SHA1":
        this.opieGenerator = new SHAGenerator();
        messageDigest = MessageDigest.getInstance(hashAlgorithm);
        break;
      default:
        throw new IllegalArgumentException("Cannot create OPIEGenerator with algo: " + hashAlgorithm);
    }
    this.opieSecretState = new OPIESecretState(
        password.getBytes("ASCII"),
        seed.toLowerCase().getBytes("ASCII"),
        count,
        messageDigest);
  }

  @Test
  public void rfcTestHexEncoded() {
    byte[] otpBytes = opieGenerator.generateOPIEBytes(opieSecretState);
    String otpHex = HexEncoder.encode(otpBytes);
    Assert.assertEquals(expectedHexOutput, otpHex.toUpperCase());
  }

  @Test
  public void rfcTestWordEncded() {
    byte[] otpBytes = opieGenerator.generateOPIEBytes(opieSecretState);
    String otpWords = WordEncoder.encode(otpBytes);
    Assert.assertEquals(expectedPasswordOutput, otpWords.toUpperCase());
  }
}
