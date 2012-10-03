---
title: Spring
description: Spring Application Development with Cloud Foundry
tags:
    - spring
    - mongodb
    - redis
    - code-attached
---

This is a guide for Java developers using the Spring framework and PostgreSQL database to build and deploy their apps on Cloud Foundry. It shows you how to set up and successfully deploy Spring Java PostgreSQL applications to Cloud Foundry.


Before you get started, you need the following:

+  A [Cloud Foundry account](http://cloudfoundry.com/signup)

+  The [vmc](/tools/vmc/installing-vmc.html) Cloud Foundry command line tool

+  A [Spring Tool Suite™ (STS)](http://www.springsource.org/spring-tool-suite-download) installation

+  A Cloud Foundry plugin for STS.


## Introduction

In this tutorial, we take the following user case.

Create and Deploy an Expense Reporting App on Cloud Foundry.

## Subtopics

+ [Create Spring MVC Template Project](#create-spring-mvc-template-project)
+ [ExpenseReport App](#expensereport-app)
+ [Add Spring Security to ExpenseReport App](#add-spring-security-to-expensereport-app)
+ [Build and Run the App Locally](#build-and-run-the-app-locally)
+ [Deploy the App on Cloud Foundry Using VMC](#deploy-the-app-on-cloud-foundry-using-vmc)
+ [Deploy the App on Cloud Foundry Using STS](#deploy-the-app-on-cloud-foundry-using-sts)



## Create Spring MVC Template Project

This section provides details on how to get started with STS (SpringSource Tool Suite) and Spring MVC.Please open your STS and select dashboard by clicking,
    Help -> Dashboard.

From the dashboard view select Spring Template Project.This will open like this.
![Spring MVC Template Project](/images/spring_tutorial/spring_template_project_mvc.png)


then choose `Spring MVC Project`.
The first time you do it, STS might ask you to download some extra elements from the Internet. 
![spring_template_project_mvc_download.png](/images/spring_tutorial/spring_template_project_mvc_download.png)

You’ll need to give your project a name and a top-level package:


Click on finish – and STS will create the project.

Before run the project.Select `Run As -> Maven clean`
![maven_clean.png](/images/spring_tutorial/maven_clean.png)

Once Maven clean completed select` Run As -> Maven install`.It will download dependencies from pom.xml.
![maven_install.png](/images/spring_tutorial/maven_install.png)

Select `Run As -> Maven build`
![maven_build.png](/images/spring_tutorial/maven_build.png)

And give Maven goal as , `tomcat:run`
![maven_run.png](/images/spring_tutorial/maven_run.png)


Once server starts completed,STS will open the home page for the project which displays Hello world message with the current server time.
![Hello World Output](/images/spring_tutorial/hello_world.png)


## ExpenseReport App

ExpenseReport is used to create and manage expenses.

In our expensereport application,we are using two roles.

1.   Normal User - can create expense,view status of expense,edit expense,delete expense.

2.   Admin User -  approve expense,reject expense which are created by normal user.

     Once Normal User submits his expense it will go to admin work items.Admin can approve or reject the expense.

![spring-expensereport-usecase.png](/images/spring_tutorial/usecase_diagram.png)

![spring-expensereport-usecase_admin.png](/images/spring_tutorial/usecase_diagram_admin.png)

These expenses are created by the users and the data
is stored in underlying PostgreSQl database.

The class diagram for our application is :

![spring-expensereport-class_diagram.png](/images/spring_tutorial/class_diagram.png)

Before start,add the following dependencies in your pom.xml,

  + postgresql-9.1-901.jdbc4.jar
  + hibernate-envers-3.5.0-Final.jar
  + commons-fileupload-1.2.1.jar
  + commons-io-1.3.jar

##Add Entities

   For our application we need to add Expense,User,UserRole,Attachment classes.Create the classes as a POJO with getters and setters for its properties
and annotate your business class with @Entity. By default JPA will take class name as table name.if you want to store in different table name then provide @Table annotation with name property. Create a package for entities as com.springsource.html5expense.model and add all entities.

```java
package com.springsource.html5expense.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "EXPENSE")
public class Expense implements Serializable{

	@Id
	@GeneratedValue
	private Long id;

	private String description;

	private Double amount;

	private State state = State.NEW;

	private Date expenseDate;

	@OneToOne
	private User user;

	@OneToOne
	private ExpenseType expenseType;

	@OneToOne
	@Cascade(CascadeType.DELETE_ORPHAN)
	private Attachment attachment;

	public Expense(){

	}

	public Expense(String description,ExpenseType expenseType,Date expenseDate,
			Double amount,User user,Attachment attachment){
		this.description = description;
		this.expenseType = expenseType;
		this.expenseDate = expenseDate;
		this.amount = amount;
		this.user = user;
		this.attachment = attachment;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ExpenseType getExpenseType() {
		return expenseType;
	}

	public void setExpenseType(ExpenseType expenseType) {
		this.expenseType = expenseType;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getExpenseDate() {
		return expenseDate;
	}

	public void setExpenseDate(Date expenseDate) {
		this.expenseDate = expenseDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Attachment getAttachment() {
		return attachment;
	}

	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}
}

```
```java
package com.springsource.html5expense.model;

import java.io.Serializable;
import java.sql.Blob;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.annotations.Type;

@Entity
public class Attachment implements Serializable{
	


	@Id
	@GeneratedValue
	private Long id;
	
	private String fileName;
	
	@Type(type="org.hibernate.type.BinaryType") 
	private byte[] content;
	
	private String contentType;
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileNmae() {
		return fileName;
	}

	public void setFileNmae(String fileNmae) {
		this.fileName = fileNmae;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public Attachment(){
		
	}
	public Attachment(String fileName,String contentType,byte[] content){
		this.content = content;
		this.contentType = contentType;
		this.fileName = fileName;
	}

}

```
```java
package com.springsource.html5expense.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class ExpenseReport {
	
	@Id
	@GeneratedValue
	private Long expenseReportId;
	
	
	@OneToOne
	private Expense expense;
	
	public  ExpenseReport(){
		
	}
	
	public Long getExpenseReportId() {
		return expenseReportId;
	}

	public void setExpenseReportId(Long expenseReportId) {
		this.expenseReportId = expenseReportId;
	}


	public Expense getExpense() {
		return expense;
	}

	public void setExpense(Expense expense) {
		this.expense = expense;
	}

	public  ExpenseReport(Expense expense){
		this.expense = expense;
	}

}

```

```java
package com.springsource.html5expense.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ExpenseType implements Serializable {

	@Id
	@GeneratedValue
	private Long id;

	private String name;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}


```

```java
package com.springsource.html5expense.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Role implements Serializable {

	@Id
	@GeneratedValue
	private Long roleId;
	
	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	private String roleName;
}


```
```java
package com.springsource.html5expense.model;

public enum State {

	NEW,
	
	OPEN,
	
	IN_REVIEW,
	
	REJECTED,
	
	APPROVED
}

```
```java
package com.springsource.html5expense.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="USERS")
public class User implements Serializable{

	@Id
	@GeneratedValue
	private Long userId;

	private String userName;

	private String password;

	private String emailId;

	private boolean enabled;

	@OneToOne
	private Role role;

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public User(){

	}
	public User(String userName,String password,String mailId){
		this.userName = userName;
		this.password = password;
		this.emailId = mailId;
	}
}
```

##Create controller classes

   Controller is reponsible for mapping the request.create methods to map the URL request. Create a package for controllers as com.springsource.html5expense.controller and add the following controller classes.

```java
package com.springsource.html5expense.controller;

import java.security.Principal;
import java.sql.Blob;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.springsource.html5expense.model.Attachment;
import com.springsource.html5expense.model.Expense;
import com.springsource.html5expense.model.ExpenseType;
import com.springsource.html5expense.model.User;
import com.springsource.html5expense.service.AttachmentService;
import com.springsource.html5expense.service.ExpenseService;
import com.springsource.html5expense.service.ExpenseTypeService;
import com.springsource.html5expense.service.RoleService;
import com.springsource.html5expense.service.UserService;

@Controller
public class ExpenseController {


	@Autowired
	ExpenseService expenseService;

	@Autowired
	AttachmentService attachmentService;

	@Autowired
	ExpenseTypeService expenseTypeService;

	@Autowired 
	UserService userService;

	@Autowired
	RoleService roleService;

	public ExpenseService getExpenseService() {
		return expenseService;
	}

	public void setExpenseService(ExpenseService expenseService) {
		this.expenseService = expenseService;
	}

	public AttachmentService getAttachmentService() {
		return attachmentService;
	}

	public void setAttachmentService(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}

	public ExpenseTypeService getExpenseTypeService() {
		return expenseTypeService;
	}

	public void setExpenseTypeService(ExpenseTypeService expenseTypeService) {
		this.expenseTypeService = expenseTypeService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public RoleService getRoleService() {
		return roleService;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}


	@RequestMapping(value="/loadApprovalExpenses" ,method = RequestMethod.GET)
	public String loadApprovedExpenses(ModelMap model){
		List<Expense> approvedExpenseList = getExpenseService().getPendingExpensesList();
		model.addAttribute("approvedExpenseList", approvedExpenseList);
		return "expenseapproval";

	}

	@RequestMapping(value="/deleteExpense",method=RequestMethod.GET)
	public String deleteExpense(HttpServletRequest request){
		String expenseId = request.getParameter("expenseId");
		Expense expense = getExpenseService().getExpense(new Long(expenseId));
		getExpenseService().deleteExpense(new Long(expenseId));
		return "redirect:/";
	}

	@RequestMapping(value="/editExpense",method=RequestMethod.GET)
	public String editExpense(HttpServletRequest request){
		String expenseId = request.getParameter("expenseId");
		Expense expense = getExpenseService().getExpense(new Long(expenseId));
		request.setAttribute("expense",expense);
		List<ExpenseType> expenseTypeList = expenseTypeService.getAllExpenseType();
		request.setAttribute("expenseTypeList", expenseTypeList);
		request.setAttribute("isEdit", "true");
		return "newexpense";
	}

	@RequestMapping(value="/changeState",method=RequestMethod.POST)
	public String changeState(HttpServletRequest request){
		String expenseId = request.getParameter("expenseId");
		String action = request.getParameter("action");
		getExpenseService().changeExpenseStatus(new Long(expenseId), action);
		List<Expense> approvedExpenseList = getExpenseService().getPendingExpensesList();
		request.setAttribute("approvedExpenseList", approvedExpenseList);
		return "expenseapproval";
	}


	@RequestMapping(value="/updateExpense",method=RequestMethod.POST)
	public String updateExpense(HttpServletRequest request){
		String expenseId = request.getParameter("expenseId");
		Expense expense = getExpenseService().getExpense(new Long(expenseId));
		String description = request.getParameter("description");
		String amount = request.getParameter("amount");
		String expenseTypeVal = request.getParameter("expenseTypeId");
		System.out.println("expense Id "+expenseTypeVal);
		ExpenseType expenseType = getExpenseTypeService().getExpenseTypeById(new Long(expenseTypeVal));
		expense.setExpenseType(expenseType);
		getExpenseService().updateExpense(new Long(expenseId), description, new Double(amount),expenseType);
		User user = (User)request.getSession().getAttribute("user");
		List<Expense> pendingExpenseList = getExpenseService().getExpensesByUser(user);
		request.setAttribute("pendingExpenseList",pendingExpenseList);
		return "myexpense";
	}

	@RequestMapping(value="/createNewExpenseReport",method = RequestMethod.POST)
	public String createNewExpenseReport(@RequestParam("file") MultipartFile file,HttpServletRequest request){
		String description = request.getParameter("description");
		String expenseTypeVal =request.getParameter("expenseTypeId");
		ExpenseType expenseType = expenseTypeService.getExpenseTypeById(new Long(expenseTypeVal));
		String amount = request.getParameter("amount");
		Date expenseDate = new Date();
		Double amountVal = new Double(amount);
		User user = (User)request.getSession().getAttribute("user");
		String fileName = "";
		String contentType = "";
		if(file!=null){
			try{
                            fileName =file.getOriginalFilename();
                            contentType = file.getContentType();
                            Attachment attachment = new Attachment(fileName, contentType,  file.getBytes());
                            attachmentService.save(attachment);
                            Long id = expenseService.createExpense(description,expenseType,expenseDate,
    				amountVal,user,attachment);
			}catch(Exception e){

			}
		}
		return "redirect:/";
	}

	@RequestMapping(value="/createNewExpense")
	public String createNewExpense(ModelMap model){

		List<ExpenseType> expenseTypeList = expenseTypeService.getAllExpenseType();
		model.addAttribute("expenseTypeList", expenseTypeList);
		return "newexpense";
	}

}
```
```java
package com.springsource.html5expense.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Blob;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.springsource.html5expense.model.Attachment;
import com.springsource.html5expense.service.AttachmentService;
import com.springsource.html5expense.service.ExpenseService;



@Controller
public class FileAttachmentController {

	    @Autowired
	    private ExpenseService expenseService;

	    @Autowired
	    private AttachmentService attachmentService;

	    @RequestMapping(value = "/save", method = RequestMethod.POST)
	    public String save(@RequestParam("file") MultipartFile file) {
	        try {

	            String fileName =file.getOriginalFilename();
	            String contentType = file.getContentType();
	            Attachment attachment = new Attachment(fileName, contentType, file.getBytes());
	            attachmentService.save(attachment);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return "redirect:/index.html";
	    }

	    @RequestMapping(value="/download")
	    public void download(HttpServletRequest request,HttpServletResponse response) throws Exception{
	    	String attachmentId = request.getParameter("attachmentId");
	    	         Attachment  attachment = this.attachmentService.getAttachment(new Long(attachmentId));
	    	         response.setContentType(attachment.getContentType());
	    	         response.setHeader("Content-Disposition","attachment; filename=\"" + attachment.getFileName() +"\"");
	    	         response.setHeader("cache-control", "must-revalidate");
	    	         OutputStream out = response.getOutputStream();
	    	         out.write(attachment.getContent());
	    	         out.flush();

	    }
}
```

```java

```

##Add Service Interfaces

   Add ExpenseService interface which is responsible to fetch pending expenses,approved expenses,create expense,fetch expenses based on user,update an expense.Create JpaExpenseServiceImpl Class that implements this ExpenseService interface and do your implementation to get the data from database and annotate this class as @Service and @Transactional. @Service is a special component that marks service layer.Since we are using JPA for persistence, autowire your EntityManager.

```java
package com.springsource.html5expense.service;

import java.util.List;

import com.springsource.html5expense.model.Attachment;

public interface AttachmentService {
	public void save(Attachment attachment);

	public Attachment getAttachment(Long attachmentId);

	public List<Attachment> getAttachmentByExpenseId(Long expenseId);

	public void deleteAttachment(Long id);
}
```
```java
package com.springsource.html5expense.service;

import java.util.List;

import com.springsource.html5expense.model.Expense;
import com.springsource.html5expense.model.ExpenseReport;

public interface ExpenseReportService {

	public Long createExpenseReport(Expense expense);

	public ExpenseReport getExpenseReportById(Long expensReportId);

	public List getAllExpenseReports();
}
```
```java
package com.springsource.html5expense.service;

import java.util.Date;
import java.util.List;

import com.springsource.html5expense.model.Attachment;
import com.springsource.html5expense.model.Expense;
import com.springsource.html5expense.model.ExpenseType;
import com.springsource.html5expense.model.User;

public interface ExpenseService {

	public Long createExpense(String description,ExpenseType expenseType,Date expenseDate,
			Double amount,User user,Attachment attachment);

	public Expense getExpense(Long expenseId);

	public List getAllExpenses();

	public List<Expense> getExpensesByUser(User user);

	public List<Expense> getPendingExpensesList();

	public List<Expense> getApprovedAndRejectedExpensesList();

	public Expense changeExpenseStatus(Long expenseId,String state);

	public void deleteExpense(Long expenseId);

	public void updateExpense(Long expenseId,String description,Double amount,ExpenseType expenseType);
}
```
```java
package com.springsource.html5expense.service;

import java.util.List;

import com.springsource.html5expense.model.ExpenseType;

public interface ExpenseTypeService {

	public List<ExpenseType> getAllExpenseType();

	public ExpenseType getExpenseTypeById(Long id);
}
```
```java
package com.springsource.html5expense.service;

import com.springsource.html5expense.model.Role;

public interface RoleService {

	public Role getRole(Long id);

	public Role getRoleByName(String name);
}
```
```java
package com.springsource.html5expense.service;

import com.springsource.html5expense.model.Role;
import com.springsource.html5expense.model.User;

public interface UserService {

	public User getUserById(Long id);

	public User getUserByUserName(String userName);

	public User createUser(String userName,String password,String mailId,Role role);

}
```

   
##Configuring Spring application:

 To make our application ready we have to configure our application. Create a class with @Configuration annotation to configure your beans.It is a alternative approach to bean definition instead of xml configuration.We can pass this class a argument to spring container as a source for bean creation. To declare a bean, simply annotate a method with the @Bean annotation in your config class. You use this method to register a bean definition within an ApplicationContext of the type specified as the method's return value. By default, the bean name will be the same as the method name.

```java
package com.springsource.html5expense.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.springsource.html5expense.controller.FileAttachmentController;
import com.springsource.html5expense.controller.LoginController;
import com.springsource.html5expense.model.Attachment;
import com.springsource.html5expense.model.Expense;
import com.springsource.html5expense.service.ExpenseService;
import com.springsource.html5expense.serviceImpl.JpaExpenseServiceImpl;
import com.springsource.html5expense.serviceImpl.JpaRoleServiceImpl;

@Configuration
@EnableTransactionManagement

public class ComponentConfig {


	@Bean
    public DataSource dataSource()  {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setUrl(String.format("jdbc:postgresql://%s:%s/%s", "localhost", "5432", "postgres"));
        dataSource.setDriverClass(org.postgresql.Driver.class);
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres");
        return dataSource;
    }

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
	    LocalContainerEntityManagerFactoryBean emfb = new LocalContainerEntityManagerFactoryBean();
	    emfb.setJpaVendorAdapter( jpaAdapter());
	    emfb.setDataSource(dataSource());
	    emfb.setJpaPropertyMap(createPropertyMap());
	    emfb.setJpaDialect(new HibernateJpaDialect());
	    emfb.setPersistenceUnitName("sample");
	    emfb.setPackagesToScan(new String[]{Expense.class.getPackage().getName()});
	    return emfb;
	}

	public Map<String,String> createPropertyMap()
	{
		Map<String,String> map= new HashedMap();
		map.put(org.hibernate.cfg.Environment.HBM2DDL_AUTO, "create");
		map.put(org.hibernate.cfg.Environment.HBM2DDL_IMPORT_FILES, "import.sql");
		map.put("hibernate.c3p0.min_size", "5");
		map.put("hibernate.c3p0.max_size", "20");
		map.put("hibernate.c3p0.timeout", "360000");
		map.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
		return map;

	}

	@Bean
	public JpaVendorAdapter jpaAdapter() {
	    HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
	    hibernateJpaVendorAdapter.setShowSql(true);
	    hibernateJpaVendorAdapter.setDatabase(Database.POSTGRESQL);
	    hibernateJpaVendorAdapter.setShowSql(true);
	    hibernateJpaVendorAdapter.setGenerateDdl(true);
	    return hibernateJpaVendorAdapter;
	}


	@Bean
	public PlatformTransactionManager transactionManager() {
	    final JpaTransactionManager transactionManager =
	        new JpaTransactionManager();

	    transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
	    Map<String,String> jpaProperties = new HashMap<String,String>();
	    jpaProperties.put("transactionTimeout","43200");
	    transactionManager.setJpaPropertyMap(jpaProperties);

	    return transactionManager;
	}

	@Bean
	public MultipartResolver multiPartResolver(){
		CommonsMultipartResolver multiPartResolver = new org.springframework.web.multipart.commons.CommonsMultipartResolver();
		multiPartResolver.setMaxUploadSize(10000000);
		return multiPartResolver;
	}

}
```
```java
package com.springsource.html5expense.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.springsource.html5expense.controller.ExpenseController;
import com.springsource.html5expense.model.Expense;
import com.springsource.html5expense.serviceImpl.JpaExpenseServiceImpl;


@Configuration
@Import(ComponentConfig.class)
@PropertySource("/config.properties")
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter
 {
     @Override
     public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {

     }

    @Value("${debug}")
    private boolean debug;

    
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public MessageSource messageSource() {
        String[] baseNames = "messages,errors".split(",");
        ResourceBundleMessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
        resourceBundleMessageSource.setBasenames(baseNames);
        return resourceBundleMessageSource;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {

    }


}
```

 
To get Spring Transaction support,annotate your class with @EnableTransactionManagement which enables Spring's annotation-driven transaction management capability, similar to the support found in Spring's <tx:*> XML namespace. 


Then configure the components using @ComponentScan which Configures component scanning directives for use with @Configuration classes. Provides support parallel with Spring XML's <context:component-scan> element and pass one of basePackageClasses(), basePackages() value.


Then modify your web.xml to load this Config class as part of your contextConfiglocation or create your custom class to initialize ApplicationContext by implementing WebApplicationInitializer interface.Override onStartUp() method of WebApplicationInitializer and create AnnotationConfigWebApplicationContext.
```java
		AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
		applicationContext.setServletContext(sc);
		applicationContext.register(AppConfig.class);
		applicationContext.refresh();
```


##Add Spring Security to ExpenseReport App

This section provides how to get integrate ExpenseReport App with Spring Security.
Before start,add the following dependencies in your pom.xml,

  + spring-security-core.jar
  + spring-security-web.jar
  + spring-security-config.jar

## Enable Spring Security:

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


To load this file as part of configuration add @ImportResource({ "classpath:spring-security.xml"}). in ComponentConfig class.

create custom login.jsp with two input elements for username and password as j_username and j_password.These are spring's placeholder for the username and password respectively.

when the form is submitted ,it will be sent to the following action URL:
j_spring_security_check.

Add logout functionality:
   add j_spring_security_logout link in your application. once your clicked this link,spring invalidate user session.



##Build and Run the App Locally
 
      Now we have successfully created expensereport application.we can run the application in locally to test it.STS has Fabric tc server,select and add your project to server now click finish.
![STS Fabric Server.png](/images/spring_tutorial/localhost_login.png)


Once server starts completed,STS will open the home page for the project which dispalys login page.

![spring-expensereport-login.png](/images/spring_tutorial/localhost_login.png)

![spring-expensereport-create_expense.png](/images/spring_tutorial/create_new_expense.png)

![spring-expensereport-user_expense.png](/images/spring_tutorial/my_expense.png)


##Deploy the App on Cloud Foundry Using VMC
Deployment to Cloud Foundry can be done in two simple steps:

+  Create war file
+  Push the war file using `vmc push`

Commands below show the exact commands and output when the app is deployed:

Go to your project work space and issue the following commands.

``` bash
$ mvn war:war
[INFO] Scanning for projects...
[INFO]                                                                         
[INFO] ------------------------------------------------------------------------
[INFO] Building SpringMVC-ExpenseReport 1.0.0-BUILD-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[INFO] 
[INFO] --- maven-war-plugin:2.1.1:war (default-cli) @ html5expense ---
[INFO] Packaging webapp
[INFO] Assembling webapp [html5expense] in [/home/senthils/Code/From Home/SpringMVC-ExpenseReport/target/html5expense-1.0.0-BUILD-SNAPSHOT]
[INFO] Processing war project
[INFO] Copying webapp resources [/home/senthils/Code/From Home/SpringMVC-ExpenseReport/src/main/webapp]
[INFO] Webapp assembled in [380 msecs]
[INFO] Building war: /home/senthils/Code/From Home/SpringMVC-ExpenseReport/target/html5expense-1.0.0-BUILD-SNAPSHOT.war
[INFO] WEB-INF/web.xml already added, skipping
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 4.443s
[INFO] Finished at: Mon Sep 17 15:57:50 IST 2012
[INFO] Final Memory: 6M/124M
[INFO] ------------------------------------------------------------------------

$ vmc push
Would you like to deploy from the current directory? [Yn]: /home/senthils/.rvm/gems/ruby-1.9.2-head/gems/interact-0.4.8/lib/interact/interactive.rb:569: warning: Insecure world writable dir /home/senthils/Downloads/springsource in PATH, mode 040777

Application Name: html5expense
Detected a Java SpringSource Spring Application, is this correct? [Yn]: Y
Application Deployed URL [html5expense.cloudfoundry.com]: 
Memory reservation (128M, 256M, 512M, 1G, 2G) [512M]: 128M
How many instances? [1]: 
Bind existing services to 'html5expense'? [yN]: 
Create services to bind to 'html5expense'? [yN]: Y
1: mongodb
2: mysql
3: postgresql
4: rabbitmq
5: redis
What kind of service?: 3
Specify the name of the service [postgresql-94876]: 
Create another? [yN]: 
Would you like to save this configuration? [yN]: 
Creating Application: OK
Creating Service [postgresql-94876]: OK
Binding Service [postgresql-94876]: OK
Uploading Application:
  Checking for available resources: OK
  Processing resources: OK
  Packing application: OK
  Uploading (10K): OK   
Push Status: OK
Staging Application 'html5expense': OK                                          
Starting Application 'html5expense': OK
```

##Deploy the App on Cloud Foundry Using STS

1.     Go to servers view,right click add New Server.Now you can see Cloud Foundry under VMware.Select Cloud Foundry,then click next.It will ask your account information and Choose VMware Cloud Foundry - http://api.cloudfoundry.com from the URL.
![spring-expensereport-login.png](/images/spring_tutorial/cloud_foundry.png)

2.     Now give your Cloud Foundry account information.Click validate and make sure you have entered  valid credentials.
![cloud foundry account information.png](/images/spring_tutorial/cloud_foundry_account.png)

3.     Click next it will show you add and remove view.Now you can select our spring application.Once you added your application it will open new window with application name and application type.You can enter your application name.
![spring-expensereport-login.png](/images/spring_tutorial/cloud_foundry_project_deploy.png) 
![spring-expensereport-project-deploy.png](/images/spring_tutorial/project_deploy_step2.png)


4.     Click next you can see deployed URL(your application name+cloudfoundry.com) and memory reservation.by default memory reservation selected to 512M.You can increase memory if required..
![spring-expensereport-project-deploy-step-2.png](/images/spring_tutorial/project_deploy_step3.png)

5.     Click next now it will ask for services selection.Since we are using postgresql in our application we need to add postgresql service.Click add services to server it will open service configuration window.select type as Postgre SQL database service(VFabric) and give name to this service then click 
![service_selection.png](/images/spring_tutorial/service_selection.png)
![create_new_service.png](/images/spring_tutorial/create_new_service.png)

6.     Now you can see your services in services selection window.Select postgresql service and click finish.Now server starts to deploy our application into Cloud Foundry.
![spring-expensereport-login.png](/images/spring_tutorial/service_selection_1.png)



Upon completion of deployment, we can go and visit the actual app at the URL `[app-name].cloudfoundry.com]`

![deployed_application_in_cloud_foundry.png](/images/spring_tutorial/deployed_application_in_cloud_foundry.png)



