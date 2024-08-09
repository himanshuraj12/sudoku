package com.hraj.sudoku


import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var grid: Array<Array<EditText>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sudokuGrid = findViewById<GridLayout>(R.id.sudokuGrid)
        grid = Array(9) { row ->
            Array(9) { col ->
                EditText(this).apply {
                    layoutParams = GridLayout.LayoutParams().apply {
                        width = 150
                        height = 150
                        setMargins(4, 4, 4, 4)
                        gravity = Gravity.CENTER
                    }
                    textSize = 24f
                    gravity = Gravity.CENTER
                    inputType = android.text.InputType.TYPE_CLASS_NUMBER
                    sudokuGrid.addView(this)
                }
            }
        }

        findViewById<Button>(R.id.validateButton).setOnClickListener {
            if (validateGrid()) {
                Toast.makeText(this, "Valid Sudoku", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Invalid Sudoku", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.solveButton).setOnClickListener {
            if (solveSudoku()) {
                updateGrid()
            } else {
                Toast.makeText(this, "Unsolvable Sudoku", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateGrid(): Boolean {
        val seen = mutableSetOf<String>()
        for (i in 0 until 9) {
            for (j in 0 until 9) {
                val value = grid[i][j].text.toString()
                if (value.isNotEmpty()) {
                    val rowKey = "row-$i-$value"
                    val colKey = "col-$j-$value"
                    val boxKey = "box-${i / 3}-${j / 3}-$value"
                    if (rowKey in seen || colKey in seen || boxKey in seen) {
                        return false
                    }
                    seen.add(rowKey)
                    seen.add(colKey)
                    seen.add(boxKey)
                }
            }
        }
        return true
    }

    private fun solveSudoku(): Boolean {
        val board = Array(9) { row ->
            Array(9) { col ->
                grid[row][col].text.toString().toIntOrNull() ?: 0
            }
        }
        return solve(board)
    }

    private fun solve(board: Array<Array<Int>>): Boolean {
        for (i in 0 until 9) {
            for (j in 0 until 9) {
                if (board[i][j] == 0) {
                    for (num in 1..9) {
                        if (isValid(board, i, j, num)) {
                            board[i][j] = num
                            if (solve(board)) {
                                return true
                            }
                            board[i][j] = 0
                        }
                    }
                    return false
                }
            }
        }
        return true
    }

    private fun isValid(board: Array<Array<Int>>, row: Int, col: Int, num: Int): Boolean {
        for (i in 0 until 9) {
            if (board[row][i] == num || board[i][col] == num ||
                board[row / 3 * 3 + i / 3][col / 3 * 3 + i % 3] == num) {
                return false
            }
        }
        return true
    }

    private fun updateGrid() {
        for (i in 0 until 9) {
            for (j in 0 until 9) {
                val value = grid[i][j].text.toString().toIntOrNull() ?: 0
                if (value == 0) {
                    grid[i][j].setText("")
                } else {
                    grid[i][j].setText(value.toString())
                }
            }
        }
    }
}
