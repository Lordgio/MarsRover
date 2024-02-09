package dev.jorgeroldan.marsrover.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GridCanvas(
    rows: Int,
    columns: Int,
    onDrawCell: DrawScope.(row: Int, column: Int, size: Float, color: Color) -> Unit,
) {
    val textMeasurer = rememberTextMeasurer()
    val drawColor = MaterialTheme.colorScheme.onBackground
    val localDensity = LocalDensity.current
    var heightModifier by remember { mutableStateOf(0.dp) }
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .requiredHeight(heightModifier)
    ) {

        val canvasWidth = size.width
        val columnMeasure = canvasWidth / (columns + 1)
        val canvasHeight = columnMeasure * (rows + 1)
        heightModifier = with(localDensity) { canvasHeight.toDp() }

        for ((rowIndex, row) in (rows downTo 0).withIndex()) {
            for ((columnIndex, column) in IntProgression.fromClosedRange(0, columns, 1).withIndex()) {
                translate(
                    left = columnIndex * columnMeasure,
                    top = rowIndex * columnMeasure,
                ) {
                    drawRect(
                        style = Stroke(width = 2.dp.value),
                        color = drawColor,
                        size = Size(columnMeasure, columnMeasure)
                    )
                    drawText(
                        textMeasurer = textMeasurer,
                        text = "$column - $row",
                        style = TextStyle(
                            fontSize = 18.sp,
                            color = drawColor,
                        ),
                        size = Size(columnMeasure, columnMeasure)
                    )
                    onDrawCell(this, row, column, columnMeasure, drawColor)
                }
            }
        }
    }
}
