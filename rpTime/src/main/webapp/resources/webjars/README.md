Javascript dependencies in this `src/main/webapp/resources/webjars` directory are managed by 
<a href="http://www.webjars.org/">WebJars</a>.

Note on JQuery map file 404's: <a href="http://stackoverflow.com/questions/18365315/jquerys-jquery-1-10-2-min-map-is-triggering-a-404-not-found">jQuery's jquery-1.10.2.min.map is triggering a 404 (Not Found)</a>.

<br/>
...except that Google App Engine requires servlet 2.5.  Webjars only works with servlet 3.0: <a href="https://code.google.com/p/googleappengine/issues/detail?id=3091">Issue Tracking</a>

<h2>Workaround</h2>

The workaround to allow WebJars on GAE uses WebJars, the <a href="https://maven.apache.org/plugins/maven-dependency-plugin/examples/unpacking-project-dependencies.html">maven-dependency-plugin</a>, 
and the <a href="https://maven.apache.org/plugins/maven-resources-plugin/examples/copy-resources.html">maven-resources-plugin</a>.  The process:

1. Declare Webjar dependencies in POM.xml (i.e., `org.webjars:angularjs:1.2.x`)
2. `dependency:unpack-dependencies` extracts contents of maven dependency into a temp dir
3. `resource:copy-resources` copies Webjar resources from temp dir to `/src/main/webapp/resources/webjars/*` (this folder)
4. Resources in `/src/main/webapp/resources/webjars/*` are omitted from Git via main `.gitignore`
5. `resource:copy-resources` copies output folder to `/war` folder for GAE deployment
