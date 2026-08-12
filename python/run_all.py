"""Run every standalone Python reference example."""

from aes_gcm_example import main as aes_gcm
from aes_key_wrap_example import main as aes_key_wrap
from ed25519_example import main as ed25519
from hashing_example import main as hashing
from hmac_example import main as hmac
from pbkdf2_example import main as pbkdf2
from private_key_serialisation_example import main as private_key_serialisation
from rsa_oaep_pss_example import main as rsa
from x25519_hkdf_example import main as x25519


def main() -> None:
    for example in (hashing, hmac, pbkdf2, aes_gcm, rsa, ed25519, x25519, aes_key_wrap, private_key_serialisation):
        example()


if __name__ == "__main__":
    main()
