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
* Restarts on hour or day intervals, or at a specific time
* Broadcasts reminders, customizable in minutes on config
* Option to enable/disable on screen pop ups, on different events
* Change restart time in-game.
* Integrated update check.
* MultiCraft support!
* Able to Start, or Pause the server timer in-game, with a reminder that its paused
* Automatically updates your old configuration file!
* Customizable popup and broadcast messages/timings
* Add color codes to shutdown messages!
* Execute commands before the server restarts on an X amount of seconds.
* Added Max Players Exception
  * If the server restarts and you have 10 players online and you set the max players to 10 your server wont restart until someone leaves and the player amount is less than or equal to 10.
* Plays sounds when a notification is displayed (Announcement/Popup)

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
autorestart_time_raw_seconds | `S` | 19421

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
| /autore help | autorestart.help |
| /autore time | autorestart.time |
| /autore now | autorestart.now |
| /autore reload | autorestart.reload |
| /autore in | autorestart.in |
| /autore pause | autorestart.pause |
| /autore resume | autorestart.resume |

---
### Config Files
<details>
<summary><code>commands.yml</code></summary>

```yaml
#░█████╗░░█████╗░███╗░░░███╗███╗░░░███╗░█████╗░███╗░░██╗██████╗░░██████╗
#██╔══██╗██╔══██╗████╗░████║████╗░████║██╔══██╗████╗░██║██╔══██╗██╔════╝
#██║░░╚═╝██║░░██║██╔████╔██║██╔████╔██║███████║██╔██╗██║██║░░██║╚█████╗░
#██║░░██╗██║░░██║██║╚██╔╝██║██║╚██╔╝██║██╔══██║██║╚████║██║░░██║░╚═══██╗
#╚█████╔╝╚█████╔╝██║░╚═╝░██║██║░╚═╝░██║██║░░██║██║░╚███║██████╔╝██████╔╝
#░╚════╝░░╚════╝░╚═╝░░░░░╚═╝╚═╝░░░░░╚═╝╚═╝░░╚═╝╚═╝░░╚══╝╚═════╝░╚═════╝░

## Run a list of commands seconds before the restart executes
## This feature is made for specific type of users, most plugins will execute save functions on disable

## Main branch
commands:
    ## Enable or disable commands to execute before the server restarts
    enabled: false
    ## The amount of time to give to execute commands
    seconds: 5
    ## The list of commands to run
    list:
        - 'say This is a test command'
        - 'say I think it works?'
## DO NOT TOUCH!!
version: 2
```

</details>
<details>
<summary><code>format.yml</code></summary>

```yaml
#███████╗░█████╗░██████╗░███╗░░░███╗░█████╗░████████╗
#██╔════╝██╔══██╗██╔══██╗████╗░████║██╔══██╗╚══██╔══╝
#█████╗░░██║░░██║██████╔╝██╔████╔██║███████║░░░██║░░░
#██╔══╝░░██║░░██║██╔══██╗██║╚██╔╝██║██╔══██║░░░██║░░░
#██║░░░░░╚█████╔╝██║░░██║██║░╚═╝░██║██║░░██║░░░██║░░░
#╚═╝░░░░░░╚════╝░╚═╝░░╚═╝╚═╝░░░░░╚═╝╚═╝░░╚═╝░░░╚═╝░░░

## These are the formatting tags your can use in all text/messages and popups
## They are configurable to suit your needs when formatting text

## Main Branch
format:
    
    ## Hours till server restart
    hours: '%h'
    
    ## Minutes till server restart
    minutes: '%m'
    
    ## Seconds till server restart
    seconds: '%s'
    
    ## Max players amount. This formats the text to `max_players.amount`
    maxplayers_amount: '%a'
    
    ## Max players amount. This formats the text to `max_players.delay`
    maxplayers_delay: '%d'
## DO NOT TOUCH!!
version: 1
```

</details>
<details>
<summary><code>global_broadcast.yml</code></summary>

