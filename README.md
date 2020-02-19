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
* Plays sounds when a notification is displayed (Announcement/Popup)
* Enable or Disable sounds

---
### Placeholder Support
Placeholder Name | Value | Example
:--- | :--- | :---
autorestart_time_formatted1 | `H`h `M`m `S`s | 5h 23m 41s
autorestart_time_formatted2 | `H` hours `M` minutes `S` seconds | 5 hours 23 minutes 41 seconds
autorestart_time_formatted3 | `H`:`M` | 5:23
autorestart_time_formatted4 | `H`:`M`:`S` | 5:23:41
autorestart_time_hour | `H` | 5
autorestart_time_minute | `M` | 23
autorestart_time_seconds | `S` | 41
autorestart_time_raw_minute | `M` | 323
autorestart_time_seconds | `S` | 19421

---
### Photos
_Photos from_ `v2.8.1`
![image1](https://proxy.spigotmc.org/3a23489f858738d26dda340964a2f13443be4324?url=http%3A%2F%2Fi.imgur.com%2FYaGitRX.jpg)
![image2](https://proxy.spigotmc.org/e99b9b87ab262ce6066eb4333c1a8413e5df8042?url=http%3A%2F%2Fi.imgur.com%2FqxP8AN6.jpg)

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
<details>
<summary><code>Commands.yml</code></summary>

```yaml
## This section will allow you to execute commands before the server restarts
commands:
    ## Enable or disable this feature
    enabled: false
    ## The seconds before restart to execute list
    seconds: 5
    ## the amount of seconds before restart to execute commands
    list:
        - 'say This is a test command'
        - 'say I think it works?'
## DO NOT TOUCH!!
version: 1
```

</details>
<details>
<summary><code>GlobalBroadcasts.yml</code></summary>

```yaml
## Global Broadcast and Private Messages adds flexibility on what you want your players to see!
## You have the option of having the player who executed the event to see a message while other don't, or
## have the player who executed the event see a global message but not a private message
## you can have the server see a global message while the player who executed the command will see
## his own message.

## The reason why you can enable or disable broadcasts and popups, is to give you the flexibility to
## choose whether you want your players to see a broadcast or a popup of the event. Or both! If you have
## both popups and broadcasts disabled, broadcasts are automatically enabled. If broadcasts, private messages,
## and popups are all disabled, then it will as well be automatically enabled in broadcasts. If broadcasts and
## private messages are disabled, but popups are enabled, console will get a private message for logs. Since
## command line cant receive popups.
    
## These are global broadcasts the everyone sees. If the same task is enabled in "private_message"
##  the player who initiated the event will not see the broadcast, but will see his own broadcast.
##  Global broadcasts include the prefix in "main.prefix" config.
global_broadcast:
    ## Enable or Disable the broadcast
    enabled:
        ## Show the broadcast on the minute reminders!
        minutes: true
        ## Show the broadcast on the last seconds of a server restart!
        seconds: true
        ## Show the broadcast when typed /autore resume or /autore pause
        status:
            resume: true
            pause: true
        ## Show the broadcast when the server time has been changed!
        change: true
        ## Show the Maxplayers broadcast
        max_players:
            ## The message broadcasted when too many players are online on restart!
            alert: true
            ## This message appears when the amount of players requirement is met!
            pre_shutdown: true
        ## Global shutdown message
        shutdown: true
    ## The configuration for broadcast messages
    ## Messages have multi line support!
    messages:
        ## %m - minutes
        minutes:
            - 'Server Will Restart In %m Minutes!'
        ## %s - seconds
        seconds:
            - 'Server is restarting in %s Seconds!'
        ## There are no tags for this section.
        status:
            resume:
                - '&cAutoRestart timer has resumed!'
            pause:
                - '&cAutoRestart timer has been paused'
        ## %h - hours, %m - minutes, %s - seconds.
        change:
            - '&cServer now is restarting in &f%h&cH &f%m&cM &f%s&cS!'
        ## Maxplayers broadcast settings
        max_players:
            ## The message broadcasted when too many players are online on restart!
            ## %a - amount
            alert:
                - '&bToo many players online for restart. Max &f%a&b amount of players allowed for a restart. Waiting for people to leave!'
            ## This message appears when the amount of players requirement is met!
            ## %d - delay
            pre_shutdown:
                - '&aServer now restarting in &f%d&a seconds!'
        ## Client restart message in game chat
        shutdown: 
            - '&cServer Restarting!'
## DO NOT TOUCH!!
version: 1
```

</details>
<details>
<summary><code>GlobalPopups.yml</code></summary>

```yaml
## To understand ticks. 1 second is relevant to 20 ticks! So a second and a half (1.5s) is 30 ticks!
## Fadein, stay, fadeout are the 3 arguments. You can edit how long it takes to fadein, fadeout etc.
## You can also put the fade numbers to 0, and then the popup's will be instant!
## Get creative!
## Global Popups duplicate rules are applied just like global Messages, same situation with private popups
## and private messages.
global_popups:
    ## Enable or Disable the Popups!
    enabled:
        ## Show the pop ups on the minute reminders!
        minutes: true
        ## Show the pop ups on the last seconds of a server restart!
        seconds: true
        ## Show the pop ups when type /autore time
        status:
            resume: true
            pause: true
        ## Show the pop ups when the server time has been changed!
        change: true
        ## Show the Maxplayers pop ups
        max_players:
            ## The pop up broadcasts when too many players are online on restart!
            alert: true
            ## This pop up appears when the amount of players requirement is met!
            pre_shutdown: true
        ## Shows the popup before restart
        shutdown: true
    ## This section is for the customization of the popup messages!
    messages:
        minutes: ## %m - minutes
            title:
                text: '&cServer Restarting In'
                fadein: 20
                stay: 40
                fadeout: 20
            subtitle:
                text: '&f%m &cMinutes!'
                fadein: 20
                stay: 40
                fadeout: 20
        seconds: ## %s - seconds
            title:
                text: '&cServer Restarting In'
                fadein: 5
                stay: 20
                fadeout: 5
            subtitle:
                text: '&f%s &cSeconds!'
                fadein: 5
                stay: 10
                fadeout: 5
        status: ## There are no tags for this section
            resume:
                title:
                    text: '&cAutoRestart has started!'
                    fadein: 20
                    stay: 40
                    fadeout: 20
                subtitle:
                    text: ''
                    fadein: 20
                    stay: 40
                    fadeout: 20
            pause:
                title:
                    text: '&cAutoRestart has been paused!'
                    fadein: 20
                    stay: 40
                    fadeout: 20
                subtitle:
                    text: ''
                    fadein: 20
                    stay: 40
                    fadeout: 20
        change: ## %h - hours, %m - minutes, %s - seconds
            title:
                text: '&cServer Restarting In'
                fadein: 20
                stay: 40
                fadeout: 20
            subtitle:
                text: '&f%h&cH &f%m&cM &f%s&cS!'
                fadein: 20
                stay: 40
                fadeout: 20
        max_players:
            alert: ## %a - amount
                title:
                    text: '&bToo many players online for restart.'
                    fadein: 20
                    stay: 40
                    fadeout: 20
                subtitle:
                    text: 'Max &f%a&b amount of players allowed for a restart.'
                    fadein: 20
                    stay: 40
                    fadeout: 20
            pre_shutdown: ## %d - delay
                title:
                    text: '&aServer now restarting in &f%d&a seconds!'
                    fadein: 20
                    stay: 40
                    fadeout: 20
                subtitle:
                    text: ''
                    fadein: 20
                    stay: 40
                    fadeout: 20
        shutdown:
            title:
                text: '&cServer is now'
                fadein: 20
                stay: 80
                fadeout: 20
            subtitle:
                text: '&cRestarting!'
                fadein: 20
                stay: 80
                fadeout: 20
## DO NOT TOUCH!!
version: 1
```

</details>
<details>
<summary><code>Main.yml</code></summary>

```yaml
## Main configuration values
main:
    ## Recalculate timer on config reload
    ## This will recalculate the restart countdown when using '/autore reload'
    recalculate_onreload: false
    ## restart modes available 'interval', and 'timestamp'
    restart_mode: 'interval'
    ## restart mode specific settings
    modes:
        ## Interval in Hours (DECIMALS ALLOWED)
        interval: 3.0
        ## Set the timestamp in 24 hour time format HH:MM
        ## Multiple timestamps allowed
        timestamp:
            - '0:00'
            - '6:00'
            - '12:00'
            - '18:00'
    ## Server wide prefix for all chat broadcasts
    prefix: '&f[&7AutoRestart&f] &e'
    ## Client restart message in game chat
    kick_message: '&cServer Restarting! We will be back up any minute!'
## DO NOT TOUCH!!
version: 3
```

</details>
<details>
<summary><code>MaxPlayers.yml</code></summary>

```yaml
## This will stop your server from shutting down if you have more than X amount of players configured in "amount".
max_players:
    ## Enable or Disable this feature
    enabled: false
    ## The max amount of players allowed for a restart. If you set this to 10, and your server has 11 people online
    ## your server will not shutdown until the amount of players is less than of equal to the amount set.
    amount: 10
    ## The delay for the server to restart once player count as been met. In seconds!
    delay: 10
## DO NOT TOUCH!!
version: 3
```

</details>
<details>
<summary><code>PrivateMessages.yml</code></summary>

```yaml
## These are private messages sent to the player when the event is triggered. If the same task is enabled in
##  "global_broadcast", this player will not see the global message, but his own.
##  Private messages include the prefix in "main.prefix" config.
private_messages:
    ## Enable or Disable the chat message
    enabled:
        ## Show the message when type /autore time
        time: true
        ## Show the message when typed /autore resume or /autore pause
        status:
            resume: true
            pause: true
        ## Show the broadcast when the server time has been changed!
        change: true
        ## "/autore pause" reminder
        pause_reminder: true
    ## Messages have multi line support!
    messages:
        ## %h - hours, %m - minutes, %s - seconds
        time:
            - '&cServer restarting in &f%h&cH &f%m&cM &f%s&cS!'
        ## There are no tags for this section. [Duplicated in Global_Messages]
        status:
            resume:
                - '&cYou have resumed AutoRestart timer!'
            pause:
                - '&cYou have paused AutoRestart timer'
        ## %h - hours, %m - minutes, %s - seconds [Duplicated in Global_Messages]
        change:
            - '&cServer now is restarting in &f%h&cH &f%m&cM &f%s&cS!'
        ## "/autore pause" reminder
        pause_reminder:
            - "&cDon't forget that the server countdown is still paused!"
## DO NOT TOUCH!!
version: 1
```
 
</details>
<details>
<summary><code>PrivatePopups.yml</code></summary>

```yaml
## Private popups
private_popups:
    ## Enable or Disable the Popups!
    enabled:
        ## Show the pop ups when type /autore time
        time: true
        ## Show the pop ups when typed /autore resume or /autore pause
        ## [Duplicated in Global_Messages]
        status:
            resume: true
            pause: true
        ## Show the pop ups when the server time has been changed!
        ## [Duplicated in Global_Messages]
        change: true
        ## "/autore pause" reminder
        pause_reminder: true
    ## This section is for the customization of the popup messages!
    messages:
        time: ## %h - hours, %m - minutes, %s - seconds
            title:
                text: '&cServer Restarting In'
                fadein: 20
                stay: 40
                fadeout: 20
            subtitle:
                text: '&f%h&cH &f%m&cM &f%s&cS!'
                fadein: 20
                stay: 40
                fadeout: 20
        status: ## There are no tags for this section
            resume:
                title:
                    text: '&cYou started AutoRestart back up!'
                    fadein: 20
                    stay: 40
                    fadeout: 20
                subtitle:
                    text: ''
                    fadein: 20
                    stay: 40
                    fadeout: 20
            pause:
                title:
                    text: '&cYou have paused AutoRestart!'
                    fadein: 20
                    stay: 40
                    fadeout: 20
                subtitle:
                    text: ''
                    fadein: 20
                    stay: 40
                    fadeout: 20
        change: ## %h - hours, %m - minutes, %s - seconds
            title:
                text: '&cYou Changed Restart Time to'
                fadein: 20
                stay: 40
                fadeout: 20
            subtitle:
                text: '&f%h&cH &f%m&cM &f%s&cS!'
                fadein: 20
                stay: 40
                fadeout: 20
        pause_reminder: ## There are no tags for this section
            title:
                text: "&cDon't forget that"
                fadein: 20
                stay: 40
                fadeout: 20
            subtitle:
                text: '&cAutoRestart timer is still paused!'
                fadein: 20
                stay: 40
                fadeout: 20
## DO NOT TOUCH!!
version: 1
```

</details>
<details>
<summary><code>Reminder.yml</code></summary>

```yaml
## Reminders will popup in the specified times to let players know when is the next restart
reminder:
    ## Enables or Disables restart reminder
    enabled:
        minutes: true
        seconds: true
    ## minutes before restart
    minutes:
        - 15
        - 10
        - 5
        - 1
    ## AutoRestart will countdown the seconds to restart starting at the entered value
    seconds: 5
    ## This will remind you that the server timer is paused! This message will be sent to
    ## people with autorestart.start permission!
    ## This is set in minutes!
    pause_reminder: 10
## DO NOT TOUCH!!
version: 1
 ```
 
</details>
<details>
<summary><code>Sounds.yml</code></summary>

```yaml
## This file will allow you to enable/disable sound effects in AutoRestart
## If the broadcast message or popup is disabled the sound will not play!
sounds:
    ## This is the sound that is played when everyone receives a message.
    ## For example, an automated (Server restarting in 15 minutes) is a global broadcast
    broadcast:
        ## Enables or disables the sound
        enabled: true
    ## This is the sound that is played when a player requests information from the plugin.
    ## For example, when typing `/autore time`, this is a player requested popup.
    private:
        ## Enables or disables the sound
        enabled: true
    ## This is the dramatic restart sound that is played seconds before the server restarts.
    ## You can configure when the sound will start to play, "Its a little long and dramatic"
    shutdown:
        ## Enables or disables the sound
        enabled: true
        ## I recommend having it at 5 seconds, this is the perfect duration of the sound
        ## But if you want to make it earlier, that is up to you.
        seconds: 5
## DO NOT TOUCH!
version: 1
```

</details>

---
### To Do List
```text
* Add more features (Need feedback from reviews for ideas!)
* Make it where the server restarts on crash!
* Add BungeeCord support
* Add a boss bar for server countdown
```

---
### Plugin Metrics
![bStats](https://bstats.org/signatures/bukkit/AutoRestart.svg)