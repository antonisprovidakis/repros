# repros
Repros is the final project for the course "Advanced Software Engineering", in the context of my MSc. degree.
It's a Room Energy Profiling System (Android app + Arduino + 3rd-party weather services) with monitoring and
intervention capabilities. Note: the "intervention" is not fully functional, **but stands for a concept**.
The app collects environmental metrics from:
  a) smart-device's embedded sensors,
  b) Arduino microcontroller installed in a room and
  c) 3rd-party weather service, and monitors the Energy Profile of the room.
In addition, the app can control (intervene) electrical appliances, if necessary.

For more info see [documentation](./documentation) folder

## Third-party libraries
Repros make use of the following libraries:

  * [SensingKit-Android](https://github.com/SensingKit/SensingKit-Android), under GNU LGPL v3.0 

  * [AndroidWidgets](https://github.com/ntoskrnl/AndroidWidgets) 

  * [android-motion-detection](https://github.com/phishman3579/android-motion-detection), under Apache License 2.0 
