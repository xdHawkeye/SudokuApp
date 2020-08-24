package com.example.sudoku.view.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.sudoku.game.Cell
import kotlin.math.min

class SudokuSolverBoardView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private var sqrtSize = 3
    private var size = 9

    private var cellSizePixels = 0F

    private var selectedRow = 0
    private var selectedCol = 0

    private  var listener: OnTouchListener? = null

    private var cells: List<Cell>? = null

    private val thickLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 4F
    }

    private val thinLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.parseColor("#12161a")
        strokeWidth = 2F
    }

    private val selectedCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#3b43b3")
    }

    private val conflictingCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#1E272E")
    }

    private val otherCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#282f36")
    }

    private val startingCellTextPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#d2dae2")
        typeface = Typeface.DEFAULT_BOLD
    }

    private val textPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#0fbcf9")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val sizePixels = min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(sizePixels, sizePixels)
    }

    override fun onDraw(canvas: Canvas) {
        updateMeasurements(width)
        cellSizePixels = (width / size).toFloat()
        fillCells(canvas)
        drawLines(canvas)
        drawText(canvas)
    }

    private fun updateMeasurements(width: Int) {
        cellSizePixels = (width / size).toFloat()
        textPaint.textSize = cellSizePixels / 1.5F
        startingCellTextPaint.textSize = cellSizePixels / 1.5F
    }

    private fun fillCells(canvas: Canvas) {
        if (selectedRow == -1 || selectedCol == -1) return

        for (r in 0..size) {
            for (c in 0..size) {
                if (r == selectedRow && c == selectedCol){
                    fillCell(canvas, r, c, selectedCellPaint)
                } else if (r == selectedRow || c == selectedCol){
                    fillCell(canvas, r, c, conflictingCellPaint)
                } else if (r / sqrtSize ==  selectedRow / sqrtSize && c / sqrtSize == selectedCol / sqrtSize){
                    fillCell(canvas, r, c, conflictingCellPaint)
                } else {
                    fillCell(canvas, r, c, otherCellPaint)
                }
            }
        }
    }

    private fun fillCell(canvas: Canvas, r: Int, c: Int, paint: Paint) {
        canvas.drawRect(c * cellSizePixels, r * cellSizePixels, (c+ 1) * cellSizePixels, (r + 1) * cellSizePixels, paint)
    }

    private fun drawLines(canvas: Canvas) {
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), thickLinePaint)

        for (i in 1 until size){
            val paintToUse = when (i % sqrtSize){
                0 -> thickLinePaint
                else -> thinLinePaint
            }

            canvas.drawLine(
                    i * cellSizePixels,
                    0F,
                    i * cellSizePixels,
                    height.toFloat(),
                    paintToUse
            )

            canvas.drawLine(
                    0F,
                    i * cellSizePixels,
                    width.toFloat(),
                    i * cellSizePixels,
                    paintToUse
            )

        }
    }

    private fun drawText(canvas: Canvas) {
        cells?.forEach {cell ->
            if (cell.value == 0) {} else {

                val textBounds = Rect()

                val row = cell.row
                val col = cell.col
                val valueString = cell.value.toString()

                val paintToUse = if (cell.isSolved) {textPaint} else {startingCellTextPaint}
                textPaint.getTextBounds(valueString, 0, valueString.length, textBounds)
                val textWidth = paintToUse.measureText(valueString)
                val textHeight = textBounds.height()

                canvas.drawText(valueString,
                        (col * cellSizePixels) + cellSizePixels / 2 - textWidth / 2,
                        (row * cellSizePixels) + cellSizePixels / 2 + textHeight / 2,
                        paintToUse)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                handleTouchEvent(event.x, event.y)
                true
            }
            else -> false
        }
    }

    private fun handleTouchEvent(x: Float, y: Float) {
        val possibleSelectedRow = (y / cellSizePixels).toInt()
        val possibleSelectedCol = (x / cellSizePixels).toInt()
        listener?.onCellTouched(possibleSelectedRow, possibleSelectedCol)
    }

    fun updateSelectedCellUI(row: Int, col: Int) {
        selectedRow = row
        selectedCol = col
        invalidate()
    }

    fun updateCells(cells: List<Cell>) {
        this.cells = cells
        invalidate()
    }

    fun registerListener(listener: OnTouchListener) {
        this.listener = listener
    }

    interface OnTouchListener {
        fun onCellTouched(row: Int, col: Int)
    }
}