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

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import com.gmail.xep426.dynamictimewarping.DTWarp

private const val LEFT_TO_RIGHT = 0

class MainActivity : Activity(), SensorEventListener {

    private lateinit var uiManager : UIManager
    private lateinit var sensorManager : SensorManager
    private lateinit var linearAccelerometer: Sensor
    private var referenceSequenceBuffer = FloatArray(200)
    private var numberOfRecordedValues = 0
    private var recordingReferenceSequence = false
    private var toggleStartDetecting = false
    private lateinit var dtwarp : DTWarp
    private var firstValue = true
    var detectionThreshold = 100f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up GUI elements
        uiManager = UIManager(this)

        // Set up linear accelerometer
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        linearAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
    }

    fun startRecordingReferenceSequence(v: View){
        // Modify GUI
        uiManager.deActivateView(uiManager.startDetectingButton, uiManager.referenceSequenceGraphView)

        // Clear previous reference sequence buffer and reinitialize variables for new recording
        uiManager.referenceSequenceGraphView.clearBuffer()
        recordingReferenceSequence = true
        numberOfRecordedValues = 0

        // Start reading accelerometer data
        sensorManager.registerListener(this, linearAccelerometer, SensorManager.SENSOR_DELAY_FASTEST)
    }

    fun startDetecting(v:View){
        if(!toggleStartDetecting){
            // Modify UI
            uiManager.clearBuffers(uiManager.liveSensorDataGraphView, uiManager.liveDtwDistanceGraphView)
            uiManager.activateView(uiManager.liveSensorDataGraphView, uiManager.liveDtwDistanceGraphView)
            uiManager.liveSensorDataGraphView.color = Color.rgb(255,255,255)
            uiManager.deActivateView(uiManager.recordReferenceSequenceButton)
            uiManager.toggleStartButtonAppearance()

            // Start reading accelerometer data
            sensorManager.registerListener(this, linearAccelerometer, SensorManager.SENSOR_DELAY_FASTEST)

            // Initialize DTWarp object with recorded reference sequence
            dtwarp = DTWarp(referenceSequenceBuffer)

            firstValue = true
            toggleStartDetecting = true
        }else{
            // Modify UI
            uiManager.deActivateView(uiManager.liveSensorDataGraphView, uiManager.liveDtwDistanceGraphView)
            uiManager.activateView(uiManager.recordReferenceSequenceButton)
            uiManager.toggleStartButtonAppearance()

            // Stop reading accelerometer data
            sensorManager.unregisterListener(this)

            toggleStartDetecting = false
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        // Determine whether to write reference sequence or whether to perform dtw. Send event to the corresponding method
        if(recordingReferenceSequence) event?.let {
            writeValueToReferenceSequence(it)
        } else event?.let {
            // Calculate distance between reference sequence and live data received so far
            var returnedDistance= dtwarp.exec(event!!.values[2])

            // Lower the threshold if first dtw computation already falls below it
            if(firstValue && returnedDistance < detectionThreshold){
                uiManager.distanceHud.threshold = returnedDistance-50
                uiManager.distanceHud.invalidate()
                detectionThreshold = returnedDistance - 50
            }
            firstValue = false
            // Draw distance onto DTW distance GraphView
            uiManager.liveDtwDistanceGraphView.draw(-returnedDistance)
            // Draw value onto live sensor GraphView
            uiManager.liveSensorDataGraphView.draw(event!!.values[2])
            // Check if distance is below threshold
            isBelowThreshold(returnedDistance)
        }
    }

    private fun isBelowThreshold(value : Float){
        if (value < detectionThreshold){
            // Modify UI
            uiManager.toggleStartButtonAppearance()
            uiManager.activateView(uiManager.recordReferenceSequenceButton)
            uiManager.liveSensorDataGraphView.color = Color.rgb(27,200,32)

            // Stop reading accelerometer data
            sensorManager.unregisterListener(this)

            toggleStartDetecting = false
        }
    }

    private fun writeValueToReferenceSequence(event:SensorEvent){
        // Check if the end of reference sequence buffer has been reached
        if(numberOfRecordedValues >100 && numberOfRecordedValues< referenceSequenceBuffer.size+100){
            // Modify UI
            uiManager.activateView(uiManager.referenceSequenceGraphView)

            // Record sensor value to reference sequence
            referenceSequenceBuffer[numberOfRecordedValues-100] = event!!.values[2]

            // Display sensor value on its corresponding GraphView
            uiManager.referenceSequenceGraphView.draw(event!!.values[2], LEFT_TO_RIGHT)

        }else if(numberOfRecordedValues > referenceSequenceBuffer.size+100){
            // Update UI
            uiManager.activateView(uiManager.startDetectingButton)

            // Stop reading accelerometer data
            sensorManager.unregisterListener(this)

            recordingReferenceSequence = false
        }
        numberOfRecordedValues += 1
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}