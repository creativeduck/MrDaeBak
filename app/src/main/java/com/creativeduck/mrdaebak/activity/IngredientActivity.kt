package com.creativeduck.mrdaebak.activity

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.creativeduck.mrdaebak.adapter.IngredientAdapter
import com.creativeduck.mrdaebak.databinding.ActivityIngredientBinding
import com.creativeduck.mrdaebak.entity.IngredientItemModel
import com.creativeduck.mrdaebak.entity.IngredientListModel
import com.creativeduck.mrdaebak.entity.IngredientModel
import com.creativeduck.mrdaebak.network.RemoteService
import com.creativeduck.mrdaebak.util.OnItemClickListener
import com.creativeduck.mrdaebak.util.getResponse
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class IngredientActivity :
    BaseActivity<ActivityIngredientBinding>(ActivityIngredientBinding::inflate) {

    @Inject
    lateinit var service: RemoteService
    private lateinit var ingredientAdapter: IngredientAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbar(binding.tbIngredient, setHome = false, setTitle = true)

        initClick()
        initAdapter()
        loadData()
    }

    private fun loadData() {
        service.getIngredient().getResponse(
            success = {
//                val response = it.ingredientList
                // TODO 식자재 요청 API 연동하기
                val response = listOf(
                    IngredientModel("오이", 10000, 3),
                    IngredientModel("굴", 10000, 3),
                    IngredientModel("소고기", 10000, 3),
                    IngredientModel("계란", 10000, 3),
                    IngredientModel("버터", 10000, 3)
                )

                ingredientAdapter.submitList(
                    response.map { model ->
                        IngredientItemModel(model, model.amount > 0)
                    }
                )
            },
            failure = {
                showCustomToast("오류가 발생했습니다.")
            }
        )
    }


    override fun initClick() {
        binding.btnIngredientOrder.setOnClickListener {
            // TODO 식자재 변경 API 요청
            service.updateIngredient(
                IngredientListModel(
                    ingredientAdapter.currentList.map {
                        it.ingredient
                    }
                )
            ).getResponse(
                success = {
                    loadData()
                },
                failure = {
                    showCustomToast("오류가 발생했습니다.")
                }
            )
        }
    }

    override fun initAdapter() {
        ingredientAdapter = IngredientAdapter(
            object : OnItemClickListener {
                override fun onItemClicked(view: View, pos: Int) {
                    val item = ingredientAdapter.currentList[pos]
                    item.ingredient.amount++
                    if (!item.isChecked)
                        item.isChecked = true
                    ingredientAdapter.notifyItemChanged(pos)
                }
            },
            object : OnItemClickListener {
                override fun onItemClicked(view: View, pos: Int) {
                    val item = ingredientAdapter.currentList[pos]
                    if (item.ingredient.amount > 0)
                        item.ingredient.amount--
                    if (item.ingredient.amount == 0)
                        item.isChecked = false
                    ingredientAdapter.notifyItemChanged(pos)
                }
            }
        ) {
            val item = ingredientAdapter.currentList[it]
            item.isChecked = !item.isChecked
            item.ingredient.amount = if (item.isChecked) 1 else 0
            ingredientAdapter.notifyItemChanged(it)
        }

        binding.rcIngredient.apply {
            adapter = ingredientAdapter
            itemAnimator = null
            addItemDecoration(DividerItemDecoration(this@IngredientActivity, RecyclerView.VERTICAL))
        }
    }
}