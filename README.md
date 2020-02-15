# AutoRestart
Restart your server automatically today, and clear some of your RAM for a faster, smoother Minecraft experience. This integration is seamless and works with Spigot. Please follow the instructions to ensure a proper setup!
### Instructions

##### Check if `spigot.yml` is configured
Before we install `AutoRestart-v4.jar` into your plugins folder, we need to configure `spigot.yml`. To check if you need to configure `spigot.yml`, type `/restart` in the console and see if the server restarts without installing `AutoRestart-v4.jar`. 

_AutoRestart_ uses spigots native restart method to restart your server, and since most panels and frameworks are compatible with this function. Why make a new one?

##### If `/restart` works, skip this step
Go into `spigot.yml` and find the `restart-script` node. Change the `restart-script` to the name of the script used to start your server. If your run file is named `start.bat` then change `restart-script` to the following:
```yaml
## Windows
restart-script: start.bat
```
```yaml
## Linux / Bash
restart-script: ./start.sh
```

##### Install `AutoRestart-v4.jar`
Now that we have `spigot.yml` configured, move `AutoRestart-v4.jar` into your `/plugins` folder. Start the server, and make sure that there are not errors caused by `AutoRestart`. If any errors are displayed, please **CONTACT ME** so I may address it immediately so other players wont receive the same issue!

##### Test and Enjoy
Log into your server, and type `/autore time` to display the time the server will restart. If you do not get any `Title Popups`, check the console to see if you have any errors.

`AutoRestart`'s core timing and messaging system should be compatible with all _Spigot_ version's, but the `Title-API` provided by `AutoRestart` changes version to version.

If you did not receive any errors, then your done! Change the config and make the plugin yours! If any errors are displayed, please **CONTACT ME** so I may address it immediately so other players wont receive the same issue!