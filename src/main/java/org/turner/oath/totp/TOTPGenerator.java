package org.turner.oath.totp;

import javax.crypto.Mac;
import org.turner.oath.OATHSecretState;
import org.turner.oath.utils.AbstractOATHGenerator;
import org.turner.oath.utils.OATHUtils;

/**
 *
 * @author turner
 */
public class TOTPGenerator extends AbstractOATHGenerator {

  public TOTPGenerator(Mac mac) {
    super(mac);
  }

  @Override
  protected byte[] getInternalState(final OATHSecretState secretState) {
    assert secretState instanceof TOTPSecretState;
    TOTPSecretState totpSecretState = (TOTPSecretState) secretState;
    return OATHUtils.longBytes(totpSecretState.getTimeStepValue());
  }
  
}
