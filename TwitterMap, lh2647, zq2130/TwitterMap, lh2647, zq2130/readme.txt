Specifications

1. OverView
This simple web app realize the data obtaining from Twitter and info showing on the global map, named TwitterMap. The app consists of three main part, the database (Amazon SimpleDB), server side (Java Servlet, using tomcat 7.0), and a simple user interface. The user can pick up a keywords, or use search box to see certain Twitter status, and the result could be presented in various form through click on the buttons in the UI. This web app has already been put on Amazon Elastic Beanstalk with Elastic LoadBalancing. 

2. Steps to run the code
You can eaisly use URL:http://cs6998lh2647-env.elasticbeanstalk.com. OR

Since this is a AWS web application, you need to create such a web project, and put the file in corresponding directory. Besides, you need to import the .jar file, I have put all the three-party library in the directory lib. Please import that bfore you run the code since I import them using address on my laptop. 

The app is based on Tomcat, you can run the file on server specifying using Tomcat 7.0. 

3. Two Versions
To better describe the function of this app, we implement two versions. 

-Version 1(URL:http://cs6998lh2647-env.elasticbeanstalk.com): We first use Twitter Stream API to fetch the data that contains certain keywords, and then store the data into SimpleDB using domain named MySTORE. This process takes several days, and the number of the records in SimpleDB exceeds some tens of thousands. Using this version, you can easily find the distribution of message and density of different area.

-Version 2(URL:http://cs6998lh2647-env.elasticbeanstalk.com/main.html): we run a server on the Amazon EC2, and create a new domain calling Twitter. The server could continuously fetch data from Twitter, and communicate with the SimpleDB. It will renew the database if it find the number of records in the database reaches a contain value. Thus, we could have a near real time TwitterMap. 

4. Database
We simply applies Amazon SimpleDB, creating two domains named MySTORE and Twitter. 

-DataLoader.class: realize the data obtain and store for version 1. It contains two functions to initialize the database and put the extracted data into database.

-Loader.class: For version 2.

-simpledb.class: perform database queries for version 1. It provides the function query() so that the servlet can easily fetch data through specifying keyword.  

-simpledb1.class: For version 2.


5. Servlet 
-server.class: Relized the servlet for version 1, it will take the keyword from front-end, and call function query(keyword) to fetch results and push back to front-end. 

-server.class1: The servlet for version 2.

Since I use Jquery in front-end, I wrap the result in json format through a three-part library. Also the result get from database is a List of record, where record denotes an object of class record. The object contains five fields, including x, y, username, location and content. 

6. Front-End
-There are several buttons displayed on the map, separately with the function listed below:

-Search Keywords: Enter any key word in the search box then press "search" button. All tweets with this keyword will show on the map as markers according to their location. Clicking marker will show infowindow with correspondent tweet content on that marker.

-food: All tweets with the word food in it will show on the map as markers according to their location. Clicking markers will show infowindow with correspondent tweet content on that marker.

-movie, sport, news are all the same with food.

-All: All tweets with all four keywords listed above will show on the map.

-Hide Markers: Hide all markers on the map.

-Show All Markers: Show all the markers.

-Delete Markers: Delete all the markers. After this action, no markers would show on the map again except that you search the keyword again.

-toggleHeatmap: Show Heatmap or Hide Heatmap. Heatmap is to show the density of tweets.

-Change gradient: Change gradient of heatmap

-Change radius: Change radius of heatmap

-Change opacity: Change opacity of heatmap

By using jQuery to communicate with back-end (send correspendent keyword), fetch data from database and store markers' location in TaxiData[], tweets' content in tweet[] and show those on Google map.


 