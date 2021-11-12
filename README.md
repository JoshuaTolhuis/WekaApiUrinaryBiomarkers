# WekaApiUrinaryBiomarkers
Thema 9 - Java Weka Wrapper - Joshua Tolhuis - Version 1.0
-----

This repo contains the java wrapper to classify instances given by a user. It will classify what type of systemic scleroris someone has based on antibody data the user will give as input. 

## Dependencies

The dependencies below are configured in the build.gradle file. As long as this build.gradle file is used there should be no problem.

Dependencies:
- Java version 11 or higher is neccessary to run this script.
- Apache CLI version 1.4
- Weka API version 3.8.0

## Input
This project is based around a inputfile and will work with a certain input to function
Example Input Data:
|plasma_CA19_9|Creatinine|LYVE1|RGE1B|TFF1|

## Set-Up

Only csv files are supported.

    Clone the repo via the command line onto your own pc
    The jar file can be found in WekaApiRunner/build/libs/
    Drag the .csv file of your choice into the WekaApiRunner/build/libs/ folder.
    Navigate, on the terminal, to the WekaApiRunner/build/libs/ folder.
    Run the script like this:
    ~$ java -jar WekaApiRunner-1.0-SNAPSHOT-all.jar -f <filename>
    
    For help run it like this:
    ~$ java -jar WekaApiRunner-1.0-SNAPSHOT-all.jar -h

## The output

The output with the classified labels will be printed on the terminal and look like this:
3,710.8,0.19227,3.055294,32.89096,0
3,941,0.46371,1.044345,14.36436,0
3,13740,0.32799,5.232527,123.10473,0
3,1488,1.50423,8.200958,411.938275,0

It will be shown as a comma seperated file. At the end of each row the classification for this instance will be showed.

Next to the output on the screen it will also be written to a file. This file is called classification.csv and can be found in the resources folder.

## Contact

For questions about the program please use the following email adress:
j.o.tolhuis@st.hanze.nl
