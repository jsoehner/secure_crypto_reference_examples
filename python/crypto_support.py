"""Shared helpers for the secure cryptography examples."""

from __future__ import annotations

import hmac as stdlib_hmac
import os


def random_bytes(length: int) -> bytes:
    if length <= 0:
        raise ValueError("length must be positive")
    return os.urandom(length)


def constant_time_equal(left: bytes, right: bytes) -> bool:
    return stdlib_hmac.compare_digest(left, right)
