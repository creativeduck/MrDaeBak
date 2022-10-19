package com.creativeduck.mrdaebak.activity

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.creativeduck.mrdaebak.ApplicationClass.Companion.EDITING
import com.creativeduck.mrdaebak.ApplicationClass.Companion.EDIT_COMPLETED
import com.creativeduck.mrdaebak.R
import com.creativeduck.mrdaebak.databinding.ActivitySettingBinding

class SettingActivity : BaseActivity<ActivitySettingBinding>(ActivitySettingBinding::inflate) {

    private lateinit var menu: Menu
    private var reviseState = EDIT_COMPLETED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbar(binding.tbSetting, true)

        initClick()
        initData()
    }

    override fun initData() {
        // TODO 사용자 이름 및 주소 불러오기
        with(binding) {
            tvSettingName.setText("안광민")
            tvSettingAddress.setText("서울시 동대문구")
        }
        binding.tvSettingAddress.isEnabled = false
        binding.tvSettingName.isEnabled = false
    }
    
    override fun initClick() {
        binding.btnSettingRevise.setOnClickListener {
            // 수정 완료 상태라면, 수정중 상태로 변경
            if (reviseState == EDITING) {
                reviseUserInfo()
                binding.tvSettingAddress.isEnabled = false
                binding.tvSettingName.isEnabled = false
                reviseState = EDIT_COMPLETED
                binding.btnSettingRevise.apply {
                    backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(applicationContext, R.color.black)
                    )
                    setTextColor(getColor(R.color.white))
                    text = "정보 수정"
                }
            }
            else {
                binding.tvSettingAddress.isEnabled = true
                binding.tvSettingName.isEnabled = true
                reviseState = EDITING
                binding.btnSettingRevise.apply {
                    backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(applicationContext, R.color.kakao_yellow)
                    )
                    setTextColor(getColor(R.color.black))
                    text = "수정 완료"
                }
            }
        }
    }

    private fun reviseUserInfo() {
        // TODO 여기서 회원 정보 수정한 거 DB 에 반영하기
    }

    private fun withdrawal() {

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.menu_setting, this.menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.menu_setting_sign_out -> {
                // TODO 회원 탈퇴 로직

            }
            else -> return true
        }
        return super.onOptionsItemSelected(item)
    }
}