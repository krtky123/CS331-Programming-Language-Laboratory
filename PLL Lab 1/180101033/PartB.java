import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Random; 
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ThreadLocalRandom; 

class part2
{
    public static final int INTERVAL = 3000000; // Total number of points given in question > 10^6
    public static double delta_x = ((2.0)/INTERVAL); // delta x as per algorithm
    public static double simpson_value = 0.0;  // Integration value using Simpson method
    public static int num_threads = 0; // Total number of threads 
    ReentrantLock lock = new ReentrantLock(); // Lock for synchronous calculation


    public double fun(double x)
    {

      x = delta_x*x-1.0;
      double power = x*x;
      double negPower = -power;
      double simpson_upper = Math.pow(Math.E, negPower/2.0);
      return (1.0*simpson_upper);
    }

    public static double fun1(double x)
    {

      x = delta_x*x-1.0;
      double power = x*x;
      double negPower = -power;
      double simpson_upper = Math.pow(Math.E, negPower/2.0);
      return (1.0*simpson_upper);
    }


    class threadForSimpson implements Runnable
    {
      int[] intervals = new int[2];
      public threadForSimpson(int[] arg)
      {
        intervals[0] = arg[0];
        intervals[1] = arg[1];
      }

      @Override
      public void run()
      {
        for(int i=intervals[0];i<=intervals[1];i++)
        {

            double simpson_x0=fun(i);
            if(i%2==1 && i!=INTERVAL)
            {
              simpson_x0*=4;
            }
            else if(i%2==0 && i!=0 && i!=INTERVAL)
            {
              simpson_x0*=2;
            }
            lock.lock();
            simpson_value += simpson_x0; 
            lock.unlock();
        } 
      }
      
    }


    public void aprroxSimpsonFun(){
        ExecutorService ES = Executors.newFixedThreadPool(num_threads); // Total number of threads 

        int interval_per_thread = INTERVAL/num_threads;

        int curr_n = 0;
        // assigning task to threads
        for(int i=1; i<=num_threads; i++){
          int[] intervals = new int[2]; 
          intervals[0] = curr_n;
          intervals[1] = curr_n + interval_per_thread;
          if(i==num_threads)
          {
          	intervals[1] = INTERVAL;
          }
          ES.submit(new threadForSimpson(intervals));
          curr_n = curr_n+1+interval_per_thread;
        }

        ES.shutdown();
        // Wait while all the threads are terminated
        while(!ES.isTerminated()){

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
      System.out.println(" ");

      part2 objectOfSimpson = new part2();


      double simpson_value_naive = 0.0;
      simpson_value_naive += fun1(0);
      for(int i=1;i<INTERVAL;i++)
      {
        double simpson_x0=fun1(i);

        if(i%2==1)
        {
          simpson_x0*=4;
        }
        else if(i%2==0)
        {
          simpson_x0*=2;
        }
        simpson_value_naive += simpson_x0; 
      
      }
      simpson_value_naive += fun1(INTERVAL);
      double simpson_lower_naive = Math.sqrt(2*Math.PI);
      double aprroxSimpson_naive = (simpson_value_naive*(delta_x)/(3.0*simpson_lower_naive)); // apply formulla

      System.out.println(aprroxSimpson_naive + " is the value of integration using composite Simpson 1/3 rule without using threads");
      System.out.println(" ");



      objectOfSimpson.aprroxSimpsonFun();
      double simpson_lower = Math.sqrt(2*Math.PI);
      double aprroxSimpson = (simpson_value*(delta_x)/(3.0*simpson_lower));
      System.out.println(aprroxSimpson + " is the value of integration using composite Simpson 1/3 rule using " + num_threads + " Threads");
      System.out.println(" ");


      
    }
        
}
