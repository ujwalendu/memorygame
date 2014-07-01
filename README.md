memorygame
==========

This is a very simple memory based game. The player will be presented with 9
images in grid of 3X3. This grid will be visible for 15 seconds. Once 15
seconds are passed all the images will be flipped. At the bottom one image will
be shown to player. The player has to guess the location if the image. If the
image matches with the one at bottom then the image will continue to be
displayed otherwise it will flipped back and player has to do another guess.
Once all images are identified the game will be over.


The current code is dependent on Android Universal Image Loader. An another
android image loading library taken from

https://github.com/nostra13/Android-Universal-Image-Loader.git

The jar has been added to the libs folder
https://github.com/nostra13/Android-Universal-Image-Loader/raw/master/downloads/universal-image-loader-1.9.2-with-sources.jar


Building APK
============

git clone https://github.com/ujwalendu/memorygame.git

There are two ways to build the apk.
1) Using eclipse

	- Open eclipse and import this as Android project.
	- Rigth click on the project in project explorer and do run->run as
	  Android application.
This will build the apk in the bin directory.

2) Using command line
	cd /path/to/the/cloned/repo
	export SDK_ROOT=/path/to/the/android/sdk/
	$SDK_ROOT/tools/android update project -p .
	ant clean debug
This will create bin/MainActivity-debug.apk.



