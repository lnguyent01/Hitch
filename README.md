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

# List of known issues
(These are not bugs in the code. Rather, they are uncontrollable issues that arise as a result of some outside force)
1. Logging in and signing up take a longer amount of time the worse the internet connection is
2. The map may not work unless you have a powerful enough computer (in terms of RAM, mostly) and have enabled graphics acceleration in the options menu of your Android Virtual Device. Also, make sure "Location Services" are enabled on your Android Virtual Device.

# Access to the Firebase Database
In our Draft Project, there was a link to our Firebase Database (https://hitch-7466c.firebaseio.com/). Invitations to join this Firebase project were sent to chandanaupadhyaya@ucsb.edu and jcai00@ucsb.edu. However, this is no longer neccessary. All use cases can be successfully tested without manipulating Firebase data directly. In fact, manipulating data directly in the Firebase Database may cause bugs. Please do not do so unless a new bug that impedes testing is discovered.

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
7. If successful, you will be able to see all the details of the Ride you selected: the driver’s name, their profile picture, where the driver is departing from, when they are leaving, where the driver is going to, the Ride’s description, how many seats are available, and a button to "Cancel Request"

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
7. If successful, you will be able to see all the details of the Ride you selected: the driver’s name, their profile picture, where the driver is departing from, when they are leaving, where the driver is going to, the Ride’s description, how many seats are available, a button to “Accept Requests”, and a button to view "Accepted Passengers"

# Steps to test Use Case UC10: Driver accepts Passenger(s) to their Ride
1. Open the app
2. Log in (Or create an account first, then log in)
3. Click the "Rides" button
4. Now, click the "My Posts" button
5. If successful, all the Rides that you created will be visible, which may be none (you may need to make a post first (UC2))
6. Click on a Ride post
7. You will be able to see all the details of the Ride you selected
8. Click the "Accept Requests" button
9. You will see a list of all the people who have requested to join your Ride
10. Click on a person to accept them (they will not disappear from the list until you have backed out to the “My Posts” menu)
11. Back out to the “My Posts” menu
12. Click on the same Ride post you were just viewing
13. Click the “Accepted Passengers” button
14. If successful, the person you just accepted will be visible (if you log in to the account that was accepted, this Ride will also be visible in their “Accepted Rides” section under the “Rides” section accessible by clicking the “Rides” button on the homepage)


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
3. Click the profile icon in the navigation bar at the bottom of the screen (last one on the right)
4. If successful, the information you entered previously (either from account creation (UC1) or editing the profile (UC14) will be displayed

# Steps to test Use Case UC14: User edits their profile
1. Open the app
2. Log in (Or create an account first, then log in)
3. Click the profile icon in the navigation bar at the bottom of the screen (last one on the right)
4. First, click on the edit profile button, located in the top left corner
5. Then, input the desired fields to update your profile
6. Select "Finish", then the profile will be updated.
7. If successful, the information you entered previously will be displayed, overwriting any previous information, while unchanged fields will display the same information as before

# Steps to test Use Case UC15: User cancels their request to join a Ride
1. Open the app
2. Log in (Or create an account first, then log in)
3. Click the "Rides" button
4. Now, click the "Requested Rides" button
5. If successful, all the Rides that you have requested will be visible, which may be none (you may need to request to join first (UC3))
6. Click on a Ride post
7. You will be able to see all the details of the Ride you selected
8. Click the "Cancel Request" button
9. If successful, the Ride will no longer appear in your "Requested Rides" section (if you log in to the account that made the Ride post, the account that cancelled their request will no longer appear under the "Accept Passengers" menu from this Ride's details screen)

# Steps to test Use Case UC16: Driver views accepted passengers
1. Open the app
2. Log in (Or create an account first, then log in)
3. Click the "Rides" button
4. Now, click the "My Posts" button
5. If successful, all the Rides that you created will be visible, which may be none (you may need to make a post first (UC2))
6. Click on a Ride post
7. You will be able to see all the details of the Ride you selected
8. Click the "Accepted Passengers" button
9. If successful, a list of all the accepted passengers will be displayed (if none are accepted, a message will appear telling you so, instead of taking you to an empty list)

# Steps to test Use Case UC17: User changes profile picture
1. Open the app
2. Log in (Or create an account first, then log in)
3. Click on the profile icon in the bottom of the navigation bar (last icon)
4. User clicks on the profile picture icon
5. User chooses icon to set as their new profile picture (If you have no pictures available, take one with the "Camera" app)
6. If successful, when viewing you profile, the picture you chose will be visible, replacing the default profile picture

# Steps to test Use Case UC18: Driver deletes post
1. Open the app
2. Log in (Or create an account first, then log in)
3. Click the "Rides" button
4. Now, click the "My Posts" button
5. Click the specific post to be deleted
6. Click the delete post button
7. If successful, the post will no longer be visible in the "My Rides" section (any accounts that were requested or accepted to the deleted post will no longer see this post in their "Requested Rides" section or their "Accepted Rides" section, respectively)

# Steps to test Use Case UC19: User deletes account
1. Open the app
2. Log in (Or create an account first, then log in)
3. Click on the “Profile” icon (last one on the right)
4. Click on the “Edit Profile” button
5. Click on the “Delete Account”, at the very bottom
6. If successful:  
   a. The user will be removed from the database  
   b. All of the user’s posts will be deleted (UC18)  
   c. All of the user’s requests to join Rides will be cancelled (UC15)  
   d. User will be removed from the list of accepted passengers of any Ride that they have been accepted to, and that Ride gains an available spot
