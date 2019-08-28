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

