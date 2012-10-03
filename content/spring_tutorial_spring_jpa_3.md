---
title: Spring Application Developement Tutorial
description: Create Domain Objects,Services,Controller
tags:
    - Spring MVC
    - Spring Application Configuration
    - JPA

---

This section provides how to add domain class,controllers and data accessing using JPA.
Before start,add the following dependencies in your pom.xml,
  + postgresql.jar
  + spring-jdbc.jar
  + spring-orm.jar
  + spring-tx.jar

## Use Case for ExpenseReport Application.

    In our expensereport application,we are using two roles.
1.   Noraml User - can create expense,view status of expense,edit expense,delete expense.

2.   Admin User - approve expense,reject expense which are created by normal user.

     Once Normal User submitted his expense it will go to admin work items.Admin can approve or reject the expense.

Find below a class diagram of our Expense Report application

![class-diagram.png](/images/spring_tutorial/class_diagram.png)

## Add business class

      For our application we need to create Expense,User,UserRole,Attachment classes.Create the classes as a POJO with getters and setters for its properties
and annotate your business class with @Entity.By default JPA will take class name as table name.if you want to store in different table name then provide @Table annotation with name property.

```java
@Entity
@Table(name = "Expenses")
public class Expense implements Serializable{
@Id
Long id;

String description;


}

```


## Create Controller Classes

    Controller is reponsible for mapping the request.create methods to map the URL request.

```java
@Controller
public class ExpenseController {
	

	@Autowired
	ExpenseService expenseService;
	
	public ExpenseService getExpenseService() {
		return expenseService;
	}

	public void setExpenseService(ExpenseService expenseService) {
		this.expenseService = expenseService;
	}

	@RequestMapping(value="/loadApprovalExpenses" ,method = RequestMethod.GET)
	public String loadApprovedExpenses(ModelMap model){
		List<Expense> approvedExpenseList = getExpenseService().getPendingExpensesList();
		model.addAttribute("approvedExpenseList", approvedExpenseList);
		return "4";
		
	}
	

```

## Create Service Classes

   Create ExpenseService interface which is responsible to fetch pending expenses,approved expenses,create expense,fetch expenses based on user,update an expense.Create JpaExpenseServiceImpl Class that implements this ExpenseService interface and do your implementation to get the data from database and annotate this class as @Service and @Transactional. @Service is a special component that marks service layer.Since we are using JPA for persistence, autowire your EntityManager.

```java
@Service
public class JpaExpenseServiceImpl implements ExpenseService {
	
	private EntityManager entityManager;
	
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Transactional
	public Long createExpense(String description,ExpenseType expenseType,Date expenseDate,
			Double amount,User user,Attachment attachment) {
		// TODO Auto-generated method stub
		Expense expense = new Expense(description,expenseType,expenseDate,amount,user,attachment);
		ExpenseReport report = new ExpenseReport(expense);
		entityManager.persist(expense);
		entityManager.persist(report);
		return expense.getId();
	}
}
```

   
##Configuring Spring application:

 To make our application ready we have to configure our application.Create a class with @Configuration annotation to configure your beans.It is a alternative approach to bean definition instead of xml configuration.We can pass this class a argument to spring container as a source for bean creation.

```java
@Configuration
public class AppConfig{
	
}
```

 To declare a bean, simply annotate a method with the @Bean annotation in your config class. You use this method to register a bean definition within an ApplicationContext of the type specified as the method's return value. By default, the bean name will be the same as the method name. The following is a simple example of a @Bean method declaration for DataSource bean:
```java
    @Bean
    public DataSource dataSource()  {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setUrl(String.format("jdbc:postgresql://%s:%s/%s", "localhost", 5432, "postgres"));
        dataSource.setDriverClass(org.postgresql.Driver.class);
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres");
        return dataSource;
    }
```
Then define bean for EntityManagerFactory and TransactionManager.

```java
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
	    LocalContainerEntityManagerFactoryBean emfb = new LocalContainerEntityManagerFactoryBean();
	    emfb.setJpaVendorAdapter( jpaAdapter())
	    return emfb;
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
	    final JpaTransactionManager transactionManager =
	        new JpaTransactionManager();
	    transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
	    transactionManager.setJpaPropertyMap(jpaProperties);
	    return transactionManager;
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

