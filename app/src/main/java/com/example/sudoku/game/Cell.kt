package com.example.sudoku.game

class Cell(
    val row: Int,
    val col: Int,
    var value: Int,
    var isStartingCell: Boolean = false,
    var notes: MutableSet<Int> = mutableSetOf(),
    var isSolved: Boolean = false,
    var isOpen: Boolean = true
)