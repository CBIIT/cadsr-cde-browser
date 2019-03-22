#~/bin/sh
echo "we are in the /usr/src/cdebrowser/cadsr-cde-browser directory"
git pull
if [ $tag != 'origin/master'  ] && [ $tag != 'master' ]; then
  git checkout tags/$tag
fi

# Function to check if wildfly is up #
function wait_for_server() {
  until `/opt/wildfly/bin/jboss-cli.sh -c --controller=localhost:19990 ":read-attribute(name=server-state)" 2> /dev/null | grep -q running`; do
    sleep 1
  done
}

echo "=> build application and copy artifacts to /local/content/cdebrowser"

mvn -f v5/cdebrowser/client/pom.xml -DCADSR_DS_USER=${CADSR_DS_USER} -DCADSR_DS_PSWD=${CADSR_DS_PSWD} -Dtag=${tag} -Dtier=${tier} -P AHP -s v5/cdebrowser/settings.xml clean package
mvn -f v5/cdebrowser/server/pom11.xml -DCADSR_DS_USER=${CADSR_DS_USER} -DCADSR_DS_PSWD=${CADSR_DS_PSWD} -Dtag=${tag} -Dtier=${tier} -Djavax.xml.parsers.DocumentBuilderFactory=com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl -P AHP -s v5/cdebrowser/settings.xml clean package

cp v5/cdebrowser/client/target/cdebrowserClient.war \
   v5/cdebrowser/server/target/cdebrowserServer.war \
   v5/cdebrowser/server/target/classes/caDSR_CDE_Browser5_modules.cli \
   v5/cdebrowser/server/target/classes/caDSR_CDE_Browser5_setup_deploy.cli /local/content/cdebrowser/artifacts
cp v5/cdebrowser/server/target/cdebrowserServer-v5-Dependencies/ojdbc7-12.1.0.2.0.jar /local/content/cdebrowser/modules

echo "=> starting wildfly in background"
/opt/wildfly/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0 &

echo "=> Waiting for the server to boot"
wait_for_server

echo "=> deploying modules"
/opt/wildfly/bin/jboss-cli.sh --file=/local/content/cdebrowser/artifacts/caDSR_CDE_Browser5_modules.cli

echo "=> reloading wildfly"
/opt/wildfly/bin/jboss-cli.sh --connect controller=localhost:19990 command=:reload

echo "=> Waiting for the server to reload"
wait_for_server

echo "=> deploying"
/opt/wildfly/bin/jboss-cli.sh --file=/local/content/cdebrowser/artifacts/caDSR_CDE_Browser5_setup_deploy.cli

echo "=> shutting wildfly down"
/opt/wildfly/bin/jboss-cli.sh --connect controller=localhost:19990 command=:shutdown

echo "=> starting wildfly in foreground"
/opt/wildfly/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0 
