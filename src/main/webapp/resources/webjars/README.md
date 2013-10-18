Javascript dependencies in this `src/main/webapp/resources/webjars` directory are managed by 
<a href="http://www.webjars.org/">WebJars</a>.

Note on JQuery map file 404's: <a href="http://stackoverflow.com/questions/18365315/jquerys-jquery-1-10-2-min-map-is-triggering-a-404-not-found">jQuery's jquery-1.10.2.min.map is triggering a 404 (Not Found)</a>.

<br/>
...except that Google App Engine requires servlet 2.5.  Webjars only works with servlet 3.0:

<a href="https://code.google.com/p/googleappengine/issues/detail?id=3091">Issue Tracking</a>

