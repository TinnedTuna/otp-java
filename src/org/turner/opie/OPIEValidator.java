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
            OPIEUtils.fromDictionaryWords(userSuppliedOPIE));
  }
  
  public static OPIESecretState generateNextState(
          final OPIESecretState opieSecretState,
          final OPIEGenerator opieGenerator) {
    assert opieSecretState != null;
    assert opieGenerator != null;
    byte[] nextSecret = opieGenerator.generateOPIEBytes(opieSecretState);
    return new OPIESecretState(
            nextSecret, 
            opieSecretState.getSeed(), 
            opieSecretState.getHashCounts() - 1, 
            opieSecretState.getMessageDigest());
  }
  
}
