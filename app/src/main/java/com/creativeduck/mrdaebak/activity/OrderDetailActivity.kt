package com.creativeduck.mrdaebak.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.creativeduck.mrdaebak.R
import com.creativeduck.mrdaebak.adapter.OrderDetailAdapter
import com.creativeduck.mrdaebak.config.ApplicationClass
import com.creativeduck.mrdaebak.config.ApplicationClass.Companion.ORDER_LIST_ITEM_ID
import com.creativeduck.mrdaebak.databinding.ActivityOrderDetailBinding
import com.creativeduck.mrdaebak.data.MenuModel
import com.creativeduck.mrdaebak.data.OrderDto
import com.creativeduck.mrdaebak.data.OrderState
import com.creativeduck.mrdaebak.data.RemoteService
import com.creativeduck.mrdaebak.util.getResponse
import com.creativeduck.mrdaebak.util.moneyFormat
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OrderDetailActivity :
    BaseActivity<ActivityOrderDetailBinding>(ActivityOrderDetailBinding::inflate) {

    @Inject
    lateinit var service: RemoteService

    private lateinit var menu: Menu
    private lateinit var orderDetailAdapter: OrderDetailAdapter
    private var orderId = 0L
    private var mrUserId = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbar(binding.tbOrderDetail, true);

        orderId = intent.getLongExtra(ORDER_LIST_ITEM_ID, 0L)

        initAdapter()
        loadData()
    }

    // TODO API 연동하기
    private fun loadData() {
        service.getOrderDetail(mrUserId, orderId).getResponse(
            success = {
//                response ->
                // TODO 임시
                val response = OrderDto(
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
                )

                with(binding) {
                    with(response) {
                        if (orderState == OrderState.NOT_RECEIVED.name) {
                            btnOrderDetailCancel.visibility = View.VISIBLE
                            btnOrderDetailCancel.setOnClickListener { cancelOrder(orderState) }
                        }
                        tvOrderDetailOrderState.text = orderState
                        tvOrderDetailDinner.text = "$dinner / $style"
                        tvOrderDetailPrice.text = totalPrice.moneyFormat()
                        tvOrderDetailOrderTime.text = orderTime
                        tvOrderDetailAddress.text = address
                    }
                }
                orderDetailAdapter.submitList(response.menuList)
            },
            failure = {
                showCustomToast("오류가 발생했습니다.")
            }
        )
    }

    // TODO 주문 취소 API 연동
    private fun cancelOrder(orderState: String) {
        if (orderState == OrderState.NOT_RECEIVED.name) {
            service.updateOrder(orderId, OrderState.CANCELED.name).getResponse(
                success = {
                    loadData()
                },
                failure = {
                    showCustomToast("오류가 발생했습니다.")
                }
            )
        } else {
            showCustomToast("주문 취소할 수 없습니다")
            loadData()
        }
    }

    override fun initAdapter() {
        orderDetailAdapter = OrderDetailAdapter()

        binding.rcOrderDetail.apply {
            adapter = orderDetailAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.menu_order_detail, this.menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.menu_order_detail_refresh -> {
                loadData()
            }
            else -> return true
        }
        return super.onOptionsItemSelected(item)
    }
}