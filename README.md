
java -jar native1-jar-with-dependencies.jar
java -jar -Ddb="./native1.sqlite" native1-jar-with-dependencies.jar
native-image -jar  native1-jar-with-dependencies.jar

