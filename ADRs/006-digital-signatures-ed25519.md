# ADR 006: Digital Signatures with Ed25519

## Status
Accepted

## Context
We need a modern, high-performance, and secure digital signature scheme.

## Decision
Use Ed25519 for digital signatures.

## Rationale
- **Performance**: Ed25519 is extremely fast and has small signature and key sizes.
- **Security**: It is designed to be resistant to many side-channel attacks and does not require high-quality random numbers during the signing process (unlike ECDSA).
- **Simplicity**: Ed25519 is easy to implement correctly and has been widely adopted in modern protocols.

## Security Considerations
- **Key Management**: Private keys must be kept secure and never shared.
- **Verification**: Always verify the signature against the expected public key and the original message.
