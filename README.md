# Hitch
Link for Static Diagram: https://www.lucidchart.com/invitations/accept/2c7b62d3-613a-4de1-9c9e-436d5247e70d
Link for Sequence Diagram: https://www.lucidchart.com/invitations/accept/b5aa8a9f-6cb3-49eb-ba5c-91d7d1069e56

# Steps to build and run project using Android Studio's emulator
0. Enable Virtualization for your device, the specifics of this depends on your processor and OS but
   this usually involves enabling a setting in your device's BIOS
1. Download and install the latest version of Android Studio
2. Clone the project onto the machine with Android Studio
3. Open the project in Android Studio
4. Select Build->Make Project (or hit Ctrl+F9)
5. Tools->Android->AVD Manager
6. Click Create Virtual Device
7. Make sure Phone is the selected category
8. Select Pixel 2 from the list of devices
9. Press next
10. Choose the API 27 release with Google APIs enabled
11. Enter a device name if you want a different name from the default name
12. Select Portrait for the Startup Orientation
13. For Graphics, select Automatic
14. Make sure Enable Device Frame is checked
15. Press finish
16. Go to Run->Run App (or hit Shift+F10)
17. Under Available Virtual Devices, select the device you created and click Ok
18. Once the device has finished setting up, press the circle and then the up arrow
19. Click on the Settings app and go to Apps & notifications
20. Select See All Apps and click on Hitch
21. Click on Permissions and enable the Location permission
22. Press the circle and then the up arrow
23. Click on the Hitch app
24. App should start

# List of known bugs
1. Logging in takes a random amount of time, from fast to a minute or two
2. Posts have IDs, as a way of finding a specific post. When you add a post, it alwasy has an ID of 0, so new posts overwrite each other
3. Accepting requests deletes everyone from the potential_passengers field, decremonts spots, but does not move people to the accepted_passengers field
4. The map is inconsistent

# How to test even with bugs
We are fully aware that these bugs impede testing. Please go to https://hitch-7466c.firebaseio.com/, to the database section. Invitations to join this Firebase project have been sent to chandanaupadhyaya@ucsb.edu and jcai00@ucsb.edu.

1. To get around Bug 3, find the relevant account's uid, in the "Authentication" section on the left sidebar. Copy it, then paste it into the "accepted_passengers" field on any post. Now, you should be able to successfully test UC4 and UC5.

# Steps to test Use Case UC1: User makes a new account
1. Open the app
2. Click on the "Sign up" text after "Don't have an account?"
3. Enter your details
4. Click the "Sign up" button
5. Attempt to log in. If you succeed, then you have successfully created a new account

# Steps to test Use Case UC2: Driver makes a post
1. Open the app
2. Log in (Or create an account first, then log in)
3. Click on the green square button with a plus icon
4. Enter whatever details you wish
5. Post your Ride post
6. Go back to the main menu
7. Click the "Rides" button
8. Now, click the "My Posts" button
9. If the post was added successfully, the post you just added should be visible

# Steps to test Use Case UC3: Passenger requests to join a Driver’s Ride
1. Open the app
2. Log in (Or create an account first, then log in)
3. Enter a destination in the Search Bar (If you simply want to see all the available Rides, then after you have entered some text, delete that text, then exit out of the search bar completely, by clicking the "x" twice)
4. Select a Ride post from the list
5. Click on it
6. Press the "Request to Join" button
7. Go back to the main menu
8. Click the "Rides" button
9. Now, click the "Requested Rides" button
10. If the post was added successfully, the post you just added should be visible

# Steps to test Use Case UC4: User views the Rides they’ve been accepted to
1. Open the app
2. Log in (Or create an account first, then log in)
3. Click the "Rides" button
4. Now, click the "Accepted Rides" button
5. All the Rides that you have been accepted to will be visible, which may be none (you may need to use another account to make a post (UC2), have this account request to join it (UC3), then use the first account to accept requests (UC11) in order to have at least one Ride post visible on this page)

# Steps to test Use Case UC5: User views all the details of a Ride they’ve been accepted to
1. Open the app
2. Log in (Or create an account first, then log in)
3. Click the "Rides" button
4. Now, click the "Accepted Rides" button
5. If successful, all the Rides that you have been accepted to will be visible, which may be none (you may need to use another account to make a post (UC2), have this account request to join it (UC3), then use the first account to accept requests (UC11) in order to have at least one Ride post visible on this page)
6. Click on a Ride post
7. If successful, you will be able to see all the details of the Ride you selected: the driver’s name, their profile picture, where the driver is departing from, when they are leaving, where the driver is going to, the Ride’s description, and how many seats are available

