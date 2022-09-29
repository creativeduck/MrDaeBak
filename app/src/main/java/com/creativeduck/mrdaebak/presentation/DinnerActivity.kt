package com.creativeduck.mrdaebak.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.creativeduck.mrdaebak.R
import com.creativeduck.mrdaebak.databinding.ActivityDinnerBinding

class DinnerActivity : AppCompatActivity() {

    companion object {
        const val VALENTINE = 0
        const val FRENCH = 1
        const val ENGLISH = 2
        const val CHAMPAGNE = 3
        const val DINNER_TYPE = "dinner type"
    }

    private lateinit var binding: ActivityDinnerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDinnerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initClick()
    }

    private fun initClick() {
        binding.tvDinnerValentine.setOnClickListener {
            startStyle(VALENTINE)
        }

        binding.tvDinnerFrench.setOnClickListener {
            startStyle(FRENCH)
        }

        binding.tvDinnerEnglish.setOnClickListener {
            startStyle(ENGLISH)
        }

        binding.tvDinnerChampagne.setOnClickListener {
            startStyle(CHAMPAGNE)
        }
    }

    private fun startStyle(dinner: Int) {
        val intent = Intent(this, StyleActivity::class.java)
        intent.putExtra(DINNER_TYPE, dinner)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
}