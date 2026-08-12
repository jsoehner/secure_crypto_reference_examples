# ADR 002: Authenticated Encryption with AES-GCM

## Status
Accepted

## Context
We need a way to provide confidentiality (encryption) and integrity (authentication) for data. Traditional modes like AES-CBC provide confidentiality but are vulnerable to padding oracle attacks and do not provide integrity.

## Decision
Use AES-GCM (Galois/Counter Mode) for all symmetric authenticated encryption.

## Rationale
- **Authenticated Encryption (AEAD)**: AES-GCM provides both confidentiality and integrity in a single operation.
- **Performance**: AES-GCM is highly efficient and often hardware-accelerated.
- **Standardization**: It is the standard for TLS 1.3 and other modern protocols.
- **Security**: It prevents tampering with the ciphertext, as any modification will result in a decryption failure (InvalidTag).

## Security Considerations
- **Nonce Uniqueness**: Nonces must NEVER be reused with the same key. We use 96-bit (12-byte) nonces generated via CSPRNGs.
- **AAD**: Support for Additional Authenticated Data (AAD) allows binding the ciphertext to context (e.g., headers, IDs).
