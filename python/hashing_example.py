"""SHA-256 hashing example. Hashes are not password storage or message authentication."""

from cryptography.hazmat.primitives import hashes


def sha256(data: bytes) -> bytes:
    digest = hashes.Hash(hashes.SHA256())
    digest.update(data)
    return digest.finalize()


def main() -> None:
    print(f"HashingExample SHA-256={sha256(b'secure coding reference').hex()}")


if __name__ == "__main__":
    main()
