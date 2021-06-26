# Android Dynamic Time Warping Library

DTWarp is a Kotlin implementation of the [dynamic time warping](https://en.wikipedia.org/wiki/Dynamic_time_warping) algorithm. DTWarp caches float values in a ring buffer and calculates the distance between a reference sequence (provided during initialization) and the cached data every time it receives a new value. The smaller the calculated distance the more similar the two sequences being compared will be.

The example app lets you record a reference sequence of z-axis accellerometer values. This reference sequence can then be compared to a continuous live stream of z-axis accelerometer data. Once a distance below a set threshold is computed the example app will halt DTWarp execution.

![Image](https://media3.giphy.com/media/gHhreeLlak3Qph5fDQ/giphy.gif)

Check it out on: [Google Play](https://play.google.com/store/apps/details?id=com.gmail.xep426.dynamictimewarpingexample)

## Download

Step 1. Add the JitPack repository to your build file 

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Step 2. Add the dependency

```
dependencies {
        implementation 'com.github.xep426:DTWarp-Android:v.0.1.0'
	}
```

## Usage

Step 1. Initialize a DTWarp object by passing it a reference sequence of floating point values `DTWarp(FloatArray)`

Step 2. Repeatedly call `DTWarp#exec(Float)` in order to compare a continous stream of values to the reference sequence. `DTWarp#exec(Float)` will return a distance for each invocation.


## License 

MIT License

Copyright (c) 2020 xep426

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
