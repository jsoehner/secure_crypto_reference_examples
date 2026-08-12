# ADR 001: Cryptographic Standards and Library Selection

## Status
Accepted

## Context
The project aims to provide a secure reference for common cryptographic operations. We need to select standard libraries and algorithms that are widely accepted, well-vetted, and provide strong security guarantees.

## Decision
- **Library**: Use the `cryptography` library for Python and standard JCA/JCE providers for Java. These are industry standards.
- **Algorithms**: Prioritize modern, high-security primitives (e.g., AES-GCM, X25519, Ed25519) over older, potentially weaker ones (e.g., AES-CBC, RSA-PKCS1v1.5).
- **Standardization**: Ensure that all implementations follow standard parameter choices (e.g., SHA-256, 256-bit keys) unless a specific use case requires otherwise.

## Consequences
- **Security**: Reduces the risk of using "homegrown" crypto or deprecated algorithms.
- **Maintainability**: Using standard libraries makes it easier for other developers to understand and maintain the code.
- **Portability**: Standard libraries are available across most platforms.
