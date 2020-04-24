App Name: Mobile App Parking APP
Team Members: David Kibbe, Vishesh Patel, Pavan Narahari (Team ZOM aka Team DVP)

Mobile App Parking APP by Team ZOM (David Kibbe, Vishesh Patel, Pavan Narahari)
Description:
The app allows users to find parking spots around their location using Google Maps and the Streetline Guidance API. The parking spots appear as markers near the user,and clicking on the marker will display available data on price.
Usage and features:
-          Find parking: allows the user to find parking spots based on the location they search for. These spots can be on street parking, off street parking, and off street parkedge parking (which is a type of parking interfacing service that the API owner Streetline provides some users). The application will find those spots and mark their coordinates on the map for the user to view.
-          Parking spot data: clicking on a parking spot marker will display price data available for the parking spot.
-          Search filtering: Allows the user to filter by the amount of locations they want to search by
-          Favorites: Allows the user to save favorite parking spots. Mapped to a firebase database unique to each user, so it’ll be there every time you log out and back in. 
Incompletable features
Some data is not available based on the data the API has. In our proposal we mentioned that we wanted to be able to filter by distance and/or price. Some locations have price data, but many do not so we were unable to reliably implement that function. However, the by-point method will get the closest locations to the specified point first.
Bugs:
-          GRPC error. This issue seems to occur when pulling the repo, but cloning it causes the app to run properly. It crashes the application and seems to be caused due to some access and configuration issues.
- API authentication and Maps API sometimes not working on start up. Usually this requires a restart or re-clone to fix
- Sometimes Google Maps itself just will not work, impacting users and apps worldwide, including our own humble final project for this class. https://outage.report/google-maps
Future work:
-          Potentially using a better API. This app is really as good as the data provided to it, so with more data availability and opportunities the functionality for the app would be easier to think up and create.
-        Navigation. This would have the Maps API set up directions for a user to a parking spot of their choice.
-     Dark mode. This is essential for elite and night time users.

