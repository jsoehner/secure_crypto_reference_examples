# Secure Crypto Reference Examples

This repository provides reference implementations and best practices for cryptographic operations in Python, Java, and PowerShell.

## Project Status

### Recent Updates
- **ADR Gatekeeper**: Installed a new governance system to ensure all significant architectural decisions are documented via Architectural Decision Records (ADRs).
- **Skill Relocation**: Optimized the agent's skill set by moving all specialized procedures to the root `skills/` directory.
- **Language Demo Fix**: Corrected `demo_langs.ps1` by removing the invalid `-ErrorAction` flag from the `javac` command, ensuring proper compilation and execution of Java examples.

## Directory Structure
- `ADRs/`: Architectural Decision Records.
- `java/`: Java cryptographic implementations.
- `python/`: Python cryptographic implementations.
- `powershell/`: PowerShell cryptographic implementations.
- `scripts/`: Project utilities and gatekeeper tools.
- `tools/`: Configuration and metadata for the ADR Gatekeeper.
- `piolium/`: Security audit artifacts (partial).
