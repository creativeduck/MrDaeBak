package com.creativeduck.mrdaebak.presentation.adapter.detail

import androidx.recyclerview.widget.RecyclerView
import com.creativeduck.mrdaebak.databinding.ItemDetailHeaderBinding
import com.creativeduck.mrdaebak.databinding.ItemDetailItemBinding
import com.creativeduck.mrdaebak.domain.detail.DetailModel

class DetailItemViewHolder(
    private val binding: ItemDetailItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: DetailModel.DetailItemModel) {
        with(binding) {
            tvFoodDetailAmount.text = "${item.amount}개"
            tvFoodDetailPrice.text = "${item.price}원"
            tvFoodDetailTitle.text = item.title
            cbFoodDetail.isChecked = item.isChecked
        }
    }
}

class DetailHeaderViewHolder(
    private val binding: ItemDetailHeaderBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: DetailModel.DetailHeaderModel) {

    }
}