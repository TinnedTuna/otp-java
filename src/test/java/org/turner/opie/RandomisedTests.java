package org.turner.opie;

import net.java.quickcheck.generator.iterable.Iterables;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.turner.opie.utils.OPIEUtils;
import org.turner.otp.tests.FixedLengthByteArrayGenerator;


/**
 *
 * @author turner
 */
@RunWith(JUnit4.class)
public class RandomisedTests {
  
  @Test
  public void bytesToWords() {
    for (byte[] bytes : Iterables.toIterable(new FixedLengthByteArrayGenerator(8))) {
      Assert.assertEquals(bytes, 
              OPIEUtils.wordsToBytes(
              OPIEUtils.bytesToWords(bytes)
              )
              );
    }
  }
}
