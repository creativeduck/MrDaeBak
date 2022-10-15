package com.creativeduck.mrdaebak.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.creativeduck.mrdaebak.R
import com.creativeduck.mrdaebak.databinding.ActivityOrderDetailBinding
import com.creativeduck.mrdaebak.adapter.OrderDetailAdapter
import com.creativeduck.mrdaebak.model.OrderDetailModel

class OrderDetailActivity : BaseActivity<ActivityOrderDetailBinding>(ActivityOrderDetailBinding::inflate) {
    private val orderDetailList = listOf(
        OrderDetailModel(1L, "에그 베네딕트", 7590, 3),
        OrderDetailModel(2L, "스테이크", 7590, 3),
        OrderDetailModel(3L, "와인", 7590, 3),
        OrderDetailModel(4L, "참치", 7590, 3),
        OrderDetailModel(5L, "빵", 7590, 3),
        OrderDetailModel(6L, "샴페인", 7590, 3)
    )

    private lateinit var menu: Menu
    private lateinit var orderDetailAdapter: OrderDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbar(binding.tbOrderDetail, true);

        initData()
        initAdapter()
    }

    private fun initAdapter() {
        orderDetailAdapter = OrderDetailAdapter()
        // TODO 주문 내역 리스트 설정
        binding.rcOrderDetail.apply {
            adapter = orderDetailAdapter
        }
        orderDetailAdapter.submitList(orderDetailList)
    }

    private fun initData() {
        with(binding) {
            // TODO 주문 상태 설정
            tvOrderDetailDeliveryState.text = "배달중"
            // TODO 디너, 스타일 설정
            tvOrderDetailDinner.text = "발렌타인 디너 / 스타일 디너"

            // TODO 총 주문 가격 설정
            tvOrderDetailPrice.text = "13.4544원"

            // TODO 주문 일시 설정
            tvOrderDetailOrderTime.text = "2022년 3월 23일"

            // TODO 주문 주소 설정
            tvOrderDetailAddress.text = "서울시 동대문구"
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
                // TODO 배달 상태 갱신
            }
            else -> return true
        }
        return super.onOptionsItemSelected(item)
    }
}