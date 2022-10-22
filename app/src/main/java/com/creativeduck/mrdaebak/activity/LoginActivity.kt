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
import com.creativeduck.mrdaebak.entity.LoginRequestDto
import com.creativeduck.mrdaebak.entity.LoginResponseDto
import com.creativeduck.mrdaebak.network.RemoteService
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
            // 토큰 없는 경우는 이렇게 처리
            when (error.message) {
                "User denied access" -> {
                    showCustomToast("로그인을 하셔야 앱을 이용하실 수 있습니다.")
                }
                "user cancelled." -> {
                    showCustomToast("로그인을 하셔야 앱을 이용하실 수 있습니다.")
                }
                else -> {
                    showCustomToast("오류가 발생하였습니다.")
                }
            }
        } else {
            if (token == null) {
                showCustomToast("오류가 발생했습니다\n다시 로그인을 시도해주세요")
            } else {
                val accessToken = token.accessToken
                encryptionSharedPreferences.edit()
                    .putString(KAKAO_ACCESS_TOKEN, accessToken).apply()

                NetworkManager.checkNetwork(this, {
                    login(accessToken)
                }, {
                    showCustomToast("네트워크에 연결되지 않았습니다.")
                })
            }
        }
    }

    // TODO 백엔드에 요청해서 JWT 가져오는 것 구현
    private fun login(accessToken: String) {
        service.login(LoginRequestDto(accessToken)).getResponse(
            success = {
//                    response ->
                // TODO 임시
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
                showCustomToast("오류가 발생했습니다.")
            }
        )
    }

    private fun kakaoLogin() {
        // 카카오앱 설치되어 있다면
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                handleLogin(token, error)
            }
        }
        // 카카오톡이 없다면
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
                        showCustomToast("로그인이 필요합니다.")
                    } else {
                        showCustomToast("알 수 없는 오류가 발생하였습니다.")
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
                                showCustomToast("네트워크에 연결되지 않았습니다.")
                            })
                    } else {
                        showCustomToast("토큰은 있지만 기기에 토큰이 없습니다.")
                    }
                }
            }
        }
    }
}