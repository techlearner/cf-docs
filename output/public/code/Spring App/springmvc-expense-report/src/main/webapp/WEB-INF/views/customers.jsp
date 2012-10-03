<!doctype html>
<html ng-app>
<head>

    <script src="<c:url value="resources/javascript/jquery-1.8.1.js"/>" ></script>
	<script src="<c:url value="resources/javascript/angular-1.0.0rc6.js"/>" ></script>
	<script src="<c:url value="resources/javascript/expense.js"/>" ></script>
</head>
<body>
 <script language = "javascript" type = "text/javascript">
     <!--
		 $(function(){
		  utils.setup( '${context}'); 
		})
 	   //   utils.setup( '${context}');
    //-->
    </script>
<h2>Customer Data </h2>

<div ng-controller="ExpenseCtrl">
    <div>
        <form class="well form-search" ng-submit="lookupExpense()">
            <label> Search by ID</label>
            <input type="text" ng-model="id" class="input-medium search-query" width="5" size="5" placeholder="expense #">
            <button type="submit" class="btn btn-primary" ng-click="lookupCustomer()" >
                <a class="icon-search"></a>
            </button>
        </form>
    </div>

    <form class="form-horizontal" ng-submit="updateExpense">
        <fieldset>
            <legend>
                <span class="customer-visible-{{!isExpenseLoaded()}}"> Create New Customer </span>
                <span class="customer-visible-{{isExpenseLoaded()}}"> Update {{expense.amount}} {{expense.description}} - {{expense.id}} </span>
            </legend>
            <div class="control-group">
                <label class="control-label" for="fn">First Name:</label>
                <div class="controls">
                    <input class="input-xlarge" id="fn" type="text" ng-model="expense.description" placeholder="description"  required="required"/>
                    <p class="help-block">Change the first name</p>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="ln">Last Name:</label>
                <div class="controls">
                    <input class="input-xlarge" id="ln" type="text" ng-model="expense.amount" placeholder="amount"  required="required"/>
                    <p class="help-block">Change the last name</p>
                </div>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-primary" ng-click="save()" ng-model-instant>
                    <a class="icon-plus"></a> Save
                </button>
                <button class="btn " ng-click="trash()"><a class="icon-trash"></a> Cancel</button>
            </div>
        </fieldset>
    </form>
</div>
</body>
</html>