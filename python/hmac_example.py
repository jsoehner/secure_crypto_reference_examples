"""HMAC-SHA-256 with a random 256-bit secret and constant-time verification."""

from cryptography.exceptions import InvalidSignature
from cryptography.hazmat.primitives import hashes, hmac

from crypto_support import random_bytes


def compute(key: bytes, message: bytes) -> bytes:
    signer = hmac.HMAC(key, hashes.SHA256())
    signer.update(message)
    return signer.finalize()


def verify(key: bytes, message: bytes, tag: bytes) -> bool:
    verifier = hmac.HMAC(key, hashes.SHA256())
    verifier.update(message)
    try:
        verifier.verify(tag)
        return True
    except InvalidSignature:
        return False


def main() -> None:
    key = random_bytes(32)
    message = b"authenticated message"
    tag = compute(key, message)
    assert verify(key, message, tag)
    assert not verify(key, message + b"!", tag)
    print(f"HmacExample tag={tag.hex()}")


if __name__ == "__main__":
    main()
