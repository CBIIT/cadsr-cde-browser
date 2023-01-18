#!/bin/bash

rm -rf /tmp/cadsr-cde-browser-temp
cp -R /tmp/cadsr-cde-browser /tmp/cadsr-cde-browser-temp
cd /tmp/cadsr-cde-browser-temp

mvn -f v5/cdebrowser/pom.xml -s v5/cdebrowser/settings.xml -DCADSR_DS_PORT=$CADSR_DS_PORT -DCADSR_DS_USER=cdebrowser -DCDEBRWSR_DS=jdbc/CDEBrowserDS -Dtier=DEV -DCADSR_DS_HOST=$CADSR_DS_HOST -DCADSR_DS_PSWD=$CADSR_DS_PSWD -DCADSR_DS_TNS.ENTRY=DSRDEV -Dcde-settings-file=v5/cdebrowser/settings.xml -P 12c clean install

cp /tmp/cadsr-cde-browser-temp/v5/cdebrowser/client/target/cdebrowserClient.war /usr/local/tomcat/webapps
cp /tmp/cadsr-cde-browser-temp/v5/cdebrowser/server/target/cdebrowserServer.war /usr/local/tomcat/webapps
