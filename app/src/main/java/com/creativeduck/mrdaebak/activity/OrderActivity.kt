package com.creativeduck.mrdaebak.activity

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.creativeduck.mrdaebak.R
import com.creativeduck.mrdaebak.adapter.OrderAdapter
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.DINNER_CHAMPAGNE_NAME
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.DINNER_ENGLISH
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.DINNER_ENGLISH_NAME
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.DINNER_FRENCH
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.DINNER_FRENCH_NAME
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.DINNER_TYPE
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.DINNER_VALENTINE
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.DINNER_VALENTINE_NAME
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.MR_USER_ID
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.ORDER_BACON
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.ORDER_BREAD
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.ORDER_CHAMPAGNE
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.ORDER_COFFEE
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.ORDER_COFFEE_PORT
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.ORDER_EGG
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.ORDER_SALAD
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.ORDER_STEAK
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.ORDER_WINE
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.STYLE_DELUXE
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.STYLE_DELUXE_NAME
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.STYLE_GRAND
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.STYLE_GRAND_NAME
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.STYLE_SIMPLE
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.STYLE_SIMPLE_NAME
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.STYLE_TYPE
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.encryptionSharedPreferences
import com.creativeduck.mrdaebak.databinding.ActivityOrderBinding
import com.creativeduck.mrdaebak.data.MenuModel
import com.creativeduck.mrdaebak.data.OrderDto
import com.creativeduck.mrdaebak.data.OrderModel
import com.creativeduck.mrdaebak.data.UserDto
import com.creativeduck.mrdaebak.data.RemoteService
import com.creativeduck.mrdaebak.util.OnItemClickListener
import com.creativeduck.mrdaebak.util.getResponse
import com.creativeduck.mrdaebak.util.goActivityWithInt
import com.creativeduck.mrdaebak.util.moneyFormat
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class OrderActivity : BaseActivity<ActivityOrderBinding>(ActivityOrderBinding::inflate) {

    @Inject
    lateinit var service: RemoteService

    private var dinnerType = 0
    private var styleType = 0
    private lateinit var mrUserName: String
    private lateinit var mrUserAddress: String
    private lateinit var mrUserRank: String
    private var mrUserId = 0L
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var menuList: List<OrderModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        dinnerType = intent.getIntExtra(DINNER_TYPE, DINNER_VALENTINE)
        styleType = intent.getIntExtra(STYLE_TYPE, STYLE_SIMPLE)

        mrUserId = encryptionSharedPreferences.getLong(MR_USER_ID, 0L)
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
                initAdapter()
            },
            failure = {
                showCustomToast("오류가 발생했습니다.")
                finish()
            }
        )
    }

    private fun initView() {
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

        // 텍스트 설정
        with(binding) {
            tvOrderDinner.text =
                when (dinnerType) {
                    DINNER_VALENTINE -> {
                        DINNER_VALENTINE_NAME
                    }
                    DINNER_FRENCH -> {
                        DINNER_FRENCH_NAME
                    }
                    DINNER_ENGLISH -> {
                        DINNER_ENGLISH_NAME
                    }
                    else -> {
                        DINNER_CHAMPAGNE_NAME
                    }
                }
            tvOrderStyle.text =
                when (styleType) {
                    STYLE_SIMPLE -> {
                        STYLE_SIMPLE_NAME
                    }
                    STYLE_GRAND -> {
                        STYLE_GRAND_NAME
                    }
                    else -> {
                        STYLE_DELUXE_NAME
                    }
                }
        }
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
                setMenuChecked(
                    ORDER_CHAMPAGNE,
                    ORDER_BREAD,
                    ORDER_WINE,
                    ORDER_STEAK,
                    ORDER_COFFEE_PORT
                )
                (menuList[ORDER_BREAD] as OrderModel.OrderItemModel).menu.amount = 4
            }
        }
    }

    private fun setMenuChecked(vararg pos: Int) {
        for (p in pos) {
            if (menuList[p] is OrderModel.OrderItemModel)
                (menuList[p] as OrderModel.OrderItemModel).run {
                    isChecked = true
                    menu.amount = 1
                }
        }
    }

    override fun initClick() {
        binding.btnOrder.setOnClickListener {
            showOrderDialog()
        }
    }

    // 이때 파라미터 전달받기
    private fun orderFood() {
        var totalPrice = 0L
        // TODO 스타일 가격 추가하기
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
        val menuRequestList = ArrayList<MenuModel>()
        for (menu in menuList) {
            if (menu is OrderModel.OrderItemModel && menu.isChecked) {
                menuRequestList.add(MenuModel(menu.menu.name, menu.menu.price, menu.menu.amount))
                totalPrice += menu.menu.price
            }
        }
        totalPrice /= 10
        totalPrice *= when (mrUserRank) {
            "브론즈" -> {
                9
            }
            else -> {
                8
            }
        }
        // TODO 데이터 형식 정하기
        val requestDto = OrderDto(
            dinner = binding.tvOrderDinner.text.toString(),
            style = binding.tvOrderStyle.text.toString(),
            menuList = menuRequestList,
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

    override fun initAdapter() {
        orderAdapter = OrderAdapter(
            object : OnItemClickListener {
                override fun onItemClicked(view: View, pos: Int) {
                    val item = orderAdapter.currentList[pos] as OrderModel.OrderItemModel
                    item.menu.amount++
                    if (!item.isChecked)
                        item.isChecked = true
                    orderAdapter.notifyItemChanged(pos)
                }
            },
            object : OnItemClickListener {
                override fun onItemClicked(view: View, pos: Int) {
                    val item = orderAdapter.currentList[pos] as OrderModel.OrderItemModel
                    if (item.menu.amount > 0)
                        item.menu.amount--
                    if (item.menu.amount == 0)
                        item.isChecked = false
                    orderAdapter.notifyItemChanged(pos)
                }
            }
        ) {
            val item = orderAdapter.currentList[it] as OrderModel.OrderItemModel
            item.isChecked = !item.isChecked
            item.menu.amount = if (item.isChecked) 1 else 0
            orderAdapter.notifyItemChanged(it)
        }
        binding.recyclerOrder.apply {
            adapter = orderAdapter
            itemAnimator = null
            addItemDecoration(DividerItemDecoration(this@OrderActivity, VERTICAL))
        }
        orderAdapter.submitList(menuList)
    }

    private fun showOrderDialog() {
        val orderDialog = Dialog(this)
        orderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        orderDialog.setContentView(R.layout.dialog_order)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(orderDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)))
        }

        val tvOrderDinner: TextView = orderDialog.findViewById(R.id.tv_dialog_order_dinner)
        tvOrderDinner.text = binding.tvOrderDinner.text

        val tvOrderStyle: TextView = orderDialog.findViewById(R.id.tv_dialog_order_style)
        tvOrderStyle.text = binding.tvOrderStyle.text

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

        for (menu in menuList) {
            if (menu is OrderModel.OrderItemModel && menu.isChecked) {
                val text = menu.menu.name + " ${menu.menu.amount}개"
                orderListText.append(text)
                orderListText.append('\n')
                totalPrice += menu.menu.price
            }
        }
        if (orderListText.lastIndex > 0)
            orderListText.deleteAt(orderListText.lastIndex)

        val tvOrderPrice: TextView = orderDialog.findViewById(R.id.tv_dialog_order_price)
        tvOrderPrice.text = "총 주문 가격 : " + totalPrice.moneyFormat()

        tvOrderList.text = orderListText.toString()

        val btnOrderOkay: TextView = orderDialog.findViewById(R.id.btn_dialog_order_okay)
        btnOrderOkay.setOnClickListener {
            orderFood()
            orderDialog.dismiss()
        }

        val btnOrderNo: TextView = orderDialog.findViewById(R.id.btn_dialog_order_no)
        btnOrderNo.setOnClickListener {
            orderDialog.dismiss()
        }
        orderDialog.show()
    }
}