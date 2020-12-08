# **HOPE**

Hope has been a 48 hour journey into building a lost and found application that works across the entire world. Submitted to ***Oxford Hacks 2020***, the aim is to make reporting lost items as easy as possible for the users so that they are obliged to report it.

Similarly it would be as easy as finding it; as it is to report it. Due to the project's grand ambitions and limiing time constraint, the lost portion of the project did remain incomplete; but given an opportunity to work on it again, would try my best to complete it.
<br/><br/>

## **LANGUAGES USED**
- **Android Studio:** Primarily Android Studio has been used to create the application. Taking Images to report lost items as well as searching for them would be done using this app.
- **Python:** Python was used to automatically identify the lost items from their images so not 1 word has to be entered from the users side.
- **Firebase:** Firebase realtime database to store all lost item info and Firebase Storage to store the images of lost items taken by the user.
<br/><br/>

## **SOFTWARE DESIGN**

### **MAIN PAGE**

The main page is where you are presented with an option of 'lost' or 'found'. As a person who has a recently lost an item, you would have to press the 'lost' button.
If you in case find an unattended item that appears to have been lost press the 'found' button and report the item to us.

<img align="center" alt="MainPage" width="25%" src="https://github.com/Rain1213/Hope/blob/master/screenshots/mainPg.JPG?raw=true" />
<br/><br/>

### **FOUND PAGE**

When you click on 'Found', you will be taken to the found main page. To keep things as simple as possible, all the finder has to do is take a picture of the found item. If the person wants to go an extra step and return the item to the police station, she/he could check the 'return to the police station' check box so that the lost person is,if he finds the item through the app, can take it easy that his item is in safe hands.

What remains is to submit the info and its done. SIMPLE!!

<img align="center" alt="FoundPage" width="25%" src="https://github.com/Rain1213/Hope/blob/master/screenshots/lostPg.JPG?raw=true" />

<img align="center" alt="AfterFoundPage" width="25%" src="https://github.com/Rain1213/Hope/blob/master/screenshots/afterLostPg.JPG?raw=true" />
<br/><br/>


## **BACKEND**

### **FIREBASE STORAGE**
To store the image I needed it to store it over the internet so that its url can be accessed in the future. I made use of firebase storage for this.

<img align="center" alt="FirebaseStorage" width="100%" src="https://github.com/Rain1213/Hope/blob/master/screenshots/FirebaseStorage.jpg?raw=true" />

<br/><br/>

### **FIREBASE REALTIME DATABASE**

I needed a database to store the the location details along with object details in realtime. Best way was to make of firebase realtime database for this. Intially the details of "type" remains empty. This is the field which recognizes and classifies the image.

<img align="center" alt="FirebaseStorage" width="100%" src="https://github.com/Rain1213/Hope/blob/master/screenshots/FirebaseDatabase.jpg?raw=true" />

<br/><br/>


### **OBJECT IDENTIFICATION**
 Remember the finder has not written a single word as to what the item she/he found is. Therefore to segregate items into groups of items like "wallets", "bagpacks", "smartphones" etc. I made it so that the image stored in the Firebase Storage has to go through a python script to identify what the object is.
 The flow of data is as follows:

 The python scripts check if the "type" value of any child in firebase database is empty. If yes, it gets the value from the url, which contains the camera taken image of a found item. The script identifies what the object is and returns the value and fills the "type" column in the realtime database.

 <img align="center" alt="FirebaseStorage" width="100%" src="https://github.com/Rain1213/Hope/blob/master/screenshots/ObjectRecognition.jpg?raw=true" />
