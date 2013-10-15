rpTime
=========

## Build

    mvn package
    

## Run

    
### Run Via Jetty

    mvn clean jetty:run
    

### Run Via Tomcat

    mvn clean tomcat7:run
    

### Run Via Command Line (Java+Jetty)

    java $JAVA_OPTS -jar ./target/dependency/jetty-runner.jar --port $PORT ./target/*.war


### Run Via Eclipse Google App Engine Plugin

1. Create a 'Google App Engine' server via Servers Tab::Right-Click::New Server::Google::Google App Engine
2. Add the project to the newly created Google App Engine container
3. Click 'Start' (play button)

### Run Via Eclipse WTP

1. Project::Right-Click::Properties::Project Facts tab
2. Check: Dynamic Web Module (3.0+), Java, JavaScript 
3. Add to web container (Tomcat, VMWare TCServer, etc.) 
4. Click 'Start' (play button)


## Verify

Use a browser to access:

* http://localhost:8888 &nbsp;&nbsp;&nbsp;&nbsp;<i>(if running via Google App Engine)</i>
* http://localhost:8080 &nbsp;&nbsp;&nbsp;&nbsp;<i>(if running via Jetty)</i>
* http://localhost:8080/rptime &nbsp;&nbsp;&nbsp;&nbsp;<i>(if running via Tomcat or Eclipse WTP)</i>
