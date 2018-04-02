This project provides a simple web application which can be used in certifying the OXD java implementation accordingly to the OpenID Certification program (http://openid.net/certification/). 

# Prerequisites

- Java (1.8+)
- Maven (3.0.4+)
- Git (2.5.1+)
- Docker (18.0.0+)
- Docker Compose (1.18.0+)

# Setting up the OIDC RP server
Download or clone the project from github:

````
https://github.com/bhammond604/oidctest.git   (NOTE: the forked modified version)
````

Open the following file:

````
/oidctest/docker/rp_test/conf.py
````

Set the baseurl property so as it points to a normal FQDN value. For example:

````
baseurl = "https://vnik2.gluu.info"    (TIP: set the FQDN as an alias into /etc/hosts file by pointing to the local IP)
````

Save the file, move to the root folder (oidctest) and type:

````
docker-compose -f docker/docker-compose.yml up   (NOTE: you may need to use the root user)
````

When the the process of starting up the containers is completed, open another terminal and type:

````
docker container ls
````

The docker will show a list with the running containers including the one for the rp_test server. Copy its container_id (in the leftmost column) and type:

````
docker exec -it <container_id> /bin/bash
````

With this command you login the linux in the running container via it bash interface. Now you have access to the internal files and especially to the log files. We are interested in these types of logs:

1. /log:  This directory keeps the log files that contain information about the tests we run. They are the files that we collect in order to claim the certification (NOTE: the files are categorized by client_id like gluu)
2. rp_test.log: This file is the rp_test server's log file. It contains information during startup, and any other messages that are logged by the server's code itself or even exceptions

To make sure that the RP server is running, open a browser and type:

````
http://localhost:8080   (TIP: ignore the warning for the SSL and proceed)
````

If everything was successful, you will see the home page of the server where you can choose what type of tests you want to perform.

# Setting up the OXD
Download and install the OXD version you want locally and as described at:

````
https://gluu.org/docs/oxd/
````

Start the OXD server as mentioned in the documentation. If you accept the default configuration, then the server is running in 8099 port.

# Installing the Web app
Download or clone the project from github:

````
mkdir ~/oxd 
cd ~/oxd 
git clone https://github.com/GluuFederation/oxd-rp-certification.git
````

Open the application.properties file to configure the application:

````
cd ./oxd-rp-certification
sudo nano src/main/resources/application.properties
````

Set the following properties and then save your changes:

````
logging.file= ~/oxd/oxd.log   (or provide your location)

oxd.server.host=localhost      (OXD server's host)
oxd.server.port=8099           (OXD server's port)
oxd.server.email=<your_email>  (an email address)

oxd.server.op-host=<the RP server's URL>/gluu   (the FQDN that you had set in conf.py file. For example: https://vnik2.gluu.info:8080/gluu/)
````

Given that your OXD server is already installed and running, execute:

````
mvn clean install -DskipTests
mvn spring-boot:run
````

# Using the application

Open your browser and hit:

````
https://localhost:8443
````

If everything is correct you will see a list of tests that are mandatory for certifying against the Basic Profile of OIDC RP Certification program.

For every test, you can click on its "Test" button in order to trigger the relevant process in the backend. The outcome of the process should be a set of log statements that will appear into your oxd.log file. This file along with potentially a few images that need to be taken manually on your browser will be the information that will be sent to the OIDC for claiming the compliance against all tests.

For more information about how to run the tests, collect the necessary information, and organize the final deliverable, please read the [cert.doc](https://github.com/GluuFederation/oxd-rp-certification/tree/master/doc/cert.doc) file.

Finally for more information about the RP certification refer to the official site at: 
[http://openid.net/certification/rp_testing/](http://openid.net/certification/rp_testing/)
