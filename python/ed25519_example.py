"""Ed25519 signing and verification."""

from cryptography.exceptions import InvalidSignature
from cryptography.hazmat.primitives.asymmetric import ed25519


def sign(private_key: ed25519.Ed25519PrivateKey, message: bytes) -> bytes:
    return private_key.sign(message)


def verify(public_key: ed25519.Ed25519PublicKey, message: bytes, signature: bytes) -> bool:
    try:
        public_key.verify(signature, message)
        return True
    except InvalidSignature:
        return False


def main() -> None:
    private_key = ed25519.Ed25519PrivateKey.generate()
    message = b"signed message"
    signature = sign(private_key, message)
    assert verify(private_key.public_key(), message, signature)
    assert not verify(private_key.public_key(), message + b"!", signature)
    print(f"Ed25519Example signature={signature.hex()}")


if __name__ == "__main__":
    main()
