---
title: Spring Application Developement Tutorial
description: To Integrate Spring MVC with Spring Application
tags:
    - Spring MVC
    - Spring Security

---

This section provides how to get integrate Spring MVC Application with Spring Security.
Before start,add the following dependencies in your pom.xml,

  + spring-security-core.jar
  + spring-security-web.jar
  + spring-security-config.jar

## Integrate Spring Security to Existing Spring MVC Project:

 To enable Spring security we need to add the following steps:

 1.      Add a DelegatingFilterProxy in the web.xml

 2.      Declare a custom XML config named spring-security.xml

 In the web.xml we declare an instance of a DelegatingFilterProxy.This will filter the requests based on the declared url-pattern.
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

 In the spring-security.xml,define HTTP security configuration by adding http tag.And define which url pattern to intecept.

```xml
<http auto-config="true" use-expressions="true">
		<intercept-url pattern="/index.jsp" access="hasAnyRole		('ROLE_USER','ROLE_MANAGER')" />
		<form-login login-page="/login" default-target-url="/welcome"
			authentication-failure-url="/loginfailed" />
		<logout logout-success-url="/logout" />
</http>

```

Create Logincontroller and add the mapping for login,logout,loginfailed.

 Define dataSource bean,to get database connection
```xml
<bean id="dataSource" class="org.springframework.jdbc.datasource.SimpleDriverDataSource">
 
			<property name="driverClass" value="org.postgresql.Driver" />
			<property name="url" value="jdbc:postgresql://192.168.6.30:5432/postgres" />
			<property name="username" value="postgres" />
		<property name="password" value="postgres" /
```


In Spring Security configuration file,add "jdbc-user service" tag and define the query to get the data from database.

```xml
<authentication-manager>
	   <authentication-provider>
		<jdbc-user-service data-source-ref="dataSource"
 
		   users-by-username-query="
		      select username,password, enabled 
		      from users where username=?" 
 
		   authorities-by-username-query="
		      select u.username, ur.authority from users u, user_roles ur 
		      where u.user_id = ur.user_id and u.username =?  " 
 
		/>
	   </authentication-provider>
	</authentication-manager>
```


Modify the <context-param> section of your web.xml, to add a reference to this file(spring-security), causing it to be loaded as part of Spring's application context:

```xml
<context-param>
   <param-name>contextConfigLocation</param-name>
   <param-value>
       classpath:applicationContext-servlet.xml
       /WEB-INF/spring-security.xml
   </param-value>
</context-param>
```

create custom login.jsp with two input elements for username and password as j_username and j_password.These are spring's placeholder for the username and password respectively.

when the form is submitted ,it will be sent to the following action URL:
j_spring_security_check.

Add logout functionality:
   add j_spring_security_logout link in your application. once your clicked this link,spring invalidate user session.


