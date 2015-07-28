#!/bin/bash

# The Git commit date/time was originally passed to maven with Git command in back ticks -Dscm_date=`git show --pretty=format:%ci master |sed -n 1p` on the maven command line
# AntHillPro did not execute the command within the back ticks.  The environment variable created by this command line argument was used
# to populate a JSON file which provides the data for "Version Popup"

# This script is called by maven (configured in v5/cdebrowser/client/pom.xml)

#if git is not on the PATH, then we are probably running within AntHillPro and it will be in /usr/loclal/bin
which git > /dev/null
if [  $? -ne 0 ]
then
   echo Git is not on the $PATH
   export scm_date2=`/usr/local/bin/git show --pretty=format:%ci | sed -n 1p| sed 's/ \-/ \\\-/g'`
else
    export scm_date2=`git show --pretty=format:%ci | sed -n 1p| sed 's/ \-/ \\\-/g'`
fi
echo Commit Date/Time: $scm_date2
# Not using sed with -i because it does not work on Mac
#sed -i "s/^scm-date.*/scm-date=${scm_date2}/g" src/main/resources/cdeBrowser.server.properties

sed  "s/\"scm_date\":.*/\"scm_date\":\"${scm_date2}\"/g" src/main/webapp/version.json > /tmp/version.json
cp /tmp/version.json src/main/webapp/version.json

