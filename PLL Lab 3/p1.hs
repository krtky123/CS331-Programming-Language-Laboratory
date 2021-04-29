--squareRootFuntion computes squareRoot by the Babylonian method 
cal_sqrt :: Double -> Double
cal_sqrt n = mysqrt 0 n
    where mysqrt :: Double -> Double -> Double
          mysqrt x y
            | (abs (x - y)) < 0.00001 = x
             | tmp*tmp > n = mysqrt x tmp
            | otherwise = mysqrt tmp y
              where tmp = (x+y) / 2


--main function that runs 10 test cases 
main = do
       putStr "The square root of 23.56 is "
       print(cal_sqrt 23.56)
       putStr "The square root of 123.6 is "
       print(cal_sqrt 123.6)
       putStr "The square root of 18.00 is "
       print(cal_sqrt 18.00)
       putStr "The square root of 36.00 is "
       print(cal_sqrt 36.00)
       putStr "The square root of 144.56 is "
       print(cal_sqrt 144.56)
       putStr "The square root of 189.59 is "
       print(cal_sqrt 189.59)
       putStr "The square root of 196.6 is "
       print(cal_sqrt 196.6)
       putStr "The square root of 147.69 is "
       print(cal_sqrt 147.69)
       putStr "The square root of 2256.56 is "
       print(cal_sqrt 2256.56)
       putStr "The square root of 2583.256 is "
       print(cal_sqrt 2583.256)


