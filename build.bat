set JAVA_HOME=./openlogic-openjdk-8u392-b08-windows-64
call gradlew build
ren build/libs/compactmachines3-1.12.2-3.1.0.jar build/libs/compactmachines3-3.1.0-GTCP.jar
echo "Output in build/libs"