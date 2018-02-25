This project provides a simple web application which can be used in certifying the OXD java implementation accordingly to the OpenID Certification program (http://openid.net/certification/). 

# Prerequisites

- Java (1.8+)
- Maven (3.0.4+)
- Git (2.5.1+)

# Installing
Download or clone the project from github:

````
mkdir ~oxd 
cd ~/oxd 
git clone https://github.com/GluuFederation/oxd-rp-certification.git
````

Open the application.properties file to configure the application:

````
cd ./oxd-rp-certification
sudo nano /src/main/resources/application.properties
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
mvn spring
````

# Using the application

Open your browser and hit:

````

````

If everything is correct you will see a list of tests that are mandatory for certifying against the Basic Profile of OIDC RP Certification program.
