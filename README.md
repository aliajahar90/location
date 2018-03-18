This project is build to simplify the process of getting current location using google play services.I have been looking all over the internet for options where in one click you get the locations.
You do not have to handle so much of logic whether settings is satisfied or not, whether permission is allowed or not, whether playservice is available or not.
Well i tried to reduce the burden by separating the logic of access permission, check settings and connecting to play service api for location in an independent package.
Now you just have to copy that package to your application and define some gradle in your app module file and your are good to get location.
 
 To use this please follow the following steps
 
 step 1: add following in your app level build.gradle file.
 
           compile 'com.google.android.gms:play-services:11.4.2'
           compile 'pub.devrel:easypermissions:1.0.1'
           
  step 2: add following permission to your manifest
          <uses-permission android:name="android.permission.INTERNET" /> (Optional)
         <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"></uses-permission>
         <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"></uses-permission>
      
  step 3: Add following lines to your strings.xml file
            <string name="rationale_location">This app needs to access location.</string>
            <string name="setting">Settings</string>
             <string name="cancel">Cancel</string>
             
  step 4: Add copy and past location package along with all the files to your project.(Go to app->src->main->java->aliani->locator->location  
  
  And congratulation, you have successfully set it up.
  
	
	Now in your activity or fragment wherver you want to get location just override on resume method of activity or fragment and add following.

		  @Override
    protected void onResume() {
        super.onResume();
        new PermissionImpl(MainActivity.this,MainActivity.this).giveMeCurrentLocation();
    }

 
 this will ask you to implement LocationView interface and the following callbacks will give you the location .
 
    @Override
    public void next(@NonNull Location location) {
        textLocation.setText(location.getLatitude()+"\n"+location.getLongitude());
        Log.v("Here",location.getProvider());
    }


    @Override
    public void error() {
        //show appropriate error text here.
    }
 

