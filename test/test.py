"""
Before running test.py, you need to download jdk-24 and install the package "jpype":
```bash
pip install Jpype1
```
"""
import jpype
import atexit

jpype.startJVM(classpath=['com.locked_chess.jar', 'org.json.jar'])
LockedChessCentre = jpype.JClass("com.locked_chess.LockedChessCentre")
LockedChessClass = LockedChessCentre.LockedChessClass
WriterLockedChessClass = LockedChessCentre.WriterLockedChessClass
ChessPieceClass = LockedChessCentre.ChessPieceClass
AllRecords = LockedChessCentre.AllRecords
LockedChessRobot = LockedChessCentre.LockedChessRobotClass

@atexit.register
def close_JVM():
  jpype.shutdownJVM()

def main():
  locked_chess_class = LockedChessClass()
  locked_chess_class.game_start()
  print(locked_chess_class.legal_operation())
  print(locked_chess_class.return_game())
  locked_chess_class.operation('u')
  locked_chess_class.operation('l')
  print(locked_chess_class.legal_operation())
  print(locked_chess_class.return_game())
  locked_chess_class.operation(locked_chess_class.legal_operation()[0])
  print(locked_chess_class.calculate_all_chains())

if __name__ == '__main__':
  main()
  
