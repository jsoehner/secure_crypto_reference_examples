"""Test suite for all Python cryptography examples."""

import unittest
from aes_gcm_example import encrypt, decrypt, generate_key
from aes_key_wrap_example import wrap, unwrap
from ed25519_example import sign, verify
from hashing_example import sha256
from hmac_example import compute, verify as hmac_verify
from pbkdf2_example import derive, verify as pbkdf2_verify
from rsa_oaep_pss_example import encrypt as rsa_encrypt, decrypt as rsa_decrypt, sign as rsa_sign, verify as rsa_verify, generate_private_key
from x25519_hkdf_example import derive_key
from crypto_support import random_bytes, constant_time_equal


class TestCryptography(unittest.TestCase):
    def test_hashing(self):
        data = b"test data"
        digest = sha256(data)
        self.assertEqual(len(digest), 32)
        self.assertNotEqual(digest, sha256(data + b" "))

    def test_hmac(self):
        key = random_bytes(32)
        message = b"test message"
        tag = compute(key, message)
        self.assertTrue(hmac_verify(key, message, tag))
        self.assertFalse(hmac_verify(key, message + b"!", tag))

    def test_pbkdf2(self):
        password = b"test password"
        stored = derive(password)
        self.assertTrue(pbkdf2_verify(password, stored))
        self.assertFalse(pbkdf2_verify(b"wrong password", stored))

    def test_aes_gcm(self):
        key = generate_key()
        plaintext = b"secret message"
        aad = b"aad"
        envelope = encrypt(key, plaintext, aad)
        decrypted = decrypt(key, envelope, aad)
        self.assertEqual(plaintext, decrypted)

    def test_rsa_oaep_pss(self):
        private_key = generate_private_key()
        public_key = private_key.public_key()
        message = b"rsa message"
        ciphertext = rsa_encrypt(public_key, message)
        self.assertEqual(message, rsa_decrypt(private_key, ciphertext))
        signature = rsa_sign(private_key, message)
        self.assertTrue(rsa_verify(public_key, message, signature))

    def test_ed25519(self):
        from cryptography.hazmat.primitives.asymmetric import ed25519
        private_key = ed25519.Ed25519PrivateKey.generate()
        public_key = private_key.public_key()
        message = b"ed25519 message"
        signature = sign(private_key, message)
        self.assertTrue(verify(public_key, message, signature))

    def test_x25519_hkdf(self):
        from cryptography.hazmat.primitives.asymmetric import x25519
        alice = x25519.X25519PrivateKey.generate()
        bob = x25519.X25519PrivateKey.generate()
        salt = random_bytes(16)
        info = b"info"
        alice_key = derive_key(alice, bob.public_key(), salt, info)
        bob_key = derive_key(bob, alice.public_key(), salt, info)
        self.assertTrue(constant_time_equal(alice_key, bob_key))

    def test_aes_key_wrap(self):
        kek = random_bytes(32)
        content_key = random_bytes(32)
        wrapped = wrap(kek, content_key)
        recovered = unwrap(kek, wrapped)
        self.assertTrue(constant_time_equal(content_key, recovered))


if __name__ == "__main__":
    unittest.main()
