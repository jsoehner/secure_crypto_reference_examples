# ADR 009: Automated SBOM & CBOM Supply Chain Governance

## Status
Accepted

## Context
As a reference implementation repository for production-grade cryptographic operations across Python and Java, ensuring verifiable software supply chain integrity and transparent cryptographic inventory is paramount. 

Modern cybersecurity standards and regulatory frameworks (e.g., Executive Order 14028, NIST post-quantum migration guidance) require continuous visibility into both third-party software dependencies and deployed cryptographic algorithms, protocols, key lengths, and padding modes. Relying solely on manual audits or traditional vulnerability scans leaves blind spots regarding software component provenance and quantum-vulnerable cryptographic primitives.

To maintain trust, compliance, and proactive security governance, the project requires an automated, repeatable pipeline capable of generating Software Bills of Materials (SBOMs), Cryptographic Bills of Materials (CBOMs), verifying artifact schemas, and assessing Post-Quantum Cryptography (PQC) readiness.

## Alternatives Considered

1. **Manual Inventory Spreadsheets & Periodic Reviews**:
   - *Pros*: Minimal tooling setup.
   - *Cons*: Prone to human error, becomes stale immediately upon dependency updates, lacks automated enforcement in CI/CD.
2. **SBOM-Only Dependency Scanning (Anchore/Syft)**:
   - *Pros*: Established industry standard for component and package inventory.
   - *Cons*: Captures package dependencies but provides zero visibility into specific cryptographic primitives, curves, key lengths, or quantum vulnerability status within source code.
3. **Automated Dual-Engine SBOM + CBOM Pipeline with CI Verification Harness (Chosen)**:
   - *Pros*: Combines Anchore/Syft dual-format SBOM generation (CycloneDX and SPDX) with cbomkit and semantic Abstract Syntax Tree (AST) cryptographic scanning; validates integrity via a verification test harness; produces automated PQC migration dashboards in CI.
   - *Cons*: Requires initial setup and minor compute overhead during GitHub Actions CI runs.

## Decision
We adopt an automated, dual-engine Software and Cryptographic Bill of Materials governance framework integrated into both local developer workflows and GitHub Actions CI:

1. **Dual-Format SBOM Generation**:
   - Utilize Anchore/Syft to generate both CycloneDX JSON (`oss/sbom.cyclonedx.json`) and SPDX JSON (`oss/sbom.spdx.json`) for comprehensive package and dependency tracking.
2. **CBOM Generation & Semantic AST Analysis**:
   - Utilize `cbomkit-action` for baseline CBOM generation (`oss/cbom.json`).
   - Supplement with `scripts/scan_crypto_ast.py` to perform semantic AST analysis across Python and multi-language call-sites, reconciling discovered primitives against CBOM definitions.
3. **Automated Verification Harness**:
   - Execute `scripts/test_boms.sh` during CI to enforce schema compliance, non-empty artifact generation, and integrity checks.
4. **PQC Readiness Reporting**:
   - Run `scripts/analyze_cbom.py` to evaluate classical vs. post-quantum algorithm posture, producing actionable JSON/Markdown migration summaries published to GitHub Step Summaries and `docs/security/CRYPTOGRAPHIC_AUDIT.md`.
5. **Local Developer Ergonomics**:
   - Provide `scripts/generate_boms.sh` to allow developers to generate and inspect BOM artifacts locally prior to pushing changes.

## Consequences
- **Positive**:
  - Full transparency and provenance tracking for all third-party dependencies and cryptographic primitives.
  - Continuous CI enforcement preventing silent introduction of deprecated or non-compliant algorithms.
  - Proactive posture tracking for future Post-Quantum Cryptography (PQC) transitions.
  - Alignment with industry supply chain security best practices.
- **Negative / Trade-offs**:
  - Introduces a small execution overhead in GitHub Actions workflows (BOM generation and AST analysis).
  - Developers must ensure local Docker/tooling environments are available if generating full container BOMs locally.
