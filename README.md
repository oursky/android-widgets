# Oursky Widgets Library [![Release](https://jitpack.io/v/oursky/android-widgets.svg)](https://jitpack.io/#oursky/android-widgets)
> Commonly used widgets for Android

![android](https://img.shields.io/badge/android-kotlin-blue.svg)
[![Build Status](https://travis-ci.org/oursky/android-widgets.svg?branch=master)](https://travis-ci.org/oursky/android-widgets)

This repository contain some useful and generic widgets. The widgets is implemented in kotlin but is also usable for Java.

### Usage
Edit top level `build.gradle` and add `maven { url 'https://jitpack.io' }`, for example:
```
allprojects {
  repositories {
    google()
    jcenter()
    maven { url 'https://jitpack.io' }
  }
}
```
Next, edit `{app}/build.gradle`, add dependencies:
```
dependencies {
  ...
  implementation 'com.github.oursky:android-widgets:0.0.1'
}
```
Replace `0.0.1` with the latest version. For a list of version, check the [releases page](https://github.com/oursky/android-widgets/releases).

###### NOTE for using snapshot
You may also use the special version tag `master-SNAPSHOT` to access latest features. Note that gradle will cache the dependency so you may want to add this to `app/build.gradle`
```
configurations.all {
    // do not cache changing modules, ie. 'com.github.oursky:android-widgets:master-SNAPSHOT'
    resolutionStrategy.cacheChangingModulesFor 0, 'seconds'
}
```
See [this](https://github.com/jitpack/jitpack.io) for detail.

Now you should be able to use the widgets directly. See here for a [list of widgets](library/src/main/java/com/oursky/widget).
