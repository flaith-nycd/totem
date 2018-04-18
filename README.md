# ToTeM
I have just started to learn JAVA and I made a program using a local database (HSQLDB) and access to a remote database (MariaDB)

I'm using IntelliJ IDEA

To make it work you'll need the following libraries:
* [HyperSQL - HSQLDB](http://hsqldb.org/)
* [AccessRemoteMySQLDB from rohit7209]([https://github.com/rohit7209/AccessRemoteMySQLDB)

For the remote database, it could be called "***totem***", and here is the structure of the table I'm using
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