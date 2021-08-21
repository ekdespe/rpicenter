#!/bin/bash


DEFAULT_PATH_INSTALL="/opt/rpicenter"
DEFAULT_PATH_LOG="/opt/rpicenter/logs"
ENABLE_STARTUP="y"

echo "Type the path to install ( Enter to default at $DEFAULT_PATH_INSTALL )"
read   key_press

if [[ $key_press != "" ]]
then
        DEFAULT_PATH_INSTALL=$key_press

fi

echo "Installing using  path $DEFAULT_PATH_INSTALL"

echo "Type the path to save log  ( Enter to default at $DEFAULT_PATH_LOG )"
read   key_press

if [[ $key_press != "" ]]
then
        DEFAULT_PATH_LOG=$key_press

fi
echo "Installing using  path $DEFAULT_PATH_LOG"

echo "Do you want add the startup boot ? (y)es - (n) ?"
read ENABLE_STARTUP


mkdir $DEFAULT_PATH_INSTALL
mv target/iot-1.0-SNAPSHOT-spring-boot.jar $DEFAULT_PATH_INSTALL


touch /etc/systemd/system/rpicenter.service
if [[ $DEFAULT_PATH_LOG != "/opt/rpicenter/logs" ]]
then
    ln logs/rpicenter.log $DEFAULT_PATH_LOG
fi

cat > /etc/systemd/system/rpicenter.service <<-EOF
[Unit]
Description=Softwre to monitoring sensors iot

[Service]
User=$USER
WorkingDirectory=$DEFAULT_PATH_INSTALL
ExecStart=java -jar iot-1.0-SNAPSHOT-spring-boot.jar

[Install]
WantedBy=multi-user.target
EOF

systemctl daemon-reload
systemctl start rpicenter.service

if [[ $ENABLE_STARTUPs = "y" ]]
then
         systemctl enable rpicenter.service

fi


echo "To start the service type : systemctl start  rpicenter.service"
echo "To stop the service type  : systemctl  stop  rpicenter.service"
echo "To see the status type    : systemctl status rpicenter.service"
