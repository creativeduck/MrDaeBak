package com.creativeduck.mrdaebak.activity

import android.os.Bundle
import android.util.Log
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.JWT_TOKEN
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.KAKAO_ACCESS_TOKEN
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.MR_USER_ID
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.MR_USER_ROLE
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.ROLE_COOK
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.ROLE_RIDER
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.encryptionSharedPreferences
import com.creativeduck.mrdaebak.databinding.ActivityLoginBinding
import com.creativeduck.mrdaebak.data.LoginRequestDto
import com.creativeduck.mrdaebak.data.LoginResponseDto
import com.creativeduck.mrdaebak.data.RemoteService
import com.creativeduck.mrdaebak.util.NetworkManager
import com.creativeduck.mrdaebak.util.getResponse
import com.creativeduck.mrdaebak.util.goActivityWithInt
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {

    @Inject
    lateinit var service: RemoteService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initClick()
        checkLogin()
    }

    private fun startMain(role: String) {
        when (encryptionSharedPreferences.getString(MR_USER_ROLE, "ROLE_USER")) {
            "ROLE_MANAGER" -> {
                goActivityWithInt<IngredientActivity>(clear = true)
            }
            "ROLE_COOK" -> {
                goActivityWithInt<OrderReceiptActivity>(Pair("ROLE", ROLE_COOK), clear = true)
            }
            "ROLE_RIDER" -> {
                goActivityWithInt<OrderReceiptActivity>(Pair("ROLE", ROLE_RIDER), clear = true)
            }
            else -> {
                goActivityWithInt<DinnerActivity>(clear = true)
            }
        }
    }

    override fun initClick() {
        binding.btnLogin.setOnClickListener {
            kakaoLogin()
        }
    }

    private fun handleLogin(token: OAuthToken?, error: Throwable?) {
        if (error != null) {
            Log.d("HELLO", error.message ?: "error is null");
            // ?????? ?????? ????????? ????????? ??????
            when (error.message) {
                "User denied access" -> {
                    showCustomToast("???????????? ????????? ?????? ???????????? ??? ????????????.")
                }
                "user cancelled." -> {
                    showCustomToast("???????????? ????????? ?????? ???????????? ??? ????????????.")
                }
                else -> {
                    showCustomToast("????????? ?????????????????????.")
                }
            }
        } else {
            if (token == null) {
                showCustomToast("????????? ??????????????????\n?????? ???????????? ??????????????????")
            } else {
                val accessToken = token.accessToken
                encryptionSharedPreferences.edit()
                    .putString(KAKAO_ACCESS_TOKEN, accessToken).apply()

                NetworkManager.checkNetwork(this, {
                    login(accessToken)
                }, {
                    showCustomToast("??????????????? ???????????? ???????????????.")
                })
            }
        }
    }

    // TODO ???????????? ???????????? JWT ???????????? ??? ??????
    private fun login(accessToken: String) {
        service.login(LoginRequestDto(accessToken)).getResponse(
            success = {
//                    response ->
                // TODO ??????
                val response = LoginResponseDto(303, "", 33L, "ROLE_MANAGER")
                if (response.code == 404) {
                    goActivityWithInt<RegisterActivity>(clear = true)
                } else {
                    with(encryptionSharedPreferences) {
                        val mrUserid = response.userId
                        edit().putString(JWT_TOKEN, response.jwt).apply()
                        edit().putLong(MR_USER_ID, mrUserid).apply()
                        edit().putString(MR_USER_ROLE, response.role).apply()
                        startMain(response.role)
                    }
                }
            },
            failure = {
                showCustomToast("????????? ??????????????????.")
            }
        )
    }

    private fun kakaoLogin() {
        // ???????????? ???????????? ?????????
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                handleLogin(token, error)
            }
        }
        // ??????????????? ?????????
        else {
            UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
                handleLogin(token, error)
            }
        }
    }

    private fun checkLogin() {
        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { _, error ->
                if (error != null) {
                    if (error is KakaoSdkError && error.isInvalidTokenError()) {
                        showCustomToast("???????????? ???????????????.")
                    } else {
                        showCustomToast("??? ??? ?????? ????????? ?????????????????????.")
                    }
                } else {
                    val accessToken =
                        encryptionSharedPreferences.getString(KAKAO_ACCESS_TOKEN, null)
                    if (accessToken != null) {
                        NetworkManager.checkNetwork(this,
                            callApi = {
                                val role=  encryptionSharedPreferences.getString(MR_USER_ROLE, null)
                                role?.let { startMain(it) }
                            },
                            notConnected = {
                                showCustomToast("??????????????? ???????????? ???????????????.")
                            })
                    } else {
                        showCustomToast("????????? ????????? ????????? ????????? ????????????.")
                    }
                }
            }
        }
    }
}