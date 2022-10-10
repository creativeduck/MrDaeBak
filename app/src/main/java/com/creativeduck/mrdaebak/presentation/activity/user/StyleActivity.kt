package com.creativeduck.mrdaebak.presentation.activity.user

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.creativeduck.mrdaebak.R
import com.creativeduck.mrdaebak.databinding.ActivityStyleBinding
import com.creativeduck.mrdaebak.presentation.activity.BaseActivity
import com.creativeduck.mrdaebak.presentation.activity.user.DinnerActivity.Companion.CHAMPAGNE
import com.creativeduck.mrdaebak.presentation.activity.user.DinnerActivity.Companion.DINNER_TYPE
import com.creativeduck.mrdaebak.presentation.activity.user.DinnerActivity.Companion.VALENTINE

class StyleActivity : BaseActivity<ActivityStyleBinding>(ActivityStyleBinding::inflate) {

    companion object {
        const val SIMPLE = 0
        const val GRAND = 1
        const val DELUXE = 2
        const val STYLE_TYPE = "style type"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStyleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var dinnerType = intent.getIntExtra(DINNER_TYPE, VALENTINE)
        initClick(dinnerType)
    }

    private fun initClick(dinnerType: Int) {
        with(binding) {
            tvStyleSimple.setOnClickListener {
                if (dinnerType == CHAMPAGNE) {
                    Toast.makeText(applicationContext, "샴페인 축제 디너는 그랜드 또는 디럭스 스타일만 가능합니다.", Toast.LENGTH_SHORT).show()
                }
                startOrder(dinnerType, SIMPLE)
            }

            tvStyleGrand.setOnClickListener {
                startOrder(dinnerType, GRAND)
            }

            tvStyleDeluxe.setOnClickListener {
                startOrder(dinnerType, DELUXE)
            }
        }
    }

    private fun startOrder(dinnerType: Int, styleType: Int) {
        val intent = Intent(this, OrderActivity::class.java)
        intent.putExtra(DINNER_TYPE, dinnerType)
        intent.putExtra(STYLE_TYPE, styleType)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
}