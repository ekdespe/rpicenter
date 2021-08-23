#RPICENTER

###Description

The rpicenter is a webserver built at  to get the data of  arbitrary numbers of iot sensors and 
persist this information to  database with a feature tha permits get data for later analysis.


###Author : Erik Ferreira - ekdespe@gmail.com
###Stack
- Java 8
- MQTT
- RavenDB
- Bash
- Raspberrypi
###Features
- Arbitrary number of nodes supported
- Add of iot node dynamically
- Low consuming of resources
- Persistence at data over ravenDB built for iot devices approaches
###Requirements
- RaspberryPi (2 or higher)
- OpenJDK
- Apache Maven
- Any iot sensor with wi-fi and MQTT library support (suggest NodeMCU8266 or ESP32)

###Documentation
This project has a javadoc built-in, to generate it  please use $ mvn javadoc:javadoc
###Logs
The log directory must be chosen at install script to some OS supported 
###Installation
#### GNU/LINUX
1. Clone the project
2. `mvn spring-boot:repackage`
3. `chmod +x install.sh` 
4. `./install.sh`
5. Check the installation at `systemctl status rpicenter.service`
#### Windows
####Docker


