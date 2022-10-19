package com.creativeduck.mrdaebak.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import com.creativeduck.mrdaebak.ApplicationClass.Companion.ROLE_COOK
import com.creativeduck.mrdaebak.ApplicationClass.Companion.ROLE_MANAGER
import com.creativeduck.mrdaebak.ApplicationClass.Companion.ROLE_RIDER
import com.creativeduck.mrdaebak.ApplicationClass.Companion.ROLE_USER
import com.creativeduck.mrdaebak.databinding.ActivityRegisterBinding
import com.creativeduck.mrdaebak.util.goActivityWithInt

class RegisterActivity: BaseActivity<ActivityRegisterBinding>(ActivityRegisterBinding::inflate) {

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

    private fun register(name: String, address: String, role: Int) {
        // TODO 회원정보 post 보내기
        // TODO 임시로 한 것
        when (role) {
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
//        // TODO 다시 로그인 화면으로 이동해서 로그인하기
//        startNewActivityAndClear<LoginActivity>()
    }

    override fun initClick() {
        with(binding) {
            btnRegister.setOnClickListener {
                val name = editSignInName.text.toString()
                val address = editSignInAddress.text.toString()
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