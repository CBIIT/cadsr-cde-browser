#~/bin/sh
# if [ -d "/usr/src/cdebrowser/cadsr-cde-browser" ]; then
#         echo "Skipping cadsr-cde-browser install"
#         cd /usr/src/cdebrowser/cadsr-cde-browser
#         git pull
#         echo "pulling new code"
# else
#   git clone https://github.com/CBIIT/cadsr-cde-browser 
#   echo "Cloning the repository"
# fi

exec /opt/wildfly/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0