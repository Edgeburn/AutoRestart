# AutoRestart
Restart your server automatically today, and clear some of your RAM for a faster, smoother Minecraft experience. This integration is seamless and works with Spigot. Please follow the instructions to ensure a proper setup!

---
### Instructions

##### Check if spigot.yml is configured
Before we install `AutoRestart-v4.jar` into your plugins folder, we need to configure `spigot.yml`. To check if you need to configure `spigot.yml`, type `/restart` in the console and see if the server restarts without installing `AutoRestart-v4.jar`. 

_AutoRestart_ uses spigots native restart method to restart your server, and since most panels and frameworks are compatible with this function. Why make a new one?

##### If /restart works, skip this step
Go into `spigot.yml` and find the `restart-script` node. Change the `restart-script` to the name of the script used to start your server. If your run file is named `start.bat` then change `restart-script` to the following:
```yaml
## Windows
restart-script: start.bat
```
```yaml
## Linux / Bash
restart-script: ./start.sh
```

##### Install AutoRestart-v4.jar
Now that we have `spigot.yml` configured, move `AutoRestart-v4.jar` into your `/plugins` folder. Start the server, and make sure that there are not errors caused by `AutoRestart`. If any errors are displayed, please **CONTACT ME** so I may address it immediately so other players wont receive the same issue!

##### Test and Enjoy
Log into your server, and type `/autore time` to display the time the server will restart. If you do not get any `Title Popups`, check the console to see if you have any errors.

`AutoRestart`'s core timing and messaging system should be compatible with all _Spigot_ version's, but the `Title-API` provided by `AutoRestart` changes version to version.

If you did not receive any errors, then your done! Change the config and make the plugin yours! If any errors are displayed, please **CONTACT ME** so I may address it immediately so other players wont receive the same issue!

---
### Features
* Restarts on hour interval, able to use decimals on config!
* Restarts at a specific timestamp!
* Broadcasts reminders, customizable in minutes on config!
* Broadcasts final reminders on last 5 seconds!
* Editable reminder messages!
* Editable shut down messages!
* Editable seconds messages!
* Option to enable/disable on screen pop ups, on different events!
* Change restart time in-game.
* Integrated update check.
* MultiCraft support!
* Force saves world when restarts!
* Able to Start, or Pause the server timer in-game!
* Reminds you every ten minute that timer is paused! (Customizable)
* Automatically updates your old configuration file!
* Customizable popup messages!
* Customizable popup timings!
* Add color codes to shutdown messages!
* Execute commands before the server restarts on an X amount of seconds.
* Configurable to enable, and disable commands on last seconds, and
  change the amount of seconds before done so.
* Added Max Players Exception.
  * If the server restarts and you have 10 players online and you set the max players to 10 your server wont restart until someone leaves and the player amount is less than or equal to 10.
* Configurable Max Players Exception!
  * Change restart delay.
  * Change amount of max players.
  * Enable/Disable feature.
  * Set exception message when too many players online.
  * Set message before delayed restart when the amount of players have been met.
  * Shutdown message for Max Players Exception!
  
 ---
 ### Photos
 _Photos from_ `v2.8.1`
 ![alt text](https://proxy.spigotmc.org/3a23489f858738d26dda340964a2f13443be4324?url=http%3A%2F%2Fi.imgur.com%2FYaGitRX.jpg)
 
 ![alt text](https://proxy.spigotmc.org/e99b9b87ab262ce6066eb4333c1a8413e5df8042?url=http%3A%2F%2Fi.imgur.com%2FqxP8AN6.jpg)
 
 ---
 ### Commands
 | Command | Description |
 | :--- | :--- |
 | /autore help | Shows help screen |
 | /autore time | Shows exactly when is the next server restart |
 | /autore now | Restarts the server now |
 | /autore reload | Reloads the config file |
 | /autore in | Sets the time the server will restart |
 | /autore pause | Pauses the server timer |
 | /autore resume | Resumes the server timer |
 
 ---
 ### Permissions
 | Command | Permission |
 | :--- | :--- |
 | /autore help |  |
 | /autore time |  |
 | /autore now | autorestart.now |
 | /autore reload | autorestart.reload |
 | /autore in | autorestart.in |
 | /autore pause | autorestart.pause |
 | /autore resume | autorestart.resume |
 
 ---
 ### Config Files