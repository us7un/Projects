This demo simulates a truckstop where trucks receive loads and complete tasks accordingly.
There are 6 different commands you can give via an input text file:
1 - create_parking_lot <capacity_constraint> <truck_limit>
Example: create_parking_lot 10 15 creates a parking lot with 10 minimum load capacity and 15 maximum number of trucks. Output: None
2 - delete_parking_lot <capacity_constraint>
Example: delete_parking_lot 10 deletes the parking lot we just created. Output: None
3 - add_truck <truck_id> <truck_capacity>
Example: add_truck 1 100 will try to add this truck to the parking lot with capacity 100 if it exists. 
If it does not exist, then it will try to add it to the largest smaller capacity parking lot (its inorder predecessor). 
If no such lot exists or all are full, it will not add the truck. Output: <capacity> if added or -1 if not added
4 - ready <capacity>
Example: ready 40 will check the waiting section of the parking lot with 40 capacity constraint and move the earliest waiting truck to its ready section.
If the waiting section of lot 40 is empty, it will keep on checking its inorder successors until it finds an available lot. 
If no such lot exists, it will not do anything. Output: <truck_id> <capacity> if a truck is moved or -1 if no such lot exists
5 - load <capacity> <load_amount>
Example: load 40 100 will try to distribute a total load of 100 to the truck in the ready section of lot 40. 
If all trucks are filled and there is still some load remaining, it will move on to lot 40's inorder successor. 
If no such lot exists the remaining load will go to waste. Output: <truck_id1> <constraint_of_new_load> - <truck_id2> <constraint_of_new_load> - ...
6 - count <capacity>
Example: count 50 will return the total truck count in the parking lot with capacity constraint of 50. Output: <truck_count>

All of these outputs will be written in a text file "output.txt" which can either be inserted into the source directory or will be created automatically if it does not exist there.
