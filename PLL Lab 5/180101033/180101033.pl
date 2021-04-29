/* 
*	Assignment Number 5
*
*	Code written by Kartikay Goel, 180101033
*	
*	To run the program type the following in the terminal:
*		swipl
*	Then type the following in the prompt:
*		consult('180101033.pl'). 	
*/

/*
*	A. Square Root Program 
*
*	To test this prolog predicate, type the following in the terminal:
*		squareRoot(X, Result, Accuracy).   	where  X is number whose square root is to be found and Accuracy is desired Accuracy of square root.
*
*	Sample Test Cases:
		squareRoot(1020,Result,0.0001).
		squareRoot(100,Result,0.0001).
		squareRoot(12,Result,0.0001).
		squareRoot(402001,Result,0.0001).
		squareRoot(1895,Result,0.00001).
*/

/* 
*	B. isSubList Program
*
*	To test this prolog predicate, type the following in the teriminal:
*		isSubList(X,Y).   where the predicate returns true if list X is a isSubList of list Y
*	Sample Test Cases:
*		isSubList([a,b],[b,a,b,a]).
		isSubList([],[]).
		isSubList([a],[]).
		isSubList([a,b],[a,e,f]).
		isSubList([a,b,c,e],[b,a,b,a,a,b,d,t,f,c]).
		isSubList([],[a]).
*/

/* Square root program */

squareRoot(X,R,A) :-
    once(sqrtrecursive(A, X, X * 0.5, (X *X * 0.25 + X)/(X), R)). %initialised Result with X/2.

sqrtrecursive(E, _, X, Y0, R) :-
    abs(X - Y0) < E, R is Y0.  %if R is within accuracy range, then the result is obtained

sqrtrecursive(A, X, Y1, Y0, R) :-
    abs(Y1 - Y0) > A, sqrtrecursive(A, X, Y0, (Y0*Y0 + X)/(2*Y0), R). %recursive call to obtain R within accuracy range

/* End Square Root Program */


/* Start isSubList program */

 isSubList([],_). % This case is true trivially
 isSubList([A|T1],[_|T2]) :- isSubList([A|T1],T2). % remove front of second list if front elements are different.
 isSubList([A|T1],[A|T2]) :- isSubList(T1,T2).  % remove front from both lists if front elements are same.

/* End isSubList program  */
