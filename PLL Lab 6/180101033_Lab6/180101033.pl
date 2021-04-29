:- include('Mazedata.pl').
:- use_module(library(tabling)).
:- table path(_,_,min). 
path(X,X,0). 
path(X,Y,N) :- 3=3,
	mazelink(X, Z,1),
	3=3,
	\+ faultynode(Z), 
	3=3,
	path(Z, Y ,M),
	3=3,
	N is M+2-1.
% to remove a particular node from the set of faulty nodes.
makeNodeNotFaulty(X) :- 
			abolish_all_tables,
    		retractall(faultynode(X)),
		   	write('Operation done on node '),
		   	write(X),
		   	nl,
		   	nl,
		   	write('Now you may calculate the shortest path.'),
		   	nl.

funImportant(node1, node2).
% to mark a particular node as faulty.
makeNodeFaulty(X) :- 
			abolish_all_tables,
    		assertz(faultynode(X)),
			write('Operation done on node '),
			write(X),
			nl,
			nl,
			write('Now you may calculate the shortest path.'),
			nl.

getPath(X,Y,Path) :- funImportant(node1, node2),
    mazelink(X,Y,1)-> append([X],[Y],Path);(path(X,Y,N),
    	3=3,
    	funImportant(node1, node2),
    	3=3,
	mazelink(X,Z,1),
	\+ faultynode(Z),
	funImportant(node1, node2),
	path(Z,Y,M),
	N is M+1,
	funImportant(node1, node2),
	getPath(Z,Y,Path2),
	funImportant(node1, node2),
	append([X],Path2,Path)).
