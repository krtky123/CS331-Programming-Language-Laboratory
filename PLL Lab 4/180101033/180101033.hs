-- CS 331 Assignment 4
-- Submitted By: 
-- Name: Kartikay Goel
-- Roll Number - 180101033
-- Email id: kartikay@iitg.ac.in

-- import statements
import System.IO
import Data.Maybe

-- How to run the program:
-- 1) Type ghci on the terminal(Open the terminal in the program's directory)
-- 2) Load the program using > :l 180101033.hs
-- 3) To run normally, type main on the terminal, and provide comma separated integers when prompted on the terminal. (For ex- 10,9,5,6,8)
-- 4) To run testcases, type testcases on the terminal.

-- A readme file has been attached in the zip file.


import Data.List
-- Data structure used is tree
-- Contains a value and two pointers(these two pointers depict the subtrees)

data TreeDS krtky = Empty | Node
      { 
        value :: krtky,
        right :: TreeDS krtky,
        left :: TreeDS krtky        
      } 

-- buildBST func generates a Tree from the given list of integers.
buildBST :: [Int] -> TreeDS Int

-- LCM function: recursively finds the lcm of all integers
lowestCommonMultiple :: [Int] -> Int

-- order functions generates a list from the given tree input recursively.
preorder :: TreeDS krtky -> [krtky]       --  value  left  right
inorder :: TreeDS krtky -> [krtky]         --  left  value  right
postorder :: TreeDS krtky -> [krtky]      -- left  right  value

buildBST [] = Empty
buildBST (x:xs) = Node pivot (buildLeft) (buildRight)
  where 
    buildLeft = buildBST left_Subtree
    buildRight = buildBST right_Subtree
    pivot = x -- root value is head of list
    left_Subtree = filter (<=pivot) xs
    right_Subtree = filter (>pivot) xs

preorder Empty = []
preorder (Node v r l) = v : plpr 
  where plpr = (pr <> pl) 
        pl = preorder l
        pr = preorder r 

inorder Empty = []
inorder (Node v r l) = iril
  where iril = (ir <>(v:il)) 
        il = inorder l 
        ir = inorder r

postorder Empty = []
postorder (Node v r l) = plpr <> [v] 
  where plpr = (pr <> pl) 
        pl = postorder l
        pr = postorder r 

lowestCommonMultiple [] = 1
lowestCommonMultiple (x:xs) = lcm x (lcmm xs) -- uses inbuilt func lcm to find recursively.
  where lcmm = lowestCommonMultiple

-- This function takes in a string of comma separated integers as input and prints the results.
mainfnc :: String -> IO()
mainfnc str = do
  putStrLn ""
  let listTemp = "[" ++ str ++ "]" -- string str is updated to have a list format.
  let list = read listTemp :: [Int] -- string listTemp is read as a list of Int and stored as list
  let tree = buildBST list -- BST is generated from the list using buildBST func

  putStr "---------------- START A ---------------------------------\n"
  -- ----------------------------- PART-A -----------------------------------------------------------
  putStr "Part A start: \n"

  putStr "Entered list of integers: "
  print list
  putStr "---------------- END A ---------------------------------\n"
  putStr "\n"
  putStr "---------------- START B ---------------------------------\n"
  -- ----------------------------- PART-B -----------------------------------------------------------
  putStr "Part B start: \n"

  putStr "LCM of the entered list of integers is: "
  print (lowestCommonMultiple list) -- print LCM of the integers entered
  putStr "---------------- END B---------------------------------\n"
  putStr "\n"
  putStr "---------------- START C---------------------------------\n"
  -- ----------------------------- PART-C -----------------------------------------------------------
  putStr "Part C start: \n"

  putStr "Inorder traversal of the entered list of integers is: "
  print (inorder tree)
  putStr "PreOrder traversal of the entered list of integers is: "
  print (preorder tree)
  putStr "PostOrder traversal of the entered list of integers is: "
  print (postorder tree)
  putStr "---------------- END C---------------------------------\n"

-- MAIN function (takes a string of integers separated by commas as input on terminal from the user)
main = do
  putStrLn "Enter a list of comma separated intergers: (For ex- 10,9,5,6,8) "
  list <- getLine
  mainfnc list -- calls mainfnc described above

  -- This function has various testcases which tests the mainfnc
testcases = do
  mainfnc "3,2,35,3,2,3,4,5,5,23,26,89,-2"
  mainfnc "-3,2,-35,3,-2,3,-4,5,-5,23"
  mainfnc "0,35,0,2,-3,0,5,-8,56,85,23,-256"
  mainfnc "267890,-582,11,36789,100,278,215,-652035,3,2567,3,5894,155,5467,-763,5672,5"
  mainfnc "1123,-234,13456,34564,-378,25678,35675,-323,22345,323456,43456,5567,-56543,6233,-2987,50987"