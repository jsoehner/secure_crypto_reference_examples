# Secure Cryptography Reference Examples

These reference examples were derived from the C# Bouncy Castle demonstrations listed in the Bouncy Castle section of [Asecuritysite](https://asecuritysite.com/index/#bouncy123), ending at the next section boundary; AI was used to analyse the demonstrated cryptographic capabilities and generate functionally similar Java implementations using standard JCA/JCE APIs and Python implementations using the `cryptography` package, while deliberately replacing educational or legacy patterns with modern secure defaults such as SHA-256, HMAC-SHA256, PBKDF2-HMAC-SHA256, HKDF-SHA256, AES-GCM, RSA-OAEP, RSA-PSS, Ed25519, X25519 and AES key wrapping.

## Architecture Decision Records (ADRs)
Detailed explanations of the cryptographic choices and security considerations can be found in the [ADRs](./ADRs) directory.

## Contents

- `java/`: complete Java 17+ examples using standard JCA/JCE.
- `python/`: complete Python 3.10+ examples using `cryptography`.
- Each file is independently runnable and rejects tampering or invalid inputs where applicable.

## Java

```bash
cd java
./run_all.sh
```

On Windows, compile from the `java` directory:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
javac -d out (Get-ChildItem -Recurse src/main/java -Filter *.java).FullName
Get-ChildItem src/main/java/org/example/crypto/*Example.java | ForEach-Object {
    java -cp out "org.example.crypto.$($_.BaseName)"
}
```

## Python

```bash
cd python
python -m pip install -r requirements.txt
python run_all.py
```

## Tests

```bash
cd python
python test_crypto.py
```

## Production-use notes

- Keep keys in a KMS, HSM, platform keystore or secrets manager rather than source code.
- Bind algorithm version, key identifier and business context as AES-GCM associated data.
- Never reuse an AES-GCM nonce with the same key.
- Treat authentication failures as security events; do not return unauthenticated plaintext.
- Calibrate password-KDF work factors for the deployment environment and record the parameters with the salt.
- Use hybrid encryption for large payloads; RSA-OAEP is intended for small values such as content-encryption keys.

## Security Audit
A full security audit and analysis can be found in [SECURITY_AUDIT.md](./SECURITY_AUDIT.md).
