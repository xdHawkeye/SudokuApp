package com.example.sudoku.view

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.sudoku.R
import com.example.sudoku.game.Cell
import com.example.sudoku.view.custom.SudokuBoardView
import com.example.sudoku.viewmodel.SudokuViewModel
import kotlinx.android.synthetic.main.activity_main2.*


class MainActivity : AppCompatActivity(), SudokuBoardView.OnTouchListener {

    private lateinit var viewModel : SudokuViewModel
    private lateinit var numberButtons: List<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        SudokuBoardView.registerListener(this)

        viewModel = ViewModelProviders.of(this).get(SudokuViewModel::class.java)
        viewModel.sudokuGame.selectedCellLiveData.observe(this, Observer { updateSelectedCellUI(it) })
        viewModel.sudokuGame.cellsLiveData.observe(this, Observer { updateCells(it) })
        //viewModel.sudokuGame.isTakingNotesLiveData.observe(this, Observer { updateNoteTakingUI(it)})
        viewModel.sudokuGame.highlightedKeysLiveData.observe(this, Observer { updateHighlightedKeys(it) })

        val numberButtons = listOf(onebutton, twobutton, threebutton, fourbutton, fivebutton, sixbutton, sevenbutton, eightbutton, ninebutton)

        numberButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                viewModel.sudokuGame.handleInput(index + 1)
            }
        }

        notesButton.setOnClickListener { viewModel.sudokuGame.changeNoteTakingState() }
    }

    private fun updateCells(cells: List<Cell>?) = cells?.let {
        SudokuBoardView.updateCells(cells)
    }

    private fun updateSelectedCellUI(cell: Pair<Int, Int>?) = cell?.let {
        SudokuBoardView.updateSelectedCellUI(cell.first, cell.second)
    }

//    private fun updateNoteTakingUI(isNoteTaking: Boolean?) = isNoteTaking?.let {
//        if (it) {
//            notesButton.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
//        } else {
//            notesButton.setBackgroundColor(Color.LTGRAY)
//        }
//    }

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
        viewModel.sudokuGame.updateSelectedCell(row, col)
    }

//    fun nextActivity(view: View) {
//        val intent = Intent(this, Solver::class.java)
//        startActivity(intent)
//    }

}