```yaml
#░██████╗░██╗░░░░░░█████╗░██████╗░░█████╗░██╗░░░░░
#██╔════╝░██║░░░░░██╔══██╗██╔══██╗██╔══██╗██║░░░░░
#██║░░██╗░██║░░░░░██║░░██║██████╦╝███████║██║░░░░░
#██║░░╚██╗██║░░░░░██║░░██║██╔══██╗██╔══██║██║░░░░░
#╚██████╔╝███████╗╚█████╔╝██████╦╝██║░░██║███████╗
#░╚═════╝░╚══════╝░╚════╝░╚═════╝░╚═╝░░╚═╝╚══════╝
#
#██████╗░██████╗░░█████╗░░█████╗░██████╗░░█████╗░░█████╗░░██████╗████████╗░██████╗
#██╔══██╗██╔══██╗██╔══██╗██╔══██╗██╔══██╗██╔══██╗██╔══██╗██╔════╝╚══██╔══╝██╔════╝
#██████╦╝██████╔╝██║░░██║███████║██║░░██║██║░░╚═╝███████║╚█████╗░░░░██║░░░╚█████╗░
#██╔══██╗██╔══██╗██║░░██║██╔══██║██║░░██║██║░░██╗██╔══██║░╚═══██╗░░░██║░░░░╚═══██╗
#██████╦╝██║░░██║╚█████╔╝██║░░██║██████╔╝╚█████╔╝██║░░██║██████╔╝░░░██║░░░██████╔╝
#╚═════╝░╚═╝░░╚═╝░╚════╝░╚═╝░░╚═╝╚═════╝░░╚════╝░╚═╝░░╚═╝╚═════╝░░░░╚═╝░░░╚═════╝░

## Broadcasts adds flexibility on what you want your players to see!
## Everyone will see these messages, except the person initiating the command.
## They will receive their own Private Message.

## You have the option to enabled/disable these functions, as well as individually enabling/disabling
## the text message or the title popup.

## Global broadcasts include the prefix in "main.prefix" config.
## Messages have multi line support!
## All messages can use the format tags specified in the format.yml config

## To understand ticks. 1 second is relevant to 20 ticks! So a second and a half (1.5s) is 30 ticks!
## timing is displayed as fadein:stay:fadeout
## Example: 20:40:20
##      fadein: 20 (ticks) (1 second)
##      stay: 40 (ticks) (2 second)
##      fadeout: 20 (ticks) (1 second)
## Fadein, stay, fadeout are the 3 arguments. You can edit how long it takes to fadein, fadeout etc.
## You can also put the fade numbers to 0, and then the popup's will be instant!
## Get creative!

## Main Branch
global_broadcasts:
    ##▒█▀▄▀█ ▀█▀ ▒█▄░▒█ ▒█░▒█ ▀▀█▀▀ ▒█▀▀▀ ▒█▀▀▀█
    ##▒█▒█▒█ ▒█░ ▒█▒█▒█ ▒█░▒█ ░▒█░░ ▒█▀▀▀ ░▀▀▀▄▄
    ##▒█░░▒█ ▄█▄ ▒█░░▀█ ░▀▄▄▀ ░▒█░░ ▒█▄▄▄ ▒█▄▄▄█
    ## Broadcast message for the minute reminder
    minutes:
        message:
            enabled: true
            text:
                - 'Server Will Restart In %m Minutes!'
        popup:
            enabled: true
            title:
                text: '&cServer Restarting In'
                timing: '20:40:20'
            subtitle:
                text: '&f%m &cMinutes!'
                timing: '20:40:20'
    ##▒█▀▀▀█ ▒█▀▀▀ ▒█▀▀█ ▒█▀▀▀█ ▒█▄░▒█ ▒█▀▀▄ ▒█▀▀▀█
    ##░▀▀▀▄▄ ▒█▀▀▀ ▒█░░░ ▒█░░▒█ ▒█▒█▒█ ▒█░▒█ ░▀▀▀▄▄
    ##▒█▄▄▄█ ▒█▄▄▄ ▒█▄▄█ ▒█▄▄▄█ ▒█░░▀█ ▒█▄▄▀ ▒█▄▄▄█
    ## Broadcast message for the second reminder
    seconds:
        message:
            enabled: true
            text:
                - 'Server is restarting in %s Seconds!'
        popup:
            enabled: true
            title:
                text: '&cServer Restarting In'
                timing: '5:20:5'
            subtitle:
                text: '&f%s &cSeconds!'
                timing: '5:20:5'
    ##▒█▀▀▀█ ▀▀█▀▀ ░█▀▀█ ▀▀█▀▀ ▒█░▒█ ▒█▀▀▀█
    ##░▀▀▀▄▄ ░▒█░░ ▒█▄▄█ ░▒█░░ ▒█░▒█ ░▀▀▀▄▄
    ##▒█▄▄▄█ ░▒█░░ ▒█░▒█ ░▒█░░ ░▀▄▄▀ ▒█▄▄▄█
    ## Show the broadcast when '/autore resume' or '/autore pause' is called
    status:
        ## Show the broadcast when typed '/autore resume'
        resume:
            message:
                enabled: true
                text:
                    - '&cAutoRestart timer has resumed!'
            popup:
                enabled: true
                title:
                    text: '&cAutoRestart has started!'
                    timing: '20:40:20'
                subtitle:
                    text: ''
                    timing: '20:40:20'
        ## Show the broadcast when typed '/autore pause'
        pause:
            message:
                enabled: true
                text:
                    - '&cAutoRestart timer has been paused'
            popup:
                enabled: true
                title:
                    text: '&cAutoRestart has been paused!'
                    timing: '20:40:20'
                subtitle:
                    text: ''
                    timing: '20:40:20'
    ##▒█▀▀█ ▒█░▒█ ░█▀▀█ ▒█▄░▒█ ▒█▀▀█ ▒█▀▀▀
    ##▒█░░░ ▒█▀▀█ ▒█▄▄█ ▒█▒█▒█ ▒█░▄▄ ▒█▀▀▀
    ##▒█▄▄█ ▒█░▒█ ▒█░▒█ ▒█░░▀█ ▒█▄▄█ ▒█▄▄▄
    ## Show the broadcast when '/autore in' is called
    change:
        message:
            enabled: true
            text:
                - '&cServer now is restarting in &f%h&cH &f%m&cM &f%s&cS!'
        popup:
            enabled: true
            title:
                text: '&cServer Restarting In'
                timing: '20:40:20'
            subtitle:
                text: '&f%h&cH &f%m&cM &f%s&cS!'
                timing: '20:40:20'
    ##▒█▀▄▀█ ░█▀▀█ ▀▄▒▄▀   ▒█▀▀█ ▒█░░░ ░█▀▀█ ▒█░░▒█ ▒█▀▀▀ ▒█▀▀█ ▒█▀▀▀█
    ##▒█▒█▒█ ▒█▄▄█ ░▒█░░   ▒█▄▄█ ▒█░░░ ▒█▄▄█ ▒█▄▄▄█ ▒█▀▀▀ ▒█▄▄▀ ░▀▀▀▄▄
    ##▒█░░▒█ ▒█░▒█ ▄▀▒▀▄   ▒█░░░ ▒█▄▄█ ▒█░▒█ ░░▒█░░ ▒█▄▄▄ ▒█░▒█ ▒█▄▄▄█
    ## Show the Maxplayers broadcast
    max_players:
        ## The message broadcasted when too many players are online on restart!
        alert:
            message:
                enabled: true
                text:
                    - '&bToo many players online for restart. Max &f%a&b amount of players allowed for a restart. Waiting for players to leave!'
            popup:
                enabled: true
                title:
                    text: '&bToo many players online for restart.'
                    timing: '20:40:20'
                subtitle:
                    text: 'Max &f%a&b amount of players allowed for a restart.'
                    timing: '20:40:20'
        ## This message appears when the amount of players requirement is met!
        pre_shutdown:
            message:
                enabled: true
                text:
                    - '&aServer now restarting in &f%d&a seconds!'
            popup:
                enabled: true
                title:
                    text: '&aServer now restarting in &f%d&a seconds!'
                    timing: '20:40:20'
                subtitle:
                    text: ''
                    timing: '20:40:20'
        ## This message appears when the timeout has been reached
        timeout:
            message:
                enabled: true
                text:
                    - '&aPlayers took too long to leave!'
            popup:
                enabled: true
                title:
                    text: '&aPlayers took too long to leave!'
                    timing: '20:40:20'
                subtitle:
                    text: '&aServer now restarting in &f%d&a seconds!'
                    timing: '20:40:20'
    ##▒█▀▀▀█ ▒█░▒█ ▒█░▒█ ▀▀█▀▀ ▒█▀▀▄ ▒█▀▀▀█ ▒█░░▒█ ▒█▄░▒█
    ##░▀▀▀▄▄ ▒█▀▀█ ▒█░▒█ ░▒█░░ ▒█░▒█ ▒█░░▒█ ▒█▒█▒█ ▒█▒█▒█
    ##▒█▄▄▄█ ▒█░▒█ ░▀▄▄▀ ░▒█░░ ▒█▄▄▀ ▒█▄▄▄█ ▒█▄▀▄█ ▒█░░▀█
    ## Global shutdown message
    shutdown:
        message:
            enabled: true
            text:
                - '&cServer Restarting!'
        popup:
            enabled: true
            title:
                text: '&cServer is now'
                timing: '20:80:20'
            subtitle:
                text: '&cRestarting!'
                timing: '20:80:20'
## DO NOT TOUCH!!
version: 2
```

