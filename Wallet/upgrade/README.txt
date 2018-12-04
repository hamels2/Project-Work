To read javadocs go to upgrade/javadocs/index.html
Build instructions:
To build and run tests cd upgrade/wallet and run mvn package


Assumptions made:
One assumption I made was that the library should be able to store the data 
generated in a persistent manner. This is why sqlite and the
sql* classes are used.

Discussion:

Architecture:

I broke down my implementation of the requirements into three classes.
The Wallet class acts as a factory for creating accounts and storing the info into
the database. This enforces the relationship that every account has a wallet,
and cannot exist on its own. The Account class carries the info
about which wallet it comes from, what its balance is, and its transaction history.
The class is meant to act as a data structure so other than retrieving its info
it doesn't perform any business logic. The business logic is perfomed through the
transaction class. This class is a set of static methods that perform withdrawals, deposits
and transfers as well as recording these transactions into the account class. 
I find that this architecture really sticks well to the single responsibility principle.

SqlInsertManager and SqlQueryManager are two interfaces for creating classes that
interact with the database. By using these interfaces the database used and its internal
structure is decoupled from the library implementation. The Wallet class and Transaction class both
require these two interfaces in their methods/constructors.This allows developers to seemlessly 
use the library and have all the data they generate automatically be stored into a database.
Any and all accounts, wallets and transactions made using the library are stored
and are automatically accessed when creating their respective objects. 

SqlWalletInsert and SqlWalletquery are the two implementations of these interfaces. 
They were developed around the structure of the VirtualWallet database and were included
for the sake of showing a complete solution. Normally I wouldn't include these two classes 
in the library package since they're too specific to be of general use in a library, 
or I would have another class that specifically creates a database in the same structure that 
these two classes use. However, I have kept it as is for the sake of simplicity.



