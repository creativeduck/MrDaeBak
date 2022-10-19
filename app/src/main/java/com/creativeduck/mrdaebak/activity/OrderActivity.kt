package com.creativeduck.mrdaebak.activity

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.TextView
import com.creativeduck.mrdaebak.ApplicationClass.Companion.DINNER_ENGLISH
import com.creativeduck.mrdaebak.ApplicationClass.Companion.DINNER_FRENCH
import com.creativeduck.mrdaebak.ApplicationClass.Companion.DINNER_TYPE
import com.creativeduck.mrdaebak.ApplicationClass.Companion.DINNER_VALENTINE
import com.creativeduck.mrdaebak.ApplicationClass.Companion.ORDER_BACON
import com.creativeduck.mrdaebak.ApplicationClass.Companion.ORDER_BREAD
import com.creativeduck.mrdaebak.ApplicationClass.Companion.ORDER_CHAMPAGNE
import com.creativeduck.mrdaebak.ApplicationClass.Companion.ORDER_COFFEE
import com.creativeduck.mrdaebak.ApplicationClass.Companion.ORDER_COFFEE_PORT
import com.creativeduck.mrdaebak.ApplicationClass.Companion.ORDER_EGG
import com.creativeduck.mrdaebak.ApplicationClass.Companion.ORDER_SALAD
import com.creativeduck.mrdaebak.ApplicationClass.Companion.ORDER_STEAK
import com.creativeduck.mrdaebak.ApplicationClass.Companion.ORDER_WINE
import com.creativeduck.mrdaebak.ApplicationClass.Companion.STYLE_GRAND
import com.creativeduck.mrdaebak.ApplicationClass.Companion.STYLE_SIMPLE
import com.creativeduck.mrdaebak.ApplicationClass.Companion.STYLE_TYPE
import com.creativeduck.mrdaebak.R
import com.creativeduck.mrdaebak.adapter.OrderAdapter
import com.creativeduck.mrdaebak.databinding.ActivityOrderBinding
import com.creativeduck.mrdaebak.model.OrderModel
import com.creativeduck.mrdaebak.service.RemoteService
import com.creativeduck.mrdaebak.util.OnItemClickListener
import com.creativeduck.mrdaebak.util.getResponse
import com.creativeduck.mrdaebak.util.goActivityWithInt
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class OrderActivity : BaseActivity<ActivityOrderBinding>(ActivityOrderBinding::inflate) {

    @Inject
    lateinit var service: RemoteService

    private lateinit var orderAdapter: OrderAdapter
    private lateinit var modelList: List<OrderModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val dinnerType = intent.getIntExtra(DINNER_TYPE, DINNER_VALENTINE)
        val styleType = intent.getIntExtra(STYLE_TYPE, STYLE_SIMPLE)

        initList()
        initText(dinnerType, styleType)
        initSelectedOrder(dinnerType, styleType)
        initClick()
        initAdapter()
    }

    private fun initList() {
        modelList = listOf(
            OrderModel.OrderHeaderModel("애피타이저"),
            OrderModel.OrderItemModel(
                1, "샐러드", 7500, 0, false
            ),
            OrderModel.OrderItemModel(
                2, "에그 스크램블", 7500, 0, false
            ),
            OrderModel.OrderItemModel(
                3, "베이컨", 7500, 0, false
            ),
            OrderModel.OrderItemModel(
                4, "바게트빵", 7500, 0, false
            ),
            OrderModel.OrderHeaderModel("메인디시"),
            OrderModel.OrderItemModel(
                5, "스테이크", 7500, 0, false
            ),
            OrderModel.OrderHeaderModel("드링크"),
            OrderModel.OrderItemModel(
                6, "와인", 7500, 0, false
            ),
            OrderModel.OrderItemModel(
                7, "샴페인", 7500, 0, false
            ),
            OrderModel.OrderItemModel(
                8, "커피", 7500, 0, false
            ),
            OrderModel.OrderItemModel(
               9,  "커피포트", 7500, 0, false
            ),
        )
    }

    private fun initText(dinnerType: Int, styleType: Int) {
        // 텍스트 설정
        with(binding) {
            tvOrderDinner.text =
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
                        "샴페인 디너"
                    }
                }
            tvOrderStyle.text =
                when (styleType) {
                    STYLE_SIMPLE -> {
                        "심플 디너"
                    }
                    STYLE_GRAND -> {
                        "그랜드 디너"
                    }
                    else -> {
                        "디럭스 디너"
                    }
                }
        }
    }

    private fun setMenuChecked(vararg pos: Int) {
        for(p in pos) {
            if (modelList[p] is OrderModel.OrderItemModel)
                (modelList[p] as OrderModel.OrderItemModel).run {
                    isChecked = true
                    amount = 1
                }
        }
    }

    private fun initSelectedOrder(dinnerType: Int, styleType: Int) {
        // 메뉴 설정
        when (dinnerType) {
            DINNER_VALENTINE -> {
                setMenuChecked(ORDER_WINE, ORDER_STEAK)
            }
            DINNER_FRENCH -> {
                setMenuChecked(ORDER_COFFEE, ORDER_WINE, ORDER_SALAD, ORDER_STEAK)
            }
            DINNER_ENGLISH -> {
                setMenuChecked(ORDER_EGG, ORDER_BACON, ORDER_BREAD, ORDER_STEAK)
            }
            else -> {
                setMenuChecked(ORDER_CHAMPAGNE, ORDER_BREAD, ORDER_WINE, ORDER_STEAK, ORDER_COFFEE_PORT)
                (modelList[ORDER_BREAD] as OrderModel.OrderItemModel).amount = 4
            }
        }
        when (styleType) {
            STYLE_SIMPLE -> {
                "심플 디너"
            }
            STYLE_GRAND -> {
                "그랜드 디너"
            }
            else -> {
                "디럭스 디너"
            }
        }
    }

    override fun initClick() {
        binding.btnOrder.setOnClickListener {
            showOrderDialog()
        }
    }

    private fun orderFood() {
        // TODO 주문 데이터 POST 하기
        service.listPokemons().getResponse(
            success = { body ->
                for(result in body.results) {
                    Log.d("HELLO", result.name + " / " + result.url)
                }
                val orderList = modelList.filter {
                    (it is OrderModel.OrderItemModel) && (it.isChecked)
                }.map { it as OrderModel.OrderItemModel }
                Log.d("HELLO", orderList.toString());

                // TODO 주문 상세 내역 화면으로 이동 -> 일단 메인 화면으로 이동하자
                goActivityWithInt<DinnerActivity>(clear = true)
            },
            failure = {
                Log.d("HELLO", it.message.toString())
            }
        )
    }

    override fun initAdapter() {
        orderAdapter = OrderAdapter(
            object : OnItemClickListener {
                override fun onItemClicked(view: View, pos: Int) {
                    val item = orderAdapter.currentList[pos] as OrderModel.OrderItemModel
                    item.amount++
                    orderAdapter.notifyItemChanged(pos)
                }
            },
            object : OnItemClickListener {
                override fun onItemClicked(view: View, pos: Int) {
                    val item = orderAdapter.currentList[pos] as OrderModel.OrderItemModel
                    if (item.amount > 0)
                        item.amount--
                    orderAdapter.notifyItemChanged(pos)
                }
            }
        ) {
            val item = orderAdapter.currentList[it] as OrderModel.OrderItemModel
            item.isChecked = !item.isChecked
            orderAdapter.notifyItemChanged(it)
        }
        binding.recyclerOrder.apply {
            adapter = orderAdapter
            itemAnimator = null
        }
        orderAdapter.submitList(modelList)
    }

    private fun showOrderDialog() {
        val orderDialog = Dialog(this)
        orderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        orderDialog.setContentView(R.layout.dialog_order)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            Objects.requireNonNull(orderDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)))
        }

        val btnOrderOkay : TextView = orderDialog.findViewById(R.id.btn_order_okay)
        btnOrderOkay.setOnClickListener {
            orderFood()
            orderDialog.dismiss()
        }

        val btnOrderNo : TextView = orderDialog.findViewById(R.id.btn_order_no)
        btnOrderNo.setOnClickListener {
            orderDialog.dismiss()
        }

        orderDialog.show()
    }
}