# GAJA   
  * Check Location   
  * Find a fast way (~~Public transport~~, Car, ETC..)   
  * Provide Start, Goal information
  * Provide alarm option
  * Provide Bookmark DB
## Required   
  * Android OS
     > ___Minimum Specifications___ : Android API 20 (Android 5.0 [LOLLIPOP])   
  * Location Access
  * Network Access   
## Code
* ___res/values/client_id.xml___
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="naver_map_sdk_client_id" translatable="false">___Client KEY ID___</string>
</resources>
```
* ___res/values/client_secret.xml___
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="naver_map_sdk_client_secret" translatable="false">___Client KEY SECRET___</string>
</resources>
```
## Info
![info](https://user-images.githubusercontent.com/13824758/140650719-3fd41779-fb37-44d6-9ff1-d3e35a391a98.png)   
* ___Build Info___   
  > Android Gradle Plugin Version - ___4.1.0___   
  > Gradle Version - ___6.5___   
```Gradle
android {
    compileSdk 30

    defaultConfig {
        applicationId "com.example.gaja"
        minSdk 21
        targetSdk 30
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }
    ...
}
```   
   * ___Dependencies___   
     > Naver Maps SDK[https://www.ncloud.com/product/applicationService/maps]
```Gradle
dependencies {
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2-native-mt'
    implementation 'com.squareup.moshi:moshi:1.12.0'
    kapt 'com.squareup.moshi:moshi-kotlin-codegen:1.12.0'
    implementation	'com.squareup.retrofit2:converter-moshi:2.9.0'
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation 'com.naver.maps:map-sdk:3.12.0'
    implementation 'com.google.android.gms:play-services-location:18.0.0'
    implementation 'androidx.navigation:navigation-fragment-ktx:2.3.5'
    implementation 'androidx.navigation:navigation-ui-ktx:2.3.5'
    implementation 'androidx.core:core-ktx:1.6.0'
    implementation 'androidx.appcompat:appcompat:1.3.1'
    implementation 'com.google.android.material:material:1.5.0-alpha02'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.1'
    implementation 'androidx.legacy:legacy-support-v4:1.0.0'
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.3'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'
}
```   
   * ___Example Image___   
    ![example](https://user-images.githubusercontent.com/13824758/140650747-c65bdd49-9d13-4358-ad7f-1386ae688ebe.png)   
