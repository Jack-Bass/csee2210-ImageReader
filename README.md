# csee2210-ImageReader

Welcome to ImageReader!  This app was created for my CSEE 2210 class at the University of Georgia.
That probably means that you aren't allowed to copy this code, but feel free to use it.  I am sorry 
that it looks weird in GitHub's viewer, but all the indents are right if you look at this in another
program.

## What Is This Project?

This project was designed to help dyslexic students with improving their reading comprehension.
Using the CameraApp class, the user can take a picture using the Raspberry Pi Camera Module,
and it will be read to them using text-to-speech software provided by the GNUstep speech
engine.  For our project, we bundled this software into a jar file and executed it on a
Raspberry Pi 3 B+.  For a demonstration of the design, please see THIS YouTube video.

## How to Run

In order to run this project, you will need:

    1. A Raspberry Pi (any Pi 1, 2, or 3 model should work)
    2. The Raspberry Pi camera module
    3. Google Tesseract installed
       sudo apt-get install tesseract-ocr
    4. GNUstep speech engine installed
       sudo apt-get install gnustep-gui-runtime

When setting up your project directory, be sure to include:

    1. a folder to keep the class files
    2. a "resources" folder at the root of your directory

## Final Thoughts

Thank you for taking the time to read this!  We hope you may get some use out of it.

-Jack Bass and Reece Griffith