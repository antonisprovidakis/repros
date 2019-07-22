<p align="center">
  <img alt="repros" src="https://user-images.githubusercontent.com/15313363/61642667-0b914b80-acaa-11e9-9a38-ca4fe6f7b68a.png" width="256">
</p>

## About
rePROs is the final project for the course "Advanced Software Engineering", in the context of my MSc. degree.
It's a Room Energy Profiling System (Android app + Arduino + 3rd-party weather services) with monitoring and
intervention capabilities. Note: the "intervention" part is not fully functional, **but stands for a concept**.
The app collects environmental metrics from:
  a) smart-device's embedded sensors,
  b) Arduino microcontroller installed in a room and
  c) 3rd-party weather service, and monitors the Energy Profile of the room.
In addition, the app can control (intervene) electrical appliances, if necessary.

### Non-Intrusive Profiling - System Architecture
![non-intrusive profiling](https://user-images.githubusercontent.com/15313363/61643590-e271ba80-acab-11e9-8cda-c5b569976046.png)

### Intrusive Profiling - System Architecture
![intrusive profiling](https://user-images.githubusercontent.com/15313363/61644211-f538bf00-acac-11e9-8c63-879d691fcb9b.png)

For more info see [documentation](./documentation) folder

## Screenshots

### Non-Intrusive Profiling
![non-intrusive timeframe definition](https://user-images.githubusercontent.com/15313363/61644381-3630d380-acad-11e9-9293-34e6f68c48b6.png)

![non-intrusive profiling 1](https://user-images.githubusercontent.com/15313363/61644384-3630d380-acad-11e9-941e-62a12a198098.png)

![non-intrusive profiling 2](https://user-images.githubusercontent.com/15313363/61644385-36c96a00-acad-11e9-9ba1-2bdb8d240fd0.png)

![non-intrusive assessment results](https://user-images.githubusercontent.com/15313363/61644386-36c96a00-acad-11e9-9e9e-f820e47643d4.png)

<p align="center">
  <img alt="non-intrusive assessment recommendations" src="https://user-images.githubusercontent.com/15313363/61644388-37620080-acad-11e9-9cb9-2aaf2033452f.png" />
</p>

### Intrusive Profiling
![intrusive scan call for action](https://user-images.githubusercontent.com/15313363/61644335-29ac7b00-acad-11e9-953c-e392db7d067e.png)

![intrusive scanning of qr code](https://user-images.githubusercontent.com/15313363/61644336-29ac7b00-acad-11e9-8a03-1feb763e03c8.png)

<p align="center">
    <img alt="intrusive encoded json" src="https://user-images.githubusercontent.com/15313363/61650764-11445c80-acbd-11e9-8cbe-0a4741bbd44a.png" />
  <img alt="intrusive room info 1" src="https://user-images.githubusercontent.com/15313363/61644338-29ac7b00-acad-11e9-9973-dc4953d81598.png" />
  <img alt="intrusive room info 2" src="https://user-images.githubusercontent.com/15313363/61644339-2a451180-acad-11e9-82b8-e90a701f23b1.png" />
  <img alt="intrusive profiling 1" src="https://user-images.githubusercontent.com/15313363/61644340-2a451180-acad-11e9-9d89-e8fc6a1a2e98.png" />
  <img alt="intrusive profiling 2" src="https://user-images.githubusercontent.com/15313363/61644342-2b763e80-acad-11e9-950c-7f8309d4efed.png" />
  <img alt="non-intrusive assessment results" src="https://user-images.githubusercontent.com/15313363/61644345-2b763e80-acad-11e9-94e3-1e173f898da4.png" />
  <img alt="intrusive assessment recommendations" src="https://user-images.githubusercontent.com/15313363/61644348-2c0ed500-acad-11e9-9cbd-4428d630b48b.png" />
  <img alt="intrusive assessment appliances" src="https://user-images.githubusercontent.com/15313363/61644349-2c0ed500-acad-11e9-9620-2c09185e6cdb.png" />
</p>

## Third-party Libraries
rePROs make use of the following libraries:

  * [SensingKit-Android](https://github.com/SensingKit/SensingKit-Android), under GNU LGPL v3.0 

  * [AndroidWidgets](https://github.com/ntoskrnl/AndroidWidgets) 

  * [android-motion-detection](https://github.com/phishman3579/android-motion-detection), under Apache License 2.0 
