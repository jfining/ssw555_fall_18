# ssw555_fall_18
Project repository for SSW555 Fall 2018


# Instructions to setup development environment

## Install
- [JDK 1.7](http://www.oracle.com/technetwork/java/javase/downloads/java-archive-downloads-javase7-521261.html)
- [Maven](https://maven.apache.org/download.cgi) - [Additional window config](https://maven.apache.org/install.html)
- [Eclipse](https://www.eclipse.org/downloads/) - Pick Java flavor during installation prompt. That will add maven support.

## Import code
- ```git clone https://github.com/jfining/ssw555_fall_18.git```

## Test project is working with command
- Change current directory to 'ssw555_fall_18'
- Type following commands. There should be no errors during executions of the commands.
  - ```mvn package```
  - ```cd target```
  - ```java -jar gedcom-disk-1.0-SNAPSHOT.jar "../Submissions/Project3/proj03test.ged"```

## Import project
- Get code
- Import project to Maven
  - File
  - Select 'Maven' from the list
  - 'Existing Maven Projects'
  - Next
  - Then select root of the project 'ssw555_fall_18'
  - 'Select folder'
  - 'Finish'

## Add debug configuration
- Right click on "App" class and then click debug(This will add configuration)
- Add a break point in main function(Just to test that debug is working correctly)
- Set argument for debug configuration
  - Click on arrow next to debug icon in toolbar
  - Click "Debug Configuration..."
  - Expand "Java Application" click on "App (1)"[This could be different for you] - Click on 'Arguments' tab
  - In 'Program arguments' add absolute path to .ged file with quotes. Example: "C:\Users\...\Submissions\Project3\proj3test.ged"
  - Click on 'Apply'
  - Click on 'Debug'
