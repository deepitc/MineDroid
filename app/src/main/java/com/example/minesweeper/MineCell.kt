package com.example.minesweeper

import android.content.Context
import android.graphics.Color
import android.view.Gravity

class MineCell(context: Context) : androidx.appcompat.widget.AppCompatButton(context) {
    var isMine = false
    var isRevealed = false

    fun setMineCount(count: Int) {
        isRevealed = false
        if (count > 0) {
            text = count.toString()
        }
    }

    fun reveal() {
        isRevealed = true
        isEnabled = false
        if (isMine or text.equals("F")) {
            setBackgroundColor(Color.RED)
            text = "X"
        } else {
            setBackgroundColor(Color.parseColor("#B0C4DE")) // Light Steel Blue
            if (text.isNotEmpty()) {
                when (text.toString().toInt()) {
                    1 -> setTextColor(Color.BLUE)
                    2 -> setTextColor(Color.GREEN)
                    3 -> setTextColor(Color.RED)
                    4 -> setTextColor(Color.MAGENTA)
                }
            }
        }
        gravity = Gravity.CENTER
    }

    // Click listener setter
    fun setOnCellClickListener(listener: OnClickListener?) {
        setOnClickListener(listener)
    }

    // Long click listener setter
    fun setOnCellLongClickListener(listener: OnLongClickListener?) {
        setOnLongClickListener(listener)
    }
}

