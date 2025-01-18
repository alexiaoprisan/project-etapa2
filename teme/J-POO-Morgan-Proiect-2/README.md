#### Oprișan Alexia-Ioana, 324CA

# Project Assignment POO - J. POO Morgan - Phase One

### READ ME

## Project Description
The project implements a **banking system** that simulates the basic functionalities of a bank.
The system enables the creation of users, each of whom can have multiple accounts and cards.
Users are able to perform various operations, including:
- Making online payments
- Transferring money to other users
- Paying an amount of money split between multiple users

## Project Structure
The project includes a **BankingManager** responsible for processing commands and storing data
for users, as well as handling input related to exchange rates. The commands are processed
using a **CommandFactory**, which creates the appropriate command object based on the input
command name. The classes used to execute the commands implement the **Command** interface.

Design patterns used in the project:
- **Singleton** design pattern for the UserRegistry, CommerciantRegistry and 
ExchangeRatesRegistry classes
- **Factory** design pattern for creating accounts and cards
- **Strategy** design pattern for the cashback and checking the saved discounts to apply them
- **Command** design pattern for executing commands

### The user registry

The **UserRegistry** class is responsible for managing a collection of users within the banking
system. It follows the **Singleton design pattern** to ensure that only one instance of the
registry exists throughout the application. This class provides various methods for adding,
retrieving and searching users, accounts and cards, which will help in executing the commands.

The **User** class represents a user within the system, including their personal information,
accounts and transaction history. A user can own multiple accounts and each account may have
associated cards. This class offers methods for managing user details, including first name,
last name and email, as well as handling accounts and transactions.

### The account implementation

The **Account** interface defines the essential operations and properties for managing a bank
account. It includes methods to get details: the account's currency, type, IBAN, balance,
minimum balance, alias and associated cards. It also allows creating new cards, retrieving a
card by its number and managing transactions linked to the account.

The account can be of two types: **Savings Account** and **CLassic Account**.

##### Classic Account
The **ClassicAccount** class represents a standard bank account. It includes basic fields and
is designed for general usage, offering functionalities like adding cards, retrieving card
details and tracking transactions related to the account. It also maintains a list of
**Commerciants**, which will be used for creating account reports and a payments record for
online transactions.

##### Savings Account
The **SavingsAccount** class represents a bank account specifically designed for saving money
and earning interest. Similar to the ClassicAccount, it has fields like currency, account type,
IBAN, balance and minimum balance. However, the SavingsAccount includes an additional
field—**interestRate**—which determines the rate at which interest is earned on the account
balance.
This class also features a **SavingsReport**, which tracks transactions related to interest
earnings or changes in interest.

#### Business Account
The **BusinessAccount** class is a specialized bank account designed for businesses, 
offering shared access and streamlined financial management.
The account has an owner, which creates the account and can make several operations on it,
like adding associates, setting limits for the sum of money that can be spent or deposited
or for the minimum balance.

Users Roles for the business accounts:
* Owner: Full control over account settings, user management, and limits. Cannot hold other roles.
* Manager: Can perform all transactions, manage any card, but cannot modify limits or manage associates.
* Employee: The employee is authorized to perform payments using either a card or bank transfer, 
with an initial limit of 500 RON. They can also deposit funds into the business account, 
subject to the same 500 RON limit. Additionally, the employee has the ability to
create cards and delete them, but only if they are the creator of the respective cards. 
They are permitted to make payments using any type of card.

Each Business Account stores information about the owner and associates in two 
separate lists: one for managers and another for employees. Additionally, there are
hashmaps that track spending and deposits made by both managers and employees, 
aiding in the generation of detailed reports. Furthermore, a list of commerciants 
is maintained to monitor payments processed through the account.

Accounts are created using the **AccountFactory** class (using the factory design pattern),
which provides a static createAccount method. 
This method takes the account type (classic or savings) and other necessary details and
returns an instance of the appropriate account type.

### The card implementation
The **Card** interface defines the basic structure for a card, including methods to get and set
the card number, type and status. The card type can be either "regular" or "oneTimeCard," and
the status can be "active," "warning," or "frozen."

The **"one-time card"** is designed to be used only once for a transaction. After the transaction,
it is deleted and a new card is created to replace it.

The **CardFactory** class is responsible for creating instances of different card types,
"regular" or "oneTimePay." It ensures that only the specified types of cards are created and that
it cannot be instantiated. The **createCard** method generates the
appropriate card based on the type provided (either a one-time use or a regular card).

