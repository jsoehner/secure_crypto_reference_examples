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

## 🛡️ Security & Cryptographic Posture

This project tracks Software & Cryptographic Bill of Materials (SBOM & CBOM) and Post-Quantum Cryptography (PQC) readiness. See the latest [Cryptographic Audit & PQC Migration Report](docs/security/CRYPTOGRAPHIC_AUDIT.md).

## Automated SBOM & CBOM Supply Chain Pipeline

This repository incorporates an automated Software Bill of Materials (SBOM) and Cryptographic Bill of Materials (CBOM) supply chain pipeline to ensure complete component visibility, cryptographic transparency, and post-quantum migration readiness.

### Continuous Integration Workflow (`.github/workflows/sbom-cbom.yml`)

The automated CI workflow executes on pushes and pull requests to `main`/`master` and tag releases:
1. **Dual-Format SBOM Generation**: Anchore / Syft generates CycloneDX (`oss/sbom.cyclonedx.json`) and SPDX 2.3 (`oss/sbom.spdx.json`) dependency inventories.
2. **CBOM Generation**: `cbomkit-action` inspects the codebase to extract cryptographic assets into `oss/cbom.json`.
3. **Semantic AST Crypto Auditing**: `scripts/scan_crypto_ast.py` conducts deep Abstract Syntax Tree parsing across source files to detect cryptographic primitives, algorithm choices, and key usages, reconciling findings against the CBOM.
4. **Automated Verification Harness**: `scripts/test_boms.sh` enforces schema validity, file non-emptiness, and detection coverage.
5. **PQC Readiness Analysis**: `scripts/analyze_cbom.py` assesses classical vs. post-quantum readiness, generating markdown step summaries and pushing audit records to `docs/security/CRYPTOGRAPHIC_AUDIT.md`.
6. **Artifact Archival**: All generated BOM files under `oss/` are published as workflow artifacts with 90-day retention.

### Local Script Tooling

Developers can inspect, generate, and test BOM artifacts locally using the provided scripts:

```bash
# Generate SBOM, CBOM, and run AST cryptographic scans locally into oss/
bash scripts/generate_boms.sh

# Run the automated verification test harness
bash scripts/test_boms.sh oss

# Analyze cryptographic posture and generate PQC readiness summaries
python3 scripts/analyze_cbom.py oss/cbom.json --sbom oss/sbom.cyclonedx.json --markdown oss/crypto-summary.md
```

### Architectural Decision Record

For detailed background, architectural rationale, and evaluation of alternatives, see [ADR 009: Automated SBOM & CBOM Supply Chain Governance](ADRs/009-automated-sbom-cbom-supply-chain-governance.md).

