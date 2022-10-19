package com.creativeduck.mrdaebak.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.creativeduck.mrdaebak.ApplicationClass.Companion.ROLE_RIDER
import com.creativeduck.mrdaebak.ApplicationClass.Companion.SHOW_STATE_ME
import com.creativeduck.mrdaebak.R
import com.creativeduck.mrdaebak.databinding.ActivityIngredientBinding

class IngredientActivity : BaseActivity<ActivityIngredientBinding>(ActivityIngredientBinding::inflate) {

    private lateinit var menu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbar(binding.tbIngredient, setHome = false, setTitle = true)

        initClick()
    }

    override fun initClick() {
        binding.btnIngredientOrder.setOnClickListener {
            // TODO 주문 후 상태 변경
            
        }
    }

    private fun loadData(type: Int, showState: Int) {
        // TODO 타입에 따라, 그리고 내 목록인지 전체 목록인지 여부에 따라 분기처리해서 API 요청하기
//        orderReceiptAdapter.submitList()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.menu_ingredient, this.menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_ingredient_order -> {
                // TODO 여기선 단지 아이템 상태를 바꾸기만 하자
                // TODO 어차피 수정 버튼을 눌러서 다시 원래 상태로 돌아오니까
                item.title = ""
                loadData(ROLE_RIDER, SHOW_STATE_ME)
            }
            else -> return true
        }
        return super.onOptionsItemSelected(item)
    }
}