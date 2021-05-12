cd /d %~dp0
mvn install:install-file -DgroupId=org.apache.flink -DartifactId=flink-connector-elasticsearch7_2.11  -Dversion=1.9.0 -Dpackaging=jar -Dfile=./flink-connector-elasticsearch7_2.11-1.9.0.jar

mvn install:install-file -DgroupId=com.oracle.jdbc -DartifactId=ojdbc8  -Dversion=18.3.0.0 -Dpackaging=jar -Dfile=./ojdbc8-18.3.0.0.jar 