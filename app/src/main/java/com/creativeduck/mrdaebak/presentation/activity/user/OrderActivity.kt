package com.creativeduck.mrdaebak.presentation.activity.user

import android.os.Bundle
import android.view.View
import com.creativeduck.mrdaebak.databinding.ActivityOrderBinding
import com.creativeduck.mrdaebak.presentation.model.OrderModel
import com.creativeduck.mrdaebak.presentation.activity.BaseActivity
import com.creativeduck.mrdaebak.presentation.activity.user.DinnerActivity.Companion.DINNER_TYPE
import com.creativeduck.mrdaebak.presentation.activity.user.DinnerActivity.Companion.ENGLISH
import com.creativeduck.mrdaebak.presentation.activity.user.DinnerActivity.Companion.FRENCH
import com.creativeduck.mrdaebak.presentation.activity.user.DinnerActivity.Companion.VALENTINE
import com.creativeduck.mrdaebak.presentation.activity.user.StyleActivity.Companion.GRAND
import com.creativeduck.mrdaebak.presentation.activity.user.StyleActivity.Companion.SIMPLE
import com.creativeduck.mrdaebak.presentation.activity.user.StyleActivity.Companion.STYLE_TYPE
import com.creativeduck.mrdaebak.presentation.adapter.order.OrderListAdapter
import com.creativeduck.mrdaebak.util.OnItemClickListener

class OrderActivity : BaseActivity<ActivityOrderBinding>(ActivityOrderBinding::inflate) {

    private lateinit var orderListAdapter: OrderListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val dinnerType = intent.getIntExtra(DINNER_TYPE, VALENTINE)
        val styleType = intent.getIntExtra(STYLE_TYPE, SIMPLE)

        initText(dinnerType, styleType)
        initClick()
        initAdapter()
    }

    private fun initText(dinnerType: Int, styleType: Int) {
        with(binding) {
            tvOrderDinner.text =
                when (dinnerType) {
                    VALENTINE -> { "발렌타인 디너" }
                    FRENCH -> { "프렌치 디너" }
                    ENGLISH -> { "잉글리시 디너" }
                    else -> { "샴페인 디너" }
            }
            tvOrderStyle.text =
                when (styleType) {
                    SIMPLE -> { "심플 디너" }
                    GRAND -> { "그랜드 디너" }
                    else -> { "디럭스 디너" }
                }
        }
    }

    private fun initClick() {
        binding.btnOrder.setOnClickListener {
                // TODO 주문 데이터 POST 하기

                // TODO 주문 상세 내역 화면으로 이동

                // TODO 주문 상세 화면에서 뒤로오면 메인 액티비티가 좋을 거 같은데, 처리 어떻게 할까.

        }
    }
    private fun initAdapter() {
        orderListAdapter = OrderListAdapter(
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


        val detailList = ArrayList<OrderModel>()
        val modelList = listOf<OrderModel>(
            OrderModel.OrderHeaderModel("애피타이저"),
            OrderModel.OrderItemModel(
                "샐러드", 7500, 0, false
            ),
            OrderModel.OrderItemModel(
                "에그 스크램블", 7500, 0, false
            ),
            OrderModel.OrderItemModel(
                "베이컨", 7500, 0, false
            ),
            OrderModel.OrderItemModel(
                "바게트빵", 7500, 0, false
            ),
            OrderModel.OrderHeaderModel("메인디시"),
            OrderModel.OrderItemModel(
                "스테이크", 7500, 0, false
            ),
            OrderModel.OrderHeaderModel("드링크"),
            OrderModel.OrderItemModel(
                "와인", 7500, 0, false
            ),
            OrderModel.OrderItemModel(
                "샴페인", 7500, 0, false
            ),
            OrderModel.OrderItemModel(
                "커피", 7500, 0, false
            ),
        )
        detailList.addAll(modelList)
        orderListAdapter.submitList(modelList)
    }
}