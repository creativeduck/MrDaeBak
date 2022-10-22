package com.creativeduck.mrdaebak.activity

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.creativeduck.mrdaebak.R
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.EDITING
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.EDIT_COMPLETED
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.MR_USER_ID
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.encryptionSharedPreferences
import com.creativeduck.mrdaebak.databinding.ActivitySettingBinding
import com.creativeduck.mrdaebak.entity.UserDto
import com.creativeduck.mrdaebak.network.RemoteService
import com.creativeduck.mrdaebak.util.getResponse
import com.creativeduck.mrdaebak.util.goActivityWithInt
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingActivity : BaseActivity<ActivitySettingBinding>(ActivitySettingBinding::inflate) {

    @Inject
    lateinit var service: RemoteService
    private lateinit var menu: Menu
    private var reviseState = EDIT_COMPLETED
    private var mrUserId = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbar(binding.tbSetting, true)

        mrUserId = encryptionSharedPreferences.getLong(MR_USER_ID, 0L)

        initClick()
        initData()
    }

    override fun initData() {
        // TODO 사용자 이름 및 주소 불러오기
        service.getUser(mrUserId).getResponse(
            success = {
//                response ->
                val response = UserDto("안광민", "서울시 동대문구", "브론즈")

                with(binding) {
                    tvSettingName.setText(response.name)
                    tvSettingAddress.setText(response.address)
                    tvSettingRank.text = response.rank
                }
                binding.tvSettingAddress.isEnabled = false
                binding.tvSettingName.isEnabled = false
            },
            failure = {
                showCustomToast("오류가 발생했습니다.")
            }
        )
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
        val requestDto = UserDto(
            binding.tvSettingName.text.toString(),
            binding.tvSettingAddress.text.toString()
        )
        service.reviseUser(mrUserId, requestDto).getResponse(
            success = { response ->
                with(binding) {
                    tvSettingName.setText(response.name)
                    tvSettingAddress.setText(response.address)
                    tvSettingRank.text = response.rank
                }
            },
            failure = {
                showCustomToast("오류가 발생했습니다.")
            }
        )

    }

    // TODO 탈퇴 로직 연동하기
    private fun withdrawal() {
        service.withdrawal(mrUserId).getResponse(
            success = {
                goActivityWithInt<LoginActivity>(clear = true)
            },
            failure = {
                showCustomToast("오류가 발생했습니다.")
            }
        )
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
                withdrawal()
            }
            else -> return true
        }
        return super.onOptionsItemSelected(item)
    }
}