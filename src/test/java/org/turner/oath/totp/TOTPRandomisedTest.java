package org.turner.oath.totp;

import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.Mac;
import net.java.quickcheck.generator.iterable.Iterables;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.turner.oath.OATHGenerator;
import org.turner.oath.OATHValidator;
import org.turner.otp.generators.TOTPStateGenerator;

/**
 *
 * @author turner
 */
@RunWith(JUnit4.class)
public class TOTPRandomisedTest {
  
  @Test
  public void lengthProperty() throws NoSuchAlgorithmException {
    OATHGenerator oathGenerator = new TOTPGenerator(Mac.getInstance("HmacSHA1"));
    for (TOTPSecretState secretState : Iterables.toIterable(new TOTPStateGenerator())) {
      String generateOtp = oathGenerator.generateOtp(secretState);
      Assert.assertEquals(secretState.getLength(), generateOtp.length());
    }
  }
  
  @Test
  public void allIntegers() throws NoSuchAlgorithmException {
    Pattern digitsMatcher = Pattern.compile("\\d{6,8}");
    OATHGenerator oathGenerator = new TOTPGenerator(Mac.getInstance("HmacSHA1"));
    for (TOTPSecretState secretState : Iterables.toIterable(new TOTPStateGenerator())) {
      String generatedOtp = oathGenerator.generateOtp(secretState);
      Matcher matcher = digitsMatcher.matcher(generatedOtp);
      Assert.assertTrue(matcher.matches());
    }
  }
  
  @Test
  public void totpValidates() throws NoSuchAlgorithmException {
  OATHGenerator oathGenerator = new TOTPGenerator(Mac.getInstance("HmacSHA1"));
    for (TOTPSecretState secretState : Iterables.toIterable(new TOTPStateGenerator())) {
      String generatedOtp = oathGenerator.generateOtp(secretState);
      Assert.assertTrue(OATHValidator.validateOtp(generatedOtp, secretState, oathGenerator));
    }
  }
}
