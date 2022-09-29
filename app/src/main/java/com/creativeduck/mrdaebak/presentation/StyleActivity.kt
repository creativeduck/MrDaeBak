package com.creativeduck.mrdaebak.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.creativeduck.mrdaebak.databinding.ActivityStyleBinding
import com.creativeduck.mrdaebak.presentation.DinnerActivity.Companion.CHAMPAGNE
import com.creativeduck.mrdaebak.presentation.DinnerActivity.Companion.DINNER_TYPE
import com.creativeduck.mrdaebak.presentation.DinnerActivity.Companion.VALENTINE

class StyleActivity: AppCompatActivity() {

    companion object {
        const val SIMPLE = 0
        const val GRAND = 1
        const val DELUXE = 2
        const val STYLE_TYPE = "style type"
    }

    lateinit var binding: ActivityStyleBinding

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
                startRevise(dinnerType, SIMPLE)
            }

            tvStyleGrand.setOnClickListener {
                startRevise(dinnerType, GRAND)
            }

            tvStyleDeluxe.setOnClickListener {
                startRevise(dinnerType, DELUXE)
            }
        }
    }

    private fun startRevise(dinnerType: Int, styleType: Int) {
        val intent = Intent(this, ReviseActivity::class.java)
        intent.putExtra(DINNER_TYPE, dinnerType)
        intent.putExtra(STYLE_TYPE, styleType)
        startActivity(intent)
    }
}