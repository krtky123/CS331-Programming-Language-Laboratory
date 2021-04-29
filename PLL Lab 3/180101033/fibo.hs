
--function that captures fibonacci numbers from the fibonaci seq
fibonacci number = fibonacci_seq!!number

--function that generates list of fibonacci numbers in O(n) time complexity
fibonacci_seq = 0 : 1 : zipWith (+) fibonacci_seq (tail fibonacci_seq)

--main function consisting of 10 test cases 
main =  do
    putStrLn("The 1st fibonacci number is:")
    print(fibonacci 1)
    putStrLn("The 2nd fibonacci number is:")
    print(fibonacci 2)
    putStrLn("The 4th fibonacci number is:")
    print(fibonacci 4)
    putStrLn("The 6th fibonacci number is:")
    print(fibonacci 6)
    putStrLn("The 8th fibonacci number is:")
    print(fibonacci 8)
    putStrLn("The 11th fibonacci number is:")
    print(fibonacci 11)
    putStrLn("The 50th fibonacci number is:")
    print(fibonacci 50)
    putStrLn("The 100tn fibonacci number is:")
    print(fibonacci 100)
    putStrLn("The 150th fibonacci number is:")
    print(fibonacci 150)
    putStrLn("The 200th fibonacci number is:")
    print(fibonacci 200)
    