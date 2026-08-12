# Secure Crypto Reference Examples

This repository provides reference implementations and best practices for cryptographic operations in Python, Java, and PowerShell.

## Project Status

### Recent Updates
- **Python Dependencies**: Installed the `cryptography` library to support the Python reference examples.\n- **Java Path Resolution**: Updated `java/run_all.sh` to use absolute paths relative to the script location, ensuring consistent execution regardless of the working directory.\n- **Language Demo Fix**: Corrected `demo_langs.ps1` by removing the invalid `-ErrorAction` flag from the `javac` command, ensuring proper compilation and execution of Java examples.

## How to Run Demos

You can run the full suite of secure code examples for Python and Java using the provided scripts.

### Bash
```bash
./demo_langs.sh
```

### PowerShell
```powershell
.\demo_langs.ps1
```

The scripts will prompt you to choose which language(s) you want to demonstrate (python, java, or both).

## Secure Code Examples

Below is a snippet of the AES-256-GCM authenticated encryption example in both Python and Java.

### Python Example (`python/aes_gcm_example.py`)
```python
from cryptography.hazmat.primitives.ciphers.aead import AESGCM
from crypto_support import random_bytes

def encrypt(key: bytes, plaintext: bytes, aad: bytes | None) -> Envelope:
    nonce = random_bytes(12)
    return Envelope(nonce, AESGCM(key).encrypt(nonce, plaintext, aad))
```

### Java Example (`java/src/main/java/org/example/crypto/AesGcmExample.java`)
```java
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesGcmExample {
    public static byte[] encrypt(byte[] key, byte[] plaintext, byte[] aad) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        byte[] nonce = new byte[12];
        new SecureRandom().nextBytes(nonce);
        GCMParameterSpec spec = new GCMParameterSpec(128, nonce);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), spec);
        cipher.updateAAD(aad);
        return cipher.doFinal(plaintext);
    }
}
```
