---
title: Spring Application Developement Tutorial
description: Explains how to push your application to Cloudfoundry using VMC
tags:
    - Spring Application
    - VMC
    - Cloud Foundry
    - Micro Cloud

---

This section provides how to push your application to Cloudfoundry and access the services using VMC.

## Prerequisites

+ Create an account in Cloudfoundry.com and get register your domain.

+ Get VMware player and install it.

+ Get Micro Cloud Foundry.

+ To use VMC,get install latest Ruby and install VMC gem.

+ To access Cloud Foundry PostgreSQL service in your local machine,you should have PostgreSQL server in your machine.

## Configure Micro Cloud Foundry

1.     Open your VMware player,click open a new virtual machine,now select micro cloud foundry location and click finish.

2.     Now click play virtual machine.

3.     Once VM started,select 1 to configure.

4.     Press Enter. It will ask to create VCAP root password .choose your password and re-enter to verify it.

5.     Then next select your network option in that case select DHCP and give PROXY information ,if you are behind firewall like http://172.168.76.176:8083 otherwise give none.

6.    Enter your mail-id and  agree terms of services then Micro Cloud Foundry will start install in your machine.

7.    Once installation process completed,you can see your Identity is ok in green color and you will get one IP address to access your Micro Cloud Foundry.

8.    Now go to your browser and enter your url Identity you will get this message **Welcome to VMware's Cloud Application Platform**.If you are not getting this message your configuration might be wrong please reconfigure it.


## To deploy your Spring application to Micro Cloud Foundry using VMC:

1.     Go to terminal and issue vmc target to your micro cloud.

```bash
prompt$ vmc target http://api.{mycloud}.cloudfoundry.me
```

2.     Now create new user to access Micro Cloud Foundry by typing

```bash
prompt$ vmc register
```

3.     Now login to Micro Cloud Foundry with your credentials.

```bash
prompt$ vmc login
```

4.     Then give vmc push to push your application.

```bash
prompt$ vmc push
Would you like to deploy from the current directory? [Yn]:
```Enter NO and give your application path

5.     Next Enter Application Name:

6.     Then enter your application type as spring since we are pushing Spring application.

7.     Then you can see application deployed url and press enter

8.     Next select Memory reservation,by default Micro Cloud Foundry will give 512M.

9.     Select instances 1.

10.   Next VMC will ask you to create any services to bind your application since we are using postgresql services.Enter 'Y" and select postgresql and press Enter.

11.    Once this completed VMC will ask you to save configuration,select default value and enter.Now VMC starts to push our application into
      Micro Cloud Foundry


## Add CloudFoundry Server to deploy your Spring application to Cloud Foundry:

1.     Go to terminal and issue vmc target to your Cloud Foundry.

```bash
prompt$ vmc target http://api.cloudfoundry.com
```

2.     Now login to Cloud Foundry with your credentials.

```bash
prompt$ vmc login
```

3.    Then give vmc push to push your application.

```bash
prompt$ vmc push
Would you like to deploy from the current directory? [Yn]:
```Enter NO  and give your application path

4.      Next Enter Application Name:

5.      Then enter your application type as spring since we are pushing Spring application.

6.      Then you can see application deployed url and press enter

7.      Next select Memory reservation,by default Micro Cloud Foundry will give 512M.

8.      Select instances 1.

9.      Next VMC will ask you to create any services to bind your application since we are using postgresql services.Enter 'Y" and select postgresql and press Enter.

10.     Once this completed VMC will ask you to save configuration,select default value and enter.Now VMC starts to push our application into Micro Cloud Foundry


##Database Tunnel

1.     Go to terminal and type vmc tunnel

```bash
prompt$ vmc tunnel
```

2.     Then vmc will list the availble services to Cloud Foundry.Now select the service which you bind to our application.

3.     Then vmc will start to deploy caldecott and you will get database information like this.

```bash
Deploying tunnel application 'caldecott'.
Uploading Application:
Checking for available resources: OK
Packing application: OK
Uploading (1K): OK   
Push Status: OK
Binding Service [postgres]: OK
Staging Application 'caldecott':OK                                             
Starting Application 'caldecott': .
Starting Application 'caldecott':OK 
Getting tunnel connection info: OK
Service connection info: 
username : uca03bb911e9d4c5f818c7554763e59e9
password : p8637c62f6bd8428391029dedd4133411
name     : dfeb4299178dd47a5b9a00fb26ce0bbe4
Starting tunnel to postgres on port 10000.
1: none
2: psql
```

4.   Now select option 2 to open your psql client.
