package com.creativeduck.mrdaebak.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.creativeduck.mrdaebak.R
import com.creativeduck.mrdaebak.adapter.OrderReceiptAdapter
import com.creativeduck.mrdaebak.config.ApplicationClass
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.MR_USER_ID
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.ROLE_COOK
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.SHOW_STATE_LIST
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.SHOW_STATE_ME
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.encryptionSharedPreferences
import com.creativeduck.mrdaebak.databinding.ActivityOrderReceiptBinding
import com.creativeduck.mrdaebak.entity.MenuModel
import com.creativeduck.mrdaebak.entity.OrderDto
import com.creativeduck.mrdaebak.entity.OrderState
import com.creativeduck.mrdaebak.network.RemoteService
import com.creativeduck.mrdaebak.util.AllSpaceDecoration
import com.creativeduck.mrdaebak.util.getResponse
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OrderReceiptActivity :
    BaseActivity<ActivityOrderReceiptBinding>(ActivityOrderReceiptBinding::inflate) {

    @Inject
    lateinit var service: RemoteService
    private var role = ROLE_COOK
    private var showState = SHOW_STATE_LIST
    private lateinit var menu: Menu
    private var mrUserId = 0L
    private lateinit var orderReceiptAdapter: OrderReceiptAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        role = intent.getIntExtra("ROLE", ROLE_COOK)
        binding.tbOrderReceipt.title = when (role) {
            ROLE_COOK -> "조리 대기 목록"
            else -> "배달 대기 목록"
        }
        setToolbar(binding.tbOrderReceipt, setHome = false, setTitle = true)

        mrUserId = encryptionSharedPreferences.getLong(MR_USER_ID, 0L)

        initAdapter()
        loadData()
    }

     override fun initAdapter() {
        orderReceiptAdapter = OrderReceiptAdapter { item ->
            startCooking(item.id, item.orderState)
        }

        binding.rcOrderReceipt.apply {
            adapter = orderReceiptAdapter
            addItemDecoration(AllSpaceDecoration(-1, 0,10, 0, 0))
        }
    }

    private fun startCooking(orderId: Long, orderState: String) {
        when (orderState) {
            OrderState.NOT_RECEIVED.name -> {
                // TODO 조리 시작
                service.startCook(mrUserId, orderId).getResponse(
                    success = {
                        loadData()
                    },
                    failure = {
                        showCustomToast("오류가 발생했습니다.")
                    }
                )
            }
            OrderState.COOKING.name -> {
                // TODO 조리완료
                service.updateOrder(orderId, OrderState.FINISH_COOK.name).getResponse(
                    success = {
                        loadData()
                    },
                    failure = {
                        showCustomToast("오류가 발생했습니다.")
                    }
                )
            }
            OrderState.FINISH_COOK.name -> {
                // TODO 배달 시작
                service.startDelivery(mrUserId, orderId).getResponse(
                    success = {
                        loadData()
                    },
                    failure = {
                        showCustomToast("오류가 발생했습니다.")
                    }
                )
                loadData()
            }
            OrderState.DELIVERING.name -> {
                // TODO 배달 완료
                service.updateOrder(orderId, OrderState.DELIVERED.name).getResponse(
                    success = {
                        loadData()
                    },
                    failure = {
                        showCustomToast("오류가 발생했습니다.")
                    }
                )
                loadData()
            }

        }
    }

    private fun loadData() {

        val response = listOf(
            OrderDto(
                id = 1,
                dinner = ApplicationClass.DINNER_ENGLISH_NAME,
                style = ApplicationClass.STYLE_DELUXE_NAME,
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
                dinner = ApplicationClass.DINNER_VALENTINE_NAME,
                style = ApplicationClass.STYLE_DELUXE_NAME,
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
                dinner = ApplicationClass.DINNER_ENGLISH_NAME,
                style = ApplicationClass.STYLE_DELUXE_NAME,
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
                dinner = ApplicationClass.DINNER_ENGLISH_NAME,
                style = ApplicationClass.STYLE_DELUXE_NAME,
                totalPrice = 7500,
                orderState = OrderState.DELIVERING.name,
                menuList = listOf(
                    MenuModel("에그 샌드위치", 7400, 3)
                ),
                orderTime = "2022년 3월 2일",
                address = "서울시 동대문구"
            ),
            OrderDto(
                id = 5,
                dinner = ApplicationClass.DINNER_ENGLISH_NAME,
                style = ApplicationClass.STYLE_DELUXE_NAME,
                totalPrice = 7500,
                orderState = OrderState.DELIVERING.name,
                menuList = listOf(
                    MenuModel("에그 샌드위치", 7400, 3)
                ),
                orderTime = "2022년 3월 2일",
                address = "서울시 동대문구"
            ), OrderDto(
                id = 6,
                dinner = ApplicationClass.DINNER_ENGLISH_NAME,
                style = ApplicationClass.STYLE_DELUXE_NAME,
                totalPrice = 7500,
                orderState = OrderState.FINISH_COOK.name,
                menuList = listOf(
                    MenuModel("에그 샌드위치", 7400, 3)
                ),
                orderTime = "2022년 3월 2일",
                address = "서울시 동대문구"
            )
        )

        // TODO role 에 따라 다르게 요청하기
        if (role == ROLE_COOK) {
            if (showState == SHOW_STATE_LIST) {
                showCustomToast("전체 조리 내역")
                // TODO 전체 조리 내역 조회
//                service.getCooking().getResponse(
//                    success = {
////                        response ->
//                        orderReceiptAdapter.submitList(response)
//                    },
//                    failure = {
//                        showCustomToast("오류가 발생했습니다.")
//                    }
//                )
            }
            else {
                showCustomToast("요리사 조리 내역")
                // TODO 해당 요리사의 조리 내역 조회
//                service.getCookingForCook(mrUserId).getResponse(
//                    success = {
////                        response ->
//                        orderReceiptAdapter.submitList(response)
//                    },
//                    failure = {
//                        showCustomToast("오류가 발생했습니다.")
//                    }
//                )
            }
        } else {
            // TODO mrUserId 가 0이면 일반 조회, 0 보다 크면 해당 배달원의 배달 내역 조회
            if (showState == SHOW_STATE_LIST) {
                // TODO 전체 배달 내역 조회
                showCustomToast("전체 배달 내역")
//                service.getDelivery().getResponse(
//                    success = {
////                        response ->
//                        orderReceiptAdapter.submitList(response)
//                    },
//                    failure = {
//                        showCustomToast("오류가 발생했습니다.")
//                    }
//                )
            }
            else {

                showCustomToast("배달원 배다 ㄹ내역")
            // TODO 해당 배달원의 배달 내역 조회
//                service.getDeliveryForRider(mrUserId).getResponse(
//                    success = {
////                        response ->
//                        orderReceiptAdapter.submitList(response)
//                    },
//                    failure = {
//                        showCustomToast("오류가 발생했습니다.")
//                    }
//                )
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        if (role == ROLE_COOK)
            menuInflater.inflate(R.menu.menu_cooking, this.menu)
        else
            menuInflater.inflate(R.menu.menu_delivery, this.menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_cooking -> {
                // 목록을 보여주고 있었다면, 요리사의 목록을 보여주는 것으로 전환
                if (showState == SHOW_STATE_LIST) {
                    item.title = "조리 대기 목록"
                    binding.tbOrderReceipt.title = "조리 목록"
                    showState = SHOW_STATE_ME
                    loadData()
                } else {
                    item.title = "조리 목록"
                    binding.tbOrderReceipt.title = "조리 대기 목록"
                    showState = SHOW_STATE_LIST
                    loadData()
                }
            }
            R.id.menu_delivery -> {
                if (showState == SHOW_STATE_LIST) {
                    item.title = "배달 대기 목록"
                    binding.tbOrderReceipt.title = "배달 목록"
                    showState = SHOW_STATE_ME
                    loadData()
                } else {
                    item.title = "배달 목록"
                    binding.tbOrderReceipt.title = "배달 대기 목록"
                    showState = SHOW_STATE_LIST
                    loadData()
                }
            }
            else -> return true
        }
        return super.onOptionsItemSelected(item)
    }
}