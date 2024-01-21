package com.github.patrickpaul.security.hashing

data class SaltedHash(
    val hash: String,
    val salt: String
)
