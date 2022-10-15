package com.creativeduck.mrdaebak.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import com.creativeduck.mrdaebak.databinding.ActivitySignInBinding

class SignInActivity: BaseActivity<ActivitySignInBinding>(ActivitySignInBinding::inflate) {

    companion object {
        const val ROLE_USER = 0
        const val ROLE_MANAGER = 1
        const val ROLE_COOK = 2
        const val ROLE_RIDER = 3
    }

    private val btnList = ArrayList<Button>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBtn()
        initClick()

    }

    private fun initBtn() {
        with(binding) {
            btnList.add(btnSignInRoleCook)
            btnList.add(btnSignInRoleRider)
            btnList.add(btnSignInRoleUser)
            btnList.add(btnSignInRoleManager)

            btnState(btnSignInRoleCook)
            btnState(btnSignInRoleRider)
            btnState(btnSignInRoleUser)
            btnState(btnSignInRoleManager)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun btnState(button: Button) {
        button.setOnTouchListener { _, _ ->
            for(btn in btnList) {
                btn.isPressed = btn == button
            }
            true
        }
    }

    private fun getPressedBtn(): Int {
        with(binding) {
            for(btn in btnList) {
                if (btn.isPressed) {
                    return when (btn) {
                        btnSignInRoleUser -> {
                            ROLE_USER
                        }
                        btnSignInRoleManager -> {
                            ROLE_MANAGER
                        }
                        btnSignInRoleCook -> {
                            ROLE_COOK
                        }
                        btnSignInRoleRider -> {
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

    private fun signIn(name: String, address: String, role: Int) {
        // TODO 회원정보 post 보내기
        // TODO 임시로 한 것
        when (role) {
            ROLE_USER -> {
                startNewActivityAndClear<DinnerActivity>()
            }
            ROLE_COOK -> {
                startNewActivityWithInt<OrderReceiptActivity>(Pair("ROLE", ROLE_COOK))
            }
            ROLE_RIDER -> {
                startNewActivityWithInt<OrderReceiptActivity>(Pair("ROLE", ROLE_RIDER))
            }
            ROLE_MANAGER -> {
                startNewActivityAndClear<IngredientActivity>()
            }
        }
//        // TODO 그 이후 이동하기
//        startNewActivityAndClear<LoginActivity>()
    }

    private fun initClick() {
        with(binding) {
            btnSignIn.setOnClickListener {
                val name = editSignInName.text.toString()
                val address = editSignInAddress.text.toString()
                val btn = getPressedBtn()
                signIn(name, address, btn)
            }
        }
    }

}