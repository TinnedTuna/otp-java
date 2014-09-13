package org.turner.oath.totp;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.Mac;
import net.java.quickcheck.generator.iterable.Iterables;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.turner.oath.OATHGenerator;
import org.turner.oath.OATHValidator;
import org.turner.otp.generators.TOTPStateGenerator;

/**
 *
 * @author turner
 */
@RunWith(Parameterized.class)
public class TOTPRandomisedTest {
  
  @Parameterized.Parameters
  public static Collection<Object[]> hmacAlgorithms() throws NoSuchAlgorithmException {
    return Arrays.asList(new Object[][] {
      { Mac.getInstance("HmacSHA1") },
      { Mac.getInstance("HmacSHA256") },
      { Mac.getInstance("HmacSHA512") }
    });
  }

  public final Mac macAlgorithm;
  
  public TOTPRandomisedTest(final Mac macAlgorithm) {
    assert macAlgorithm != null;
    this.macAlgorithm = macAlgorithm;
  }
  
  @Test
  public void lengthProperty() {
    OATHGenerator oathGenerator = new TOTPGenerator(macAlgorithm);
    for (TOTPSecretState secretState : Iterables.toIterable(new TOTPStateGenerator())) {
      String generateOtp = oathGenerator.generateOtp(secretState);
      Assert.assertEquals(secretState.getLength(), generateOtp.length());
    }
  }
  
  @Test
  public void allIntegers() {
    Pattern digitsMatcher = Pattern.compile("\\d{6,8}");
    OATHGenerator oathGenerator = new TOTPGenerator(macAlgorithm);
    for (TOTPSecretState secretState : Iterables.toIterable(new TOTPStateGenerator())) {
      String generatedOtp = oathGenerator.generateOtp(secretState);
      Matcher matcher = digitsMatcher.matcher(generatedOtp);
      Assert.assertTrue(matcher.matches());
    }
  }
  
  @Test
  public void totpValidates() {
    OATHGenerator oathGenerator = new TOTPGenerator(macAlgorithm);
    for (TOTPSecretState secretState : Iterables.toIterable(new TOTPStateGenerator())) {
      String generatedOtp = oathGenerator.generateOtp(secretState);
      Assert.assertTrue(OATHValidator.validateOtp(generatedOtp, secretState, oathGenerator));
    }
  }
  
  @Test
  public void modifiedTotpDoesNotValidate() {
    OATHGenerator oathGenerator = new TOTPGenerator(macAlgorithm);
    for (TOTPSecretState secretState : Iterables.toIterable(new TOTPStateGenerator())) {
      String generatedOtp = oathGenerator.generateOtp(secretState);
      Assert.assertFalse(OATHValidator.validateOtp(generatedOtp+"0", secretState, oathGenerator));
    }
  }
}