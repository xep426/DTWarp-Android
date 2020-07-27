package com.gmail.xep426.dynamictimewarping

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class DTWarpUnitTest {
    @Test
    fun check_dtw_distances_with_test_sequence(){

        // Initialize testSequence [10f,10f,10f,10f,10f]
        val testSequence = FloatArray(5){10f}
        val dtwTest = DTWarp(testSequence)
        // Emulate the stream of five 10f values followed by five 0f values
        assertEquals(40f, dtwTest.exec(10f)) // Comparing [0f,0f,0f,0f,10f] with testSequence
        assertEquals(30f, dtwTest.exec(10f)) // Comparing [0f,0f,0f,10f,10f] with testSequence
        assertEquals(20f, dtwTest.exec(10f)) // Comparing [0f,0f,10f,10f,10f] with testSequence
        assertEquals(10f, dtwTest.exec(10f)) // Comparing [0f,10f,10f,10f,10f] with testSequence
        assertEquals(0f, dtwTest.exec(10f)) // Comparing [10f,10f,10f,10f,10f] with testSequence

        assertEquals(10f, dtwTest.exec(0f)) // Comparing [0f,10f,10f,10f,10f] with testSequence
        assertEquals(20f, dtwTest.exec(0f)) // Comparing [0f,0f,10f,10f,10f] with testSequence
        assertEquals(30f, dtwTest.exec(0f)) // Comparing [0f,0f,0f,10f,10f] with testSequence
        assertEquals(40f, dtwTest.exec(0f)) // Comparing [0f,0f,0f,0f,10f] with testSequence
        assertEquals(50f, dtwTest.exec(0f)) // Comparing [0f,0f,0f,0f,0f] with testSequence
    }
}