</details>
<details>
<summary><code>main.yml</code></summary>

```yaml
#███╗░░░███╗░█████╗░██╗███╗░░██╗
#████╗░████║██╔══██╗██║████╗░██║
#██╔████╔██║███████║██║██╔██╗██║
#██║╚██╔╝██║██╔══██║██║██║╚████║
#██║░╚═╝░██║██║░░██║██║██║░╚███║
#╚═╝░░░░░╚═╝╚═╝░░╚═╝╚═╝╚═╝░░╚══╝

## Main branch
main:
    ## Sets the restart command after players have been kicked
    execution: 'restart'
    ## This will recalculate the restart countdown when using '/autore reload'
    recalculate_onreload: false
    ## Restart modes available 'interval', and 'timestamp'
    restart_mode: 'interval'
    ## Restart mode specific settings
    modes:
        ## Restart server in intervals
        interval:
            ## Set the multiplication factor
            ## Options are 'hours' or 'days'
            factor: 'hours'
            ## Set the value of the interval
            ## Decimals are allowed
            value: 3.0
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
version: 4
```

</details>
<details>
<summary><code>max_players.yml</code></summary>

```yaml
#███╗░░░███╗░█████╗░██╗░░██╗  ██████╗░██╗░░░░░░█████╗░██╗░░░██╗███████╗██████╗░░██████╗
#████╗░████║██╔══██╗╚██╗██╔╝  ██╔══██╗██║░░░░░██╔══██╗╚██╗░██╔╝██╔════╝██╔══██╗██╔════╝
#██╔████╔██║███████║░╚███╔╝░  ██████╔╝██║░░░░░███████║░╚████╔╝░█████╗░░██████╔╝╚█████╗░
#██║╚██╔╝██║██╔══██║░██╔██╗░  ██╔═══╝░██║░░░░░██╔══██║░░╚██╔╝░░██╔══╝░░██╔══██╗░╚═══██╗
#██║░╚═╝░██║██║░░██║██╔╝╚██╗  ██║░░░░░███████╗██║░░██║░░░██║░░░███████╗██║░░██║██████╔╝
#╚═╝░░░░░╚═╝╚═╝░░╚═╝╚═╝░░╚═╝  ╚═╝░░░░░╚══════╝╚═╝░░╚═╝░░░╚═╝░░░╚══════╝╚═╝░░╚═╝╚═════╝░

## This will stop your server from shutting down if you have more than X amount of players configured in "amount".

## This is a beta feature. If you have any ideas, or recommendations about this feature you would like to
## add. Do not hesitate to message me on SpigotMC. I would love to hear what your think I should change/add!
## I love feedback and ideas.

## Main branch
max_players:
    ## Enable or Disable this feature
    enabled: false
    ## The max amount of players allowed for a restart. If you set this to 10, and your server has 11 people online
    ## your server will not shutdown until the amount of players is less than of equal to the amount set.
    amount: 10
    ## The delay for the server to restart once player count as been met.
    ## If players join the server during this delay, the server will continue to restart.
    ## This is in seconds
    delay: 10
    ## The timeout is the amount of time AutoRestart will give to the players to log out.
    ## If the amount is never reached within X minutes, then the server
    ## Will force restart. This prevents the server from never restarting due to high player traffic,
    ## but will give players time to wrap up their tasks.
    ## This is in minutes.
    timeout: 15
## DO NOT TOUCH!!
version: 4
```

