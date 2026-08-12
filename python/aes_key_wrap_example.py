"""AES key wrap with padding for protecting key material under a KEK."""

from cryptography.hazmat.primitives.keywrap import (
    aes_key_unwrap_with_padding,
    aes_key_wrap_with_padding,
)

from crypto_support import constant_time_equal, random_bytes


def wrap(key_encryption_key: bytes, key_to_wrap: bytes) -> bytes:
    return aes_key_wrap_with_padding(key_encryption_key, key_to_wrap)


def unwrap(key_encryption_key: bytes, wrapped_key: bytes) -> bytes:
    return aes_key_unwrap_with_padding(key_encryption_key, wrapped_key)


def main() -> None:
    key_encryption_key = random_bytes(32)
    content_key = random_bytes(32)
    wrapped = wrap(key_encryption_key, content_key)
    recovered = unwrap(key_encryption_key, wrapped)
    assert constant_time_equal(content_key, recovered)
    print(f"AesKeyWrapExample wrapped={wrapped.hex()}")


if __name__ == "__main__":
    main()
