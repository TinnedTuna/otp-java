package org.turner.opie;

import org.turner.oath.OATHSecretState;
import org.turner.opie.utils.OPIEUtils;

/**
 *
 * @author turner
 */
public class OPIEValidator {

  public static boolean validateOPIE(
          final String userSuppliedOPIE,
          final OPIESecretState secretState, 
          final OPIEGenerator opieGenerator) {
    assert userSuppliedOPIE != null;
    assert secretState != null;
    assert opieGenerator != null;
    byte[] generatedOPIE = opieGenerator.generateOPIEBytes(secretState);
    return OPIEUtils.constantTimeEquals(generatedOPIE, 
            OPIEUtils.wordsToBytes(userSuppliedOPIE));
  }

  
}
