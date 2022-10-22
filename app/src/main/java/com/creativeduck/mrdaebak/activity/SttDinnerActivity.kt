package com.creativeduck.mrdaebak.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.MotionEvent.ACTION_DOWN
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.DINNER_CHAMPAGNE
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.DINNER_ENGLISH
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.DINNER_FRENCH
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.DINNER_TYPE
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.DINNER_VALENTINE
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.PERMISSION
import com.creativeduck.mrdaebak.databinding.ActivitySttDinnerBinding
import com.creativeduck.mrdaebak.util.dispatch
import com.creativeduck.mrdaebak.util.goActivityWithInt
import kotlinx.coroutines.*

class SttDinnerActivity :
    BaseActivity<ActivitySttDinnerBinding>(ActivitySttDinnerBinding::inflate) {

    private lateinit var listener: RecognitionListener
    private lateinit var personalIntent: Intent
    private lateinit var speechRecognizer: SpeechRecognizer
    private var recording = false //녹음중인지 여부
    private val btnList = ArrayList<LinearLayoutCompat>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkRecordPermission() //녹음 퍼미션 체크

        initClick()
        initRecord()
    }

    override fun initClick() {
        with(binding) {
            fabSttDinerRecord.setOnClickListener {
                startRecord()
            }

            btnList.add(btnDinnerValentine)
            btnList.add(btnDinnerEnglish)
            btnList.add(btnDinnerFrench)
            btnList.add(btnDinnerChampagne)

            btnState(btnDinnerValentine)
            btnState(btnDinnerEnglish)
            btnState(btnDinnerFrench)
            btnState(btnDinnerChampagne)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun btnState(button: LinearLayoutCompat) {
        button.setOnTouchListener { _, e ->
            if (e.action == ACTION_DOWN) {
                for (btn in btnList) {
                    btn.isPressed = btn == button
                    if (btn.isPressed) {
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(500)
                            // TODO 각 버튼에 따라 이동하는 로직 추
                            when (btn) {
                                binding.btnDinnerValentine -> {
                                    goActivityWithInt<SttStyleActivity>(
                                        Pair(
                                            DINNER_TYPE,
                                            DINNER_VALENTINE
                                        )
                                    )
                                }
                                binding.btnDinnerFrench -> {
                                    goActivityWithInt<SttStyleActivity>(
                                        Pair(
                                            DINNER_TYPE,
                                            DINNER_FRENCH
                                        )
                                    )
                                }
                                binding.btnDinnerChampagne -> {
                                    goActivityWithInt<SttStyleActivity>(
                                        Pair(
                                            DINNER_TYPE,
                                            DINNER_CHAMPAGNE
                                        )
                                    )
                                }
                                binding.btnDinnerEnglish -> {
                                    goActivityWithInt<SttStyleActivity>(
                                        Pair(
                                            DINNER_TYPE,
                                            DINNER_ENGLISH
                                        )
                                    )
                                }
                            }
                            cancel()
                        }
                    }
                }
            }
            true
        }
    }

    private fun initRecord() {
        //녹음하기 위해 intent 객체 생성
        personalIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        personalIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)
        personalIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR") //한국어

        listener = object : RecognitionListener {
            override fun onReadyForSpeech(bundle: Bundle) {}
            override fun onBeginningOfSpeech() {}

            override fun onRmsChanged(v: Float) {}
            override fun onBufferReceived(bytes: ByteArray) {}
            override fun onEndOfSpeech() {}

            override fun onError(error: Int) {    //토스트 메세지로 에러 출력
                val message: String =
                    when (error) {
                        SpeechRecognizer.ERROR_AUDIO -> "오디오 오류"
                        SpeechRecognizer.ERROR_CLIENT -> return
                        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "권한 없음"
                        SpeechRecognizer.ERROR_NETWORK -> "네트워크 에러"
                        SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "네트워크 타임아웃"
                        SpeechRecognizer.ERROR_NO_MATCH -> {
                            if (recording) startRecord()
                            return  //토스트 메세지 출력 X
                        }
                        SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RECOGNIZER가 바쁨"
                        SpeechRecognizer.ERROR_SERVER -> "서버"
                        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "말하기 시간초과"
                        else -> "알 수 없는"
                    }
                showCustomToast("$message 오류가 발생했습니다.")
            }

            //인식 결과가 준비되면 호출
            override fun onResults(bundle: Bundle) {
                val matches =
                    bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION) //인식 결과를 담은 ArrayList

                //인식 결과
                var newText = ""
                for (i in matches!!.indices) {
                    newText += matches[i]
                }
                with(binding) {
                    tvSttRecord.text = newText
                    when (newText) {
                        "잉글리시 디너", "잉글리쉬 디너" -> btnDinnerEnglish.dispatch(700)
                        "프렌치 디너" -> btnDinnerFrench.dispatch(700)
                        "발렌타인 디너" -> btnDinnerValentine.dispatch(700)
                        "샴페인 축제 디너" -> btnDinnerChampagne.dispatch(700)
                        else -> showCustomToast(newText)
                    }
                }
            }

            override fun onPartialResults(bundle: Bundle) {}
            override fun onEvent(i: Int, bundle: Bundle) {}
        }

    }

    //녹음 시작
    fun startRecord() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(applicationContext)
        speechRecognizer.setRecognitionListener(listener)
        speechRecognizer.startListening(intent)
        binding.tvSttRecord.text = "음성인식 중..."
    }

    private fun checkRecordPermission() {
        //안드로이드 버전이 6.0 이상
        if (Build.VERSION.SDK_INT >= 23) {
            //인터넷이나 녹음 권한이 없으면 권한 요청
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.INTERNET
                ) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_DENIED
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.INTERNET,
                        Manifest.permission.RECORD_AUDIO
                    ), PERMISSION
                )
            }
        }
    }
}