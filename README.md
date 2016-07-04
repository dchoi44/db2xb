"# db2xb" 

7/4/2016

Todo:
1. implement io for hash tables ; optional, in case if building table is slow 
	--> with 3 files of input(~60MB total), the code runs in 5 secs and about 20% is for making hashtable. 
	--> negligible for now
	

input:
	nt files in the ./input folder.
	no sub-directories will be checked. items should be separated by tabs not spaces. this would cause problems.
	if the output file is nearly empty, look up if periods are separated by tabs(this should work though).
		
output:
	nt files in the ./output folder.
	name of the output file would be the same as the original but with the prefix c_
	