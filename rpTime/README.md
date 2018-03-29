rpTime
=========


# Requirements

* Google App Engine (1.8.6 as of this writing)
* Maven 3.1 (per <a href="https://developers.google.com/appengine/docs/java/tools/maven">Google App Engine</a>)
* Java 7 (per <a href="https://developers.google.com/appengine/docs/java/gettingstarted/installing">Google App Engine</a>)


# Build

    mvn package
    

# Run

    
### Run Via Google App Engine    

    mvn appengine:devserver
    

### Run Via Eclipse Google App Engine Plugin

1. Install the <a href="https://developers.google.com/appengine/docs/java/tools/eclipse">Eclipse Google App Engine Plugin</a>
2. Create a 'Google App Engine' server via Servers Tab::Right-Click::New Server::Google::Google App Engine
3. Add the project to the newly created Google App Engine container
4. Click 'Start' (play button)


## Verify

Use a browser to access:

* http://localhost:8888 