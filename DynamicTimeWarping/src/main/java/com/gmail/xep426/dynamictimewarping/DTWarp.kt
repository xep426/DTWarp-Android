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

package com.gmail.xep426.dynamictimewarping

import kotlin.math.max
import kotlin.math.min
import kotlin.math.abs

class DTWarp (private val referenceSequence: FloatArray) {
    // Two dimensional array used by the DTW algorithm to dynamically calculate the distance between two sequences
    private var cumulativeDistanceMatrix : Array<FloatArray>

    // Variable set to referenceSequence length. Data obtained from the sensor will split into sequences
    // of this length for comparison to the referenceSequence.
    private var sequenceLength : Int = 0

    // An array that holds sensor data. Values are currently written/read from this object without the use of
    // the modulo operator. This array is therefore twice the length of the reference sequence.
    private var ringBuffer : FloatArray

    // A variable for keeping track of the ringBuffer index intended for writing the next incoming sensor value
    private var bufferWritePointer : Int = 0

    // A variable for keeping track of the first index of the sensor data sequence intended for DTW computation.
    // Data from the ringBuffer from this index until sequenceLength will be compared to the referenceSequence.
    private var bufferReadPointer : Int = 0

    // This variable restricts the DTW computation along the diagonal of the cumulativeDistanceMatrix */
    private var window = 10

    init {
        sequenceLength = referenceSequence.size
        ringBuffer = FloatArray(referenceSequence.size*2)
        bufferWritePointer = (referenceSequence.size - 1)
        cumulativeDistanceMatrix = Array(sequenceLength+1){ FloatArray(referenceSequence.size+1) }
    }

    /**
     * Repeatedly call this method to append sensor values to the end of the ring buffer.
     * A DTW computation will be performed for every method call. It will compare the last n
     * (where n is the length of the referenceSequence) values to the referenceSequence.
     * Since the ringBuffer is initialized with zeros and populated back to front, n can
     * initially be less than the referenceSequence length.
     *
     * @param value
     *        sensor value
     * @return the distance between the streamed sensor data and the referenceSequence as determined per DTW computation
     */
    fun exec(value : Float): Float {
        writeToRingBuffer(value)
        return performDTW()
    }

    private fun performDTW():Float{
        // Reset all values within cumulativeDistanceMatrix
        for(i in 0..sequenceLength) {
            for (j in 0..sequenceLength){
                cumulativeDistanceMatrix[i][j] = Float.POSITIVE_INFINITY;
            }
        }

        // Initialize origin
        cumulativeDistanceMatrix[0][0] = 0f

        // Calculate shortest distance between reference sequence and sequence of streamed values
        for (i in 1..sequenceLength){
            for (j in max(1, i - window) until min(sequenceLength+1, i+window)){
                val cost = abs(referenceSequence[i-1]-ringBuffer[bufferReadPointer+j-1])
                cumulativeDistanceMatrix[i][j] = cost + min(cumulativeDistanceMatrix[i-1][j-1], min(cumulativeDistanceMatrix[i-1][j],cumulativeDistanceMatrix[i][j-1]))
            }
        }

        bufferReadPointer = (bufferReadPointer + 1) % sequenceLength

        // Return shortest time warped distance between sequences
        return cumulativeDistanceMatrix[sequenceLength][sequenceLength]
    }

    private fun writeToRingBuffer(value : Float){
        ringBuffer[bufferWritePointer] = value
        ringBuffer[bufferWritePointer+sequenceLength] = value
        bufferWritePointer = (bufferWritePointer + 1) % sequenceLength
    }
}