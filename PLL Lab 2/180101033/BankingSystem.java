/*
Name - Kartikay Goel
Roll Number - 180101033
*/
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.StampedLock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.*;


public class BankingSystem extends ThreadSafeLinkedList
{
    public int stringToInt(String s)
    {
        return Integer.parseInt(s);
    }

    public long stringToLong(String s)
    {
        return Long.parseLong(s);
    }

    public String longToString(long a)
    {
        return Long.toString(a);
    }

    public String intToString(int a)
    {
        return Integer.toString(a);
    }

    // public static long N_USERS = 10;	//debugging
    // public static int numberOfTests = 5;	//debugging
    public static long N_USERS = 100000;	// number of users are 10^4 per branch
    // and there are 10 branches
    public static int numberOfTests = 10000000;	// 10^5 transactions per updater, number of updaters 
    // is 100 so number of tasks is 10^7
    public static long CashLeftLimit = 100L, CashRightLimit = 100000L;
    public static Set<Long> all_accounts = new HashSet<Long>(); 	// All accounts at present

    public static Long[] all_accounts_array = new Long[10000000+5];
    int isFilledArr = -1;


    public static ThreadSafeLinkedList[] LLS = new ThreadSafeLinkedList[10];
    public static ExecutorService[] ES = new ExecutorService[10];
    public static StampedLock[] Listlocks = new StampedLock[10];
    public long generateRandomNumber(long leftLimit, long rightLimit)
    {
        long key = leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
        return key;
    }

    public Boolean isEqual(String a, String b)
    {
    	return a.equals(b);
    }
    public long getRandomAccNo()
    {

        if(isFilledArr==-1){
            return -1;
        }
        int item = new Random().nextInt(isFilledArr);
        return all_accounts_array[item];
    }

    public static String reverseString(String str){  
        StringBuilder sb=new StringBuilder(str);  
        sb.reverse();  
        return sb.toString();  
    }  


    public long generateAccountNumber(int index)
    {
        String account = "";
        long accoutNumber = 0L;
        while(true)
        {
            accoutNumber = 0L;
            for(int i=0;i<9;i++)
            {
                Random random = new Random();
                accoutNumber = accoutNumber*10 + (long)random.nextInt(10);
            }
            accoutNumber = accoutNumber*10 + index;
            String temp = longToString(accoutNumber);
            temp = reverseString(temp);
            accoutNumber = stringToLong(temp);

            if(temp.length() == 10)
            {
            	if(!all_accounts.contains(accoutNumber))
            	{
	                all_accounts.add(accoutNumber);
	                break;
	            }
            } 
        }
        isFilledArr++;
        all_accounts_array[isFilledArr] = accoutNumber;
        
        return accoutNumber;
    }

    public ThreadSafeLinkedList initializeList(int index)
    {
        ThreadSafeLinkedList LL = new ThreadSafeLinkedList();

        for(int i = 0; i < N_USERS/10; i++)
        {
            long temp = generateAccountNumber(index);
            LL.add(generateRandomNumber(CashLeftLimit, CashRightLimit), temp);
        }
        return LL;
    }
    public int getBranch(long account)
    {
    	while(account >= 10)
        {
            account /= 10;
        }
        return (int)account;
    }
    class UpdaterThread implements Runnable
    {
        private long account1, account2;
        private String  opcode;
        private long cash;
    	private int branch;
    	@Override
    	public void run()
    	{
    		if(isEqual(this.opcode,"depositAmount")){
    			int i = getBranch(this.account1);
    			LLS[i].updateNode_deposit(this.cash,this.account1);
    		}
            else if(isEqual(this.opcode,"withdrawAmount")){
    			int i = getBranch(this.account1);
    			LLS[i].updateNode_withdraw(this.cash,this.account1);
    		}
    		else if(isEqual(this.opcode,"transferAmount")){
    			int i = getBranch(this.account1);
    			int j = getBranch(this.account2);
    			if(LLS[i].updateNode_withdraw(this.cash, this.account1)){
                    LLS[j].updateNode_deposit(this.cash,this.account2);
    			}
    		}
    		else if(isEqual(this.opcode,"addAccount")){
    			int i = getBranch(this.account1);
    			LLS[i].add(this.cash,this.account1);
    		}
    		else if(isEqual(this.opcode,"deleteAccount")){
    			int i = getBranch(this.account1);
    			String temp = LLS[i].remove(this.account1);

    		}
    		else if(isEqual(this.opcode,"transferAccount")){
    			int i = getBranch(this.account1);
    			String removed_user = LLS[i].remove(this.account1);
                if(!isEqual(removed_user,"-1"))
                {
                    String[] removed_user_info = removed_user.split(" ");
                    String account1 = removed_user_info[0];
                    long cash1 = stringToLong(removed_user_info[1]);
                    account1 = "" + (this.branch+'0')  + account1.substring(1);
                    all_accounts.add(stringToLong(account1));
                    LLS[this.branch].add(cash1,stringToLong(account1));
                }
    		}
    	}

