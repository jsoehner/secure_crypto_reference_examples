# ADR 003: Password Hashing with PBKDF2

## Status
Accepted

## Context
Storing passwords requires a one-way hashing function that is resistant to brute-force and dictionary attacks. Simple hashes like SHA-256 are too fast and can be easily cracked.

## Decision
Use PBKDF2-HMAC-SHA256 for password hashing.

## Rationale
- **Work Factor**: PBKDF2 allows us to increase the cost of hashing via an iteration count, making brute-force attacks computationally expensive.
- **Salting**: We use a unique 16-byte salt for every password to prevent rainbow table attacks.
- **Standardization**: PBKDF2 is widely supported and a standard for password storage.

## Security Considerations
- **Iteration Count**: We use 600,000 iterations, which is high enough to provide significant protection against modern hardware while remaining acceptable for user login latency.
- **Salt Uniqueness**: Every password must have a unique, random salt.
- **Memory Safety**: Passwords are cleared from memory (in Java) as soon as they are no longer needed.
