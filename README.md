# Lejos - A few Java programs for controlling a Lego EV3 robot

This project describes how-to get Java 8 running on an EV3 brick. 

A few simple programs are included to demonstrate how the EV3 works.

Required: 
- Lego Mindstorms EV3 set
- WiFi dongle for EV3 (I use the Edimax)
- Micro SD-card (between 2GB and 32GB)
- Computer running linux/unix/os x
- Java 8 installed
- Gradle installed
- SSH installed and configured
- Your favorite Java IDE

diskutil list
sudo dd -if=./sd500.img -of=/dev/disk2

copy the contents of lejosimage directory to SD-card root

create JRE 8 embedded runtime. EV3 brick requires profile compact2

./jrecreate.sh --dest ejre-8-b132-linux-arm-sflt.tar.gz --profile compact2 --vm client
tar -czf ejre-8-b132-linux-arm-sflt ejre-8-b132-linux-arm-sflt.tar.gz

Copy this tar.gz file to the root of the SD-card.

Insert SD-card into

The SSH version on the mindstorm is a bit old, connect using:
ssh -oKexAlgorithms=+diffie-hellman-group1-sha1 root@192.168.178.43
