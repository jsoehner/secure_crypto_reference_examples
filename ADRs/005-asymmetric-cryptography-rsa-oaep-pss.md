# ADR 005: Asymmetric Cryptography with RSA-OAEP and RSA-PSS

## Status
Accepted

## Context
We need to perform asymmetric encryption and digital signatures using RSA.

## Decision
- **Encryption**: Use RSA-OAEP with SHA-256 and MGF1-SHA256.
- **Signatures**: Use RSA-PSS with SHA-256 and MGF1-SHA256.

## Rationale
- **OAEP**: RSA-OAEP is the standard for secure RSA encryption, protecting against various chosen-ciphertext attacks that affect older padding like PKCS#1 v1.5.
- **PSS**: RSA-PSS is the modern standard for RSA signatures, providing a probabilistic signature scheme that is more robust than the older PKCS#1 v1.5 signature scheme.
- **Key Size**: Use RSA-3072 keys to meet modern security requirements for longevity.

## Security Considerations
- **Padding**: Never use PKCS#1 v1.5 padding for encryption or signatures.
- **MGF1**: Ensure MGF1 uses the same hash function as the primary algorithm (SHA-256).
