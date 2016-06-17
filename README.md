# app-push-library

A Library for Android MDM clients to push applications to the device. The library can be integrated with any mobile applications to accept app install messages through FCM.

#### Integrating the Library

Add the library in the dependencies section:

``` gradle
dependencies {
    ...
    compile 'com.multunus:apppusher:0.1'
}
```

In the application’s main activity `onCreate` method, start the onemdm-installer service as follows:

``` java
public void onCreate() {
    ...
    AppPusher.init();
}
```
