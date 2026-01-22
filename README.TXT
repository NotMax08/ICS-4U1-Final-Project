Full Squire (Hollow Knight Inspired Platformer)
Group Members: Paul, Max, Julian, Robin, Angelina

Instructions:
Play the game as intended with several tries for the best experience. Intended to be difficult and rewarding to complete. Main point is to explore all rooms and fight final boss to win.
Keybinds: 
-> Movement: space jump, a/d or left/right arrow keys for direction
-> Attack: k or e for basic attack, k for magic attack.
-> Abilities:  q or h to heal, hold for 2 seconds
-? Inventory: tab (empty by default, does not count your weapon)
->Debug: g for map grid and y for room position entry testing, platform and interactive door debug visuals are booleans set in constructors

Cheat Code:
- god mode can be activated to nullify all damage via key p
- room one to room two entrance is at the top of room one
- room two to room three entrance is at the top righ tof room two
- npc room and miniboss room are found in the right and left doorways rouhgly in the middle y level in room two respectively
- boss room is found in room one and is unlocked via key found in room three
- room three needs a shrinking potion to access the top and find the key

Features:
- 2D Array system that forms a grid in each room, different numbers mean empty space, platforms, breakable objects (not implemented due to time constraints), interactive doors, regular doors (auto move to next room) and walls
- Has a debug that shows the grid when pressing g and color codes different tile types. Press once to show x number on tiles and twice to see y.
- Player has a needle as a weapon that deals melee damage and has a weapon upgrade to be found that deals more damage. 
- 7 different enemies including one boss and one miniboss.
- Boss has three attacks and two phases.
- Miniboss has two attacks and rewards weapon upgrade when defeated.
- Gravity system for player and enemies jumping and falling.
- Health system for player and enemies.
- Soul system to gather energy when player hits enemies and charges to heal.
- Breakable Spawner that spawns enemies every so often.
- 3 main rooms, two boss rooms housing the miniboss and main boss and an npc room for a potion selling npc.
- Main rooms have a camera effect allowing for players to discover a larger room space than the world screen and update background image as the player moves.
- 3 different potion types, strength, shield and shrink.
- Leaderboard save file that tracks highscores based on total playthrough time (only counts if you win)
- Player and all enemies are animated with image frames for all movement and attacks
- Inventory system keeps track of items player has like potion, key to unlcok boss room and displays items in inventory ui
- NPC shopkeeper that sells three types of potions, has custom dialogue class and shop ui to interact with

Author Paul



