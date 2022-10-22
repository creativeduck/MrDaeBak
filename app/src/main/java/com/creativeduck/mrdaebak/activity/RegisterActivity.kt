package com.creativeduck.mrdaebak.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.KAKAO_ACCESS_TOKEN
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.ROLE_COOK
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.ROLE_MANAGER
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.ROLE_RIDER
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.ROLE_USER
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.encryptionSharedPreferences
import com.creativeduck.mrdaebak.databinding.ActivityRegisterBinding
import com.creativeduck.mrdaebak.entity.RegisterRequestDto
import com.creativeduck.mrdaebak.network.RemoteService
import com.creativeduck.mrdaebak.util.getResponse
import com.creativeduck.mrdaebak.util.goActivityWithInt
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterActivity: BaseActivity<ActivityRegisterBinding>(ActivityRegisterBinding::inflate) {

    @Inject
    lateinit var service: RemoteService
    private val btnList = ArrayList<Button>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initClick()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun btnState(button: Button) {
        button.setOnTouchListener { _, _ ->
            for(btn in btnList) {
                btn.isPressed = btn == button
                if (btn.isPressed) {
                    if (btn == binding.btnRegisterRoleUser) {
                        binding.editRegisterVerificationLayout.visibility = View.GONE;
                    } else {
                        binding.editRegisterVerificationLayout.visibility = View.VISIBLE;
                    }
                }
            }
            true
        }
    }

    private fun getPressedBtn(): Int {
        with(binding) {
            for(btn in btnList) {
                if (btn.isPressed) {
                    return when (btn) {
                        btnRegisterRoleUser -> {
                            ROLE_USER
                        }
                        btnRegisterRoleManager -> {
                            ROLE_MANAGER
                        }
                        btnRegisterRoleCook -> {
                            ROLE_COOK
                        }
                        btnRegisterRoleRider -> {
                            ROLE_RIDER
                        }
                        else -> {
                            ROLE_USER
                        }
                    }
                }
            }
        }
        return ROLE_USER
    }

    private fun register(name: String, address: String, roleCode: Int) {
        // 카카오 Id
        val kakaoToken = encryptionSharedPreferences.getString(KAKAO_ACCESS_TOKEN, null)
        val role = when (roleCode) {
            ROLE_COOK -> {
                "ROLE_COOK"
            }
            ROLE_RIDER -> {
                "ROLE_RIDER"
            }
            ROLE_MANAGER -> {
                "ROLE_MANAGER"
            }
            else -> {
                "ROLE_USER"
            }
        }
        if (kakaoToken == null) {
            showCustomToast("카카오 로그인이 되어있지 않습니다.")
            return
        }
        val verificationCode = if (roleCode != ROLE_USER)
                                    binding.editRegisterVerification.text.toString()
                                else
                                    "EMPTY"
        val requestDto = RegisterRequestDto(kakaoToken, name, address, role, verificationCode)

        // TODO 회원가입 요청 보내고, 성공하면 로그인 화면으로 이동하기
//        service.register(requestDto).getResponse(
//            success = {
//                goActivityWithInt<LoginActivity>(clear = true)
//            },
//            failure = {
//                showCustomToast("오류가 발생했습니다.")
//            }
//        )

        // TODO 임시로 한 것
        when (roleCode) {
            ROLE_USER -> {
                goActivityWithInt<DinnerActivity>(clear = true)
            }
            ROLE_COOK -> {
                goActivityWithInt<OrderReceiptActivity>(Pair("ROLE", ROLE_COOK))
            }
            ROLE_RIDER -> {
                goActivityWithInt<OrderReceiptActivity>(Pair("ROLE", ROLE_RIDER))
            }
            ROLE_MANAGER -> {
                goActivityWithInt<IngredientActivity>(clear = true)
            }
        }
    }

    override fun initClick() {
        with(binding) {
            btnRegister.setOnClickListener {
                val name = editRegisterName.text.toString()
                val address = editRegisterAddress.text.toString()
                val btn = getPressedBtn()
                register(name, address, btn)
            }
            btnList.add(btnRegisterRoleCook)
            btnList.add(btnRegisterRoleRider)
            btnList.add(btnRegisterRoleUser)
            btnList.add(btnRegisterRoleManager)

            btnState(btnRegisterRoleCook)
            btnState(btnRegisterRoleRider)
            btnState(btnRegisterRoleUser)
            btnState(btnRegisterRoleManager)
        }
    }

}