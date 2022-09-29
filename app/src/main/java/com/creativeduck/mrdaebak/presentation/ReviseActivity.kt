package com.creativeduck.mrdaebak.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.creativeduck.mrdaebak.databinding.ActivityReviseBinding

class ReviseActivity: AppCompatActivity() {

    lateinit var binding: ActivityReviseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initClick()
    }

    private fun initClick() {
        binding.btnReviseToOrder.setOnClickListener {

        }
    }
}