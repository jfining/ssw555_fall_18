set -e
set -o verbose
pwd
mvn test -B
mvn package -DskipTests
cd target
java -jar gedcom-disk-1.0-SNAPSHOT.jar ../Deliverables/Project10/proj10test.ged
java -jar gedcom-disk-1.0-SNAPSHOT.jar ../Deliverables/Project8/proj8test.ged
java -jar gedcom-disk-1.0-SNAPSHOT.jar ../Deliverables/Project6/proj6test.ged
java -jar gedcom-disk-1.0-SNAPSHOT.jar ../Deliverables/Project4/proj4test.ged
java -jar gedcom-disk-1.0-SNAPSHOT.jar ../Deliverables/Project3/proj03test.ged
java -jar gedcom-disk-1.0-SNAPSHOT.jar ../Deliverables/UserStories/test_us06.ged
java -jar gedcom-disk-1.0-SNAPSHOT.jar ../Deliverables/UserStories/test_us04.ged
java -jar gedcom-disk-1.0-SNAPSHOT.jar ../Deliverables/UserStories/test_us08.ged
java -jar gedcom-disk-1.0-SNAPSHOT.jar ../Deliverables/UserStories/test_us09.ged
java -jar gedcom-disk-1.0-SNAPSHOT.jar ../Deliverables/UserStories/test_us11.ged
java -jar gedcom-disk-1.0-SNAPSHOT.jar ../Deliverables/UserStories/test_us12.ged
java -jar gedcom-disk-1.0-SNAPSHOT.jar ../Deliverables/UserStories/test_us15.ged
java -jar gedcom-disk-1.0-SNAPSHOT.jar ../Deliverables/UserStories/test_us19.ged
java -jar gedcom-disk-1.0-SNAPSHOT.jar ../Deliverables/UserStories/test_us20.ged
java -jar gedcom-disk-1.0-SNAPSHOT.jar ../Deliverables/UserStories/test_us29.ged
java -jar gedcom-disk-1.0-SNAPSHOT.jar ../Deliverables/UserStories/test_us33.ged
