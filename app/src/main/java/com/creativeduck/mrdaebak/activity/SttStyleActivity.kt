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
import com.creativeduck.mrdaebak.data.MenuModel
import com.creativeduck.mrdaebak.data.OrderDto
import com.creativeduck.mrdaebak.data.OrderModel
import com.creativeduck.mrdaebak.data.UserDto
import com.creativeduck.mrdaebak.data.RemoteService
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
    private var recording = false //??????????????? ??????
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

        checkRecordPermission() //?????? ????????? ??????
        initAdapter()

        mrUserId = encryptionSharedPreferences.getLong(ApplicationClass.MR_USER_ID, 0L)
        if (mrUserId == 0L)
            showCustomToast("????????? ??????????????????.")
        // TODO ????????? ?????? ????????????
        service.getUser(mrUserId).getResponse(
            success = {
//                response ->
                // TODO ??????
                val response = UserDto("?????????", "????????? ????????????", "?????????")
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
                showCustomToast("????????? ??????????????????.")
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
            OrderModel.OrderHeaderModel(1L, "???????????????(Appetizer)"),
            OrderModel.OrderItemModel(
                2, MenuModel("?????????", 7500, 0), false
            ),
            OrderModel.OrderItemModel(
                3, MenuModel("?????? ????????????", 7500, 0), false
            ),
            OrderModel.OrderItemModel(
                4, MenuModel( "?????????", 7500, 0), false
            ),
            OrderModel.OrderItemModel(
                5, MenuModel( "????????????", 7500, 0), false
            ),
            OrderModel.OrderHeaderModel(6, "????????????(Main Dish)"),
            OrderModel.OrderItemModel(
                7, MenuModel( "????????????", 7500, 0), false
            ),
            OrderModel.OrderHeaderModel(8, "?????????(Drink)"),
            OrderModel.OrderItemModel(
                9, MenuModel( "??????", 7500, 0), false
            ),
            OrderModel.OrderItemModel(
                10, MenuModel( "?????????", 7500, 0), false
            ),
            OrderModel.OrderItemModel(
                11, MenuModel("??????", 7500, 0), false
            ),
            OrderModel.OrderItemModel(
                12, MenuModel("?????? ??????", 7500, 0), false
            ),
        )
        // TODO ????????? ?????? ????????????
        val response = UserDto("?????????", "????????? ????????????", "?????????")
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
                            // TODO ??? ????????? ?????? ???????????? ?????? ???
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
        //???????????? ?????? intent ?????? ??????
        personalIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        personalIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)
        personalIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR") //?????????

        listener = object : RecognitionListener {
            override fun onReadyForSpeech(bundle: Bundle) {}
            override fun onBeginningOfSpeech() {
                //???????????? ????????? ??????
            }

            override fun onRmsChanged(v: Float) {}
            override fun onBufferReceived(bytes: ByteArray) {}
            override fun onEndOfSpeech() {
                //???????????? ?????? ????????? ??????
                //?????? ????????? ?????? onError??? onResults??? ?????????
            }

            override fun onError(error: Int) {    //????????? ???????????? ?????? ??????
                val message: String =
                    when (error) {
                        SpeechRecognizer.ERROR_AUDIO -> "????????? ??????"
                        SpeechRecognizer.ERROR_CLIENT -> return
                        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "?????? ??????"
                        SpeechRecognizer.ERROR_NETWORK -> "???????????? ??????"
                        SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "???????????? ????????????"
                        SpeechRecognizer.ERROR_NO_MATCH -> {
                            //????????? ???????????????(?) stopListening??? ???????????? ???????????? ??????
                            //speechRecognizer??? ?????? ???????????? ?????? ??????
                            if (recording) startRecord()
                            return  //????????? ????????? ?????? X
                        }
                        SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RECOGNIZER??? ??????"
                        SpeechRecognizer.ERROR_SERVER -> "??????"
                        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "????????? ????????????"
                        else -> "??? ??? ??????"
                    }
                showCustomToast("$message ????????? ??????????????????.")
            }

            //?????? ????????? ???????????? ??????
            override fun onResults(bundle: Bundle) {
                val matches =
                    bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION) //?????? ????????? ?????? ArrayList

                //?????? ??????
                var newText = ""
                for (i in matches!!.indices) {
                    newText += matches[i]
                }
                with(binding) {
                    tvSttRecord.text = newText
                    when (newText) {
                        "?????? ?????????" -> btnSttStyleSimple.dispatch(700)
                        "???????????????" -> btnSttStyleSimple.dispatch(700)
                        "????????? ?????????" -> btnSttStyleGrand.dispatch(700)
                        "??????????????????" -> btnSttStyleGrand.dispatch(700)
                        "????????? ?????????" -> btnSttStyleDeluxe.dispatch(700)
                        "??????????????????" -> btnSttStyleDeluxe.dispatch(700)
                        else -> showCustomToast(newText)
                    }
                }
            }

            override fun onPartialResults(bundle: Bundle) {}
            override fun onEvent(i: Int, bundle: Bundle) {}
        }
    }

    //?????? ??????
    fun startRecord() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(applicationContext)
        speechRecognizer.setRecognitionListener(listener)
        speechRecognizer.startListening(intent)
        binding.tvSttRecord.text = "???????????? ???..."
    }

    private fun startOrder(dinnerType: Int, styleType: Int) {
        val dinner: String =
            when (dinnerType) {
                DINNER_VALENTINE -> {
                    "???????????? ??????"
                }
                DINNER_FRENCH -> {
                    "????????? ??????"
                }
                DINNER_ENGLISH -> {
                    "???????????? ??????"
                }
                else -> {
                    "????????? ?????? ??????"
                }
            }
        val style: String =
            when (styleType) {
                STYLE_SIMPLE -> {
                    "?????? ?????????"
                }
                STYLE_GRAND -> {
                    "????????? ?????????"
                }
                else -> {
                    "????????? ?????????"
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
        // TODO ????????? ?????? ????????????
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
            val text = menu.name + " ${menu.amount}???"
            orderListText.append(text)
            orderListText.append('\n')
            totalPrice += menu.price
        }
        // TODO ??? ???????????? ?????? ??????????????? ????????????
        if (orderListText.lastIndex > 0)
            orderListText.deleteAt(orderListText.lastIndex)

        totalPrice /= 10
        totalPrice *= when (mrUserRank) {
            "?????????" -> {
                9
            }
            else -> {
                8
            }
        }

        val tvOrderPrice: TextView = orderDialog.findViewById(R.id.tv_dialog_order_price)
        tvOrderPrice.text = "??? ?????? ?????? : " + totalPrice.moneyFormat()

        tvOrderList.text = orderListText.toString()

        val btnOrderOkay: TextView = orderDialog.findViewById(R.id.btn_dialog_order_okay)
        btnOrderOkay.setOnClickListener {
            // ???????????? ????????? ????????????
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
        // TODO ?????? ????????? POST ??????
        service.order(mrUserId, requestDto).getResponse(
            success = {
                goActivityWithInt<DinnerActivity>(clear = true)
            },
            failure = {
                showCustomToast("????????? ??????????????????.")
            }
        )
    }

    private fun checkRecordPermission() {
        //??????????????? ????????? 6.0 ??????
        if (Build.VERSION.SDK_INT >= 23) {
            //??????????????? ?????? ????????? ????????? ?????? ??????
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