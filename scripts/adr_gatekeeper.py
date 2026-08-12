import json
import sys
import os

def load_json(path):
    with open(path, 'r') as f:
        return json.load(f)

def main():
    if len(sys.argv) < 2:
        print("Usage: python scripts/adr_gatekeeper.py \"<change description>\"")
        sys.exit(1)

    description = sys.argv[1].lower()
    config = load_json('tools/adr_analyst_config.json')
    index = load_json('tools/adr_index.json')

    print(f"--- ADR Gatekeeper Analysis ---")
    print(f"Change Description: {sys.argv[1]}")
    print(f"\nSignificance Rules:")
    for rule in config['significance_rules']:
        print(f"- {rule}")

    required = False
    matched_rule = ""
    
    # Keywords extracted from significance rules for matching
    keywords = [
        "cryptographic", "crypto", "algorithm",
        "authentication", "authorization", "session",
        "persistence", "schema", "database",
        "integration", "api", "third-party",
        "infrastructure", "deployment", "scaling",
        "security", "compliance"
    ]

    for rule in config['significance_rules']:
        rule_lower = rule.lower()
        # Check if any keyword from the rule is in the description
        # Or if the description contains significant words from the rule
        rule_keywords = [w for w in rule_lower.split() if len(w) > 3 and w not in ['any', 'change', 'to', 'the', 'or', 'with']]
        if any(kw in description for kw in rule_keywords):
            required = True
            matched_rule = rule
            break

    if required:
        print(f"\nResult: ADR REQUIRED")
        print(f"Reason: The change matches rule: '{matched_rule}'")
        
        # Check index
        found = False
        for adr in index['adr_index']:
            if adr['title'].lower() in description or adr['id'] in description:
                print(f"Note: A similar ADR might already exist: {adr['title']} ({adr['file']})")
                found = True
        
        if not found:
            print("Action: Please create a new ADR in the 'ADRs/' directory.")
    else:
        print(f"\nResult: ADR NOT REQUIRED")
        print("Reason: The change does not meet the significance threshold.")

if __name__ == "__main__":
    main()
