"""AES-256-GCM authenticated encryption with a fresh 96-bit nonce and AAD."""

from dataclasses import dataclass
from cryptography.exceptions import InvalidTag
from cryptography.hazmat.primitives.ciphers.aead import AESGCM

from crypto_support import random_bytes


@dataclass(frozen=True)
class Envelope:
    nonce: bytes
    ciphertext_and_tag: bytes


def generate_key() -> bytes:
    return AESGCM.generate_key(bit_length=256)


def encrypt(key: bytes, plaintext: bytes, aad: bytes | None) -> Envelope:
    nonce = random_bytes(12)
    return Envelope(nonce, AESGCM(key).encrypt(nonce, plaintext, aad))


def decrypt(key: bytes, envelope: Envelope, aad: bytes | None) -> bytes:
    return AESGCM(key).decrypt(envelope.nonce, envelope.ciphertext_and_tag, aad)


def main() -> None:
    key = generate_key()
    aad = b"app=v1;purpose=reference"
    plaintext = b"confidential data"
    envelope = encrypt(key, plaintext, aad)
    assert decrypt(key, envelope, aad) == plaintext
    tampered = bytes([envelope.ciphertext_and_tag[0] ^ 1]) + envelope.ciphertext_and_tag[1:]
    try:
        decrypt(key, Envelope(envelope.nonce, tampered), aad)
        raise AssertionError("tampering was not detected")
    except InvalidTag:
        print("AesGcmExample round-trip OK; tampering rejected")


if __name__ == "__main__":
    main()
