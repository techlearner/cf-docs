---
title: Spring Application Developement Tutorial
description: Explains how to push your application to Cloudfoundry using STS
tags:
    - Spring Application
    - STS
    - Cloud Foundry
    - Micro Cloud

---

This section provides how to push your application to Cloudfoundry and access the services.

## Prerequisites

+ Create an account in Cloudfoundry.com and get register your domain.

+ Get VMware player and install it.

+ Get Micro Cloud Foundry.

+ Before start,make sure you have installed Cloudfoundry plugin for STS.If not installed go to STS -> Help ->Dashboard.Scroll down to the Server and Clouds  section and choose the Cloud Foundry integration plug-in from there. Click Install.

+ To access Cloud Foundry PostgreSQL service in your local machine,you have PostgreSQL server in your machine.

## Configure Micro Cloud Foundry

1.     Open your VMware player,click open a new virtual machine,now select micro cloud foundry location and click finish.

2.     Now click play virtual machine.

3.     Once VM started,select 1 to configure.

4.     Press Enter. It will ask to create VCAP root password .choose your password and re-enter to verify it.

5.     Then next select your network option in that case select DHCP and give PROXY information ,if you are behind firewall give proxy ip like http://172.168.76.176:8083 otherwise give none.

6.    Enter your mail-id and  agree terms of services then Micro Cloud Foundry will start install in your machine.

7.    Once installation process completed,you can see your Identity is ok in green color and you will get one IP address to access your Micro Cloud Foundry.

8.    Now go to your browser and enter your url Identity you will get this message **Welcome to VMware's Cloud Application Platform**.If you are not getting this message your configuration might be wrong please reconfigure it.


## Add CloudFoundry Server to deploy your Spring application to Micro Cloud Foundry:

1.     Go to servers view,right click add New Server.Now you can see Cloud Foundry under VMware.Select Cloud Foundry,then click next.It will ask your account information and Choose Microcloud - http://api.{mycloud}.cloudfoundry.com from the URL.It will open a window to create Micocloud Target.Enter your cloud name to replace mycloud.

2.     Now give your Micro Cloud Foundry account information.Click validate and make sure you have entered  valid credentials.

3.     Click next it will show you add and remove view.Now you can select our spring application.Once you added our application it will open new window with application name and application type.You can enter your application name.

4.     Click next you can see deployed URL(your application name+cloudfoundry.com) and memory reservation.by default memory reservation selected to 512M.You can increase memory if required..

5.     Click next now it will ask for services selection.Since we are using postgresql in our application we need to add postgresql service.Click add services to server it will open service configuration window.select type as Postgre SQL database service(VFabric) and give name to this service then click finish.

6.     Now you can see your services in services selection window.Select postgresql service and click finish.Now server starts to deploy our application into Cloud Foundry.


## Add CloudFoundry Server to deploy your Spring application to Cloud Foundry:

1.     Go to servers view,right click add New Server.Now you can see Cloud Foundry under VMware.Select Cloud Foundry,then click next.It will ask your account information and Choose VMware Cloud Foundry - http://api.cloudfoundry.com from the URL.

2.     Now give your Cloud Foundry account information.Click validate and make sure you have entered  valid credentials.

3.     Click next it will show you add and remove view.Now you can select our spring application.Once you added your application it will open new window with application name and application type.You can enter your application name.

4.     Click next you can see deployed URL(your application name+cloudfoundry.com) and memory reservation.by default memory reservation selected to 512M.You can increase memory if required..

5.     Click next now it will ask for services selection.Since we are using postgresql in our application we need to add postgresql service.Click add services to server it will open service configuration window.select type as Postgre SQL database service(VFabric) and give name to this service then click finish.

6.     Now you can see your services in services selection window.Select postgresql service and click finish.Now server starts to deploy our application into Cloud Foundry.

##Database Tunnel

1.      Go to servers view,then double click VMware Cloud Foundry.It will open Overview window then select applications tab.Now you can able to see our application in List of currently deployed applications.

2.      Go to services section,select psotgresql service which we binded into our application and right click, now select Open Caldecott Tunnel.

3.      It will get the tunnel information from cloudfoundry.Once tunneling process completed ,select the service and right click,now select Show Caldecott Tunnel info.Now you can see Username,Database name,Password,Port.

4.      Now go to your postgresql client,and enter database name,username,password,port from tunnel information and provide Host as localhost.Now you can able to access your cloudfoundry postgresql service in your local machine.
       
