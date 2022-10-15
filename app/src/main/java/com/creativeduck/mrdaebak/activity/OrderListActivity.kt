package com.creativeduck.mrdaebak.activity

import android.os.Bundle
import com.creativeduck.mrdaebak.adapter.OrderListAdapter
import com.creativeduck.mrdaebak.databinding.ActivityOrderListBinding
import com.creativeduck.mrdaebak.model.OrderListModel
import com.creativeduck.mrdaebak.model.OrderState
import com.creativeduck.mrdaebak.util.AllSpaceDecoration

class OrderListActivity : BaseActivity<ActivityOrderListBinding>(ActivityOrderListBinding::inflate) {
    private val orderListList = listOf(
        OrderListModel(1L, "발렌타인 디너", "심플 디너", 80234, OrderState.NOT_RECEIVED),
        OrderListModel(2L, "샴페인 디너", "그랜드 디너", 23544, OrderState.COOKING),
        OrderListModel(3L, "잉글리시 디너", "디럭스 디너", 345234, OrderState.FINISH_COOK),
        OrderListModel(4L, "프렌치 디너", "그랜드 디너", 834563454, OrderState.DELIVERED),
        OrderListModel(5L, "발렌타인 디너", "심플 디너", 1246234, OrderState.DELIVERING),
        OrderListModel(6L, "발렌타인 디너", "심플 디너", 80234, OrderState.NOT_RECEIVED),
        OrderListModel(7L, "샴페인 디너", "그랜드 디너", 23544, OrderState.COOKING),
        OrderListModel(8L, "잉글리시 디너", "디럭스 디너", 345234, OrderState.FINISH_COOK),
        OrderListModel(9L, "프렌치 디너", "그랜드 디너", 834563454, OrderState.DELIVERED),
        OrderListModel(10L, "발렌타인 디너", "심플 디너", 1246234, OrderState.DELIVERING),
    )

    companion object {
        const val ORDER_LIST_ITEM_ID = "order_list_item_id"
    }

    private lateinit var orderListAdapter: OrderListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbar(binding.tbOrderList, true)

        initAdapter()
    }

    private fun initAdapter() {
        orderListAdapter = OrderListAdapter {
            startNewActivityWithLong<OrderDetailActivity>(Pair(ORDER_LIST_ITEM_ID, it.id))
        }
        orderListAdapter.submitList(orderListList)

        binding.rcOrderList.apply {
            adapter = orderListAdapter
            itemAnimator = null
            addItemDecoration(
                AllSpaceDecoration(
                -1,10,0,0, 0)
            )
        }
    }
}