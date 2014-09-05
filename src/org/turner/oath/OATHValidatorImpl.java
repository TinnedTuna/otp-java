package org.turner.oath;

import org.turner.oath.OATHGenerator;
import org.turner.oath.OATHSecretState;
import org.turner.oath.OATHValidator;
import org.turner.oath.utils.OATHUtils;

/**
 *
 * @author turner
 */
public class OATHValidatorImpl implements OATHValidator {
  
  public boolean validateOtp(
          final String userSuppliedOtp,
          final OATHSecretState secretState, 
          final OATHGenerator oathGenerator) {
    assert userSuppliedOtp != null;
    assert secretState != null;
    assert oathGenerator != null;
    
    if (userSuppliedOtp.length() != secretState.getLength()) {
      return false;
    }
    
    String generatedOtp = oathGenerator.generateOtp(secretState);
    
    return OATHUtils.constantTimeEquals(userSuppliedOtp, generatedOtp);
  }
  
}
