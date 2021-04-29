import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Random; 
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ThreadLocalRandom; 


class part3
{
    public static int N = 1000; 	// Number of rows as given in question
    static double[][] matrix1 = new double[N+1][N+1];
    static double[][] matrix2 = new double[N+1][N+1];   // both matrix are square matrix            

    static double[][] result = new double[N+1][N+1];
    static double[][] res = new double[N+1][N+1];

    public static int num_threads = 0; // Total number of threads given
    ReentrantLock lock = new ReentrantLock(); // Lock for synchronous calculation


    public double getRandomn()
    {
      return (ThreadLocalRandom.current().nextDouble())*10;
    }

    class threadForMultiplication implements Runnable
    {
    	int[] intervals = new int[2];
	    public threadForMultiplication(int[] arg)
	    {
	      intervals[0] = arg[0];
	      intervals[1] = arg[1];
	    }
	    @Override
	    public void run()
	    {
	    	for (int i = 0; i < N; i++) 
	        { 
	            for (int j = intervals[0]; j <= intervals[1]; j++) 
	            { 
	                for (int k = 0; k < N; k++) 
	                {
	                    result[i][j] = result[i][j] + matrix1[i][k] * matrix2[k][j]; 

	                }  
	            } 
	        } 

	    }
    	
    }

    public double[] getTwoRandomValues() // assign random number between 0 to 10
    { 
        double[] ans = new double[2]; 
        ans[0] = (ThreadLocalRandom.current().nextDouble())*10; 
        ans[1] = (ThreadLocalRandom.current().nextDouble())*10;
        return ans; 
    } 

    class threadForInitialization implements Runnable
    {
      int[] intervals = new int[2];
      public threadForInitialization(int[] arg)
      {
        intervals[0] = arg[0];
        intervals[1] = arg[1];
      }
      @Override
      public void run()
      {
        for (int i = 0; i < N; i++) 
        { 
            for (int j = intervals[0]; j <= intervals[1]; j++) 
            { 
                result[i][j] = 0.0;
            } 

        }
        
        for (int i = 0; i < N; i++) 
        { 
          for (int j = intervals[0]; j <= intervals[1]; j++) 
          { 
              double []randomValues = getTwoRandomValues();
              matrix1[i][j] = randomValues[0]; 
              matrix2[i][j] = randomValues[1]; 
          } 

        }
      }
    }



    public void matrixMultiplication()
    {
    	ExecutorService THREADS = Executors.newFixedThreadPool(num_threads); // Total number of threads given as input argument

        int currCol= 0;
        int col_per_thread = N/num_threads;

        // loop for assigning task to all threads 
       	for(int i=1; i<=num_threads; i++)
        {
          int[] intervals = new int[2]; 
          intervals[0] = currCol;
          intervals[1] = currCol + col_per_thread;
          if(i==num_threads)
          {
          	intervals[1] = N-1;
          }
       	  THREADS.submit(new threadForMultiplication(intervals));
          currCol = currCol+1+N/num_threads;
       	} 

       	THREADS.shutdown();

       	while(!THREADS.isTerminated()){

       	}
       	return;
    }

    public boolean checkMatrices()
    {
      boolean x = true;
      for(int i=0;i<N;i++)
      {
        for(int j=0;j<N;j++)
        {
          if(result[i][j] != res[i][j])
          {
            x= false;
          }
        }
      }
      return x;
    }

    public void initializeMatrices()
    {
        ExecutorService THREADSINIT = Executors.newFixedThreadPool(num_threads); // Total number of threads given as input argument

        int currCol= 0;
        int col_per_thread = N/num_threads;

        // loop for assigning task to all threads 
        for(int i=1; i<=num_threads; i++)
        {
          int[] intervals = new int[2]; 
          intervals[0] = currCol;
          intervals[1] = currCol + col_per_thread;
          if(i==num_threads)
          {
          	intervals[1] = N-1;
          }
          THREADSINIT.submit(new threadForInitialization(intervals));
          currCol = currCol+1+N/num_threads;
        } 

        THREADSINIT.shutdown();

        while(!THREADSINIT.isTerminated())
        {

        }
        return;
    }

    public static void main(String[] args)
    {
      int size = args.length;
      if(size>1)
      {
        System.out.println("Please provide only one interger denoting the number of threads between 4 and 16.");
        return;
      }
      else if(size<1)
      {
        System.out.println("Number of threads not specified... Please provide an integer between 4 and 16.");
        return;
      }
      else
      {
        num_threads = Integer.parseInt(args[0]);
        if(num_threads < 4 || num_threads >16)
        {
          System.out.println("Please provide number of threads in the range 4 to 16 both inclusive.");
          return;
        }
      }
      part3 objectForMult = new part3();

      System.out.println(" ");

      objectForMult.initializeMatrices();

      for (int i = 0; i < N; i++) 
      { 
          for (int j = 0; j < N; j++) 
          { 
              res[i][j]=0.0;
          } 
      } 

      System.out.println("Multiplcation without using threads started");
      double startTimeNaive = System.currentTimeMillis();

      for (int i = 0; i < N; i++) 
      { 
        for (int j = 0; j < N; j++) 
        { 
            for (int k = 0; k < N; k++) 
            {
              res[i][j] = res[i][j] + matrix1[i][k] * matrix2[k][j]; 
            }
        } 
      } 
      double endTimeNaive = System.currentTimeMillis();

      System.out.println("Multiplcation without using threads completed in " + (endTimeNaive - startTimeNaive));
      System.out.println(" ");

      System.out.println("Multiplcation using threads started");
      double startTime = System.currentTimeMillis();
      objectForMult.matrixMultiplication();
      double endTime = System.currentTimeMillis();
      System.out.println("Multiplcation using threads completed in " + (endTime - startTime));
      System.out.println(" ");


      if(objectForMult.checkMatrices()){
        System.out.println("Matrix Multiplcation result using "+num_threads+" Threads and Naive method is same");
        System.out.println(" ");
      }
      System.out.println(" ");
    }
        
}