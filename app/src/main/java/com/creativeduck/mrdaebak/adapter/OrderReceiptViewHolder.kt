package com.creativeduck.mrdaebak.adapter

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.creativeduck.mrdaebak.ApplicationClass.Companion.SHOW_STATE_LIST
import com.creativeduck.mrdaebak.R
import com.creativeduck.mrdaebak.databinding.ItemOrderReceiptBinding
import com.creativeduck.mrdaebak.model.OrderReceiptModel
import com.creativeduck.mrdaebak.util.moneyFormat

class OrderReceiptViewHolder(
    private val binding: ItemOrderReceiptBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: OrderReceiptModel) {
        with(binding) {
            tvOrderReceiptAddress.text = item.address
            tvOrderReceiptPrice.text = item.price.moneyFormat()
            tvOrderReceiptTime.text = item.orderTime
            tvOrderReceiptDinnerAndStyle.text = "${item.dinner} / ${item.style}"
            var menuStr = StringBuilder()
            menuStr.append("${item.menus[0].title} ${item.menus[0].amount}개")
            for(i in 1 until item.menus.size) {
                val menu = item.menus[i]
                menuStr.append(" / ${menu.title} ${menu.amount}개")
            }
            tvOrderReceiptMenus.text = menuStr

            btnOrderReceipt.apply {
                if (item.state == SHOW_STATE_LIST) {
                    backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(root.context, R.color.orange)
                    )
                    text = "조리 시작"
                }
                else {
                    backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(root.context, R.color.green)
                    )
                    text = "조리 완료"
                }
            }
        }
    }
}