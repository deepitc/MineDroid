package com.example.minesweeper

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var minesweeperGrid: MinesweeperGrid
    private lateinit var flagButton: Button
    private lateinit var resetButton: Button

    private var flaggingModeEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        minesweeperGrid = findViewById(R.id.minesweeperGrid)
        flagButton = findViewById(R.id.flagButton)
        resetButton = findViewById(R.id.resetButton)


        flagButton.setOnClickListener {
            flaggingModeEnabled = !flaggingModeEnabled
            if (flaggingModeEnabled) {
                flagButton.text = "Disable Flagging Mode"
            } else {
                flagButton.text = "Enable Flagging Mode"
            }
            minesweeperGrid.setFlaggingModeEnabled(flaggingModeEnabled)
        }

        resetButton.setOnClickListener {
            // Create an Intent to restart the activity
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

            // Finish the current activity
            finish()
        }


        // Initialize the Minesweeper grid
        minesweeperGrid.initializeGrid()
    }
}

