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

OPIE Passwords are very similar to above.

## What We Don't Do

We do not provide randomness! Use SecureRandom for that.

# Contributing

We welcome any contributions, however, in order to ensure that users of this
library can maintain a high level of confidence, we ensure that the code
quality is very high, and enforced.

Many of these requirements are enforced automatically, and a simple way to
ensure that all goes to plan is to simply run the build before making a pull
request. If that build fails for any reason, it's likely that the pull request
will be rejected without even reading the code.

## Building

We currently use Maven 3 to build the source:

```
mvn clean install
```

Will run checkstyle, FindBugs, instrument and run your tests, and perform a
cobertura check. If all of those pass with no warnings, a JAR file will be
produced in target/

## Requirements

Any new one-time password algorithms must be accompanied with the relevant
standards documentation.

You must consider the licensing implications of this.

## Testing

All code must have both boundary-value analysis testing and randomised testing.

Branch coverage, as reported by cobertura must be in excess of 95%. Any
violations of this standard will be rejected.

## Static Analysis

We use FindBugs for static analysis. Any warnings from FindBugs must be
eliminated.

## Code Style.

All contributions must adhere to the standard checkstyle rules. Any violations
will be rejected.

## Code Review

All contributions will be reviewed prior to merge. Reviewers may have questions
or suggestions regarding your contributions. Failure to respond to these
questions in a timely manner may have one of two results:

  * Your code will be re-written or altered by other people before the merge.
  * Your contribution will be rejected.

Please try to ensure that your contributions are small and simple enough to be
reviewed quickly and effectively.
