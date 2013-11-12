Induction loops input data 

a) retrieve_loops_PEC.py 
- downloads traffic volumes data from Ponts et Chausees
- writes a new xml file for each induction loop
- in every file there is a list of traffic volumes for particular dates 
- fixed direction parameter. 1 (one direction - incoming to Luxembourg), 2 (the other direction), or 3 (both directions).
- new parameters:
--weekday: Day of the week as an integer, where Monday is 0 and Sunday is 6
--days: Number of the latest days. E.g. 10 means that data from 10 latest tuesdays (if dayweek=1) will be stored in the output

example: download from 10 the most recent Tuesdays for each loop for incoming traffic
> python retrieve_loops_PEC.py --weekday=1 --days=10 --direction=1 --loops 1430 20 520 1473 1203 1232 460 479 446 1410 345 1406 304 1420 405

b) calculate_average.py
- reads all xml files in current directory
- from each file computes the average flow for every hour from particular dates
- outputs one xml file with average traffic volumes for every induction loop

example:
> python calculate_average.py 

c) add_loop_edges.py
- travers through xml nodes (input file) looking for each loop and adding an attribute "edge". WRtites new xml file (output file) 
- edges assigned to loops are hardcoded in script

example:
> python add_loop_edges.py --input=Luxembourg.loop.xml --output=Luxembourg.loop.xml