</details>
<details>
<summary><code>permissions.yml</code></summary>

```yaml
#██████╗░███████╗██████╗░███╗░░░███╗██╗░██████╗░██████╗██╗░█████╗░███╗░░██╗░██████╗
#██╔══██╗██╔════╝██╔══██╗████╗░████║██║██╔════╝██╔════╝██║██╔══██╗████╗░██║██╔════╝
#██████╔╝█████╗░░██████╔╝██╔████╔██║██║╚█████╗░╚█████╗░██║██║░░██║██╔██╗██║╚█████╗░
#██╔═══╝░██╔══╝░░██╔══██╗██║╚██╔╝██║██║░╚═══██╗░╚═══██╗██║██║░░██║██║╚████║░╚═══██╗
#██║░░░░░███████╗██║░░██║██║░╚═╝░██║██║██████╔╝██████╔╝██║╚█████╔╝██║░╚███║██████╔╝
#╚═╝░░░░░╚══════╝╚═╝░░╚═╝╚═╝░░░░░╚═╝╚═╝╚═════╝░╚═════╝░╚═╝░╚════╝░╚═╝░░╚══╝╚═════╝░

## Nothing will happen if you edit this file, this is to help you setup
## the permissions for AutoRestart.

#▒█▀▀▄ ▒█▀▀▀ ▒█▀▀▀ ░█▀▀█ ▒█░▒█ ▒█░░░ ▀▀█▀▀
#▒█░▒█ ▒█▀▀▀ ▒█▀▀▀ ▒█▄▄█ ▒█░▒█ ▒█░░░ ░▒█░░
#▒█▄▄▀ ▒█▄▄▄ ▒█░░░ ▒█░▒█ ░▀▄▄▀ ▒█▄▄█ ░▒█░░

autorestart:
    default: true
    command: '/autore'
    
autorestart.help:
    default: true
    command: '/autore help'
    
autorestart.time:
    default: true
    command: '/autore time'

#░█▀▀█ ▒█▀▀▄ ▒█▀▄▀█ ▀█▀ ▒█▄░▒█
#▒█▄▄█ ▒█░▒█ ▒█▒█▒█ ▒█░ ▒█▒█▒█
#▒█░▒█ ▒█▄▄▀ ▒█░░▒█ ▄█▄ ▒█░░▀█

autorestart.admin:
    - autorestart.in
    - autorestart.now
    - autorestart.pause
    - autorestart.resume

autorestart.in:
    default: false
    command: '/autore in'
    
autorestart.now:
    default: false
    command: '/autore now'
    
autorestart.pause:
    default: false
    command: '/autore pause'
    
autorestart.resume:
    default: false
    command: '/autore resume'

#░█▀▀█ ▒█░░░ ▒█░░░
#▒█▄▄█ ▒█░░░ ▒█░░░
#▒█░▒█ ▒█▄▄█ ▒█▄▄█

autorestart.*:
    - autorestart.help
    - autorestart.time
    - autorestart.resume
    - autorestart.pause
    - autorestart.in
    - autorestart.reload
    - autorestart.now

## DO NOT TOUCH!!
version: 1
```

