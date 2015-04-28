package org.turner.oath.totp;

import javax.crypto.Mac;

import org.turner.oath.OATHSecretState;
import org.turner.oath.utils.AbstractOATHGenerator;
import org.turner.oath.utils.OATHUtils;

/**
 * Generator for TOTP one-time passwords.
 *
 * @author turner
 * @since 1.0
 */
public class TOTPGenerator extends AbstractOATHGenerator {

    /**
     * Simply calls super.
     *
     * @param mac The MAC algorithm to use.
     */
    public TOTPGenerator(final Mac mac) {
        super(mac);
    }

    @Override
    protected final byte[] getInternalState(final OATHSecretState secretState) {
        assert secretState instanceof TOTPSecretState;
        TOTPSecretState totpSecretState = (TOTPSecretState) secretState;
        return OATHUtils.longBytes(totpSecretState.getCurrentTimeStepValue());
    }

}
