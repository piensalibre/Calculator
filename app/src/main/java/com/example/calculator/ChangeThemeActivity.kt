package com.example.calculator

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.calculator.databinding.ActivityChangeThemeBinding

class ChangeThemeActivity : AppCompatActivity() {

    lateinit var switchBinding : ActivityChangeThemeBinding
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        switchBinding = ActivityChangeThemeBinding.inflate(layoutInflater)
        val view = switchBinding.root
        enableEdgeToEdge()
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        switchBinding.toolBar2.setNavigationOnClickListener {
            finish()
        }
        switchBinding.mySwitch.setOnCheckedChangeListener { compoundButton, b ->

            sharedPreferences = this.getSharedPreferences("Dark Theme", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            if (b){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.putBoolean("switch",true)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putBoolean("switch",false)
            }

            editor.apply()
        }
    }

    override fun onResume() {
        super.onResume()
        sharedPreferences = this.getSharedPreferences("Dark Theme", Context.MODE_PRIVATE)
        val isDark = sharedPreferences.getBoolean("switch", false)
        switchBinding.mySwitch.isChecked = isDark
    }
}