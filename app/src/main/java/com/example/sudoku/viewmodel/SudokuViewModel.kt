package com.example.sudoku.viewmodel

import androidx.lifecycle.ViewModel
import com.example.sudoku.game.SudokuGame
import com.example.sudoku.game.SudokuSolver

class SudokuViewModel : ViewModel() {
    val sudokuGame = SudokuGame()
    val sudokuSolver = SudokuSolver()
}