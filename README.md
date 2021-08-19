## Introduction

This is a Maven demo project built with Java 8 that can be used to test out the [ios-appium-docker](https://github.com/shamanec/ios-appium-docker) project.

## Setup

Just build the project and execute any/all of the 4 tests in the **Tests.java** class.

## Content

 * **nativeTest()** - executes a simple test against the Preferences app using **Mobile.by.iOSClassChain("")** to identify and interact with an element.
 * **nativeTestWithVideo()** - executes the same test as above but records the test execution and saves it to a video file in */src/main/resources*
 * **nativeImageTest()** - executes a simple test against the Preferences app using **Mobile.by.image("")** and the *opencv4nodejs* library to identify and interact with an element using provided image.
 * **safariTest()** - executes a simple test in the Safari browser
