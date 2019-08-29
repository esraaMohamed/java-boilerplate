# java-boilerplate

## Capabilities needed for `Android`
```
{
  "platformName": "Android",
  "deviceName": "emulator-5554",
  "platformVersion": "8",
  "appPackage": "com.vida.healthcoach",
  "appPath": "{{path_to_project}}/resources/deploy_2019-08-22_version_2.2.84-app-distro-release.apk",
  "appActivity": "com.vida.healthcoach.StartupActivity"
}
```

## Capabilities needed for `iOS`
```
{
  "platformName": "iOS",
  "deviceName": "iPhone X",
  "automationName": "XCUITest",
  "app": "{{path_to_project}}/resources/Vida.app",
  "platformVersion": "12.2"
}
```

> To open the emulator from the command line use `emulator -avd Pixel_2_API_26` where `Pixel_2_API_26` is the device name
> In order to do that you need to be inside the emulator under `/Library/Android/sdk/emulator`
> To run the scripts go to the command line and cd into the project and then type `mvn clean test


## Task
> Create a new branch for your code from the dev branch and after finishing the test scripts create a PR for you branch to the dev branch

> Write automation scripts to test the following:
1. Write a test to test the Schedule a consultation feature from the today tab
2. Write a test to test the rescheduling of a consultation
3. Write a test to test the cancelation of a consultation
 
```diff 
- Extra: Use cucumber for one of the tests
```

### Helpful screen shots of the feature:
![Today tab](https://i.ibb.co/8g3x5LC/Screenshot-1567036156.png)
![Schedule consultation screen](https://i.ibb.co/ZS8pSqp/Screenshot-1567036183.png)
![Success screen](https://i.ibb.co/m0pgsc1/Screenshot-1567036191.png)
![Cancel consultation screen](https://i.ibb.co/KmYZPVw/Screenshot-1567036199.png)

