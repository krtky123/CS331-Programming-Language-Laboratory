Name - Kartikay Goel
Roll Number - 180101033

Email ID: kartikay@iitg.ac.in

Commands to run the code-:
1. javac BankingSystem.java
2. java BankingSystem

The code will take approximately 1 minute to show the fianl output.
The account number is of 10 digits(represents first branch of GNB if the account number is of 9 digits). The first digit of account number represents branch number of the GNB.
Time for each transaction = computation time in linked list = size of linked list = users in each branch
So, time for each transaction = 10^4
number of transactions per updater = 10^5
Total time = O(10^5 * 100 * 10^4) = O(10^11)
