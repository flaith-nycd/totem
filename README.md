# ToTeM
I have just started to learn JAVA and I made a program using a local database (HSQLDB) and access to a remote database (MariaDB)

I'm using IntelliJ IDEA

To make it work you'll need the following libraries:
* [HyperSQL - HSQLDB](http://hsqldb.org/)
* [AccessRemoteMySQLDB from rohit7209](https://github.com/rohit7209/AccessRemoteMySQLDB)

The remote database could be called "***totem***", and here is the structure of the table I'm using
```
CREATE TABLE dnr_logs
(
    id int PRIMARY KEY NOT NULL AUTO_INCREMENT,
    id_user varchar(50) NOT NULL,
    id_site varchar(50) NOT NULL,
    date datetime NOT NULL,
    message varchar(200) NOT NULL,
    id_borne varchar(50) NOT NULL,
    version varchar(20) NOT NULL
);
```

## Example
```
$ java -jar totem.jar
Launching ToTeM version 0.3.0

[2018-04-18 12:06:00] >>> Checking URL http://192.168.0.13/handleSQL.php
[2018-04-18 12:06:00] >>> Connecting...
idCase: 2 iRFID: 9002

RFID for case ID 0: 9000
RFID for case ID 1: 9001
RFID for case ID 2: 9002
RFID for case ID 3: 9003
File separator: \
Working Directory = E:\Projects\java\IdeaProjects\totem\out\artifacts\totem_jar
--- WHERE IS THE CONFIG FILE ---
dbPath: E:\Projects\java\IdeaProjects\totem\out\artifacts\totem_jar\conf.properties
--- DATABASE FROM CONFIG FILE ---
dbPath: db
dbName: db\totem_db
dbUsername: totem
dbUserPassword: totem
--- WHAT'S IN OUR LOGS TABLE ---
TOTAL LOG_COUNT: 98
   ID DATE                           MESSAGES
    0 2018-04-09 21:31:58.000000     Starting TOTEM version 0.2.0 ...
    1 2018-04-09 21:31:58.000000     Ending TOTEM version 0.2.0 ...
    2 2018-04-09 21:32:16.000000     Starting TOTEM version 0.2.0 ...
    3 2018-04-09 21:32:16.000000     Ending TOTEM version 0.2.0 ...
    4 2018-04-09 21:32:27.000000     Starting TOTEM version 0.2.0 ...
    5 2018-04-09 21:32:27.000000     Ending TOTEM version 0.2.0 ...
    6 2018-04-09 21:32:39.000000     Starting TOTEM version 0.2.0 ...
    7 2018-04-09 21:32:39.000000     Ending TOTEM version 0.2.0 ...
    8 2018-04-09 23:43:48.000000     Starting TOTEM version 0.2.0 ...
    9 2018-04-09 23:43:48.000000     Ending TOTEM version 0.2.0 ...
  ...
   88 2018-04-17 16:34:12.000000     Starting ToTeM version 0.3.0 ...
   89 2018-04-17 16:34:12.000000     Ending ToTeM version 0.3.0 ...
   90 2018-04-17 17:24:26.000000     Starting ToTeM version 0.3.0 ...
   91 2018-04-17 17:24:26.000000     Ending ToTeM version 0.3.0 ...
   92 2018-04-18 10:46:34.000000     Starting ToTeM version 0.3.0 ...
   93 2018-04-18 10:46:34.000000     Ending ToTeM version 0.3.0 ...
   94 2018-04-18 10:47:36.000000     Starting ToTeM version 0.3.0 ...
   95 2018-04-18 10:47:36.000000     Ending ToTeM version 0.3.0 ...
   96 2018-04-18 12:06:00.000000     Starting ToTeM version 0.3.0 ...
   97 2018-04-18 12:06:00.000000     Ending ToTeM version 0.3.0 ...
admin, site-test, 2018-04-10 13:09:30, Test Envoi Message local vers remote, ToTeM #321, 0.2.0
admin, site-test, 2018-04-11 12:14:28, Test Envoi Message local vers remote, ToTeM, 0.2.1
admin, site-test, 2018-04-11 20:38:49, Test Envoi Message local vers remote, ToTeM, 0.2.2
admin, SiteTest #01, 2018-04-16 11:12:01, Test Envoi Message local vers remote, ToTeM, 0.2.3
...
admin, SiteTest #01, 2018-04-17 15:18:26, Test Envoi Message local vers remote, ToTeM, 0.2.10
admin, SiteTest #01, 2018-04-18 10:46:35, Test Envoi Message local vers remote, ToTeM, 0.3.0
admin, SiteTest #01, 2018-04-18 10:47:36, Test Envoi Message local vers remote, ToTeM, 0.3.0
admin, SiteTest #01, 2018-04-18 12:06:01, Test Envoi Message local vers remote, ToTeM, 0.3.0

E:\Projects\java\IdeaProjects\totem\out\artifacts\totem_jar (master)
$
```

## In case the file (handleSQL.php) is not found in your remote server
> "rDbPHPFile=handleSQL.php" in **conf.properties**
```
E:\Projects\java\IdeaProjects\totem\out\artifacts\totem_jar (master)
λ java -jar totem.jar
Launching ToTeM version 0.3.0

[2018-04-18 13:47:24] >>> Checking URL http://192.168.0.13/handleSQL.php
[2018-04-18 13:47:24] >>> Connecting...
[2018-04-18 13:47:24] >>> Checking access to 192.168.0.13
[2018-04-18 13:47:24] >>> ERROR: Cannot access to "handleSQL.php" on the remote server http://192.168.0.13
```

## In case there is a problem to access to the remote server (the IP is not reachable)
```
E:\Projects\java\IdeaProjects\totem\out\artifacts\totem_jar (master)
λ java -jar totem.jar
Launching ToTeM version 0.3.0

[2018-04-18 13:41:58] >>> Checking URL http://192.168.2.50/handleSQL.php
[2018-04-18 13:41:58] >>> Connecting...
[2018-04-18 13:41:58] >>> Checking access to 192.168.2.50
[2018-04-18 13:41:58] >>> ERROR: Cannot access to remote server http://192.168.2.50, check the config file or the network !
```

## About AccessRemoteMySQLDB
The `mysqli` PHP extension must be installed on your server, the file **handleSQL.php** use `mysqli_connect()`