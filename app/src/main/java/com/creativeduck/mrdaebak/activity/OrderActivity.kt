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
import com.creativeduck.mrdaebak.R
import com.creativeduck.mrdaebak.activity.DinnerActivity.Companion.DINNER_TYPE
import com.creativeduck.mrdaebak.activity.DinnerActivity.Companion.ENGLISH
import com.creativeduck.mrdaebak.activity.DinnerActivity.Companion.FRENCH
import com.creativeduck.mrdaebak.activity.DinnerActivity.Companion.VALENTINE
import com.creativeduck.mrdaebak.activity.StyleActivity.Companion.GRAND
import com.creativeduck.mrdaebak.activity.StyleActivity.Companion.SIMPLE
import com.creativeduck.mrdaebak.activity.StyleActivity.Companion.STYLE_TYPE
import com.creativeduck.mrdaebak.adapter.OrderAdapter
import com.creativeduck.mrdaebak.databinding.ActivityOrderBinding
import com.creativeduck.mrdaebak.model.OrderModel
import com.creativeduck.mrdaebak.service.RemoteService
import com.creativeduck.mrdaebak.util.OnItemClickListener
import com.creativeduck.mrdaebak.util.getResponse
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class OrderActivity : BaseActivity<ActivityOrderBinding>(ActivityOrderBinding::inflate) {

    companion object {
        const val SALAD = 1
        const val EGG = 2
        const val BACON = 3
        const val BREAD = 4
        const val STEAK = 6
        const val WINE = 8
        const val CHAMPAGNE = 9
        const val COFFEE = 10
        const val COFFEE_PORT = 11
    }

    @Inject
    lateinit var service: RemoteService

    private lateinit var orderListAdapter: OrderAdapter
    private lateinit var modelList: List<OrderModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val dinnerType = intent.getIntExtra(DINNER_TYPE, VALENTINE)
        val styleType = intent.getIntExtra(STYLE_TYPE, SIMPLE)

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
                    VALENTINE -> {
                        "발렌타인 디너"
                    }
                    FRENCH -> {
                        "프렌치 디너"
                    }
                    ENGLISH -> {
                        "잉글리시 디너"
                    }
                    else -> {
                        "샴페인 디너"
                    }
                }
            tvOrderStyle.text =
                when (styleType) {
                    SIMPLE -> {
                        "심플 디너"
                    }
                    GRAND -> {
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
            VALENTINE -> {
                setMenuChecked(WINE, STEAK)
            }
            FRENCH -> {
                setMenuChecked(COFFEE, WINE, SALAD, STEAK)
            }
            ENGLISH -> {
                setMenuChecked(EGG, BACON, BREAD, STEAK)
            }
            else -> {
                setMenuChecked(CHAMPAGNE, BREAD, WINE, STEAK, COFFEE_PORT)
                (modelList[BREAD] as OrderModel.OrderItemModel).amount = 4
            }
        }
        when (styleType) {
            SIMPLE -> {
                "심플 디너"
            }
            GRAND -> {
                "그랜드 디너"
            }
            else -> {
                "디럭스 디너"
            }
        }
    }

    private fun initClick() {
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
                startNewActivityAndClear<DinnerActivity>()
            },
            failure = {
                Log.d("HELLO", it.message.toString())
            }
        )
    }

    private fun initAdapter() {
        orderListAdapter = OrderAdapter(
            object : OnItemClickListener {
                override fun onItemClicked(view: View, pos: Int) {
                    val item = orderListAdapter.currentList[pos] as OrderModel.OrderItemModel
                    item.amount++
                    orderListAdapter.notifyItemChanged(pos)
                }
            },
            object : OnItemClickListener {
                override fun onItemClicked(view: View, pos: Int) {
                    val item = orderListAdapter.currentList[pos] as OrderModel.OrderItemModel
                    if (item.amount > 0)
                        item.amount--
                    orderListAdapter.notifyItemChanged(pos)
                }
            }
        ) {
            val item = orderListAdapter.currentList[it] as OrderModel.OrderItemModel
            item.isChecked = !item.isChecked
            orderListAdapter.notifyItemChanged(it)
        }
        binding.recyclerOrder.apply {
            adapter = orderListAdapter
            itemAnimator = null
        }
        orderListAdapter.submitList(modelList)
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