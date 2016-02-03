//# Visual_Dialer_Android_ApplicationUI//
//Android Application based on a phone with sdk version 2.3 or higher.//
If you want to work on application from scratch....
Download the repository by clicking on  Download ZIP
Unzip the VisualDialer_Android.zip file. 
Download and un-zip the ADT Bundle which includes a version of Eclipse ready for Android Development:
http://developer.android.com/sdk/installing/bundle.html

Start Eclipse.

Create a new Android Application Project from our existing project:
File -> New Project
Android -> Android Project From Existing Code -> Next
Browse and select the UIAndroid folder from the unzipped location

Check "Copy projects into workspace" if you want to keep you svn repo clean
Finish

Set up a new Android Virtual Device:
Window -> Android Virtual Device Manager
New
Type any name
Pick one of the later devices (Choose the Nexus 4)
Pick a Target (Choose Android 2.3.3 - API Level 10)
OK
Close Android Virtual Device Manager

Run the application:
Right-click the project -> Run As -> Android Application
That will start the app.
Be patient, it takes a while and you will see the android logo while it is loading the emulator (this could be maybe because of my emulator choices).

Some additional steps related to Google-Play Services:

Go to VisualDialer properties
	- Go to the libraries tab
		- Remove the google-play-services
Go to "File", "Import.." and pick "Existing Android Code Into Workspace" under "Android"
 - Browse to where you downloaded the ADT Bundle
	- go to the sub folder sdk\extras\google_play_services and select it
	- You will now see a list of project. Select the top project (skip the samples)

Again, go to VisualDialer properties
 - go to Libraries tab
	- Java Build Path
		- Add JARs
			- Go to google-play-services_lib/libs and pick “google-play-services.jar”
 - Go to “Order and Export” tab
	- Select the google-play-services and click ok