</details>
<details>
<summary><code>private_messages.yml</code></summary>

```yaml
#██████╗░██████╗░██╗██╗░░░██╗░█████╗░████████╗███████╗
#██╔══██╗██╔══██╗██║██║░░░██║██╔══██╗╚══██╔══╝██╔════╝
#██████╔╝██████╔╝██║╚██╗░██╔╝███████║░░░██║░░░█████╗░░
#██╔═══╝░██╔══██╗██║░╚████╔╝░██╔══██║░░░██║░░░██╔══╝░░
#██║░░░░░██║░░██║██║░░╚██╔╝░░██║░░██║░░░██║░░░███████╗
#╚═╝░░░░░╚═╝░░╚═╝╚═╝░░░╚═╝░░░╚═╝░░╚═╝░░░╚═╝░░░╚══════╝
#
#███╗░░░███╗███████╗░██████╗░██████╗░█████╗░░██████╗░███████╗░██████╗
#████╗░████║██╔════╝██╔════╝██╔════╝██╔══██╗██╔════╝░██╔════╝██╔════╝
#██╔████╔██║█████╗░░╚█████╗░╚█████╗░███████║██║░░██╗░█████╗░░╚█████╗░
#██║╚██╔╝██║██╔══╝░░░╚═══██╗░╚═══██╗██╔══██║██║░░╚██╗██╔══╝░░░╚═══██╗
#██║░╚═╝░██║███████╗██████╔╝██████╔╝██║░░██║╚██████╔╝███████╗██████╔╝
#╚═╝░░░░░╚═╝╚══════╝╚═════╝░╚═════╝░╚═╝░░╚═╝░╚═════╝░╚══════╝╚═════╝░

## These are private messages sent to the player when the event is triggered. If the same task is enabled in
## "global_broadcast", this player will not see the global message, but his own.

## You have the option to enabled/disable these functions, as well as individually enabling/disabling
## the text message or the title popup.

## Global broadcasts include the prefix in "main.prefix" config.
## Messages have multi line support!
## All messages can use the format tags specified in the format.yml config

## To understand ticks. 1 second is relevant to 20 ticks! So a second and a half (1.5s) is 30 ticks!
## timing is displayed as fadein:stay:fadeout
## Example: 20:40:20
##      fadein: 20 (ticks) (1 second)
##      stay: 40 (ticks) (2 second)
##      fadeout: 20 (ticks) (1 second)
## Fadein, stay, fadeout are the 3 arguments. You can edit how long it takes to fadein, fadeout etc.
## You can also put the fade numbers to 0, and then the popup's will be instant!
## Get creative!

## Main Branch
private_messages:
    ##▀▀█▀▀ ▀█▀ ▒█▀▄▀█ ▒█▀▀▀
    ##░▒█░░ ▒█░ ▒█▒█▒█ ▒█▀▀▀
    ##░▒█░░ ▄█▄ ▒█░░▒█ ▒█▄▄▄
    ## Show the message when type /autore time
    time:
        message:
            enabled: true
            text:
                - '&cServer restarting in &f%h&cH &f%m&cM &f%s&cS!'
        popup:
            enabled: true
            title:
                text: '&cServer Restarting In'
                timing: '20:40:20'
            subtitle:
                text: '&f%h&cH &f%m&cM &f%s&cS!'
                timing: '20:40:20'
    ##▒█▀▀▀█ ▀▀█▀▀ ░█▀▀█ ▀▀█▀▀ ▒█░▒█ ▒█▀▀▀█
    ##░▀▀▀▄▄ ░▒█░░ ▒█▄▄█ ░▒█░░ ▒█░▒█ ░▀▀▀▄▄
    ##▒█▄▄▄█ ░▒█░░ ▒█░▒█ ░▒█░░ ░▀▄▄▀ ▒█▄▄▄█
    ## Show the message when typed '/autore resume' or '/autore pause'
    status:
        ## Show the message when typed '/autore resume'
        resume:
            message:
                enabled: true
                text:
                    - '&cYou have resumed AutoRestart timer!'
            popup:
                enabled: true
                title:
                    text: '&cYou started AutoRestart back up!'
                    timing: '20:40:20'
                subtitle:
                    text: ''
                    timing: '20:40:20'
        ## Show the message when typed '/autore pause'
        pause:
            message:
                enabled: true
                text:
                    - '&cYou have paused AutoRestart timer'
            popup:
                enabled: true
                title:
                    text: '&cYou have paused AutoRestart!'
                    timing: '20:40:20'
                subtitle:
                    text: ''
                    timing: '20:40:20'
    ##▒█▀▀█ ▒█░▒█ ░█▀▀█ ▒█▄░▒█ ▒█▀▀█ ▒█▀▀▀
    ##▒█░░░ ▒█▀▀█ ▒█▄▄█ ▒█▒█▒█ ▒█░▄▄ ▒█▀▀▀
    ##▒█▄▄█ ▒█░▒█ ▒█░▒█ ▒█░░▀█ ▒█▄▄█ ▒█▄▄▄
    ## Show the broadcast when the server time has been changed!
    change:
        message:
            enabled: true
            text:
                - '&cServer now is restarting in &f%h&cH &f%m&cM &f%s&cS!'
        popup:
            enabled: true
            title:
                text: '&cYou Changed Restart Time to'
                timing: '20:40:20'
            subtitle:
                text: '&f%h&cH &f%m&cM &f%s&cS!'
                timing: '20:40:20'
    ##▒█▀▀█ ░█▀▀█ ▒█░▒█ ▒█▀▀▀█ ▒█▀▀▀   ▒█▀▀█ ▒█▀▀▀ ▒█▀▄▀█ ▀█▀ ▒█▄░▒█ ▒█▀▀▄ ▒█▀▀▀ ▒█▀▀█
    ##▒█▄▄█ ▒█▄▄█ ▒█░▒█ ░▀▀▀▄▄ ▒█▀▀▀   ▒█▄▄▀ ▒█▀▀▀ ▒█▒█▒█ ▒█░ ▒█▒█▒█ ▒█░▒█ ▒█▀▀▀ ▒█▄▄▀
    ##▒█░░░ ▒█░▒█ ░▀▄▄▀ ▒█▄▄▄█ ▒█▄▄▄   ▒█░▒█ ▒█▄▄▄ ▒█░░▒█ ▄█▄ ▒█░░▀█ ▒█▄▄▀ ▒█▄▄▄ ▒█░▒█
    ## This shows a reminder to 'autorestart.admin' players that the server is still paused.
    ## This is to prevent the staff from forgetting to leave the server paused.
    pause_reminder:
        message:
            enabled: true
            text:
                - "&cDon't forget that the server countdown is still paused!"
        popup:
            enabled: true
            title:
                text: "&cDon't forget that"
                timing: '20:40:20'
            subtitle:
                text: '&cAutoRestart timer is still paused!'
                timing: '20:40:20'
## DO NOT TOUCH!!
version: 2
```

