# ADR 004: Key Agreement with X25519 and HKDF

## Status
Accepted

## Context
We need a way for two parties to establish a shared secret over an insecure channel to derive symmetric keys.

## Decision
Use X25519 for Elliptic Curve Diffie-Hellman (ECDH) key agreement, followed by HKDF-SHA256 for key derivation.

## Rationale
- **X25519**: A modern, high-performance, and secure curve designed to be resistant to many implementation pitfalls.
- **HKDF**: Standard way to "extract and expand" a shared secret into one or more cryptographically strong keys. It ensures that the resulting keys are independent and have the correct entropy.
- **Security**: This combination is the gold standard for modern key exchange (used in Signal, TLS 1.3, etc.).

## Security Considerations
- **Shared Secret Handling**: The raw shared secret from X25519 should never be used directly as a key. It must always pass through a KDF like HKDF.
- **Salt and Info**: HKDF uses a salt (shared) and info (context-specific) to ensure that the derived keys are cryptographically distinct for different uses.
