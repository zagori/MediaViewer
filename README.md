<a href="https://bintray.com/zagori/maven/com.zagori:mediaviwer/1.0.0/link"><img src="https://api.bintray.com/packages/zagori/maven/com.zagori:mediaviewer/images/download.svg?version=1.0.0"/></a>
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)


# Overview

MediaViewer is a simple and customizable viewer that allows displaying media (images/videos) in a sliding screen UI. The followings are some of the main features:

* Display images and videos
* Zoom in/out images
* Swipe up/down to dismiss 
* show video placeholder
* play video
* Add customizable overlay 


# Setup
#### Step 1: Add jCenter to your project build.gralde file
```gradle
allprojects {
	repositories {
		//...
		jCenter()
	}
}
```


#### Step 2: add this dependency to your app build.gradle file
```gradle
dependencies {
  //...
  implementation 'com.zagori:mediaviewer:latest-release'
}
```


# ScreenShots
<img src="https://raw.githubusercontent.com/zagori/MediaViewer/master/attachments/AVENGERS_DEMO.gif" width="250">


# Modal Viewer 
#### Simple Usage
```
ModalViewer.load(context, media_URL_list).start();
```


#### Full Usage
```
ModalViewer
  .load(this, posterImages)
  .hideStatusBar(true)
  .allowZooming(true)
  .allowSwipeToDismiss(true)
  .addOverlay(overlayView)
  //.addOverlay(R.layout.overlay_view)
  .setImageChangeListener(new OnImageChangeListener(){
      @Override
      public void onImageChange(final int position) {
      }
  })
  .start();
```


# Persistent Viewer 
Still under development


# Developer
* [LinkedIn](https://www.linkedin.com/in/yousseflabihi/).
* [Twitter](https://twitter.com/yourizagori).


# Code Contributions
I welcome code contributions through pull requests. Please feel free to help make this plugin even better!


# License
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
```
Copyright 2019 Zagori

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```