### Service Plans
The service plan system offers four options: Standard (0.2% commission on all transactions), 
Student (no commission), Silver (no commission for transactions under 500 RON, 0.1% fee above)
and Gold (no commission on any transactions). Users can upgrade to a higher plan with a one-time 
fee: 100 RON for Standard/Student to Silver, 250 RON for Silver to Gold
and 350 RON for Standard/Student to Gold. Silver to Gold upgrades are automatic after 
five transactions of 300 RON each. Once a user upgrades, all their associated accounts 
benefit from the new plan. 


## Command Descriptions

### Print Users
The `printUsers` command prints the list of users saved in the user registry. It displays the
data for each user, their accounts and cards, in json format.

### Create an account for an user
The `addAccount` command creates a new account for a user. The user must provide their email,
the account type (classic or savings) and its currency. The new account will be added to the
user's account list.

### Delete a User's Account
The `deleteAccount` command deletes a user account. The account needs to have the balance equal
to 0 in order to be deleted, otherwise an error message will be displayed.

### Create a Card Associated with an Account
The `createCard` or `createOneTimeCard` command creates a regular or one-time-use card linked
to a specified account. The user must provide their email and the account's IBAN. The card
number is generated and the card will be saved in the account's card list.

### Delete a Card
The `deleteCard` command removes a card from the system, making it unusable for future transactions.
This action is recorded in the account's transaction history.

### Add Funds to an Account
The `addFunds` command adds a specified amount of money to an account identified by its IBAN.

### Set a Minimum Balance for an Account
The `setMinimumBalance` command allows the user to set a minimum balance for their account. If the
balance falls below this amount, payments may be blocked and the card may be frozen.

### Check Card Status
The `checkCardStatus` command checks the status of a card, which can be "active," "warning"
or "frozen." The status is determined based on the card's balance and the account's minimum balance.

### Pay Online
The `payOnline` command allows the user to make an online payment with their card. The command
includes the card number, amount, currency, description and the commerciant receiving the payment.
In order to complete the transaction, the card must be active and have sufficient funds.
The account can pay in a different currency than the one it has, in which case the amount will be
converted using the exchange rate. It is also needed to check if the card is a one-time card, so
it will be deleted after the transaction and replaced with a new one.

### Bank Transfer
The `sendMoney` command facilitates transferring money between two accounts. The accounts
can have different currencies, in which case the amount will be converted using the exchange rate.
The transaction is recorded in the sender's and receiver's transaction history.

#### Cashback

Both the `payOnline` and `sendMoney` have an additional functionality: the `cashback`.
The commerciants implement a cashback strategy where 
the more you spend, the more you get back. Cashback is calculated separately for each 
account. There are two types of cashback plans available for commerciants: 
**nrOfTransactions** and **spendingThreshold**.

##### nrOfTransactions Cashback
This type of cashback is cumulative. Users earn a percentage based on the number of 
transactions they make at certain types of commerciants (Food, Clothes, Tech).
After 2 transactions at a Food commerciant, the user will receive 2% cashback 
on the next transaction at a commerciant in the Food category. After 5 transactions at 
a Clothes commerciant, the user will receive 5% cashback on the next transaction 
at a commerciant in the Clothes category. After 10 transactions at a Tech commerciant, 
the user will receive 10% cashback on the next transaction at a commerciant 
in the Tech category. Once a cashback is received for a commerciant type, 
no further cashback will be granted.

##### spendingThreshold Cashback
This plan is based on the total amount spent. Users are eligible for cashback 
based on their service plan (standard, student, silver, or gold). 
After spending a certain amount (100, 300 or 500 RON) at qualifying commerciants, 
users will receive cashback as a percentage of their current transaction.

For the cashback implementation, I applied the Strategy design pattern to handle cashback logic. 
The CashbackStrategy interface defines the applyCashback() method, which is implemented 
by strategies to handle different cashback conditions based on commerciant type or 
transaction thresholds. The CashbackManager class manages and applies these strategies 
dynamically with setStrategy() and applyCashback(). 

In the discounts implementation, I used a strategy pattern to apply various discounts
based on the type of commerciant and the user's spending behavior. 
The DiscountStrategy interface defines the method applyDiscount(), 
which is implemented by different strategies such as FoodDiscountStrategy, TechDiscountStrategy,
ClothesDiscountStrategy, and SpendingThresholdDiscountStrategy. 
The DiscountStrategyFactory class dynamically provides the appropriate discount strategy 
based on the commerciant type (e.g., food, clothes, tech) or the spending threshold. 
The discounts are stored in a list in each account.
Each strategy applies its respective discount to the user's account balance based on the amount spent.

### Set an Alias for an Account
The `setAlias` command assigns a name (alias) to an account's IBAN. This alias can be used to
identify the account more easily in future transactions (when transferring money).

