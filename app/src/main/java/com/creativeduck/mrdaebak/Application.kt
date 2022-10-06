package com.creativeduck.mrdaebak

import android.app.Application
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ApplicationClass: Application() {

    override fun onCreate() {
        super.onCreate()
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
        encryptionSharedPreferences = EncryptedSharedPreferences.create(
            "important_data",
            masterKeyAlias,
            this,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        KakaoSdk.init(this, BuildConfig.kakao_native_app_key)
    }

    companion object {
        lateinit var encryptionSharedPreferences : SharedPreferences

        // 암호화 필요한 정보
        const val KAKAO_ACCESS_TOKEN = "kakao_access_token"
    }
}