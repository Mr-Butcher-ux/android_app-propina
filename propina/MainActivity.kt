package com.example.propina


import android.animation.ArgbEvaluator
import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 10
class MainActivity : AppCompatActivity() {
    private lateinit var baseAmount : EditText
    private lateinit var seekBarTip : SeekBar
    private lateinit var tipPercent : TextView
    private lateinit var tipAmount : TextView
    private lateinit var totalAmount : TextView
    private lateinit var tvTipDescription : TextView
    private lateinit var tvNumPersons : EditText
    private lateinit var newTotal : TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        baseAmount = findViewById(R.id.baseAmount)
        seekBarTip = findViewById(R.id.seekBarTip)
        tipPercent = findViewById(R.id.tipPercent)
        tipAmount = findViewById(R.id.tipAmount)
        totalAmount = findViewById(R.id.totalAmount)
        tvTipDescription = findViewById(R.id.tvTipDescription)
        tvNumPersons = findViewById(R.id.tvNumPersons)
        newTotal = findViewById(R.id.newTotal)

        seekBarTip.progress = INITIAL_TIP_PERCENT
        tipPercent.text = "$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)

        seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG,"onProgressChanged $progress")
                tipPercent.text = "$progress%"
                computeTipAndTotal()
                updateTipDescription(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        } )

        baseAmount.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG,"afterTextChanged $s")
                computeTipAndTotal()
            }

        })

        tvNumPersons.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG,"afterTextChanged $s")
                computeFinalTotal()

            }

        })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
    }

    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription = when (tipPercent) {
            in 0..9 -> "Unaceptable!!"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            else -> "Wonderful!"

        }
        tvTipDescription.text = tipDescription
        //update the color
        val color = ArgbEvaluator().evaluate( tipPercent.toFloat() / seekBarTip.max,
            ContextCompat.getColor(this,R.color.worst_tip),
            ContextCompat.getColor(this,R.color.best_tip)
        ) as Int

        tvTipDescription.setTextColor(color)
    }

    private fun computeTipAndTotal() {
        if  (baseAmount.text.isEmpty()) {
            tipAmount.text = ""
            totalAmount.text = ""
            return
        }
        //Get the value of the base  and tip percent
        val tvBaseAmount = baseAmount.text.toString().toDouble()
        val tvTipPercent = seekBarTip.progress
        ///


        //Compute the tip and total
        val tvTipAmount = tvBaseAmount * tvTipPercent / 100
        val tvTotalAmount = tvBaseAmount + tvTipAmount

        //Update the UI
        tipAmount.text = "%.2f".format(tvTipAmount)
        totalAmount.text = "%.2f".format(tvTotalAmount)

    }

    private fun computeFinalTotal () {
        if  (tvNumPersons.text.isEmpty()) {
            newTotal.text = ""
            return
        }

        val numPersons = tvNumPersons.text.toString().toInt()
        val tvBaseAmount = baseAmount.text.toString().toDouble()
        val tvTipPercent = seekBarTip.progress
        ///


        //Compute the tip and total
        val tvTipAmount = tvBaseAmount * tvTipPercent / 100
        val tvTotalAmount = tvBaseAmount + tvTipAmount
        val tvNewTotal = tvTotalAmount / numPersons

        //Update the UI
        tipAmount.text = "%.2f".format(tvTipAmount)
        totalAmount.text = "%.2f".format(tvTotalAmount)
        newTotal.text = "%.2f".format(tvNewTotal)



    }


}