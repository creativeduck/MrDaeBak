package com.creativeduck.mrdaebak.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding
import com.creativeduck.mrdaebak.R

abstract class BaseActivity<B : ViewBinding>(private val inflate: (LayoutInflater) -> B) :
    AppCompatActivity() {
    protected lateinit var binding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun showCustomToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()

            else -> return true
        }
        return super.onOptionsItemSelected(item)
    }

    open fun setToolbar(toolbar: Toolbar, setHome: Boolean, setTitle: Boolean = false) {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(setHome)
        actionBar.setDisplayShowTitleEnabled(setTitle)
    }
    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }


    inline fun <reified T: Activity> Activity.startNewActivityWithInt(vararg value: Pair<String, Int>) {
        val intent = Intent(this, T::class.java)
        for(item in value) {
            intent.putExtra(item.first, item.second)
        }
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    inline fun <reified T: Activity> Activity.startNewActivityWithLong(vararg value: Pair<String, Long>) {
        val intent = Intent(this, T::class.java)
        for(item in value) {
            intent.putExtra(item.first, item.second)
        }
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    inline fun <reified T: Activity> Activity.startNewActivityAndClear(vararg value: Pair<String, Int>) {
        val intent = Intent(this, T::class.java)
        for(item in value) {
            intent.putExtra(item.first, item.second)
        }
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
}