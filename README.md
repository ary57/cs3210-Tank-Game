CS3210 Final Project by Alan Sorrill and Abhishek Aryal

This program is a multiplaye game which involves tanks. 
The aim of this game is to win over your opponent.

To run the program, simply run the Game.java class. 
Ensure that background_flag_of_thailand.png & BoxTexture.png are in your java working directory. (System.getProperty("user.dir"))

The Game involves 2 tanks: 
1. Shooter tank:
    Controls for Shooter Tank: 'WASD' to move and mouse to aim and shoot
2. Dropper tank:
    Controls for Dropper Tank: 'IJKL' to move and 'U' & 'P' to place boxes on either of the tank. 
        'U' places box on the left of the tank, 'P' places box on the right of the tank. 

- To win the game
    Push your opponent to the red part in the top or the bottom of the map. The first one to fall into the red zone loses the game. 
    Or 
    Force and contain your opponent to the leftmost or the rightnmost part of the map, if a tank stays in the edges of the map for too long, they lose the game. 
