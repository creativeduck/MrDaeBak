package com.creativeduck.mrdaebak.activity

import android.os.Bundle
import com.creativeduck.mrdaebak.adapter.OrderListAdapter
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.DINNER_ENGLISH_NAME
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.DINNER_VALENTINE_NAME
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.MR_USER_ID
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.ORDER_LIST_ITEM_ID
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.STYLE_DELUXE_NAME
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.encryptionSharedPreferences
import com.creativeduck.mrdaebak.databinding.ActivityOrderListBinding
import com.creativeduck.mrdaebak.entity.MenuModel
import com.creativeduck.mrdaebak.entity.OrderDto
import com.creativeduck.mrdaebak.entity.OrderState
import com.creativeduck.mrdaebak.network.RemoteService
import com.creativeduck.mrdaebak.util.AllSpaceDecoration
import com.creativeduck.mrdaebak.util.getResponse
import com.creativeduck.mrdaebak.util.goActivityWithLong
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OrderListActivity : BaseActivity<ActivityOrderListBinding>(ActivityOrderListBinding::inflate) {

    @Inject
    lateinit var service: RemoteService

    private lateinit var orderListAdapter: OrderListAdapter
    private var mrUserId = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbar(binding.tbOrderList, true, setTitle = true)

        mrUserId = encryptionSharedPreferences.getLong(MR_USER_ID, 0L)

        initAdapter()
        loadData()
    }

    // TODO API 연동하기
    private fun loadData() {
        service.getOrder(mrUserId).getResponse(
            success = {
//                response ->
                val response = listOf(
                    OrderDto(
                        id = 1,
                        dinner = DINNER_ENGLISH_NAME,
                        style = STYLE_DELUXE_NAME,
                        totalPrice = 7500,
                        orderState = OrderState.COOKING.name,
                        menuList = listOf(
                            MenuModel("에그 샌드위치", 7400, 3)
                        ),
                        orderTime = "2022년 3월 2일",
                        address = "서울시 동대문구"
                    ),
                    OrderDto(
                        id = 2,
                        dinner = DINNER_VALENTINE_NAME,
                        style = STYLE_DELUXE_NAME,
                        totalPrice = 7500,
                        orderState = OrderState.NOT_RECEIVED.name,
                        menuList = listOf(
                            MenuModel("에그 샌드위치", 7400, 3)
                        ),
                        orderTime = "2022년 3월 2일",
                        address = "서울시 동대문구"
                    ),
                    OrderDto(
                        id = 3,
                        dinner = DINNER_ENGLISH_NAME,
                        style = STYLE_DELUXE_NAME,
                        totalPrice = 7500,
                        orderState = OrderState.COOKING.name,
                        menuList = listOf(
                            MenuModel("에그 샌드위치", 7400, 3)
                        ),
                        orderTime = "2022년 3월 2일",
                        address = "서울시 동대문구"
                    ),
                    OrderDto(
                        id = 4,
                        dinner = DINNER_ENGLISH_NAME,
                        style = STYLE_DELUXE_NAME,
                        totalPrice = 7500,
                        orderState = OrderState.COOKING.name,
                        menuList = listOf(
                            MenuModel("에그 샌드위치", 7400, 3)
                        ),
                        orderTime = "2022년 3월 2일",
                        address = "서울시 동대문구"
                    ),
                    OrderDto(
                        id = 5,
                        dinner = DINNER_ENGLISH_NAME,
                        style = STYLE_DELUXE_NAME,
                        totalPrice = 7500,
                        orderState = OrderState.COOKING.name,
                        menuList = listOf(
                            MenuModel("에그 샌드위치", 7400, 3)
                        ),
                        orderTime = "2022년 3월 2일",
                        address = "서울시 동대문구"
                    ), OrderDto(
                        id = 6,
                        dinner = DINNER_ENGLISH_NAME,
                        style = STYLE_DELUXE_NAME,
                        totalPrice = 7500,
                        orderState = OrderState.COOKING.name,
                        menuList = listOf(
                            MenuModel("에그 샌드위치", 7400, 3)
                        ),
                        orderTime = "2022년 3월 2일",
                        address = "서울시 동대문구"
                    )
                )
                orderListAdapter.submitList(response)

            },
            failure = {
                showCustomToast("오류가 발생했습니다.")
            }
        )
    }

    override fun initAdapter() {
        orderListAdapter = OrderListAdapter {
            goActivityWithLong<OrderDetailActivity>(Pair(ORDER_LIST_ITEM_ID, it.id))
        }

        binding.rcOrderList.apply {
            adapter = orderListAdapter
            itemAnimator = null
            addItemDecoration(
                AllSpaceDecoration(
                    -1, 10, 0, 0, 0
                )
            )
        }
    }
}