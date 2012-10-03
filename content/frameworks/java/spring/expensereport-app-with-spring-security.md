---
title: Spring MVC App with Spring Security
description: Spring security added to ExpenseReport App.
tags:
    - Spring
    - STS
    - tomcat
    - maven
    - Spring-Security
---

## Introduction
Spring Security provides authentication and access services for J2EE-based enterprise software applications.

## Prerequisites
Before you begin this tutorial, you should be familiar with:

1.  Working with STS or Eclipse IDE.

2.  Assuming previous exercise get completed.

## Add Spring Security to ExpenseReport App
This section provides details on how to get integrate ExpenseReport App with Spring Security.
These are the required jars that we have to add in pom.xml,

  + spring-security-core.jar
  + spring-security-web.jar
  + spring-security-config.jar

## Enable Spring Security:
To enable Spring security we need to add the following steps:

1.     Add a DelegatingFilterProxy in `src/main/webapp/WEB-INF/web.xml`

2.     Create a custom XML configuration named spring-security.xml in `src/main/resources`

In the web.xml declare an instance of a DelegatingFilterProxy. This will filter the requests based on the declared url-pattern.

```xml
<filter>
    <filter-name>springSecurityFilterChain</filter-name>
    <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
</filter>
<filter-mapping>
    <filter-name>springSecurityFilterChain</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

In the spring-security.xml,define HTTP security configuration by adding http tag.And define which url pattern to intecept. Add "jdbc-user service" tag and define the query to get the data from database.

```xml
<beans:beans xmlns="http://www.springframework.org/schema/security"
    xmlns:beans="http://www.springframework.org/schema/beans" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
    http://www.springframework.org/schema/security
    http://www.springframework.org/schema/security/spring-security-3.0.3.xsd">


    <http auto-config="true" use-expressions="true">
        <intercept-url pattern="/login" filters="none" />
        <intercept-url pattern="/logout" filters="none" />
        <intercept-url pattern="/signUp**" filters="none" />
        <intercept-url pattern="/resources/**" filters="none" />
        <intercept-url pattern="/loadApprovalExpenses" access="hasRole('ROLE_MANAGER')"/>
        <intercept-url pattern="/" access="hasAnyRole('ROLE_USER','ROLE_MANAGER')"/>
        <intercept-url pattern="/**" access="isAuthenticated()" />
        <form-login login-page="/login" default-target-url="/"
            authentication-failure-url="/loginfailed" />
        <logout logout-success-url="/logout" />
        <session-management invalid-session-url="/sessionTimeout" >
            <concurrency-control max-sessions="1" />
        </session-management>
    </http>
    <beans:bean id="sas" class="org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy" />

    <authentication-manager>
        <authentication-provider>
            <jdbc-user-service data-source-ref="dataSource"
 
           users-by-username-query="
              select username,password,enabled  
              from users where username=?" 
 
           authorities-by-username-query="
              select u.username, r.rolename from users u, role r 
              where u.role_roleid = r.roleid and u.username =?  " 
 
        />
        </authentication-provider>
    </authentication-manager>

</beans:beans>

```

1.  To access the application, the user must have either `ROLE_USER`,`ROLE_MANAGER` access so to validate this we have added `<intercept-url pattern="/" access="hasAnyRole('ROLE_USER','ROLE_MANAGER')"/>`.

2.  Manager only can approve the expenses so we have restricted that access to only Manager by giving  `<intercept-url pattern="/loadApprovalExpenses" access="hasRole('ROLE_MANAGER')"/>`.

To load this file as part of configuration add `@ImportResource({ "classpath:spring-security.xml"})` in `ComponentConfig` class.

Create `Logincontroller` in `com.springsource.html5expense.contoller` package and add mapping for login,logout,loginfailed,signup.The login.jsp has two input elements for username and password as j_username and j_password.These are spring's placeholder for the username and password respectively.
when the form is submitted ,it will be sent to the following action URL:j_spring_security_check.

## Check point
Now build the app and test it locally.
Select `Run As -> Maven clean`

![maven_clean.png](/images/spring_tutorial/maven_clean.png)

Once Maven clean completed select` Run As -> Maven install`.It will download dependencies from pom.xml.

![maven_install.png](/images/spring_tutorial/maven_install.png)

Select `Run As -> Maven build`

![maven_build.png](/images/spring_tutorial/maven_build.png)

And give Maven goal as , `tomcat:run`

![maven_run.png](/images/spring_tutorial/maven_run.png)

Once server starts completed. Open your browser and enter application url : `http://localhost:8080/html5expense/createNewExpense` ,this time browser will not open a form to create new expense. It will automatically redirect to login page.

![STS Fabric Server.png](/images/spring_tutorial/localhost_login.png)


[<==Prev](/frameworks/java/spring/spring-expensereport-app-tutorial.html)                                                         [Next==>](/frameworks/java/spring/spring-app-deployment-using-STS.html)
