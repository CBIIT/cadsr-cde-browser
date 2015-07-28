#!/bin/bash

# The Git build time was originally passed to maven with Git command in back ticks -Dscm_date=`git show --pretty=format:%ci master |sed -n 1p`
# AntHillPro did not execute the command within the back ticks.  The environment variable created by this command line argument was used
# to populate a JSON file which provides the data for "Version Popup"

# This script is called by maven (configured in v5/cdebrowser/client/pom.xml)

#if git is not on the PATH, then we are probably running within AntHillPro and it will be in /usr/loclal/bin
which git > /dev/null
if [  $? -ne 0 ]
then
   echo Git is not on the PATH
   xport scm_date2=`/usr/local/bin/git show --pretty=format:%ci | sed -n 1p| sed 's/ \-/ \\\-/g'`

else
    export scm_date2=`git show --pretty=format:%ci | sed -n 1p| sed 's/ \-/ \\\-/g'`
fi

echo Update build time:  ${scm_date2}

# Not using sed with -i because it does not work on Mac
#sed -i "s/^scm-date.*/scm-date=${scm_date2}/g" src/main/resources/cdeBrowser.server.properties

echo Copy src/main/webapp/version.json to  /tmp/version.json
sed  "s/\"scm_date\":.*/\"scm_date\":\"${scm_date2}\"/g" src/main/webapp/version.json > /tmp/version.json
ls -l /tmp/
echo copy /tmp/version.json to src/main/webapp/version.json
cp /tmp/version.json src/main/webapp/version.json
cat src/main/webapp/version.json

