set -e
set -o verbose
pwd
mvn test -B
mvn package -DskipTests
cd target
java -jar gedcom-disk-1.0-SNAPSHOT.jar ../Submissions/Project3/proj03test.ged
java -jar gedcom-disk-1.0-SNAPSHOT.jar ../Submissions/UserStories/test_us06.ged
java -jar gedcom-disk-1.0-SNAPSHOT.jar ../Submissions/UserStories/test_us08.ged
