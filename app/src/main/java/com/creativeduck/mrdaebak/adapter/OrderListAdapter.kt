package com.creativeduck.mrdaebak.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.creativeduck.mrdaebak.databinding.ItemOrderListBinding
import com.creativeduck.mrdaebak.entity.OrderDto
import com.creativeduck.mrdaebak.entity.OrderState
import com.creativeduck.mrdaebak.util.ItemDiffCallback
import com.creativeduck.mrdaebak.util.moneyFormat

class OrderListAdapter(
    private val itemClick: (OrderDto) -> Unit
) : ListAdapter<OrderDto, OrderListViewHolder>(ItemDiffCallback(
    onItemsTheSame = { old, new -> old.id == new.id },
    onContentsTheSame = { old, new -> old == new }
)) {
    lateinit var inflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderListViewHolder {
        if (!::inflater.isInitialized) {
            inflater = LayoutInflater.from(parent.context)
        }
        val binding = ItemOrderListBinding.inflate(inflater, parent, false)
        return OrderListViewHolder(binding, itemClick)
    }

    override fun onBindViewHolder(holder: OrderListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class OrderListViewHolder(
    private val binding: ItemOrderListBinding,
    private val itemClick: (OrderDto) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: OrderDto) {
        binding.root.setOnClickListener {
            itemClick(item)
        }
        with(binding) {
            tvOrderListItemTitle.text = "${item.dinner} / ${item.style}"
            tvOrderListItemPrice.text = item.totalPrice.moneyFormat()
            tvOrderListItemDeliveryState.text =
                when (item.orderState) {
                    OrderState.COOKING.name -> {
                        "조리중"
                    }
                    OrderState.FINISH_COOK.name -> {
                        "조리 완료"
                    }
                    OrderState.DELIVERING.name -> {
                        "배달중"
                    }
                    OrderState.DELIVERED.name -> {
                        "배달 완료"
                    }
                    OrderState.CANCELED.name-> {
                        "취소됨"
                    }
                    else -> {
                        "미접수"
                    }
                }
        }
    }
}