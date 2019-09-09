## Solution explanation

The solution has been designed in a way of maximizing the data is displayed to the user. 
This means that information will be displayed even in cases some part is not understood properly by the service.
This means:

- Invalid birth dates are not displayed
- Invalid credit amounts are not displayed

Rows with invalid data are marked in red in the page. For that reason the csv file has been modified to create 
one row with invalid birth date.

Currently the solution reads information from the two provided files. The design has been created in a way that
it will be easy to extend it to read from other sources like ftp, rest and so on.
If more than one source of information is used (currently two files), the processing is done in parallel using different 
threads. Take into account that a time out of 10 second has been included and it can be configured in the
property file.

### What is it seen in the UI

The UI displays all the data available in a single table. This table contains ALL the data from all the sources.
 For that reason an extra column has been added that tells the source to the user.
 
Note tha rows with missing or invalid names, birth date and credit limits are displayed in RED.

## How to Run the application

- From windows: "gradlew.bat bootRun"
- Fron "Unix like" systems: "./gradlew bootRun"

Then go to your favourite browser and browse "http://localhost:8080".

If you run this in a virtual machine, just change "localhost" by the host of your VM.

## How to compite

Run "./gradlew build intTest" 

