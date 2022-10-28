package com.creativeduck.mrdaebak.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.creativeduck.mrdaebak.databinding.ItemOrderItemBinding
import com.creativeduck.mrdaebak.data.IngredientItemModel
import com.creativeduck.mrdaebak.util.ItemDiffCallback
import com.creativeduck.mrdaebak.util.OnItemClickListener

class IngredientAdapter(
    private val addListener: OnItemClickListener,
    private val minusListener: OnItemClickListener,
    private val checkListener: (Int) -> Unit
) : ListAdapter<IngredientItemModel, IngredientViewHolder>(ItemDiffCallback<IngredientItemModel>(
    onItemsTheSame = { old, new -> old.ingredient.name == new.ingredient.name },
    onContentsTheSame = { old, new -> old == new }
)) {
    private lateinit var inflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        if (!::inflater.isInitialized) {
            inflater = LayoutInflater.from(parent.context)
        }
        val binding = ItemOrderItemBinding.inflate(inflater, parent, false)
        return IngredientViewHolder(binding, addListener, minusListener, checkListener)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}


class IngredientViewHolder(
    private val binding: ItemOrderItemBinding,
    private val addListener: OnItemClickListener,
    private val minusListener: OnItemClickListener,
    private val checkListener: (Int) -> Unit
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: IngredientItemModel) {
        with(binding) {
            tvOrderAmount.text = "${item.ingredient.amount}개"
            tvOrderPrice.text = "${item.ingredient.price}원"
            tvOrderTitle.text = item.ingredient.name

            btnOrderAddAmount.setOnClickListener {
                addListener.onItemClicked(it, adapterPosition)
            }

            btnOrderMinusAmount.setOnClickListener {
                minusListener.onItemClicked(it, adapterPosition)
            }

            cbOrder.setOnCheckedChangeListener(null)
            cbOrder.isChecked = item.isChecked
            cbOrder.setOnCheckedChangeListener { _, _ ->
                checkListener(adapterPosition)
            }
        }
    }
}