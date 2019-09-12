# Final Year Project App
This repo holds all the files for an Android app that was used as part of my final year project for university.

It displays data that is collected by a Raspberry Pi with connected sensors (soil moisture, light level, temperature and humidity sensors) and then provides suggestions on how to take care of the plant in the moment by feeding the data through an expert system.

Critical files are in 
https://github.com/SamRibes/Final-Year-Project-Android-App/tree/master/app/src/main/java/fyp/sam/fypapp

Some files to look at are the Expert System classes, which takes in data from the firebase database in the form of a .txt file and outputs a suggestion as a string, and the data manager classes, which converts the data from the firebase app into a txt file and vise versa and also manages adding, reading and deleting data from the .txt file.
