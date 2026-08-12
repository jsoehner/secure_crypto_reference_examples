"""PBKDF2-HMAC-SHA-256 password verification with stored salt and work factor."""

from dataclasses import dataclass
from cryptography.exceptions import InvalidKey
from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives.kdf.pbkdf2 import PBKDF2HMAC

from crypto_support import random_bytes

ITERATIONS = 600_000  # Calibrate for the deployment environment.


@dataclass(frozen=True)
class StoredPassword:
    salt: bytes
    iterations: int
    derived_key: bytes


def _kdf(salt: bytes, iterations: int) -> PBKDF2HMAC:
    return PBKDF2HMAC(
        algorithm=hashes.SHA256(),
        length=32,
        salt=salt,
        iterations=iterations,
    )


def derive(password: bytes) -> StoredPassword:
    salt = random_bytes(16)
    return StoredPassword(salt, ITERATIONS, _kdf(salt, ITERATIONS).derive(password))


def verify(password: bytes, stored: StoredPassword) -> bool:
    try:
        _kdf(stored.salt, stored.iterations).verify(password, stored.derived_key)
        return True
    except InvalidKey:
        return False


def main() -> None:
    stored = derive(b"correct horse battery staple")
    assert verify(b"correct horse battery staple", stored)
    assert not verify(b"incorrect", stored)
    print(f"Pbkdf2Example salt={stored.salt.hex()} iterations={stored.iterations}")


if __name__ == "__main__":
    main()
