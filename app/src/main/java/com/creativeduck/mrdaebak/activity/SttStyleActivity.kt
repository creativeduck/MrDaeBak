package com.creativeduck.mrdaebak.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.MotionEvent.ACTION_DOWN
import android.view.Window
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.creativeduck.mrdaebak.R
import com.creativeduck.mrdaebak.config.ApplicationClass
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.DINNER_ENGLISH
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.DINNER_FRENCH
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.DINNER_TYPE
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.DINNER_VALENTINE
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.ORDER_BACON
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.ORDER_BREAD
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.ORDER_CHAMPAGNE
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.ORDER_COFFEE
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.ORDER_COFFEE_PORT
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.ORDER_EGG
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.ORDER_SALAD
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.ORDER_STEAK
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.ORDER_WINE
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.PERMISSION
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.STYLE_DELUXE
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.STYLE_GRAND
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.STYLE_SIMPLE
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.encryptionSharedPreferences
import com.creativeduck.mrdaebak.databinding.ActivitySttStyleBinding
import com.creativeduck.mrdaebak.entity.MenuModel
import com.creativeduck.mrdaebak.entity.OrderDto
import com.creativeduck.mrdaebak.entity.OrderModel
import com.creativeduck.mrdaebak.entity.UserDto
import com.creativeduck.mrdaebak.network.RemoteService
import com.creativeduck.mrdaebak.util.dispatch
import com.creativeduck.mrdaebak.util.getResponse
import com.creativeduck.mrdaebak.util.goActivityWithInt
import com.creativeduck.mrdaebak.util.moneyFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SttStyleActivity : BaseActivity<ActivitySttStyleBinding>(ActivitySttStyleBinding::inflate) {

    @Inject
    lateinit var service: RemoteService

    private lateinit var listener: RecognitionListener
    private lateinit var personalIntent: Intent
    private lateinit var speechRecognizer: SpeechRecognizer
    private var recording = false //녹음중인지 여부
    private val btnList = ArrayList<LinearLayoutCompat>()
    private var dinnerType = 0
    private lateinit var menuList: List<OrderModel>
    private lateinit var mrUserName: String
    private lateinit var mrUserAddress: String
    private lateinit var mrUserRank: String
    private var mrUserId = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dinnerType = intent.getIntExtra(DINNER_TYPE, DINNER_VALENTINE)

        checkRecordPermission() //녹음 퍼미션 체크
        initAdapter()

        mrUserId = encryptionSharedPreferences.getLong(ApplicationClass.MR_USER_ID, 0L)
        if (mrUserId == 0L)
            showCustomToast("오류가 발생했습니다.")
        // TODO 사용자 정보 가져오기
        service.getUser(mrUserId).getResponse(
            success = {
//                response ->
                // TODO 임시
                val response = UserDto("안광민", "서울시 동대문구", "브론즈")
                with(response) {
                    mrUserName = name
                    mrUserAddress = address
                    mrUserRank = rank
                }

                initView()
                initClick()
                initRecord()
            },
            failure = {
                showCustomToast("오류가 발생했습니다.")
                finish()
            }
        )
    }

    override fun initClick() {
        binding.fabSttDinerRecord.setOnClickListener {
            startRecord()
        }
    }

    private fun initView() {
        with(binding) {
            btnList.add(btnSttStyleSimple)
            btnList.add(btnSttStyleGrand)
            btnList.add(btnSttStyleDeluxe)

            btnState(btnSttStyleSimple)
            btnState(btnSttStyleGrand)
            btnState(btnSttStyleDeluxe)
        }

        menuList = listOf(
            OrderModel.OrderHeaderModel(1L, "애피타이저(Appetizer)"),
            OrderModel.OrderItemModel(
                2, MenuModel("샐러드", 7500, 0), false
            ),
            OrderModel.OrderItemModel(
                3, MenuModel("에그 스크램블", 7500, 0), false
            ),
            OrderModel.OrderItemModel(
                4, MenuModel( "베이컨", 7500, 0), false
            ),
            OrderModel.OrderItemModel(
                5, MenuModel( "바게트빵", 7500, 0), false
            ),
            OrderModel.OrderHeaderModel(6, "메인디시(Main Dish)"),
            OrderModel.OrderItemModel(
                7, MenuModel( "스테이크", 7500, 0), false
            ),
            OrderModel.OrderHeaderModel(8, "드링크(Drink)"),
            OrderModel.OrderItemModel(
                9, MenuModel( "와인", 7500, 0), false
            ),
            OrderModel.OrderItemModel(
                10, MenuModel( "샴페인", 7500, 0), false
            ),
            OrderModel.OrderItemModel(
                11, MenuModel("커피", 7500, 0), false
            ),
            OrderModel.OrderItemModel(
                12, MenuModel("커피 포트", 7500, 0), false
            ),
        )
        // TODO 사용자 정보 가져오기
        val response = UserDto("안광민", "서울시 동대문구", "브론즈")
        with(response) {
            mrUserName = name
            mrUserAddress = address
            mrUserRank = rank
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
                                binding.btnSttStyleSimple -> {
                                    startOrder(dinnerType, STYLE_SIMPLE)
                                }
                                binding.btnSttStyleGrand-> {
                                    startOrder(
                                        dinnerType,
                                        STYLE_GRAND
                                    )
                                }
                                binding.btnSttStyleDeluxe -> {
                                    startOrder(dinnerType, STYLE_DELUXE)
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
            override fun onBeginningOfSpeech() {
                //사용자가 말하기 시작
            }

            override fun onRmsChanged(v: Float) {}
            override fun onBufferReceived(bytes: ByteArray) {}
            override fun onEndOfSpeech() {
                //사용자가 말을 멈추면 호출
                //인식 결과에 따라 onError나 onResults가 호출됨
            }

            override fun onError(error: Int) {    //토스트 메세지로 에러 출력
                val message: String =
                    when (error) {
                        SpeechRecognizer.ERROR_AUDIO -> "오디오 오류"
                        SpeechRecognizer.ERROR_CLIENT -> return
                        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "권한 없음"
                        SpeechRecognizer.ERROR_NETWORK -> "네트워크 에러"
                        SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "네트워크 타임아웃"
                        SpeechRecognizer.ERROR_NO_MATCH -> {
                            //녹음을 오래하거나(?) stopListening을 호출하면 발생하는 에러
                            //speechRecognizer를 다시 생성하여 녹음 재개
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
                        "심플 스타일" -> btnSttStyleSimple.dispatch(700)
                        "심플스타일" -> btnSttStyleSimple.dispatch(700)
                        "그랜드 스타일" -> btnSttStyleGrand.dispatch(700)
                        "그랜드스타일" -> btnSttStyleGrand.dispatch(700)
                        "디럭스 스타일" -> btnSttStyleDeluxe.dispatch(700)
                        "디럭스스타일" -> btnSttStyleDeluxe.dispatch(700)
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

    private fun startOrder(dinnerType: Int, styleType: Int) {
        val dinner: String =
            when (dinnerType) {
                DINNER_VALENTINE -> {
                    "발렌타인 디너"
                }
                DINNER_FRENCH -> {
                    "프렌치 디너"
                }
                DINNER_ENGLISH -> {
                    "잉글리시 디너"
                }
                else -> {
                    "샴페인 축제 디너"
                }
            }
        val style: String =
            when (styleType) {
                STYLE_SIMPLE -> {
                    "심플 스타일"
                }
                STYLE_GRAND -> {
                    "그랜드 스타일"
                }
                else -> {
                    "디럭스 스타일"
                }
            }

        val orderDialog = Dialog(this)
        orderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        orderDialog.setContentView(R.layout.dialog_order)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(orderDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)))
        }

        val tvOrderDinner: TextView = orderDialog.findViewById(R.id.tv_dialog_order_dinner)
        tvOrderDinner.text = dinner

        val tvOrderStyle: TextView = orderDialog.findViewById(R.id.tv_dialog_order_style)
        tvOrderStyle.text = style

        val tvOrderList: TextView = orderDialog.findViewById(R.id.tv_dialog_order_list)

        val orderListText = StringBuilder()
        // TODO 스타일 가격 추가하기
        var totalPrice = 0L
        when (styleType) {
            STYLE_SIMPLE -> {
                totalPrice += 10000
            }
            STYLE_GRAND -> {
                totalPrice += 15000
            }
            STYLE_DELUXE -> {
                totalPrice += 20000
            }
        }
        val orderList: List<MenuModel>

        when (dinnerType) {
            DINNER_VALENTINE -> {
                orderList = getTotalMenu(ORDER_WINE, ORDER_STEAK)
            }
            DINNER_FRENCH -> {
                orderList = getTotalMenu(
                    ORDER_COFFEE, ORDER_WINE,
                    ORDER_SALAD, ORDER_STEAK
                )
            }
            DINNER_ENGLISH -> {
                orderList = getTotalMenu(
                    ORDER_EGG,
                    ORDER_BACON,
                    ORDER_BREAD, ORDER_STEAK
                )
            }

            else -> {
                (menuList[ORDER_BREAD] as OrderModel.OrderItemModel).menu.amount = 4
                orderList = getTotalMenu(
                    ORDER_CHAMPAGNE, ORDER_BREAD, ORDER_WINE, ORDER_STEAK,
                    ORDER_COFFEE_PORT
                )
            }
        }

        for (menu in orderList) {
            val text = menu.name + " ${menu.amount}개"
            orderListText.append(text)
            orderListText.append('\n')
            totalPrice += menu.price
        }
        // TODO 각 스타일에 맞게 메뉴리스트 추가하기
        if (orderListText.lastIndex > 0)
            orderListText.deleteAt(orderListText.lastIndex)

        totalPrice /= 10
        totalPrice *= when (mrUserRank) {
            "브론즈" -> {
                9
            }
            else -> {
                8
            }
        }

        val tvOrderPrice: TextView = orderDialog.findViewById(R.id.tv_dialog_order_price)
        tvOrderPrice.text = "총 주문 가격 : " + totalPrice.moneyFormat()

        tvOrderList.text = orderListText.toString()

        val btnOrderOkay: TextView = orderDialog.findViewById(R.id.btn_dialog_order_okay)
        btnOrderOkay.setOnClickListener {
            // 파라미터 어떻게 전달할까
            orderFood(dinner, style, orderList, totalPrice)
            orderDialog.dismiss()
        }

        val btnOrderNo: TextView = orderDialog.findViewById(R.id.btn_dialog_order_no)
        btnOrderNo.setOnClickListener {
            orderDialog.dismiss()
        }
        orderDialog.show()
    }

    private fun getTotalMenu(vararg index: Int): List<MenuModel> {
        val list = ArrayList<MenuModel>()
        for (idx in index) {
            with((menuList[idx] as OrderModel.OrderItemModel).menu) {
                list.add(MenuModel(name, price, amount))
            }
        }
        return list
    }

    private fun orderFood(dinner: String, style: String, orderList: List<MenuModel>, totalPrice: Long) {
        val requestDto = OrderDto(
            dinner = dinner,
            style = style,
            menuList = orderList,
            totalPrice = totalPrice,
            address = mrUserAddress
        )
        // TODO 주문 데이터 POST 하기
        service.order(mrUserId, requestDto).getResponse(
            success = {
                goActivityWithInt<DinnerActivity>(clear = true)
            },
            failure = {
                showCustomToast("오류가 발생했습니다.")
            }
        )
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