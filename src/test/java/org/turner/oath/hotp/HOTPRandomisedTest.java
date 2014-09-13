package org.turner.oath.hotp;

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
import org.turner.otp.generators.HOTPStateGenerator;

/**
 *
 * @author turner
 */
@RunWith(Parameterized.class)
public class HOTPRandomisedTest {
  
  @Parameterized.Parameters
  public static Collection<Object[]> hmacAlgorithms() throws NoSuchAlgorithmException {
    return Arrays.asList(new Object[][] {
      { Mac.getInstance("HmacSHA1") },
      { Mac.getInstance("HmacSHA256") },
      { Mac.getInstance("HmacSHA512") }
    });
  }
  
  public final Mac macAlgorithm;
  
  public HOTPRandomisedTest(final Mac macAlgorithm) {
    assert macAlgorithm != null;
    this.macAlgorithm = macAlgorithm;
  }
  
  @Test
  public void hotpLengthProperty() throws NoSuchAlgorithmException {
    OATHGenerator oathGenerator = new HOTPGenerator(macAlgorithm);
    for (HOTPSecretState secretState : Iterables.toIterable(new HOTPStateGenerator())) {
      String generateOtp = oathGenerator.generateOtp(secretState);
      Assert.assertEquals(secretState.getLength(), generateOtp.length());
    }
  }
  
  @Test
  public void hotpAllIntegers() throws NoSuchAlgorithmException {
    Pattern digitsMatcher = Pattern.compile("\\d{6,8}");
    OATHGenerator oathGenerator = new HOTPGenerator(macAlgorithm);
    for (HOTPSecretState secretState : Iterables.toIterable(new HOTPStateGenerator())) {
      String generatedOtp = oathGenerator.generateOtp(secretState);
      Matcher matcher = digitsMatcher.matcher(generatedOtp);
      Assert.assertTrue(matcher.matches());
    }
  }
  
  @Test
  public void hotpValidates() throws NoSuchAlgorithmException {
  OATHGenerator oathGenerator = new HOTPGenerator(macAlgorithm);
    for (HOTPSecretState secretState : Iterables.toIterable(new HOTPStateGenerator())) {
      String generatedOtp = oathGenerator.generateOtp(secretState);
      Assert.assertTrue(OATHValidator.validateOtp(generatedOtp, secretState, oathGenerator));
    }
  }
  
  @Test
  public void modifiedTotpDoesNotValidate() {
    OATHGenerator oathGenerator = new HOTPGenerator(macAlgorithm);
    for (HOTPSecretState secretState : Iterables.toIterable(new HOTPStateGenerator())) {
      String generatedOtp = oathGenerator.generateOtp(secretState);
      Assert.assertFalse(OATHValidator.validateOtp(generatedOtp+"0", secretState, oathGenerator));
    }
  }
  
}
