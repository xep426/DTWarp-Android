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
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import kotlin.math.max
import kotlin.math.min

private const val RIGHT_TO_LEFT = 1

class GraphView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint()
    private val path = Path()
    var label = ""
    var color = Color.rgb(255,255,255)
    var scaleFactor = 20
    var offSetGraph = true

    private var buffer = FloatArray(200){0f}
    private var bufferPointer = 0
    private var direction = RIGHT_TO_LEFT

    override fun onDraw(canvas: Canvas) {
        path.reset()
        paint.style = Paint.Style.FILL
        paint.color = Color.TRANSPARENT
        canvas.drawPaint(paint)

        paint.color = color
        paint.textSize = 35f;
        canvas.drawText(label, 20f, 45f, paint)

        var index : Int
        for (i in 0 until buffer.size - 1) {
            index = if(direction == RIGHT_TO_LEFT) i + bufferPointer else i

            val firstValue = buffer[(index) %200] * scaleFactor + if(offSetGraph) canvas.height/2 else canvas.height
            val secondValue = buffer[(index + 1)%200] * scaleFactor + if(offSetGraph) canvas.height/2 else canvas.height
            path.moveTo((i+0f)/buffer.size*canvas.width, min(canvas.height-10f,max(10f,firstValue)))
            path.lineTo((i+1f)/buffer.size*canvas.width, min(canvas.height-10f,max(10f,secondValue)))

            path.close()
            paint.strokeWidth = 1f
            paint.pathEffect = null
            paint.style = Paint.Style.STROKE
            canvas.drawPath(path, paint)
        }
    }

    fun draw(value : Float, dir : Int = RIGHT_TO_LEFT){
        direction = dir
        buffer[bufferPointer] = value
        bufferPointer = (bufferPointer + 1) % 200
        this.invalidate()
    }

    fun clearBuffer(){
        buffer = FloatArray(200){0f}
        buffer = FloatArray(200){0f}
        bufferPointer = 0
    }
}