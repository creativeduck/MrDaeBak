package com.creativeduck.mrdaebak.presentation.activity.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.creativeduck.mrdaebak.databinding.ActivitySignInBinding
import com.creativeduck.mrdaebak.presentation.activity.BaseActivity

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
                            com.creativeduck.mrdaebak.presentation.activity.auth.SignInActivity.ROLE_USER
                        }
                        btnSignInRoleManager -> {
                            com.creativeduck.mrdaebak.presentation.activity.auth.SignInActivity.ROLE_MANAGER
                        }
                        btnSignInRoleCook -> {
                            com.creativeduck.mrdaebak.presentation.activity.auth.SignInActivity.ROLE_COOK
                        }
                        btnSignInRoleRider -> {
                            com.creativeduck.mrdaebak.presentation.activity.auth.SignInActivity.ROLE_RIDER
                        }
                        else -> {
                            com.creativeduck.mrdaebak.presentation.activity.auth.SignInActivity.ROLE_USER
                        }
                    }
                }
            }
        }
        return ROLE_USER
    }

    private fun signIn(name: String, address: String, role: Int) {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
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