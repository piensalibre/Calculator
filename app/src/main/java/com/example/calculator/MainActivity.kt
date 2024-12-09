package com.example.calculator

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.icu.text.DecimalFormat
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.calculator.databinding.ActivityMainBinding
import kotlin.concurrent.fixedRateTimer

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding
    var number: String? = null

    var firstNumber: Double = 0.0
    var lastNumber: Double = 0.0
    var status: String? = null
    var operator: Boolean = false

    val myFormatter = DecimalFormat("######.######")

    var history: String = ""
    var currentResult: String = ""

    var dotControl: Boolean = true
    var buttonEqualControl: Boolean = false

    lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mainBinding.textViewResult.text = "0"

        mainBinding.btnZero.setOnClickListener {
            onNumberClicked("0")
        }

        mainBinding.btnOne.setOnClickListener {
            onNumberClicked("1")
        }
        mainBinding.btnTwo.setOnClickListener {
            onNumberClicked("2")
        }
        mainBinding.btnThree.setOnClickListener {
            onNumberClicked("3")
        }
        mainBinding.btnFour.setOnClickListener {
            onNumberClicked("4")
        }
        mainBinding.btnFive.setOnClickListener {
            onNumberClicked("5")
        }
        mainBinding.btnSix.setOnClickListener {
            onNumberClicked("6")
        }
        mainBinding.btnSeven.setOnClickListener {
            onNumberClicked("7")
        }
        mainBinding.btnEight.setOnClickListener {
            onNumberClicked("8")
        }
        mainBinding.btnNine.setOnClickListener {
            onNumberClicked("9")
        }

        mainBinding.btnAC.setOnClickListener {
            onButtonACClicked()
        }

        mainBinding.btnDel.setOnClickListener {

            number?.let {

                if (it.length == 1) {
                    onButtonACClicked()
                } else {
                    number = it.substring(0, it.length - 1)
                    mainBinding.textViewResult.text = number

                    dotControl = !number!!.contains(".")
                }


            }


        }

        mainBinding.btnDivide.setOnClickListener {

            history = mainBinding.textViewHistory.text.toString()
            currentResult = mainBinding.textViewResult.text.toString()
            mainBinding.textViewHistory.text = history.plus(currentResult).plus("/")


            if (operator) {
                when (status) {
                    "multiplication" -> multiply()
                    "division" -> divide()
                    "substraction" -> minus()
                    "adition" -> plus()
                    else -> firstNumber = mainBinding.textViewResult.text.toString().toDouble()
                }
            }
            status = "division"
            operator = false
            number = null
            dotControl = true
        }

        mainBinding.btnMulti.setOnClickListener {


            history = mainBinding.textViewHistory.text.toString()
            currentResult = mainBinding.textViewResult.text.toString()
            mainBinding.textViewHistory.text = history.plus(currentResult).plus("*")
            if (operator) {
                when (status) {
                    "multiplication" -> multiply()
                    "division" -> divide()
                    "substraction" -> minus()
                    "adition" -> plus()
                    else -> firstNumber = mainBinding.textViewResult.text.toString().toDouble()
                }
            }
            status = "multiplication"
            operator = false
            number = null
            dotControl = true
        }

        mainBinding.btnMinus.setOnClickListener {
            history = mainBinding.textViewHistory.text.toString()
            currentResult = mainBinding.textViewResult.text.toString()
            mainBinding.textViewHistory.text = history.plus(currentResult).plus("-")

            if (operator) {
                when (status) {
                    "multiplication" -> multiply()
                    "division" -> divide()
                    "substraction" -> minus()
                    "adition" -> plus()
                    else -> firstNumber = mainBinding.textViewResult.text.toString().toDouble()
                }
            }
            status = "substraction"
            operator = false
            number = null
            dotControl = true

        }

        mainBinding.btnPlus.setOnClickListener {

            history = mainBinding.textViewHistory.text.toString()
            currentResult = mainBinding.textViewResult.text.toString()
            mainBinding.textViewHistory.text = history.plus(currentResult).plus("+")

            if (operator) {
                when (status) {
                    "multiplication" -> multiply()
                    "division" -> divide()
                    "substraction" -> minus()
                    "adition" -> plus()
                    else -> firstNumber = mainBinding.textViewResult.text.toString().toDouble()
                }
            }
            status = "adition"
            operator = false
            number = null
            dotControl = true

        }

        mainBinding.btnEqual.setOnClickListener {

            history = mainBinding.textViewHistory.text.toString()
            currentResult = mainBinding.textViewResult.text.toString()


            if (operator) {
                when (status) {
                    "multiplication" -> multiply()
                    "division" -> divide()
                    "substraction" -> minus()
                    "adition" -> plus()
                    else -> firstNumber = mainBinding.textViewResult.text.toString().toDouble()
                }

                mainBinding.textViewHistory.text = history.plus(currentResult).plus("=")
                    .plus(mainBinding.textViewResult.text.toString())
            }

            operator = false
            dotControl = true
            buttonEqualControl = true

        }

        mainBinding.btnDot.setOnClickListener {


            if (dotControl) {
                number = if (number == null) {
                    "0."
                } else if (buttonEqualControl) {
                    if (mainBinding.textViewResult.text.toString().contains(".")) {

                        mainBinding.textViewResult.text.toString()

                    } else {
                        mainBinding.textViewResult.text.toString().plus(".")
                    }
                } else {
                    "$number."
                }

                mainBinding.textViewResult.text = number
            }

            dotControl = false
        }
        mainBinding.toolBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.settings_item -> {
                    val intent = Intent(this@MainActivity, ChangeThemeActivity::class.java)
                    startActivity(intent)
                    return@setOnMenuItemClickListener true

                }
                else -> return@setOnMenuItemClickListener false
            }
        }

    }


    fun onButtonACClicked() {
        number = null
        status = null
        mainBinding.textViewResult.text = "0"
        mainBinding.textViewHistory.text = ""
        firstNumber = 0.0
        lastNumber = 0.0
        dotControl = true
        buttonEqualControl = false

    }


    fun onNumberClicked(clickedNumber: String) {
        if (number == null) {
            number = clickedNumber
        } else if (buttonEqualControl) {
            number = if (dotControl) {
                clickedNumber
            } else {
                mainBinding.textViewResult.text.toString().plus(clickedNumber)
            }
            firstNumber = number!!.toDouble()
            lastNumber = 0.0
            status = null
            mainBinding.textViewResult.text = ""
        } else {
            number += clickedNumber

        }
        mainBinding.textViewResult.text = number

        operator = true
        buttonEqualControl = false
    }

    fun plus() {
        lastNumber = mainBinding.textViewResult.text.toString().toDouble()
        firstNumber += lastNumber
        mainBinding.textViewResult.text = myFormatter.format(firstNumber)

    }

    fun minus() {
        lastNumber = mainBinding.textViewResult.text.toString().toDouble()
        firstNumber -= lastNumber
        mainBinding.textViewResult.text = myFormatter.format(firstNumber)

    }

    fun multiply() {
        lastNumber = mainBinding.textViewResult.text.toString().toDouble()
        firstNumber *= lastNumber
        mainBinding.textViewResult.text = myFormatter.format(firstNumber)

    }

    fun divide() {
        lastNumber = mainBinding.textViewResult.text.toString().toDouble()

        if (lastNumber == 0.0) {
            Toast.makeText(applicationContext, "El divisor no puede ser 0", Toast.LENGTH_LONG)
                .show()
        } else {
            firstNumber /= lastNumber
            mainBinding.textViewResult.text = myFormatter.format(firstNumber)
        }


    }

    override fun onResume() {
        super.onResume()
        sharedPreferences = this.getSharedPreferences("Dark Theme",Context.MODE_PRIVATE)
        val isDarkMode = sharedPreferences.getBoolean("switch",false)
        if(isDarkMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

    }


    override fun onPause() {
        super.onPause()

        sharedPreferences = this.getSharedPreferences("calculations",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val resultToSave = mainBinding.textViewResult.text.toString()
        val historyToSave = mainBinding.textViewHistory.text.toString()
        val numberToSave = number
        val statusToSave = status
        val operatorToSave = operator
        val dotToSave = dotControl
        val equalToSave = buttonEqualControl
        val firstNumberToSave = firstNumber.toString()
        val lastNumberToSave = lastNumber.toString()

        editor.putString("result", resultToSave)
        editor.putString("history", historyToSave)
        editor.putString("number", numberToSave)
        editor.putString("status", statusToSave)
        editor.putBoolean("operator", operatorToSave)
        editor.putBoolean("dot", dotToSave)
        editor.putBoolean("equal", equalToSave)
        editor.putString("first", firstNumberToSave)
        editor.putString("last", lastNumberToSave)

        editor.apply()
    }

    override fun onStart() {
        super.onStart()

        sharedPreferences = this.getSharedPreferences("calculations",Context.MODE_PRIVATE)

        mainBinding.textViewResult.text = sharedPreferences.getString("result","0")
        mainBinding.textViewHistory.text = sharedPreferences.getString("history","")
        number = sharedPreferences.getString("number",null)
        status = sharedPreferences.getString("status",null)
        operator = sharedPreferences.getBoolean("operator",false)
        dotControl = sharedPreferences.getBoolean("dot",true)
        buttonEqualControl = sharedPreferences.getBoolean("equal", false)
        firstNumber = sharedPreferences.getString("first","0.0")!!.toDouble()
        lastNumber = sharedPreferences.getString("last","0.0")!!.toDouble()



    }
}