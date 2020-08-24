package com.example.sudoku.game

import androidx.lifecycle.MutableLiveData

class SudokuSolver {
    var selectedCellLiveData = MutableLiveData<Pair<Int, Int>>()
    var cellsLiveData = MutableLiveData<List<Cell>>()
    var highlightedKeysLiveData = MutableLiveData<Set<Int>>()

    private var selectedRow = 4
    private var selectedCol = 4

    private val board: Board

    init {
        val cells = List(9 * 9) { i -> Cell(i / 9, i % 9, 0)}
        board = Board(9, cells)

        selectedCellLiveData.postValue(Pair(selectedRow, selectedCol))
        cellsLiveData.postValue(board.cells)
    }

    fun handleInput(number: Int) {
        if (selectedRow == -1 || selectedCol == -1) return
        val cell = board.getCell(selectedRow, selectedCol)
        if (!cell.isOpen) return

        cell.value = number
        cellsLiveData.postValue(board.cells)
    }


    fun updateSelectedCell(row: Int, col: Int) {
        board.getCell(selectedRow, selectedCol)
        selectedRow = row
        selectedCol = col
        selectedCellLiveData.postValue(Pair(row, col))
    }

    fun delete() {
        val cell = board.getCell(selectedRow, selectedCol)
        if (!cell.isOpen) return
        cell.value = 0
        cellsLiveData.postValue(board.cells)
    }

    fun deleteAll() {
        val size = 9
        for (i in 0 until size) {
            for (j in 0 until size) {
                board.getCell(i, j).value = 0
                board.getCell(i, j).isSolved = false
                board.getCell(i, j).isOpen = true
            }
        }
        cellsLiveData.postValue(board.cells)
    }

    fun sudokuSolverAlgorithm() {

        val size = 9
        val sqrt = 3
        var sudokuSolveBoard = arrayOf<Array<Int>>()

        for (i in 0..8) {
            var array = arrayOf<Int>()
            for (j in 0..8) {
                array += 0
            }
            sudokuSolveBoard += array
        }

        for (i in 0 until size) {
            for (j in 0 until size) {
                val num = board.getCell(i, j).value
                sudokuSolveBoard[i][j] = num
            }
        }

        fun isSafe(row: Int, col: Int, num: Int): Boolean {

            for (d in 0 until size){
                //if that number is in row, return false
                if(sudokuSolveBoard[row][d] == num){
                        return false
                    }
            }

            for (e in 0 until size){
                //if that number is in col, return false
                if(sudokuSolveBoard[e][col] == num){
                    return false
                }
            }

            //corresponding square
            val boxRowStart = row - (row % sqrt)
            val boxColStart = col - (col % sqrt)

            for(f in boxRowStart until (boxRowStart+sqrt)){
                for(g in boxColStart until (boxColStart+sqrt)){
                    if(sudokuSolveBoard[f][g] == num){
                        return false
                    }
                }
            }

            //if no false, it's safe
            return true
        }

        fun solveSudoku(array: Array<Array<Int>>, size: Int): Boolean {

            var row = -1
            var col = -1
            var isEmpty = true
            for (i in 0 until size) {
                for (j in 0 until size) {
                    if (sudokuSolveBoard[i][j] == 0) {
                        row = i
                        col = j

                        // not all cells filled
                        isEmpty = false
                        break
                    }
                }
                if (!isEmpty) {
                    break
                }
            }

            // all cells filled
            if (isEmpty) {
                return true
            }

            for (num in 1 until 10) {
                if (isSafe(row, col, num)) {
                    sudokuSolveBoard[row][col] = num
                    if (solveSudoku(array, size)) {
                        return true
                    } else {
                        sudokuSolveBoard[row][col] = 0
                    }
                }
            }
            return false
        }

        if (solveSudoku(sudokuSolveBoard, size)) {

            for (i in 0 until size) {
                for (j in 0 until size) {
                    val num = sudokuSolveBoard[i][j]
                        if (board.getCell(i, j).value == 0){
                            board.getCell(i, j).isSolved = true
                        }
                        board.getCell(i, j).value = num
                    board.getCell(i, j).isOpen = false
                }
            }

            cellsLiveData.postValue(board.cells)
        }
    }
}