# Android ironSource Adapter + Sample App

**General Notes and Prerequisites**<BR>

The Kidoz ironSource Adapter offers support for the following ad types:

+ ironSource Interstitial Mediation 
+ ironSource Rewarded Video Mediation 
  
Until ironSource mediation SDK supports Banner mediation for custom networks publishers who choose to do so can use Kidoz banners directly
from Kidoz SDK.
  
Before publishing your first app please finish the onboarding process for Kidoz's publishers [HERE](http://accounts.kidoz.net/publishers/register?utm_source=&utm_content=&utm_campaign=&utm_medium=).
  
And follow the instructions for ironSource Custom Adapter setup [HERE](https://developers.is.com/ironsource-mobile/general/custom-adapter-setup/).<BR>
You will need to setup the network level parameters with the `Publisher Id` and `Token` you got from Kidoz:  
  
  <img width="598" alt="ironSourceNetwork" src="https://user-images.githubusercontent.com/86282008/149078934-107106f0-a526-45bc-9c93-8ca53d5bf3cc.png">

Getting Started
=================================

#### Include the following dependencies in your application gradle.build file
```groovy
dependencies {
    implementation 'org.greenrobot:eventbus:3.2.0'
    implementation 'net.kidoz.sdk:kidoz-android-native:8.9.5'
    implementation 'net.kidoz.sdk:kidoz-android-ironsource-adapter:1.0.0'
}
``` 
