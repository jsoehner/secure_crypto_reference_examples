# ADR 007: Message Authentication with HMAC

## Status
Accepted

## Context
We need to ensure the integrity and authenticity of a message using a shared secret.

## Decision
Use HMAC-SHA256 for message authentication.

## Rationale
- **Integrity**: HMAC ensures that the message has not been modified in transit.
- **Authenticity**: HMAC proves that the message was created by someone who possesses the shared secret.
- **Standardization**: HMAC-SHA256 is a standard and well-vetted construction.

## Security Considerations
- **Constant-Time Comparison**: Verification of the HMAC tag MUST be done using a constant-time comparison function to prevent timing attacks.
- **Key Strength**: Use a sufficiently long, random secret key (e.g., 256 bits).
