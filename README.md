# Android Dynamic Time Warping Library

This library, along with its example app, is a foray into gesture detection. The intended purpose is to visually demonstrate DTW's utility and make setup for further experimentation as easy as possible.


![Image](https://media0.giphy.com/media/UqNI34fQGIoyzzFXVy/giphy.gif)

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


## TODO
1. Introduce support for multi dimensional sequences
2. Reassess abstraction level

## License 

MIT License

Copyright (c) 2020 xep426

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.