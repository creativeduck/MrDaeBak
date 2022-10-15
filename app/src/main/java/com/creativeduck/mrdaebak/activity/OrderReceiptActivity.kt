package com.creativeduck.mrdaebak.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.creativeduck.mrdaebak.R
import com.creativeduck.mrdaebak.activity.SignInActivity.Companion.ROLE_COOK
import com.creativeduck.mrdaebak.activity.SignInActivity.Companion.ROLE_RIDER
import com.creativeduck.mrdaebak.adapter.OrderReceiptAdapter
import com.creativeduck.mrdaebak.databinding.ActivityOrderReceiptBinding
import com.creativeduck.mrdaebak.model.OrderModel
import com.creativeduck.mrdaebak.model.OrderReceiptModel
import com.creativeduck.mrdaebak.service.RemoteService
import com.creativeduck.mrdaebak.util.AllSpaceDecoration
import com.creativeduck.mrdaebak.util.getResponse
import javax.inject.Inject

class OrderReceiptActivity :
    BaseActivity<ActivityOrderReceiptBinding>(ActivityOrderReceiptBinding::inflate) {
    companion object {
        const val SHOW_STATE_LIST = 0
        const val SHOW_STATE_ME = 1
    }

    private val orderReceiptList = listOf(
        OrderReceiptModel(
            1L, "발렌타인 디너", "스타일 디너", 19544,
            listOf(OrderModel.OrderSimpleModel(1L, "바게트", 3),
                OrderModel.OrderSimpleModel(2L, "와인", 3),
                OrderModel.OrderSimpleModel(3L, "스테이크", 3)),
            "서울시 동대문구", "12시 39분", SHOW_STATE_ME
        ),
        OrderReceiptModel(
            2L, "발렌타인 디너", "스타일 디너", 19544,
            listOf(OrderModel.OrderSimpleModel(1L, "김치찌개", 3)),
            "서울시 동대문구", "12시 39분", SHOW_STATE_LIST

        ),
        OrderReceiptModel(
            3L, "발렌타인 디너", "스타일 디너", 19544,
            listOf(OrderModel.OrderSimpleModel(1L, "김치찌개", 3)),
            "서울시 동대문구", "12시 39분", SHOW_STATE_LIST
        ),
        OrderReceiptModel(
            4L, "발렌타인 디너", "스타일 디너", 19544,
            listOf(OrderModel.OrderSimpleModel(1L, "김치찌개", 3)),
            "서울시 동대문구", "12시 39분", SHOW_STATE_LIST
        ),
        OrderReceiptModel(
            5L, "발렌타인 디너", "스타일 디너", 19544,
            listOf(OrderModel.OrderSimpleModel(1L, "김치찌개", 3)),
            "서울시 동대문구", "12시 39분", SHOW_STATE_LIST
        )
    )

    private var role = ROLE_COOK
    private var showState = SHOW_STATE_LIST
    private lateinit var menu: Menu
    private lateinit var orderReceiptAdapter: OrderReceiptAdapter
    @Inject
    lateinit var service: RemoteService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        role = intent.getIntExtra("ROLE", ROLE_COOK)
        binding.tbOrderReceipt.title = when (role) {
            ROLE_COOK -> "조리 대기 목록"
            else -> "배달 대기 목록"
        }
        setToolbar(binding.tbOrderReceipt, setHome = false, setTitle = true)

        initAdapter()

    }

    private fun initAdapter() {
        orderReceiptAdapter = OrderReceiptAdapter()

        binding.rcOrderReceipt.apply {
            adapter = orderReceiptAdapter
            addItemDecoration(AllSpaceDecoration(-1, 0,10, 0, 0))
        }

        orderReceiptAdapter.submitList(orderReceiptList)
    }

    private fun loadData(type: Int, showState: Int) {
        // TODO 타입에 따라, 그리고 내 목록인지 전체 목록인지 여부에 따라 분기처리해서 API 요청하기
//        orderReceiptAdapter.submitList()
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
                if (showState == SHOW_STATE_LIST) {
                    item.title = "조리 대기 목록"
                    binding.tbOrderReceipt.title = "조리 목록"
                    loadData(ROLE_COOK, SHOW_STATE_ME)
                    showState = SHOW_STATE_ME
                } else {
                    item.title = "조리 목록"
                    binding.tbOrderReceipt.title = "조리 대기 목록"
                    loadData(ROLE_COOK, SHOW_STATE_LIST)
                    showState = SHOW_STATE_LIST
                }
            }
            R.id.menu_delivery -> {
                if (showState == SHOW_STATE_LIST) {
                    item.title = "배달 대기 목록"
                    binding.tbOrderReceipt.title = "배달 목록"
                    loadData(ROLE_RIDER, SHOW_STATE_ME)
                    showState = SHOW_STATE_ME
                } else {
                    item.title = "배달 목록"
                    binding.tbOrderReceipt.title = "배달 대기 목록"
                    loadData(ROLE_RIDER, SHOW_STATE_LIST)
                    showState = SHOW_STATE_LIST
                }
            }
            else -> return true
        }
        return super.onOptionsItemSelected(item)
    }
}