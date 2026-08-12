# Security Audit Report

## Summary
A comprehensive security audit was performed on the repository using the `piolium` suite. The audit focused on cryptographic primitives, dependency management, and architectural integrity.

## Key Findings
- **Dependency Risk**: The `cryptography` library version in `python/requirements.txt` needs to be bumped to `>=44.0.1` to address known vulnerabilities in OpenSSL wheels.
- **HMAC Timing**: Verified that HMAC implementations should strictly use constant-time comparison (`hmac.compare_digest`) to prevent timing-based tag recovery.
- **PBKDF2 Validation**: Identified a need for stricter input validation on PBKDF2 salts and passwords to prevent signature spoofing.
- **Demo Script Integrity**: Verified that `demo_langs.sh` and `demo_langs.ps1` have been updated to execute the full suite of reference examples rather than simple "Hello World" checks.

## Audit Artifacts
- **SBOM**: Located in `piolium/attack-surface/sbom.json`.
- **Knowledge Base**: Located in `piolium/attack-surface/knowledge-base-report.md`.
- **Gatekeeper**: The ADR Gatekeeper is now active to maintain architectural governance.

## Audit Date
2026-08-12
