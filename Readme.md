# PAYUNGI
A repo for PAYUNGI project

//sementara ![contoh2](https://drive.google.com/uc?export=view&id=asda)
## What is PAYUNGI
PAYUNGI is an Umbrella renting application for smartphone that uses Android as its OS. Payungi has two application, for the users and for the stations. In the User application, users can 

## Screenshots

Berikut beberapa gambar menunjukan User application dan Station Application

![User Application](https://drive.google.com/uc?export=view&id=11J7XXvehyI0CoYey8mDFNhtdXyVqUdfI)

# How To Develop PAYUNGI
In this section, i will explain to you how you can join the development of PAYUNGI.
Before we start, Below are the requirement to start developing PAYUNGI:
* [Java Development Kit](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) (Recommeneded to use JDK-8 or greater JDK version) 
* [Clone/Download PAYUNGi-master](https://github.com/bayusatria71/PAYUNGi)
* [Android Studio](https://developer.android.com/studio) (Latest Version 4.0)
* [Firebase Account](https://firebase.google.com/)
* [Google Cloud Platform Account](https://console.developers.google.com/) (*optional*)

After you achieved all in the list, now we can start PAYUNGI developement.

## PAYUNGI Environment Installation Guide
### Android Studio Set-up

After you download Android Studio, the first thing Android studio gonna ask is whether user want to make new project or open an existing project.

![Option](https://github.com/bayusatria71/PAYUNGi/blob/master/Images/Open%20existing%20prpject.PNG)

Choose **open an existing project**, then made sure you choose the PAYUNGI-master clone/download

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

![Permission Asked](https://github.com/bayusatria71/PAYUNGi/blob/master/Images/save.PNG)

After you agreed to allow connection of firebase with android studio, go back to Android studio and you will find another tab showing what firebase project it's should be connected to. Either make a new Firebase Project or use project that you've already had.

![Another Example Image](https://github.com/bayusatria71/PAYUNGi/blob/master/Images/PilihBuatNewPorject.PNG)

in the picture i choose to use project that i've already had, but for you that dont have any project, choose to make one and give it a name that properly fits the project, and by doing that you're ready to test your code.

### Android Virtual Devices (AVD) Set-up
Running to test your code could be done in 2 way, Use your own phone or use Android Virtual Device. I recommend you to use AVD instead your phone, it's easier this way to debug your code if it's still has bugs and error, but requires some of your memory, storage and processing to use AVD.

To start with, you could open the AVD manager thats located near run button in top right of android studio.

![AVD MANAGER](https://github.com/bayusatria71/PAYUNGi/blob/master/Images/OpenAVDManager.PNG)



## PAYUNGI Compiling Guide
### Build Debug-app

### Build Signed-app
