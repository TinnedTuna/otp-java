package org.turner.oath.totp;

import javax.crypto.Mac;
import org.turner.oath.OATHSecretState;
import org.turner.oath.utils.AbstractOATHGenerator;

/**
 *
 * @author turner
 */
public class TOTPGenerator extends AbstractOATHGenerator {

  public TOTPGenerator(Mac mac) {
    super(mac);
  }

  @Override
  protected long getInternalState(final OATHSecretState secretState) {
    assert secretState instanceof TOTPSecretState;
    TOTPSecretState totpSecretState = (TOTPSecretState) secretState;
    return totpSecretState.getCurrentTimeStep();
  }
  
}
