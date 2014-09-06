# OTP Java

This is for producing and verifying one-time passwords according to RFC 2289,
RFC 6238 and RFC 4226.

# Stability

This software is very alpha -- there's not even a test rig yet!

# Building

There's currently an ant script:

```
ant dist
```

Will get you a jar

# Usage

## OATH

To generate OTPs in your java app, and verify them, you'll need to instantiate 
the relevant MAC algorithm, and use it to construct an OATHGenerator.

The generator can be used with a secret state. When generating new secret 
states for user enrollment, be sure to provide enough bits of entropy and to 
use a SecureRandom, just just a plain Random.

For validation, you should take the user details from your data store,
instantiate a OATHSecretState and the relevant OATHGenerator, and use the 
OATHValidator to do all the heavy lifting.

## OPIE Passwords

OPIE Passwords are very similar as above.

## What We Don't Do

We do not provide randomness! Use SecureRandom for that.
