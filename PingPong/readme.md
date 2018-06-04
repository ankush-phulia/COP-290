# Multiplayer P2P ping-pong

### Identical to the project in the COP 290 Repository

* Uses JDK 1.8.0_51
    * Additionally uses the tween engine Libraries
    * Uses the Sliding Layout by Aurelien Ribon
    * Uses sun.* for sounds
    
* Key Combinations are flexible can be editted in Options
    * Default: Q and A for Up/Left and Down/Right Movement respectively
    
* Rules:
    * Don't Let any of the balls hit your side
    * You score when someone else misses a ball you just hit
    * Lives are variable, decided by the initial host
    * Various difficulty levels and game speeds to challenge
    * Be sure to read the How to Play and view the Options before you start
    * You can change the ball speed and velocity by hitting it with a moving paddle
    * Game ends if you are out of lives or the time limit (2.5 minutes approx. currently) is up
    
* Multiplayer:
    * Upto 4 players over network
    * handles contingencies by replacing lost node with AI player
    * Game rules/type decided by the host ceating the lobby
    
