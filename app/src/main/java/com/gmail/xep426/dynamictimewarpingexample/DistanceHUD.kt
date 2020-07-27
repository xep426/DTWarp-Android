/*
 * MIT License
 *
 * Copyright (c) 2020 xep426
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.gmail.xep426.dynamictimewarpingexample

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.DragEvent
import android.view.View
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class DistanceHUD @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr){

    private val paint = Paint()
    private val path = Path()
    var color = Color.rgb(255,255,255)
    var threshold = 100f

    override fun onDraw(canvas: Canvas) {
        // Restrict threshold value to canvas
        threshold = min(max(threshold, 0f),canvas.height.toFloat() - 10)

        paint.color = color
        paint.textSize = 35f;
        paint.style = Paint.Style.FILL
        paint.pathEffect = null

        canvas.drawText("▷", 5f, canvas.height-threshold+12, paint)
        canvas.drawText("◁", canvas.width - 40f, canvas.height-threshold+12, paint)
        canvas.drawText("Threshold: ${threshold.roundToInt()}", canvas.width.toFloat()/2-120, canvas.height.toFloat()-30,paint)

        path.reset()
        path.moveTo(50f,canvas.height-threshold)
        path.lineTo(canvas.width.toFloat()-48, canvas.height-threshold)
        path.close()
        paint.strokeWidth = 1f
        paint.pathEffect = DashPathEffect(floatArrayOf(10f, 20f), 0f)
        paint.style = Paint.Style.STROKE
        canvas.drawPath(path, paint)

    }
}