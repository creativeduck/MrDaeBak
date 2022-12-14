package com.creativeduck.mrdaebak.activity

import android.os.Bundle
import android.widget.Toast
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.DINNER_CHAMPAGNE
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.DINNER_TYPE
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.DINNER_VALENTINE
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.STYLE_DELUXE
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.STYLE_GRAND
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.STYLE_SIMPLE
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.STYLE_TYPE
import com.creativeduck.mrdaebak.databinding.ActivityStyleBinding
import com.creativeduck.mrdaebak.util.goActivityWithInt

class StyleActivity : BaseActivity<ActivityStyleBinding>(ActivityStyleBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbar(binding.tbStyle, true, setTitle = true)

        val dinnerType = intent.getIntExtra(DINNER_TYPE, DINNER_VALENTINE)
        initClick(dinnerType)
    }

    private fun initClick(dinnerType: Int) {
        with(binding) {
            btnStyleSimple.setOnClickListener {
                if (dinnerType == DINNER_CHAMPAGNE) {
                    Toast.makeText(applicationContext, "샴페인 축제 디너는 그랜드 또는 디럭스 스타일만 가능합니다.", Toast.LENGTH_SHORT).show()
                }
                goActivityWithInt<OrderActivity>(
                    Pair(DINNER_TYPE, dinnerType),
                    Pair(STYLE_TYPE, STYLE_SIMPLE)
                )
            }

            btnStyleGrand.setOnClickListener {
                goActivityWithInt<OrderActivity>(
                    Pair(DINNER_TYPE, dinnerType),
                    Pair(STYLE_TYPE, STYLE_GRAND)
                )
            }
            btnStyleDeluxe.setOnClickListener {
                goActivityWithInt<OrderActivity>(
                    Pair(DINNER_TYPE, dinnerType),
                    Pair(STYLE_TYPE, STYLE_DELUXE)
                )
            }
        }
    }
}