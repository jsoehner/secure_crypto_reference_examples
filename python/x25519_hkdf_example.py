"""X25519 key agreement followed by HKDF-SHA-256 key derivation."""

from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives.asymmetric import x25519
from cryptography.hazmat.primitives.kdf.hkdf import HKDF

from crypto_support import constant_time_equal, random_bytes


def derive_key(private_key: x25519.X25519PrivateKey,
               peer_public_key: x25519.X25519PublicKey,
               salt: bytes,
               info: bytes) -> bytes:
    shared_secret = private_key.exchange(peer_public_key)
    return HKDF(
        algorithm=hashes.SHA256(),
        length=32,
        salt=salt,
        info=info,
    ).derive(shared_secret)


def main() -> None:
    alice = x25519.X25519PrivateKey.generate()
    bob = x25519.X25519PrivateKey.generate()
    salt = random_bytes(16)  # Shared protocol value; do not regenerate independently per peer.
    info = b"app-v1 aes-gcm key"
    alice_key = derive_key(alice, bob.public_key(), salt, info)
    bob_key = derive_key(bob, alice.public_key(), salt, info)
    assert constant_time_equal(alice_key, bob_key)
    print(f"X25519HkdfExample derivedKey={alice_key.hex()}")


if __name__ == "__main__":
    main()
