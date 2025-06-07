# java_locked_chess_class
The game "LockedChess" implemented in Java language

To use it, you need to:

- 1. Run the src/compilation.bat
  2. Move the jar and lib/org.json.jar to folder "test".
  3. Run test.py.

## Note:

- 1. LockedChessCentre:

  This is the Centre of all of the game "locked chess".
  
- 2. LockedChessCentre.LockedChessAllInterface:

  The interface of the LockedChessGame, it contains:

  - 1. void gameStart(): Initalization the game.
    2. void game_start(): Same to "gameStart()"
    3. List<Object> legalOperation(): Return all of the legal operations now.
    4. List<Object> legal_operation(): Same to "legalOperation()".
    5. void startOperation(Object x): Doing one operation. The "x" must be in "legalOperation()" or match "^(1[0-2]|[1-9]),(1[0-2]|[1-9])$".
    6. void operation(Object x): Same to "startOperation(Object x)"
    7. String returnGame(): Return the game start with a json string.
    8. String return_game(): Same to "returnGame()"
    9. void loadsGame(String x, boolean restart_game): Loads one game from json string "x", the "restart_game" is whether to start the game.
    10. void loads_game(String x, boolean restart_game): Same to "loadsGame(String x, boolean restart_game)".
       
