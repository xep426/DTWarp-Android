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

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.core.view.GestureDetectorCompat

class UIManager (mainActivity: MainActivity) : GestureDetector.SimpleOnGestureListener(){

    private var mainActivity : MainActivity = mainActivity
    val referenceSequenceGraphView : GraphView = mainActivity.findViewById(R.id.referenceSequenceGraphView)
    val liveSensorDataGraphView : GraphView = mainActivity.findViewById(R.id.liveSensorDataGraphView)
    val liveDtwDistanceGraphView : GraphView = mainActivity.findViewById(R.id.liveDtwDistanceGraphView)
    val startDetectingButton : Button = mainActivity.findViewById(R.id.startDetectingButton)
    val recordReferenceSequenceButton : Button = mainActivity.findViewById(R.id.recordReferenceSequenceButton)
    val distanceHud : DistanceHUD = mainActivity.findViewById(R.id.distanceHudView)
    private var startButtonToggle = false
    private var detector: GestureDetectorCompat


    init {
        mainActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        referenceSequenceGraphView.label = "Reference Sequence:"
        referenceSequenceGraphView.color = Color.rgb(255,109,0)

        liveSensorDataGraphView.label = "Data Stream:"
        liveSensorDataGraphView.alpha = 0.4f

        liveDtwDistanceGraphView.label = "DTW Distance:"
        liveDtwDistanceGraphView.alpha = 0.4f
        liveDtwDistanceGraphView.offSetGraph = false
        liveDtwDistanceGraphView.scaleFactor = 1

        startDetectingButton.isClickable = false
        startDetectingButton.alpha = 0.4f
        startDetectingButton.text

        startDetectingButton.setBackgroundColor(Color.rgb(27,94,32))
        startDetectingButton.text = "Start Detecting"

        detector = GestureDetectorCompat(mainActivity, this)

        distanceHud.setOnTouchListener { _, event->
         detector.onTouchEvent(event)
         true
        }
    }

    fun activateView(vararg views: View){
        for(view in views) {
            view.alpha = 1f
            if (view is Button) {
                view.isClickable = true
            }
        }
    }

    fun deActivateView(vararg views: View){
        for(view in views) {
            view.alpha = 0.4f
            if (view is Button) {
                view.isClickable = false
            }
        }
    }

    fun toggleStartButtonAppearance(){
        if(!startButtonToggle){
            startDetectingButton.setBackgroundColor(Color.rgb(183,28,28))
            startDetectingButton.text = "Stop Detecting"
            startButtonToggle = true
        }else{
            startDetectingButton.setBackgroundColor(Color.rgb(27,94,32))
            startDetectingButton.text = "Start Detecting"
            startButtonToggle = false
        }

    }

    fun clearBuffers(vararg views: GraphView){
        for(view in views){
            view.clearBuffer()
        }
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return true
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        distanceHud.threshold += distanceY
        mainActivity.detectionThreshold = distanceHud.threshold
        distanceHud.invalidate()
        return true
    }
}