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
        // 권한
        const val PERMISSION = 1
        // 주문
        const val ORDER_LIST_ITEM_ID = "order_list_item_id"
        // 디너
        const val DINNER_VALENTINE = 0
        const val DINNER_FRENCH = 1
        const val DINNER_ENGLISH = 2
        const val DINNER_CHAMPAGNE = 3
        const val DINNER_TYPE = "dinner type"
        // 설정
        const val EDIT_COMPLETED = 0
        const val EDITING = 1
        // 스타일
        const val STYLE_SIMPLE = 0
        const val STYLE_GRAND = 1
        const val STYLE_DELUXE = 2
        const val STYLE_TYPE = "style type"

        // 음식
        const val ORDER_SALAD = 1
        const val ORDER_EGG = 2
        const val ORDER_BACON = 3
        const val ORDER_BREAD = 4
        const val ORDER_STEAK = 6
        const val ORDER_WINE = 8
        const val ORDER_CHAMPAGNE = 9
        const val ORDER_COFFEE = 10
        const val ORDER_COFFEE_PORT = 11

        // 목록 또는 내 목록 상태
        const val SHOW_STATE_LIST = 0
        const val SHOW_STATE_ME = 1

        // 역할
        const val ROLE_USER = 0
        const val ROLE_MANAGER = 1
        const val ROLE_COOK = 2
        const val ROLE_RIDER = 3
    }
}