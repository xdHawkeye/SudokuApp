package com.example.sudoku.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.sudoku.R
import com.example.sudoku.game.Cell
import com.example.sudoku.view.custom.SudokuSolverBoardView
import com.example.sudoku.viewmodel.SudokuViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.eightbutton
import kotlinx.android.synthetic.main.activity_main.fivebutton
import kotlinx.android.synthetic.main.activity_main.fourbutton
import kotlinx.android.synthetic.main.activity_main.ninebutton
import kotlinx.android.synthetic.main.activity_main.onebutton
import kotlinx.android.synthetic.main.activity_main.sevenbutton
import kotlinx.android.synthetic.main.activity_main.sixbutton
import kotlinx.android.synthetic.main.activity_main.threebutton
import kotlinx.android.synthetic.main.activity_main.twobutton
import kotlinx.android.synthetic.main.activity_main2.*

class Solver : AppCompatActivity(), SudokuSolverBoardView.OnTouchListener {

    private lateinit var viewModel : SudokuViewModel
    private lateinit var numberButtons: List<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SudokuSolverBoardView.registerListener(this)

        viewModel = ViewModelProviders.of(this).get(SudokuViewModel::class.java)
        viewModel.sudokuSolver.selectedCellLiveData.observe(this, Observer { updateSelectedCellUI(it) })
        viewModel.sudokuSolver.cellsLiveData.observe(this, Observer { updateCells(it) })
        viewModel.sudokuSolver.highlightedKeysLiveData.observe(this, Observer { updateHighlightedKeys(it)})

        val numberButtons = listOf(onebutton, twobutton, threebutton, fourbutton, fivebutton, sixbutton, sevenbutton, eightbutton, ninebutton)

        numberButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                viewModel.sudokuSolver.handleInput(index + 1)
            }
        }

        delete_img.setOnClickListener { viewModel.sudokuSolver.delete() }
        deleteAll.setOnClickListener { viewModel.sudokuSolver.deleteAll() }
        solve_img.setOnClickListener { viewModel.sudokuSolver.sudokuSolverAlgorithm() }
    }

    private fun updateCells(cells: List<Cell>?) = cells?.let {
        SudokuSolverBoardView.updateCells(cells)
    }

    private fun updateSelectedCellUI(cell: Pair<Int, Int>?) = cell?.let {
        SudokuSolverBoardView.updateSelectedCellUI(cell.first, cell.second)
    }

    private fun updateHighlightedKeys(set: Set<Int>?) = set?.let {
        numberButtons.forEachIndexed { index, button ->
            val color = if (set.contains(index + 1)) ContextCompat.getColor(
                    this,
                    R.color.white
            ) else Color.LTGRAY
            button.setBackgroundColor(color)
        }
    }

    override fun onCellTouched(row: Int, col: Int) {
        viewModel.sudokuSolver.updateSelectedCell(row, col)
    }

//    fun mainActivity(view: View) {
//        val intent = Intent(this, MainActivity::class.java)
//        startActivity(intent)
//    }

    fun textShow(view: View?) {
        showText.visibility = View.VISIBLE
    }
}