###Smartling test app

build with maven:
<pre>
mvn clean package
</pre>

view test result:
<pre>
target/site/jacoco/index.html
</pre>

start app:
<pre>
java -jar target/smartling-test-1.0-SNAPSHOT.jar
</pre>

Api docs:
<pre>
/api/v.1.0/text/reversed 
consumes: 'text/plain'
produces: 'text/plain'

status 200 - return uploaded text with reversed words.
</pre>

<pre>
/api/v.1.0/text/counts 

consumes: 'text/plain'
produces: 'application/json'

status 200 - return the count of unique words and count of all punctuation characters
{
    "uniqueWordsCount": 52,
    "allPunctuationCount": 7
}
</pre>

Exception mapping
<pre>
status 400 - Bad request.
status 500 - Internal server error.

{
    "statusCode": 400,
    "errorMessage": "It`s not an english text"
}
</pre>


