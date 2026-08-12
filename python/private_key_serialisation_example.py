"""Encrypted PKCS#8 private-key serialisation and loading."""

from cryptography.hazmat.primitives import serialization
from cryptography.hazmat.primitives.asymmetric import ed25519


def serialise_encrypted(private_key: ed25519.Ed25519PrivateKey, password: bytes) -> bytes:
    return private_key.private_bytes(
        encoding=serialization.Encoding.PEM,
        format=serialization.PrivateFormat.PKCS8,
        encryption_algorithm=serialization.BestAvailableEncryption(password),
    )


def load_encrypted(pem: bytes, password: bytes) -> ed25519.Ed25519PrivateKey:
    key = serialization.load_pem_private_key(pem, password=password)
    if not isinstance(key, ed25519.Ed25519PrivateKey):
        raise TypeError("unexpected private-key type")
    return key


def main() -> None:
    password = b"demonstration password; use a secrets source in production"
    original = ed25519.Ed25519PrivateKey.generate()
    pem = serialise_encrypted(original, password)
    recovered = load_encrypted(pem, password)
    message = b"key serialisation check"
    recovered.public_key().verify(recovered.sign(message), message)
    print("PrivateKeySerialisationExample encrypted PKCS#8 round-trip OK")


if __name__ == "__main__":
    main()
