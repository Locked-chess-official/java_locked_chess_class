# java_locked_chess_class
The game "LockedChess" implemented in Java language

## Usage
To use it, you need to:
1. Run the src/compilation.bat
2. Move the jar and lib/org.json.jar to folder "test".
3. Run test.py.

## Classes and Interfaces

### 1. LockedChessCentre
This is the central class containing all interfaces and implementations for the "locked chess" game.

### 2. Interfaces

#### LockedChessAllInterface
The main interface for the LockedChessGame, containing:

**Game Control:**
- `void gameStart()`: Initializes the game
- `void game_start()`: Alias for gameStart()
- `void loadsGame(String x, boolean restart_game)`: Loads game from JSON string
- `void loads_game(String x, boolean restart_game)`: Alias for loadsGame()
- `void loadsGame(String x)`: Loads game without restarting
- `void loads_game(String x)`: Alias for loadsGame()
- `void loadsGame(JSONObject x, boolean restart_game)`: Loads game from JSONObject
- `void loads_game(JSONObject x, boolean restart_game)`: Alias for loadsGame()
- `void loadsGame(JSONObject x)`: Loads game from JSONObject without restarting
- `void loads_game(JSONObject x)`: Alias for loadsGame()

**Game Operations:**
- `List<Object> legalOperation()`: Returns all legal operations
- `List<Object> legal_operation()`: Alias for legalOperation()
- `void startOperation(Object x)`: Performs an operation (x must be in legalOperation())
- `void operation(Object x)`: Alias for startOperation()
- `List<List<Object>> calculateAllChains()`: Calculates all possible operation chains
- `List<List<Object>> calculate_all_chains()`: Alias for calculateAllChains()
- `boolean isEqualChain(List<Object> chain1, List<Object> chain2)`: Checks if two chains are equivalent
- `boolean is_equal_chain(List<Object> chain1, List<Object> chain2)`: Alias for isEqualChain()

**Game State:**
- `String returnGame()`: Returns game state as JSON string
- `String return_game()`: Alias for returnGame()
- `Set<ChessPiece> getGame()`: Returns chess pieces set
- `Set<ChessPiece> get_game()`: Alias for getGame()
- `int getOperationNumber()`: Returns current operation number
- `int get_operation_number()`: Alias for getOperationNumber()
- `String getOperationOppsite()`: Returns current player color ("黑" or "白")
- `String get_operation_oppsite()`: Alias for getOperationOppsite()
- `ChessPiece getChooseChessLocate()`: Returns currently selected piece
- `ChessPiece get_choose_chess_locate()`: Alias for getChooseChessLocate()
- `String getOperationLastDirection()`: Returns last move direction
- `String get_operation_last_direction()`: Alias for getOperationLastDirection()
- `List<Object> getAllOperation()`: Returns all possible operations
- `List<Object> get_all_operation()`: Alias for getAllOperation()
- `boolean getHasOperated()`: Checks if operation was performed
- `boolean get_has_operated()`: Alias for getHasOperated()

**Thread Safety:**
- `void lockGame()`: Locks game state (must pair with unlockGame)
- `void unlockGame()`: Unlocks game state

#### WriterLockedChessAllInterface (extends LockedChessAllInterface)
Adds recording functionality:

- `void setProtect()`: Enables game protection
- `void setUnprotect()`: Disables game protection
- `String getFinalResult()`: Gets final game result
- `String get_final_result()`: Alias for getFinalResult()
- `void writeToRecord(String name)`: Writes game record with name
- `void writeToRecord()`: Writes game record without name
- `boolean getProtect()`: Get whether it is protected
- `boolean getPeace()`: Get whether one player have requested to peace and other makes no response

#### LockedChessRobotAllInterface
Robot player interface:

- `void setGame(LockedChessAllInterface writerLockedChess)`: Sets game to analyze
- `List<Object> getOperation()`: Gets an operation chain
- `List<Object> getBetterOperation(boolean usebetter)`: Gets better/worse operation chain

### 3. Main Classes

- private:

#### LockedChess
Implements LockedChessAllInterface with core game logic.

#### WriterLockedChess
Extends LockedChess with recording capabilities (implements WriterLockedChessAllInterface).

#### LockedChessRobot
Implements LockedChessRobotAllInterface for AI player functionality.

- public:

#### LockedChessClass
#### WriterLockedChessClass
#### LockedChessRobotClass
- `LockedChessRobotAllInterface LockedChessRobotClass()`: Return new LockedChessRobot.
- `LockedChessRobotAllInterface LockedChessRobotClass(LockedChessAllInterface writerLockedChess)`: Return new LockedChessRobot and set the game.

### 4. Helper Classes

#### AllRecords
Manages game records storage:
- `void addRecord(WriterLockedChessAllInterface writerLockedChess, String name)`: Adds a record from one game with a name.
- `void addRecord(WriterLockedChessAllInterface writerLockedChess)`: Adds a record from one game without a name.
- `void writeRecords(String filePath)`: Writes records to file
- `void readRecords(String filePath)`: Reads records from file
- `void deleteRecord(String recordTime, boolean isTime)`: Deletes a record. If "isTime" is true, the record is just the time, or it is time + "\\u2003" + name.
- `String getRecords()`: Gets all records.
- `String readRecord(String boardRecord, String operationRecord, String timeRecord)`: Translates record format.
- `void combineRecord()`: Combines records from OtherRecords.

#### OtherRecords
Manages additional records storage with similar methods to AllRecords:
- `void readRecords(String filePath)`: Reads records from file.
- `String getRecords()`: Gets all records.
- `boolean getHasCombined()`: Gets whether the records have been combined to AllRecords.

#### ChessPiece
Represents a chess piece with:
- Coordinates (x, y)
- Color ("黑" or "白")
- Standard equals(), hashCode(), toString() implementations
- `int getX()`: Get the X coordinate
- `int getY()`: Get the Y coordinate
- `String getColor()`: Get the color

### 5. Exception Classes
Custom exceptions for game errors:
- GameHasNotStartedError
- IllegalLocationError
- IllegalOperationError
- GameIsOverError
- IllegalChainError
- GameIsNotOverError (in WriterLockedChess)

## Game Rules
1. The game alternates between black ("黑") and white ("白") players every 5 operations
2. Players move pieces in specific directions ("u", "d", "l", "r")
3. Movement is constrained by board boundaries and opponent pieces
4. The game ends when a player cannot make a legal move
5. In `WriterLockedChess`, the players can operation "f"\(declare failed\), "o"\(out of time\), "p"\(request for peace\), "y"\(agree to peace\) and "n"\(not agree to peace\)
6. When operation number is 4, players can operation "c" to withdraw the choice of the piece 

## JSON Format
Game state is represented as JSON with:
- `all_locate`: 12x12 board array (1=black, -1=white, 0=empty)
- `operation_number`: Current operation step (1-5)
- `operation_oppsite`: Current player color
- `operation_last_direction`: Last move direction
- `choose_chess_locate`: Selected piece coordinates and color
- `all_operation`: List of valid operations
       
# Change
## 2025.6.8

- Fix some bug.
- Use cache to speed up.

## 2025.6.9

- Fix some bug.
- `WriterLockedChess` now support more operations.
- Sure the thread safety of the cache.
