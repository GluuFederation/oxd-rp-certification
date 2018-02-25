This project provides a simple web application which can be used in certifying the OXD java implementation accordingly to the OpenID Certification program (http://openid.net/certification/). 

# Prerequisites

- Java (1.8+)
- Maven (3.0.4+)
- Git (2.5.1+)

# Installing
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

For more information about how to run the tests, collect the necessary information, and organize the final deliverable, please read the ./doc/cert.doc file.
Finally for more information about the RP certification refer to the official site at: 
