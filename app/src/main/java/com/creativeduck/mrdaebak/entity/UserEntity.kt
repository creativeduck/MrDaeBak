package com.creativeduck.mrdaebak.entity

data class LoginResponseDto(
    val code: Int,
    val jwt: String,
    val userId: Long,
    val role: String
)

data class LoginRequestDto(
    val accessToken: String
)

data class RegisterRequestDto(
    val kakaoId: String,
    val name: String,
    val address: String,
    val role: String,
    val verificationCode: String = "EMPTY"
)

data class UserDto(
    val name: String,
    val address: String,
    val rank: String = "EMPTY"
)