An eclipse project that demo running of cellular automata. 
================================================================
Some provided test configurations are given in /test.
glider_gun.cel: glider with Configuration ( Linear, Moore, Game of Life ), size 40
glider_gun_100.cel: same as above but with size 100
glider_gun_toroid.cel: glider with Configuration ( Toroid, Moore, Game of Life ), size 40
food_chain.cel: a simple food chain that dies after some steps.
================================================================
Some videos of how to use the tool is included at /video
================================================================
cellularAutomata.jar is provided for convenience.
However, it suffers from some rendering problem that need to be fixed sometimes.
================================================================
Increase of the size to 100 doesn't affect the performance, because the implementation only check cells that need to be updated, not check all cells in the grid.
================================================================
When the glider run in toroid configuration, the bullet gonna come back from the other side, and might destroy the gun.
