Run Instructions:
To run the pre built jar: run java -jar run.jar in the demo folder. If you want to rebuild and run tests run mvn package, and a new jar from that build will be made in Eyereturn/target. After running the jar, you make get requests to http://localhost/8080/bus?id={id} where id is the intended bus id.
ex) http://localhost/8080/bus?id=2343

Architecture discussion:
The program is split into two parts, fetchAPI.java acts as a client to retrieve both the weather api and bus api, and formats that data for better readability. The Server.java file is the server that takes the info from fetchAPI client and routes it to the localhost using Akka-http. In order to leverage the full power of Akka, FetchAPI is implemented as an actor so that it's processing can be done concurrently to the server. This is benificial so that in potential future use cases multiple clients can run concurrently along with the server, which should greatly increase throughput.

Potential improvements:
FetchAPI currently runs as one actor getting both apis; this could be split into two actors- one for bus info and one for weather. This yields better seperation of concerns, however grouping them together makes formatting of their data easier and doesn't come with the added overhead of having two actors communicate with each other.

Caching can be implemented on the yahoo api since weather would not see much changes minute to minute, however caching of the bus info might not return much benefit since it's updated by the second.

Libraries/frameworks used:
Akka
Akka-http
org.json
Apache Http-Client



