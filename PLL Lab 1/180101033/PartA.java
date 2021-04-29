import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Random; 
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ThreadLocalRandom; 

class part1
{
    private static final int POINTS = 1000000; // Total points given
    private static int circlePoints = 0; // Points which will lie in circle
    private static int num_threads = 0; // Total number of threads
    ReentrantLock lock = new ReentrantLock(); // Lock for synchronous calculation

    public boolean checkOutsideCircle(double x, double y)
    {
      return !(x*x + y*y <= 1);
    }

    public static boolean checkOutsideCircle1(double x, double y)
    {
      return !(x*x + y*y <= 1);
    }
    

    public double[] getTwoRandomValues() // outputs a random value between 0 and 1
    { 
        double[] ans = new double[2]; 
        ans[0] = ThreadLocalRandom.current().nextDouble(); 
        ans[1] = ThreadLocalRandom.current().nextDouble();
        return ans; 
    } 

    public static double[] getTwoRandomValues1() // outputs a random value between 0 and 1
    { 
        double[] ans = new double[2]; 
        ans[0] = ThreadLocalRandom.current().nextDouble(); 
        ans[1] = ThreadLocalRandom.current().nextDouble(); 
        return ans; 
    } 


    class PIThread implements Runnable
    {
      private int points =0; 
      public PIThread(int n)
      {
        if(n == num_threads){
          points = (POINTS/num_threads) + POINTS%num_threads;  // assign task to last thread
        }else{
          points = POINTS/num_threads; // assigning task to all threads except last one
        }
      }

      @Override
      public void run()
      {
    	for(int i=1;i<=points;i++)
        {
          double []randomValues = getTwoRandomValues();
       		if(!checkOutsideCircle(randomValues[0],randomValues[1]))
          {
            lock.lock();
       			circlePoints++;
            lock.unlock();
          }
       	}
       }
    	
    }

    public void computePI()
    {
    	ExecutorService ES = Executors.newFixedThreadPool(num_threads);

       	for(int i=0; i<num_threads; i++)
        {
       		ES.submit(new PIThread(i));
       	}

       	ES.shutdown();
       	// Wait while all the threads are terminated
       	while(!ES.isTerminated())
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
      part1 objectOfPI = new part1();


      int circlePoints1= 1000000;
      for(int i=1;i<=POINTS;i++)
      {
        double []randomValues = getTwoRandomValues1();
        if(checkOutsideCircle1(randomValues[0],randomValues[1]))
        {
          circlePoints1--;
        }
      }
      double approxPiWithoutThreads = (4.0 * circlePoints1) / POINTS;

      System.out.println(" ");
      System.out.println("approx value of PI using Monte-Carlo Simulation using without using multithreading is: " + approxPiWithoutThreads);
      System.out.println(" ");

      objectOfPI.computePI(); 
      double CIRCLE_POINTS = circlePoints;
      double approxPi = (4.0 * CIRCLE_POINTS) / POINTS;
      System.out.println("approx value of PI using Monte-Carlo Simulation is: " + approxPi);
    }
        
}