# Steps to test Use Case UC6: User views the Rides they’ve requested to join
1. Open the app
2. Log in (Or create an account first, then log in)
3. Click the "Rides" button
4. Now, click the "Requested Rides" button
5. If successful, all the Rides that you have requested to join will be visible, which may be none (you may need to use another account to make a post (UC2) and have this account request to join it (UC3))

# Steps to test Use Case UC7: User views all the details of a Ride they’ve requested to join
1. Open the app
2. Log in (Or create an account first, then log in)
3. Click the "Rides" button
4. Now, click the "Requested Rides" button
5. If successful, all the Rides that you have requested to join will be visible, which may be none (you may need to use another account to make a post (UC2) and have this account request to join it (UC3))
6. Click on a Ride post
7. If successful, you will be able to see all the details of the Ride you selected: the driver’s name, their profile picture, where the driver is departing from, when they are leaving, where the driver is going to, the Ride’s description, and how many seats are available

# Steps to test Use Case UC8: User views the Rides they’ve posted
1. Open the app
2. Log in (Or create an account first, then log in)
3. Click the "Rides" button
4. Now, click the "My Posts" button
5. If successful, all the Rides that you created will be visible, which may be none (you may need to make a post first (UC2))

# Steps to test Use Case UC9: User views all the details of a Ride they’ve posted
1. Open the app
2. Log in (Or create an account first, then log in)
3. Click the "Rides" button
4. Now, click the "My Posts" button
5. If successful, all the Rides that you created will be visible, which may be none (you may need to make a post first (UC2))
6. Click on a Ride post
7. If successful, you will be able to see all the details of the Ride you selected: the driver’s name, their profile picture, where the driver is departing from, when they are leaving, where the driver is going to, the Ride’s description, how many seats are available, and a button to “Accept Requests”

# Steps to test Use Case UC10: Driver accepts Passenger(s) to their Ride
1. Open the app
2. Log in (Or create an account first, then log in)
3. Click the "Rides" button
4. Now, click the "My Posts" button
5. If successful, all the Rides that you created will be visible, which may be none (you may need to make a post first (UC2))
6. Click on a Ride post
7. You will be able to see all the details of the Ride you selected
8. Click the "Accept Requests" button
9. Log in to an account that was accepted to the Ride, by you, just now
10. Click the "Rides" button
11. Now, click the "Accepted Rides" button
12. If successful, the Ride you clicked the "Accept Requests" button on will be visible

# Steps to test Use Case UC11: User searches for Rides
1. Open the app
2. Log in (Or create an account first, then log in)
3. Enter a destination in the Search Bar (If you simply want to see all the available Rides, just exit out of the search bar completely, by clicking the "x" twice)
4. If successful, a list of every Ride post with a destination that starts with the information entered by the user is displayed (if you have exited out of the search bar completely, then all available Rides will be displayed instead)

# Steps to test Use Case UC12: User views posts on map
1. Open the app
2. Log in (Or create an account first, then log in)
3. Click the map icon in the navigation bar at the bottom of the screen (second from the right)
4. If initial map construction is successful, then there will be a marker at each post's destination
5. Click on a marker
6. If successful, a small window will pop up at the marker showing the destination and the post's description
7. These can be verified in two ways:
   a. View the post through the search bar and confirm the fields are the same
   b. Examine the Firebase database and verify the information is correct
8. Click on the window
9. If successful, you will be taken to a screen showing the post's details
10. These can be verified to be correct using the methods from step 7

# Steps to test Use Case UC13: User views their profile
1. Open the app
2. Log in (Or create an account first, then log in)
3. Click the profile icon in the navigation bar at the bottom of the screen (last one)
4. If this is the first time viewing your profile, you must edit the profile first (UC15)
5. If successful, you will be able to view your information

# Steps to test Use Case UC14: User edits their profile
1. Open the app
2. Log in (Or create an account first, then log in)
3. Click the profile icon in the navigation bar at the bottom of the screen (last one)
4. First, click on the edit profile button, located in the top left corner
5. Then, input the desired fields to update your profile
6. Select "Finish", then the profile will be updated.
7. If successful, the input should be displayed in the profile
8. This can be verified by checking the Firebase database and verify the information is saved there
