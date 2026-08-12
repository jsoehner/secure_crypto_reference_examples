"""RSA-3072 OAEP encryption and RSA-PSS signatures using SHA-256."""

from cryptography.exceptions import InvalidSignature
from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives.asymmetric import padding, rsa


def generate_private_key() -> rsa.RSAPrivateKey:
    return rsa.generate_private_key(public_exponent=65537, key_size=3072)


def oaep() -> padding.OAEP:
    return padding.OAEP(
        mgf=padding.MGF1(hashes.SHA256()),
        algorithm=hashes.SHA256(),
        label=None,
    )


def pss() -> padding.PSS:
    return padding.PSS(
        mgf=padding.MGF1(hashes.SHA256()),
        salt_length=hashes.SHA256().digest_size,
    )


def encrypt(public_key: rsa.RSAPublicKey, plaintext: bytes) -> bytes:
    return public_key.encrypt(plaintext, oaep())


def decrypt(private_key: rsa.RSAPrivateKey, ciphertext: bytes) -> bytes:
    return private_key.decrypt(ciphertext, oaep())


def sign(private_key: rsa.RSAPrivateKey, message: bytes) -> bytes:
    return private_key.sign(message, pss(), hashes.SHA256())


def verify(public_key: rsa.RSAPublicKey, message: bytes, signature: bytes) -> bool:
    try:
        public_key.verify(signature, message, pss(), hashes.SHA256())
        return True
    except InvalidSignature:
        return False


def main() -> None:
    private_key = generate_private_key()
    public_key = private_key.public_key()
    message = b"small secret or content-encryption key"
    assert decrypt(private_key, encrypt(public_key, message)) == message
    signature = sign(private_key, message)
    assert verify(public_key, message, signature)
    assert not verify(public_key, message + b"!", signature)
    print("RsaOaepPssExample encryption and signature OK")


if __name__ == "__main__":
    main()
