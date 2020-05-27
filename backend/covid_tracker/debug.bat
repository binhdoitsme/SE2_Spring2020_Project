cd /D "%~dp0"
java -Xdebug  -agentlib:jdwp="transport=dt_socket,server=y,suspend=y,address=1044" -Dfile.encoding="UTF-8" -cp ./target/covid_tracker-1.0-SNAPSHOT-jar-with-dependencies.jar com.hanu.App