</details>
<details>
<summary><code>reminder.yml</code></summary>

```yaml
#██████╗░███████╗███╗░░░███╗██╗███╗░░██╗██████╗░███████╗██████╗░
#██╔══██╗██╔════╝████╗░████║██║████╗░██║██╔══██╗██╔════╝██╔══██╗
#██████╔╝█████╗░░██╔████╔██║██║██╔██╗██║██║░░██║█████╗░░██████╔╝
#██╔══██╗██╔══╝░░██║╚██╔╝██║██║██║╚████║██║░░██║██╔══╝░░██╔══██╗
#██║░░██║███████╗██║░╚═╝░██║██║██║░╚███║██████╔╝███████╗██║░░██║
#╚═╝░░╚═╝╚══════╝╚═╝░░░░░╚═╝╚═╝╚═╝░░╚══╝╚═════╝░╚══════╝╚═╝░░╚═╝

## Reminders will execute in the specified times to let players know when is the next restart

## Main Branch
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
    ## AutoRestart will countdown the seconds till restart start
    seconds: 5
    ## This will remind you that the server timer is paused. This message will be sent to
    ## people with 'autorestart.admin' permission
    ## This is set in minutes
    pause_reminder: 10
## DO NOT TOUCH!!
version: 2
```

</details>
<details>
<summary><code>sounds.yml</code></summary>

```yaml
#░██████╗░█████╗░██╗░░░██╗███╗░░██╗██████╗░░██████╗
#██╔════╝██╔══██╗██║░░░██║████╗░██║██╔══██╗██╔════╝
#╚█████╗░██║░░██║██║░░░██║██╔██╗██║██║░░██║╚█████╗░
#░╚═══██╗██║░░██║██║░░░██║██║╚████║██║░░██║░╚═══██╗
#██████╔╝╚█████╔╝╚██████╔╝██║░╚███║██████╔╝██████╔╝
#╚═════╝░░╚════╝░░╚═════╝░╚═╝░░╚══╝╚═════╝░╚═════╝░

## This file will allow you to enable/disable sound effects in AutoRestart
## If the message AND the popup is disabled the sound will not play!

## Main Branch
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