package com.github.patrickpaul.security.token

interface TokenService {

    fun generate(config: TokenConfig, vararg claims: TokenClaim): String

}