# Android ironSource Adapter + Sample App

The Kidoz ironSource Adapter SDK is built and tested with ironSource mediation v7.1.13.<BR>
You should only use this version or above as it is the first stable custom mediation release. <BR>

The Adapter offers support for the following ad types:

+ ironSource Interstitial Mediation 
+ ironSource Rewarded Video Mediation 
  
Until ironSource mediation SDK supports Banner mediation for custom networks, publishers who choose to do so can use Kidoz banners directly
from Kidoz SDK.<BR>
  
Prerequisites
=================================
  
Before publishing your first app please finish the onboarding process for Kidoz's publishers [HERE](http://accounts.kidoz.net/publishers/register?utm_source=&utm_content=&utm_campaign=&utm_medium=)  
and follow the instructions for ironSource Custom Adapter setup [HERE](https://developers.is.com/ironsource-mobile/general/custom-adapter-setup/).<BR><BR>
Kidoz Network ID on ironSource is `2b618dcd` and you will need to setup the network level parameters with the `Publisher Id` and `Token` you got from Kidoz:  
  
  <img width="598" alt="ironSourceNetwork" src="https://user-images.githubusercontent.com/86282008/149078934-107106f0-a526-45bc-9c93-8ca53d5bf3cc.png">

Getting Started
=================================

#### Include the following dependencies in your application gradle.build file
```groovy
dependencies {
    implementation 'org.greenrobot:eventbus:3.2.0'
    implementation 'net.kidoz.sdk:kidoz-android-native:8.9.7'
    implementation 'net.kidoz.sdk:kidoz-android-ironsource-adapter:1.1.1'
}
``` 

IronSource Integration
=================================
  
On a general note you should follow the instructions given on the ironSource Android SDK Integration page [HERE](https://developers.is.com/ironsource-mobile/android/android-sdk/) but as far as Kidoz integration goes you only need to do the following on your activity:
  
```java
  // Initialize listeners
  IronSource.setInterstitialListener(mInterstitialListener);
  IronSource.setManualLoadRewardedVideo(mRewardedVideoManualListener);
  
  // Init ironSource
  IronSource.init(this, APP_KEY);
```
See the sample code for example as how to load and show Interstitial and Rewarded videos and receive Ad units lifecycle callbacks.
Be aware that you need to call the `IronSource.setManualLoadRewardedVideo(...)` method in order to be able to load and show Rewarded Videos programmatically from your code.
  
Kidoz Direct Banners
=================================
  
Until ironSource mediation SDK supports banners custom adapters publishers can load and show Kidoz banners by using Kidoz SDK directly.

```java
// Intializing Kidoz SDK if not already Initialized by ironSource
  
  if(KidozSDK.isInitialised()){
      // If load ironSource interstitial or Rewarded was called previously Kidoz SDK should be already initialized
      initKidozBanners();
  }
  else {
      KidozSDK.setSDKListener(new SDKEventListener() {
          @Override
          public void onInitSuccess() {
              initKidozBanners();
          }

          @Override
          public void onInitError(String error) {
              // Handle Kidoz initialization error here

          }
      });
      KidozSDK.initialize(this, `Publisher ID`, `Token`);
  }
```
Make sure the `Publisher ID` and `Token` you send on the `KidozSDK.initialize(...)` method are your own unique parameters and not the Kidoz Test parameters used in this sample. This parameters need to be the same ones you used as network level parameters when adding the Kidoz network on the ironSource dashboard.<BR>
  
See the sample code for example as how to init, load and show Kidoz Banners and receive lifecycle callbacks.
