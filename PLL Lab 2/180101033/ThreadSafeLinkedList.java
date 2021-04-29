/*
Name - Kartikay Goel
Roll Number - 180101033
*/
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.StampedLock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.*;

class ThreadSafeLinkedList
{
    private Node head; 	// head of linked list

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

    public static class Node
    {
        long cash;	// cash of user
        long accountNo;	// accountNo of user starting with branch code
        Node next;	// next pointer in linked list
        int ispresent;
        private StampedLock lock = new StampedLock(); // lock for sync calculation
        long stamp;

        // constructor of class Node
        public Node(long cash, long accountNo)
        {
            this.cash = cash;
            this.accountNo = accountNo;
            this.next = null;
            this.ispresent = 1;
            this.lock = new StampedLock();
        }
        public void lock()
        {
        	this.stamp = this.lock.writeLock();
            
        }
        public void unlock()
        {
            this.lock.unlockWrite(this.stamp);
        }
    }
    
    // constructor of class ThreadSafeLinkedList
    public ThreadSafeLinkedList()
    {
        this.head = new Node(-1L, -1L);
    }

    //check if two strings are equal
    public Boolean isEqual(String a, String b)
    {
    	return a.equals(b);
    }

    // add at the front of the linked list
    public boolean add(long cash, long accountNo)
    {
        while(true)
        {
        	Node head = this.head;
        	head.lock();
        	try
        	{
        		Node nextHead = head.next;
	        	if(nextHead == null)
	        	{
	        		Node node = new Node(cash, accountNo);
	        		head.next = node;
	        		return true;
	        	}
	        	else
	        	{
	        		nextHead.lock();
	        		try
	        		{
	        			Node node = new Node(cash, accountNo);
	        			head.next = node;
	        			node.next = nextHead;
	        			return true;
	        		}
	        		finally
	        		{
	        			nextHead.unlock();
	        		}
	        	}
        	}
        	finally
        	{
        		head.unlock();
        	}
        }    
    }

    // remove account from linked list
    public String remove(long accountNo)
    {
        while(true)
        {
            Node prev = null;
            Node curr = this.head;
            while (curr != null && curr.accountNo!=accountNo)
            {
                prev = curr;
                curr = curr.next;
            }
            // if account no. is not present
            if(curr==null)
            {
                return "-1";
            }
            Node nextnode = curr.next;
            if(nextnode == null)
            {
            	prev.lock();
            	try
            	{
                	curr.lock();
	                try
	                {
	                    prev.next = curr.next;
	                    String ans = curr.accountNo + " " + longToString(curr.cash);
	                    return ans;
	                }
	                finally
	                {
	                    curr.unlock();
	                }
	            }finally
	            {
	                prev.unlock();
	            }     
            }
            else{
                prev.lock();
                try
                {
                    curr.lock();
                    try
                    {
                        nextnode.lock();
                        try
                        {
                            prev.next = nextnode;
                            String ans = curr.accountNo + " " + longToString(curr.cash);
                            return ans;
                        }
                        finally
                        {
                            nextnode.unlock();
                        }
                    }
                    finally
                    {
                        curr.unlock();
                    }
                }finally
                {
                    prev.unlock();
                }
            }
  
        }
    }
    // update account from linked list based on withdraw
    public boolean updateNode_withdraw(long withdrawAmount, long accountNo)
    {
        while(true)
        {
            Node curr = this.head;
            while(curr != null && curr.accountNo!=accountNo)
            {
                curr = curr.next;
            }
            if(curr==null)
            {
                return false;
            }
            curr.lock();
            try
            {
					if(curr.cash - withdrawAmount < 0){return false;}
                    curr.cash -= withdrawAmount;
                    return true;

            }finally
            {
                curr.unlock();
            }   
        }               
    }
    // update account from linked list based on deposit
    public boolean updateNode_deposit(long depositAmount, long accountNo)
    {
        while(true)
        {
            Node curr = this.head;
            while (curr != null && curr.accountNo!=accountNo)
            {
                curr = curr.next;
            }
            if(curr==null)
            {
                return false;
            }
            curr.lock();
            try
            {
                curr.cash += depositAmount;
                return true;            
            }
            finally
            {
                curr.unlock();
            }            
        }       
    }
    // print linked list for debugging purposes
    public void printList()
    {
        while (true)
        {
            Node curr = this.head;
            while (curr != null)
            {
                System.out.println(curr.accountNo + " " + longToString(curr.cash) + " " + intToString(curr.ispresent));
                curr = curr.next;
            }
            System.out.println();
            return;
        }
    }
}
