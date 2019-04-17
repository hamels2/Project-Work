Run instructions:
This program was designed to run on Windows
This jar was built using maven, to rebuild run mvn package.
On successful build:
cd LM/LM/Target
java -jar LM-1.0-SNAPHOT.jar
(Program takes a little while to start up)



User instructions:
Once program is in view press the monitor button on any row
to start monitoring that pid. The pid updates every 3 seconds,
the info is also printed to the console. To stop monitoring
press the monitor button again.

Design Disucssion:
This program was modeled off the mvc architecture and was 
packaged in this way to make the discussion clearer. A strong 
benefit of this design is that the business logic can be used
for any gui implementation not just javafx.

Model:
The model consists of three classes; ProcessParser, ProcessInfo, 
and ProcessLister. ProcessParser is used to parse the command line
command that gets a specific process and extracts the memory and cpu
from that command in the form of strings. ProcessInfo is the object that
stores and updates this info. ProcessLister gets all current running processes
and stores it in a list of ProcessInfos

Controller:
The controller is responsible for updating the data recieved from the Model
to the view; it consits of two classes. The processController stores the 
ProcessInfo and is linked to the view's table cells via an observablelist. 
The UpdateTask class is the individual thread that updates each monitored process. 

View:
The view is the App class, and is the gui implementation of the program. It
uses processList to get it's list of processes, and it uses ProcessController to 
update the view when new data appears. The monitor button in the class uses the
UpdateTask class to spawn a new thread to monitor each chosen process.


Testing:
Unit testing was done on the model and controller. Since pids change overtime
testing was limited to testing pid 0 and 4 which are always there on Windows.
Testing the view was done manually.

Possible improvements:
The startup time takes a while due to having to wait for the command line process
to finish getting all tasks. One possible improvement is to have the gui start with
a loading screen to indicate to the user that the program is running and then show
the processes when that command is done. This was not implemented since UX design 
does not seem to be the focus of this project and therefore not worth 
the added complexity to the implementation.
Another possible improvement is that the model only has a specific implementation 
of parser for Windows. An interface for a generic parser can be used instead, 
and have ProcessInfo rely on that. This would decouple ProcessInfo from 
any specific OS. This was not included since the requirements stated that the program
run on only one OS.
 

