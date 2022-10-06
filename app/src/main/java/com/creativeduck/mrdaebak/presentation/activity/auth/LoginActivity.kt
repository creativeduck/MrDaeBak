package com.creativeduck.mrdaebak.presentation.activity.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.creativeduck.mrdaebak.ApplicationClass.Companion.KAKAO_ACCESS_TOKEN
import com.creativeduck.mrdaebak.ApplicationClass.Companion.encryptionSharedPreferences
import com.creativeduck.mrdaebak.R
import com.creativeduck.mrdaebak.databinding.ActivityLoginBinding
import com.creativeduck.mrdaebak.presentation.activity.BaseActivity
import com.creativeduck.mrdaebak.presentation.activity.user.DinnerActivity
import com.creativeduck.mrdaebak.util.NetworkManager
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient

class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initClick()
        checkLogin()
    }

    private fun startDinner() {
        // TODO 여기서 사용자의 역할에 따라 다른 메인화면으로 이동해야 함
        val intent = Intent(this, DinnerActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun initClick() {
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
        }
        else {
            if (token == null) {
                showCustomToast("오류가 발생했습니다\n다시 로그인을 시도해주세요")
            } else {
                val accessToken = token.accessToken
                encryptionSharedPreferences.edit()
                    .putString(KAKAO_ACCESS_TOKEN, accessToken).apply()

                NetworkManager.checkNetwork(this, {
                    // TODO accessToken 을 서버로 전송하고, 이걸 바탕으로 JWT 를 반환받아야 한다.
                    Log.d("HELLO", accessToken)
                    startDinner()
                }, {
                    showCustomToast("네트워크에 연결되지 않았습니다.")
                })
            }
        }

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
                    }
                    else {
                        showCustomToast("알 수 없는 오류가 발생하였습니다.")
                    }
                }
                else {
                    val accessToken = encryptionSharedPreferences.getString(KAKAO_ACCESS_TOKEN, null)

                    if (accessToken != null) {
                        NetworkManager.checkNetwork(this,
                        callApi = {
                            // TODO 이때도 AccessToken 으로 로그인 처리하는 거였음
                            startDinner()
                        },
                        notConnected = {
                            showCustomToast("네트워크에 연결되지 않았습니다.")
                        })
                    }
                    else {
                        showCustomToast("토큰은 있지만 알 수 없는 오류가 발생하였습니다.")
                    }
                }
            }
        }
    }
}