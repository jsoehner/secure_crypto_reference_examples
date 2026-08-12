# ADR 008: Key Wrapping and Serialization

## Status
Accepted

## Context
We need to securely store or transmit cryptographic keys, either by wrapping them with another key or by serializing them to disk.

## Decision
- **Key Wrapping**: Use AES Key Wrap with padding for protecting key material under a Key Encryption Key (KEK).
- **Serialization**: Use PKCS#8 for private key serialization with `BestAvailableEncryption`.

## Rationale
- **AES Key Wrap**: A specialized algorithm for wrapping cryptographic keys, ensuring that the wrapped key is protected from manipulation.
- **PKCS#8**: The standard for private key information, providing a structured way to store keys and their associated metadata.
- **BestAvailableEncryption**: Uses the strongest available encryption algorithm provided by the underlying platform for serializing keys to disk.

## Security Considerations
- **KEK Protection**: The Key Encryption Key (KEK) must be stored securely (e.g., in a hardware security module, a secrets manager, or an encrypted file).
- **Password Strength**: If a password is used for serialization, it must be strong and managed securely.
