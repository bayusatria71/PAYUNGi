# PAYUNGI
One of 2 repository for PAYUNGI project, This one for User Application.

**CONTENT**
* [What is PAYUNGI](#what-is-payungi)
* [Screenshots](#screenshots)
* [Environment Installation Guide](#environment-installation-guide)
  * [Android Studio Set-up](#android-studio-set-up)
  * [Firebase Set-up](#firebase-set-up)
  * [Android Virtual Devices Set-up](#android-virtual-devices-set-up)
* [PAYUNGI Compiling Guide](#payungi-compiling-guide)
  * [Build Debug-app](#build-debug-app)
  * [Build Signed-app](#build-signed-app)

## What is PAYUNGI
PAYUNGI is an Umbrella renting application for smartphone that uses Android as its OS. Payungi has two application, for the users and for the stations. In the User application, users can 

## Screenshots

These are a few picture of PAYUNGI user app and station app.

![User Application](https://drive.google.com/uc?export=view&id=11J7XXvehyI0CoYey8mDFNhtdXyVqUdfI)

# Environment Installation Guide
In this section, i will explain to you how you can join the development of PAYUNGI.
Before we start, Below are the requirement to start developing PAYUNGI:
* [Java Development Kit](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) (Recommeneded to use JDK-8 or greater JDK version) 
* [Clone/Download PAYUNGi-master](https://github.com/bayusatria71/PAYUNGi)
* [Android Studio](https://developer.android.com/studio) (Latest Version 4.0)
* [Firebase Account](https://firebase.google.com/)
* [Google Cloud Platform Account](https://console.developers.google.com/) (*optional*)

After you achieved all in the list, now we can start PAYUNGI developement.

### Android Studio Set-up

After you download Android Studio, the first thing Android studio gonna ask whether user want to make new project or open an existing project.

![Option](https://github.com/bayusatria71/PAYUNGi/blob/master/Images/Open%20existing%20prpject.PNG)

Choose **open an existing project**, then make sure you choose the PAYUNGI-master clone/download

![Example](https://github.com/bayusatria71/PAYUNGi/blob/master/Images/PayungiMaster.PNG)

after android studio open the project, it will automatically download all the library it need, starting from Gradle library and then other implemented libraries in the project.

![Gradle](https://github.com/bayusatria71/PAYUNGi/blob/master/Images/Download%20Gradle.PNG) ![OtherLibraries](https://github.com/bayusatria71/PAYUNGi/blob/master/Images/DownloadOtomatis.PNG)

This action will take about 10 - 30 minutes to download all the library, wait until it's done.
After it's done you can now see the android studio has already indexed the file so it's more easy to read

![Another Example](https://github.com/bayusatria71/PAYUNGi/blob/master/Images/SelesaiDownloadLibrary.PNG)

If you have achieve until this step then you're ready to develop your code to PAYUNGI project.


### Firebase Set-up
After You change the code and messing with it's backend, You definitely want to test it out but before you could test the code in either your own phone or Android Virtual Device(AVD). You need to connect your firebase account to the project. It could be done by checking the Firebase assistant in tools tab.

![Firebase Assistant](https://github.com/bayusatria71/PAYUNGi/blob/master/Images/Firebase.PNG)

After You click Firebase, the assistant will show up and give you all the option that Firebase gives. But what You need is to connect Firebase Account to the Android studio. So you can click the Firebase Authentication and click `email and password authentication`

![Firebase Authentication](https://github.com/bayusatria71/PAYUNGi/blob/master/Images/email%20dan%20password.PNG)

After that it will show You, a button that will open a new browser tab to Firebase Console. Connect your account that you want, and Firebase will ask for your permission.

![Permission Asked](https://github.com/bayusatria71/PAYUNGi/blob/master/Images/Permission.jpg)

After you agreed to allow connection of firebase with android studio, go back to Android studio and you will find another tab showing what firebase project it's should be connected to. Either make a new Firebase Project or use project that you've already had.

![Another Example Image](https://github.com/bayusatria71/PAYUNGi/blob/master/Images/PilihBuatNewPorject.PNG)

in the picture i choose to use project that i've already had, but for you that dont have any project, choose to make one and give it a name that properly fits the project, and by doing that you're ready to test your code.

### Android Virtual Devices Set-up
Running to test your code could be done in 2 way, Use your own phone or use Android Virtual Device. I recommend you to use AVD instead your phone, it's easier this way to debug your code if it's still has bugs and error, but requires some of your memory, storage and processing to use AVD.

To start with, you could open the AVD manager thats located near run button in top right of android studio.

![AVD MANAGER](https://github.com/bayusatria71/PAYUNGi/blob/master/Images/OpenAVDManager.PNG)

then it will show the manager

![AVD](https://github.com/bayusatria71/PAYUNGi/blob/master/Images/AvdManager.PNG)

if you doesnt have any AVD yet, click on **Create Virtual Device**

Then choose the hardware spesification that want to be virtualized, Recommended to use large screen phones (Trend)

![Choose Hardware](https://github.com/bayusatria71/PAYUNGi/blob/master/Images/Hardware.PNG)

The hardware is ready but it's still needed an Image, choose the image that fits the project, for this I recommend to use Android Pie or greater image. 

![Choose Image](https://github.com/bayusatria71/PAYUNGi/blob/master/Images/ChooseHardware.PNG)

Then click download, then it will open new tab that will automatically download the image for you.

![Download Image](https://github.com/bayusatria71/PAYUNGi/blob/master/Images/DownloadImageOtomatis.PNG)

if the download succeeded, then you can virtualize the phone to test the codes.

## PAYUNGI Compiling Guide
After you set up the Android, Firebase and the AVD, its finally time to Build the Application to an APK file. There is 2 kind of apk type, one for debug and one for signed-app ,when the app is ready to be released.

### Build Debug-app
To Build Debug app all you need to do is  go to Build tab, and choose Build APK

![Build Apk](https://github.com/bayusatria71/PAYUNGi/blob/master/Images/BuildAPK.jpg)

Then it will make the Apk for you, it would take about 1 - 2 minutes to build the APK. When it finished it will show you a baloon pop up

![Done](https://github.com/bayusatria71/PAYUNGi/blob/master/Images/BuildAPKDone.jpg)

in the baloon there's a hyperlink that will redirect you to the apk location. Click it to take you to the apk

![Example Done](https://github.com/bayusatria71/PAYUNGi/blob/master/Images/apk%20done.jpg)

After the apk is built, it can be use to install the project to phones and AVD to test the codes.
But it will need your action to move the apk manually to phone or avd storage, if you want to test it in AVD without building any APK, you can choose run the app on the AVD.

### Build Signed-app

To build Released app that will be placed in google play store, it need you to sign the app with your credential so that Google play store could guarantee the safety of your app. You sign it by build a signed apk, to do this you choose Generate Siged Apk in the build tab

![Generate Signed Apk](https://github.com/bayusatria71/PAYUNGi/blob/master/Images/signed.jpg)

After that it wants you to give the app its keystore, if you dont have one click on create new

![Create new](https://github.com/bayusatria71/PAYUNGi/blob/master/Images/CreateNewKeyStore.jpg)

Fill out all the list, make sure you remember the password. After you make the Keystore you need to define the apps type, debug or release.

![Release/Debug](https://github.com/bayusatria71/PAYUNGi/blob/master/Images/Release.jpg)

Choose release and check v1 and v2, its not necessary but if it bothering the build, just choose on of the Version.
After that it will generate your build APK, the same as building un-signed APK it will pop up a baloon that can redirect you to the location of the Apk. Thus you're done and you can upload your app to google play store without any problem.
