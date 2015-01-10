[![Stories in Ready](https://badge.waffle.io/miguelfc/marble.png?label=ready&title=Ready)](https://waffle.io/miguelfc/marble) 
This is the Marble Initiative!
======

You will find here the main repository of this project. Feel free to take a peek, download and try this code, but be aware that it is not a finished product and most probably will crash your server.

If you want to know more about the project, the people involved, and the objectives, please head to: http://marble.miguelfc.com

You can contact me through my website if you need deeper information or want to get involved in this project: http://miguelfc.com.

Installation Steps
-----

You will need a server, or collection of servers with the following:

- A Postgres Database (only version 9.3.5 has been tested)
- A Mongo Database (only version 2.6.6 has been tested)
- Java 7 or later.
- A Web Application Server (WAS) like Wildfly (I use version 8.2, but 8.1 works too).

Version numbers are just reference. I haven't got time to check other versions, but I guess that later versions of each part should work. 

So, after you have everything set up, you will need to create users and database instances for the system to use (one in Postgres and one in Mongo). You can assign the standard read/write privileges, but the user should own the database instance, so that it could create and delete tables without any issue.

Now, download, clone or export the project, and find the pom.properties file in the base path. Fill in the info for the database access, and finally package everything using maven:

mvn package

Finally, drop the generated WAR file into the deployment area of your WAS (in wildfly this will be standalone/deployments), and the system should go up in no time.

#Some notes

In wildfly you will need to increase the post size in the standalone.xml file. Look for the http-listener line, and add the max-post-size parameter with the value recommended, like in the following example:

<http-listener name="default" socket-binding="http" max-post-size="30000000" />

It is recommended also to increase the memory allocation for the WAS, and in the case of wildfly this is done from in the standalone.conf file in the bin directory. For example:

JAVA_OPTS="-Xms512m -Xmx1024m -XX:MaxPermSize=512m -Djava.net.preferIPv4Stack=true"


