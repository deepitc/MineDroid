package com.example.minesweeper

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.widget.GridLayout
import android.widget.Toast
import kotlin.random.Random

class MinesweeperGrid(context: Context, attrs: AttributeSet) : GridLayout(context, attrs) {
    private val numRows = 5
    private val numCols = 5
    private val numMines = 5
    private val cellSize = resources.getDimensionPixelSize(R.dimen.cell_size)
    private val grid: Array<Array<MineCell>> = Array(numRows) { Array(numCols) { MineCell(context) } }

    fun initializeGrid() {
        // Initialize grid cells and randomly place mines
        for (row in 0 until numRows) {
            for (col in 0 until numCols) {
                val cell = grid[row][col]
                val params = LayoutParams()
                params.width = cellSize
                params.height = cellSize
                params.rowSpec = GridLayout.spec(row)
                params.columnSpec = GridLayout.spec(col)
                params.setMargins(5, 5, 5, 5)
                addView(cell, params)
                cell.setOnClickListener { handleCellClick(row, col) }
                cell.setOnLongClickListener {
                    handleCellLongClick(row, col)
                    true
                }
                cell.setBackgroundColor(Color.CYAN)
            }
        }
        placeMines()
    }

    private fun placeMines() {
        val random = Random
        var minesToPlace = numMines

        while (minesToPlace > 0) {
            val row = random.nextInt(numRows)
            val col = random.nextInt(numCols)
            val cell = grid[row][col]

            if (!cell.isMine) {
                cell.isMine = true
                minesToPlace--
            }
        }
    }

    private fun calculateAdjacentMines() {
        for (row in 0 until numRows) {
            for (col in 0 until numCols) {
                val cell = grid[row][col]
                if (!cell.isMine) {
                    var count = 0
                    var minrow = row - 1;
                    var maxrow = minrow + 2;
                    var mincol = col - 1;
                    var maxcol = mincol + 2;

                    if (minrow < 0)
                        minrow = 0;
                    if (mincol < 0)
                        mincol = 0;
                    if (maxrow > 4)
                        maxrow = 4;
                    if (maxcol > 4)
                        maxcol = 4;

                    for (i in minrow..maxrow) {
                        for (j in mincol..maxcol) {
                            if (grid[i][j].isMine) {
                                count++
                            }
                        }
                    }
                    cell.setMineCount(count)
                }
            }
        }
    }

    private var revealedCount = 0
    private var flagsRemaining = numMines

    // Function to reveal empty cells recursively
    private fun revealEmptyCells(row: Int, col: Int) {
        for (i in -1..1) {
            for (j in -1..1) {
                val newRow = row + i
                val newCol = col + j
                if (newRow in 0 until numRows && newCol in 0 until numCols) {
                    val cell = grid[newRow][newCol]
                    if (!cell.isMine && !cell.isRevealed && cell.text == "") {
                        cell.reveal()
                        revealedCount++
                        revealEmptyCells(newRow, newCol) // Recursive call to reveal adjacent empty cells
                    }
                }
            }
        }
    }

    // Function to reveal all mines
    private fun revealMines() {
        for (row in 0 until numRows) {
            for (col in 0 until numCols) {
                val cell = grid[row][col]
                if (cell.isMine && !cell.isRevealed) {
                    cell.reveal()
                }
            }
        }
    }

    // Function to handle cell click
    fun handleCellClick(row: Int, col: Int) {
        val cell = grid[row][col]
        if (!cell.isEnabled || cell.isRevealed) {
            return
        }

        if (cell.isMine) {
            revealMines()
            showGameOverMessage(context.getString(R.string.game_over_message_case1))
        }  else if (cell.text == "") {
            calculateAdjacentMines()
            cell.reveal()
            revealedCount++

            if (cell.text == "") {
                revealEmptyCells(row, col)
            }

            if (revealedCount == numRows * numCols - numMines) {
                showGameOverMessage(context.getString(R.string.game_won_message))
            }
        }
        else {
            cell.reveal()
            revealedCount++

            calculateAdjacentMines()

            if (revealedCount == numRows * numCols - numMines) {
                showGameOverMessage(context.getString(R.string.game_won_message))
            }
        }
    }

    private var isFlaggingModeEnabled = false

    // Function to enable or disable flagging mode
    fun setFlaggingModeEnabled(flaggingModeEnabled: Boolean) {
        isFlaggingModeEnabled = flaggingModeEnabled
    }

    // Function to handle cell long click for flagging
    fun handleCellLongClick(row: Int, col: Int) {
        val cell = grid[row][col]
        if (!cell.isRevealed && isFlaggingModeEnabled) {
            if (cell.text == "" && flagsRemaining > 0 && cell.isMine) {
                cell.text = "F"
                cell.setTextColor(Color.RED)
                cell.setBackgroundColor(Color.YELLOW) // Set flagged cell background color
                flagsRemaining--
            } else if (!cell.isMine) {
                revealMines()
                showGameOverMessage(context.getString(R.string.game_over_message2))
            } else if (cell.text == "F") {
                cell.text = ""
                cell.setBackgroundColor(Color.parseColor("#B0C4DE")) // Reset to original cell color
                flagsRemaining++
            }
        }
    }

    fun resetGrid() {
        // Reset grid cells and game state
        for (row in 0 until numRows) {
            for (col in 0 until numCols) {
                val cell = grid[row][col]
                cell.isMine = false
                cell.isRevealed = false
                cell.isEnabled = true
                cell.text = ""
            }
        }

        // Reset game variables
        revealedCount = 0
        flagsRemaining = numMines

        // Place mines and calculate adjacent mines again
        placeMines()
        calculateAdjacentMines()
    }

    // Function to show game over message
    private fun showGameOverMessage(message: String) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}