		public UpdaterThread(String str)
		{
			String[] s = str.split(" ");
			this.opcode = s[0];
			switch(opcode)
			{
				case "depositAmount":	
					this.account1 = stringToLong(s[1]) ;
					this.cash = stringToLong(s[2]);
					break;
	            case "withdrawAmount":	
	                this.account1 = stringToLong(s[1]) ;
	                this.cash = stringToLong(s[2]);
	                break;
				case "transferAmount":	
					this.account1 = stringToLong(s[1]) ;
					this.account2 = stringToLong(s[2]) ;
					this.cash = stringToLong(s[3]);
					break;
				case "addAccount":
	    			this.account1 = stringToLong(s[1]) ;
					this.cash = stringToLong(s[2]);
	    			break;
				case "deleteAccount":
					this.account1 = stringToLong(s[1]) ;
					break;
				case "transferAccount":
					this.account1 = stringToLong(s[1]) ;
					this.branch = stringToInt(s[2]);
					break;
			}
		}  
	}
    public String generateOpcode()
    {
    	Random random = new Random();
    	int i = random.nextInt(1000);
    	for(int j=0; j<330; j++)
    	{
    		if(i==j)
    		{
    			return "depositAmount";
    		}
    	}
    	for(int j=330; j<660; j++)
    	{
    		if(i==j)
    		{
    			return "withdrawAmount";
    		}
    	}
    	for(int j=660; j<990; j++)
    	{
    		if(i==j)
    		{
    			return "transferAmount";
    		}
    	}
    	for(int j=990; j<993; j++)
    	{
    		if(i==j)
    		{
    			return "addAccount";
    		}
    	}
    	for(int j=993; j<996; j++)
    	{
    		if(i==j)
    		{
    			return "deleteAccount";
    		}
    	}
    	for(int j=996; j<1000; j++)
    	{
    		if(i==j)
    		{
    			return "transferAccount";
    		}
    	}
    	return "";
    }
    public String generateRandomJob(){
    	String newJob;
    	String opcode = generateOpcode(), cash_temp="",branch_temp = "" ;
    	newJob = opcode + " ";
        long account_temp, account_temp_1, account_temp_2;
        switch(opcode){
            case "depositAmount":
                 account_temp = getRandomAccNo();
                 cash_temp = longToString(generateRandomNumber(0L,CashRightLimit));
                newJob += longToString(account_temp)  + " " + cash_temp;
                break;
            case "withdrawAmount":
                 account_temp = getRandomAccNo();
                 cash_temp = longToString(generateRandomNumber(0L,CashRightLimit));
                newJob += longToString(account_temp) + " " + cash_temp;
                break;
            case "transferAmount":
                 account_temp_1 = getRandomAccNo();
                 account_temp_2 = getRandomAccNo();
                 cash_temp = longToString(generateRandomNumber(0L,CashRightLimit));
                newJob += longToString(account_temp_1) + " " + longToString(account_temp_2) + " " + cash_temp;
                break;
            case "addAccount":
                int item = new Random().nextInt(10);
                 account_temp = generateAccountNumber(item);
                 cash_temp = longToString(generateRandomNumber(0L,CashRightLimit));
                newJob += longToString(account_temp) + " " + cash_temp;
                break;
            case "deleteAccount":
                 account_temp = getRandomAccNo();
                newJob += longToString(account_temp);
                break;
            case "transferAccount":
                 account_temp = getRandomAccNo();
                 branch_temp = longToString(generateRandomNumber(0L,9L));
                newJob += longToString(account_temp) + " " + branch_temp;
                break;
        }
    	return newJob;

    }
    public void Testing()
    {
    	System.out.println("Started Testing");

    	int i=0;

    	while(i<numberOfTests)
    	{
    		String s = generateRandomJob(); // opcode a1 a2 amount ...
    		// System.out.println(s);
    		String[] s_split = s.split(" ");
    		int branch = getBranch(stringToLong(s_split[1]));
            ES[branch].submit(new UpdaterThread(s));

            i++;

    	}
    	for(int j=0;j<=9;j++)
    	{
    		ES[j].shutdown();
    		while(!ES[j].isTerminated())
    		{

    		}
    	}
    	System.out.println("Testing Finished");
    }

    public static void main(String[] args)
    {
        BankingSystem BS = new BankingSystem();
        System.out.println("Initializing lists");

        double startTime1 = System.currentTimeMillis();

        for(int i = 1; i <=10; i++)
        {
            LLS[i-1] = BS.initializeList(i-1);
            Listlocks[i-1] = new StampedLock();
            ES[i-1] = Executors.newFixedThreadPool(10);
        }

        double endTime1 = System.currentTimeMillis();
        System.out.println("Initialization Completed in " + (endTime1 - startTime1) + " ms");
        // for(int i = 0; i <= 9; i++)
        // {
        //     LLS[i].printList();
        // }
        double startTime = System.currentTimeMillis();
        BS.Testing();
        // for(int i = 0; i <= 9; i++)
        // {
        //     LLS[i].printList();
        // }
        double endTime = System.currentTimeMillis();
        System.out.println("Execution time without Threads is "+ (endTime - startTime)+ " ms");

    }
}
