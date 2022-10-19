package com.creativeduck.mrdaebak.adapter

import androidx.recyclerview.widget.RecyclerView
import com.creativeduck.mrdaebak.databinding.ItemOrderListBinding
import com.creativeduck.mrdaebak.model.OrderState
import com.creativeduck.mrdaebak.model.OrderListModel
import com.creativeduck.mrdaebak.util.moneyFormat

class OrderListViewHolder(
    private val binding: ItemOrderListBinding,
    private val itemClick: (OrderListModel) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: OrderListModel) {
        binding.root.setOnClickListener {
            itemClick(item)
        }
        with(binding) {
            tvOrderListItemTitle.text = "${item.dinner} / ${item.style}"
            tvOrderListItemPrice.text = item.price.moneyFormat()
            tvOrderListItemDeliveryState.text =
                when (item.orderState) {
                    OrderState.NOT_RECEIVED -> {
                        "미접수"
                    }
                    OrderState.COOKING -> {
                        "조리중"
                    }
                    OrderState.FINISH_COOK -> {
                        "조리 완료"
                    }
                    OrderState.DELIVERING -> {
                        "배달중"
                    }
                    OrderState.DELIVERED -> {
                        "배달 완료"
                    }
                    OrderState.CANCELED -> {
                        "취소됨"
                    }
                }
        }
    }
}