package org.turner.oath.hotp;

import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import net.java.quickcheck.generator.iterable.Iterables;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.turner.oath.OATHGenerator;
import org.turner.otp.generators.HOTPStateGenerator;

/**
 *
 * @author turner
 */
@RunWith(JUnit4.class)
public class HOTPRandomisedTest {
  
  @Test
  public void lengthProperty() throws NoSuchAlgorithmException {
    OATHGenerator oathGenerator = new HOTPGenerator(Mac.getInstance("HmacSHA1"));
    for (HOTPSecretState secretState : Iterables.toIterable(new HOTPStateGenerator())) {
      String generateOtp = oathGenerator.generateOtp(secretState);
      Assert.assertTrue(generateOtp.length() >= 6);
      Assert.assertTrue(generateOtp.length() <= 8);
    }
  }
}
