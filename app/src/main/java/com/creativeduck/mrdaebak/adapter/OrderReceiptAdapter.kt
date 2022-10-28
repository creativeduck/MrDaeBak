package com.creativeduck.mrdaebak.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.creativeduck.mrdaebak.R
import com.creativeduck.mrdaebak.databinding.ItemOrderReceiptBinding
import com.creativeduck.mrdaebak.data.OrderDto
import com.creativeduck.mrdaebak.data.OrderState
import com.creativeduck.mrdaebak.util.ItemDiffCallback
import com.creativeduck.mrdaebak.util.moneyFormat

class OrderReceiptAdapter(
    private val clickListener: (OrderDto) -> Unit
) : ListAdapter<OrderDto, OrderReceiptViewHolder>(ItemDiffCallback(
    onItemsTheSame = { old, new -> old.id == new.id },
    onContentsTheSame = { old, new -> old == new }
)) {
    private lateinit var inflater : LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderReceiptViewHolder {
        if (!::inflater.isInitialized)
            inflater = LayoutInflater.from(parent.context)

        val binding = ItemOrderReceiptBinding.inflate(inflater, parent, false)
        return OrderReceiptViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: OrderReceiptViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class OrderReceiptViewHolder(
    private val binding: ItemOrderReceiptBinding,
    private val clickListener: (OrderDto) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: OrderDto) {
        with(binding) {
            tvOrderReceiptAddress.text = item.address
            tvOrderReceiptPrice.text = item.totalPrice.moneyFormat()
            tvOrderReceiptTime.text = item.orderTime
            tvOrderReceiptDinnerAndStyle.text = "${item.dinner} / ${item.style}"
            val menuStr = StringBuilder()
            menuStr.append("${item.menuList[0].name} ${item.menuList[0].amount}개")
            for(i in 1 until item.menuList.size) {
                val menu = item.menuList[i]
                menuStr.append(" / ${menu.name} ${menu.amount}개")
            }
            tvOrderReceiptMenus.text = menuStr

            btnOrderReceipt.apply {
                setOnClickListener {
                    clickListener(item)
                }

                when(item.orderState) {
                    OrderState.NOT_RECEIVED.name -> {
                        backgroundTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(root.context, R.color.orange)
                        )
                        text = "조리 시작"
                    }
                    OrderState.COOKING.name -> {
                        backgroundTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(root.context, R.color.green)
                        )
                        text = "조리 완료"
                    }
                    OrderState.FINISH_COOK.name -> {
                        backgroundTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(root.context, R.color.orange)
                        )
                        text = "배달 시작"
                    }
                    OrderState.DELIVERING.name -> {
                        backgroundTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(root.context, R.color.green)
                        )
                        text = "배달 완료"
                    }
                    else -> {
                        visibility = View.GONE
                    }
                }
            }
        }
    }
}