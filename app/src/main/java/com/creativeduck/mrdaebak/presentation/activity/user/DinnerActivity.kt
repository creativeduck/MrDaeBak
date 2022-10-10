package com.creativeduck.mrdaebak.presentation.activity.user

import android.content.Intent
import android.os.Bundle
import com.creativeduck.mrdaebak.R
import com.creativeduck.mrdaebak.databinding.ActivityDinnerBinding
import com.creativeduck.mrdaebak.presentation.activity.BaseActivity
import com.creativeduck.mrdaebak.presentation.activity.user.stt.SttDinnerActivity

class DinnerActivity : BaseActivity<ActivityDinnerBinding>(ActivityDinnerBinding::inflate) {

    companion object {
        const val VALENTINE = 0
        const val FRENCH = 1
        const val ENGLISH = 2
        const val CHAMPAGNE = 3
        const val DINNER_TYPE = "dinner type"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDinnerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initClick()
    }

    private fun initClick() {
        with(binding) {
            btnDinnerValentine.setOnClickListener {
                startStyle(VALENTINE)
            }
            btnDinnerFrench.setOnClickListener {
                startStyle(FRENCH)
            }
            btnDinnerEnglish.setOnClickListener {
                startStyle(ENGLISH)
            }
            btnDinnerChampagne.setOnClickListener {
                startStyle(CHAMPAGNE)
            }
            fabDinnerStt.setOnClickListener {
                orderByStt()
            }
        }

    }

    private fun orderByStt() {
        // 여기선 버튼 누르면 디너 선택하고, 선택되면 바로 다음 화면으로 넘어간다.
        val intent = Intent(this, SttDinnerActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun startStyle(dinner: Int) {
        val intent = Intent(this, StyleActivity::class.java)
        intent.putExtra(DINNER_TYPE, dinner)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
}