### Upgrade Service Plan
The `upgradePlan` command allows the user to upgrade their service plan to a higher tier.
The user must pay a one-time fee to upgrade to a new plan: 100 RON for Standard/Student to Silver,
250 RON for Silver to Gold and 350 RON for Standard/Student to Gold. Silver to Gold upgrades
are automatic after five transactions of 300 RON each.

### Split Payment
The `splitPayment` command can be of two types: `equal` or `custom`.

The `equal split payment` command divides a payment equally among multiple accounts. The accounts
can have different currencies, in which case the amount needs to be converted using the exchange rate.
If one account has insufficient funds, the payment will be canceled and an error message will be displayed
for all the users involved in the transaction. When this command is made, a split payment is saved 
in the split payments registry, to wait for all the users implicated to accept the payment. 

The command `acceptSplitPayment` is used to accept a split payment by a user. In this case,
it needs to be checked if all the users involved in the split payment have accepted it. If so,
only then is the payment made. A user can refuse the payment, this can be done using the command
`rejectSplitPayment`. Then, the payment will be canceled and removed from the split payments registry,
and an error message will be displayed for all the users involved in the transaction. 

The `custom split payment` command allows the user to specify the amount to be paid by each account.
The logic is the same as for the equal split payment, but in this case, the amount is not divided equally.
Each user can accept or reject the payment, and the payment will be made only if all the users accept it.

### Add Interest
The `addInterest` command adds interest to a savings account based on the interest rate.

### Change the Interest Rate
The `changeInterestRate` command updates the interest rate of a savings account.

### Withdraw Savings
The `withdrawSavings` command allows the user to withdraw money from a savings account.
This functionality allows users who have both a savings account and at least one classic 
account to transfer funds from the savings account to the classic account in the specified currency. 
To ensure responsible financial management, a minimum age of 21 years is required for 
users to be eligible for withdrawals from a savings account.

### Cash Withdrawal
The `cashWithdrawal` command allows users to withdraw cash from an ATM, 
The withdrawal process requires the user to specify their card number, 
the amount of money they wish to withdraw, their email address, the location of the ATM
and the timestamp of the transaction. The user will have a comission for the withdrawal,
depending on the account plan of the user (standard, student, silver or gold).

### Generate Transaction Reports
The `printTransactions` command generates a transaction report for a specified user.
The report includes all the actions made by the user, such as payments, transfers, created cards
and account operations. The report is displayed in json format.

The **Transaction** class is an abstract base class designed to represent a general transaction.
It includes common properties such as timestamp and description, which are shared across
all transaction types. Specific transaction types extend this class and implement their own
version of the "toJson()" method to convert transaction details into JSON format.

The **TransactionReport** class represents a report that contains a list of transactions for the user.
It is used to generate and print reports of transactions, when the "printTransactions"
command is executed.

### Classic Report
The command `report` generates a classic report for a specified account.
The **Classic Report** is available for a classic account and for a savings account.
For the classic account, the report contains information about the account's transactions and
the class ClassicReport is used to store information about the transactions and to print
the report between two timestamps.
For a savings account, the report contains information about the account's changes in interest
and earnings and the class SavingsReport is used to store information.

### Spending Report
The command `spendingsReport` generates a spending report for a specified account.
The **Spendings Report** is saved in a classic account and contains information about the account's
transactions, including the total amount spent at each commerciant and the corresponding transactions.
At the same time, a **Commerciant** class is used to store information about the commerciant, such as
the name and the total amount spent by the account at that commerciant. The list of commerciants is
used to generate the classic report.

### Business Report
The command `businessReport` generates a business report for a specified business account.
The report can be of two types: `spendings` or `deposits`.

The **Transaction report** provides a detailed summary of the financial activity 
for each manager and employee in the business account. For each individual, 
the report will display their name, the total amount spent, and the total 
amount deposited into the account. The report organizes users by their role, with 
manager details appearing first, followed by employee information. 
This report is particularly useful for tracking expenses and deposits a
ssociated with each user. Fow generating the report, the class **BusinessTransactionReport** 
is used and the report is saved in the business account.

The **Commerciant report** focuses on tracking payments made to specific commerciants. 
For each commerciant, the report displays the total amount spent at that commerciant
and lists the managers and employees who made the payments. This helps to identify where 
money is being spent and by whom, allowing for better management and analysis 
of commerciant-related transactions. Commerciants, managers and employees are 
listed in alphabetical order to facilitate easy reference. The class **BusinessCommerciantReport** is 
used to generate the report and the report is saved in the business account.
