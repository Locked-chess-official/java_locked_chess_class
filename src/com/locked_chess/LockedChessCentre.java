package com.locked_chess;
// LockedChessCentre.java

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

@SuppressWarnings("ALL")
public class LockedChessCentre {
    public static interface LockedChessAllInterface {
        // 开始游戏的方法
        public void gameStart();

        // 同gameStart方法
        public void game_start();

        // 返回一个Object类型的列表，表示合法的操作
        public List<Object> legalOperation();

        // 同legalOperation方法
        public List<Object> legal_operation();

        // 开始操作，参数为x
        public void startOperation(Object x);

        // 同startOperation方法
        public void operation(Object x);

        // 返回游戏完整状态信息
        public String returnGame();

        // 同returnGame方法
        public String return_game();

        // 加载游戏状态，参数为x和restart_game，其中restart_game表示是否重启游戏，x为String类型
        public void loadsGame(String x, boolean restart_game);

        // 同loadsGame方法
        public void loads_game(String x, boolean restart_game);

        // 加载游戏状态，参数为x，不重启游戏
        public void loadsGame(String x);

        // 同loadsGame方法，不重启游戏
        public void loads_game(String x);

        // 加载游戏状态，参数为x和restart_game，其中restart_game表示是否重启游戏，x为JSONObject类型
        public void loadsGame(JSONObject x, boolean restart_game);

        // 同loadsGame方法，参数为x和restart_game，其中restart_game表示是否重启游戏，x为JSONObject类型
        public void loads_game(JSONObject x, boolean restart_game);

        // 加载游戏状态，参数为x，不重启游戏，x为JSONObject类型
        public void loadsGame(JSONObject x);

        // 同loadsGame方法，不重启游戏，x为JSONObject类型
        public void loads_game(JSONObject x);

        // 计算所有可能的操作链。机器应该选择一个链执行链中全部操作
        public List<List<Object>> calculateAllChains();

        // 同calculateAllChains方法
        public List<List<Object>> calculate_all_chains();

        // 判断两个操作链是否等效
        public boolean isEqualChain(List<Object> chain1, List<Object> chain2);

        // 同isEqualChain方法
        public boolean is_equal_chain(List<Object> chain1, List<Object> chain2);

        // 返回游戏棋子集合
        public Set<LockedChess.ChessPiece> getGame();

        // 同getGame方法
        public Set<LockedChess.ChessPiece> get_game();

        // 返回操作序数
        public int getOperationNumber();

        // 同getOperationNumber方法
        public int get_operation_number();

        // 返回操作方
        public String getOperationOppsite();

        // 同getOperationOppsite方法
        public String get_operation_oppsite();

        // 返回当前选择的棋子
        public LockedChess.ChessPiece getChooseChessLocate();

        // 同getChooseChessLocate方法
        public LockedChess.ChessPiece get_choose_chess_locate();

        // 返回上一步操作的方向（仅限“上下”或“左右”，且仅getOperationNumber为2或5时显示）
        public String getOperationLastDirection();

        // 同getOperationLastDirection方法
        public String get_operation_last_direction();

        // 返回当前所有可行操作
        public List<Object> getAllOperation();

        // 同getAllOperation方法
        public List<Object> get_all_operation();

        // 返回当前是否已经操作过
        public boolean getHasOperated();

        // 同getHasOperated方法
        public boolean get_has_operated();

        // 上锁(必须与unlockGame方法配对使用，应该将unlockGame方法放在finally中)
        public void lockGame();

        // 解锁(必须先调用lockGame方法，且必须在finally中调用)
        public void unlockGame();
    }

    public static interface WriterLockedChessAllInterface extends LockedChessAllInterface {
        // 设置保护
        public void setProtect();

        // 取消保护
        public void setUnprotect();

        // 同getFinalResult方法
        public String get_final_result();

        // 返回最终结果
        public String getFinalResult();

        // 将当前游戏记录写入记录文件，参数为name，表示本局名称
        public void writeToRecord(String name);

        // 将当前游戏记录写入记录文件，不指定名称
        public void writeToRecord();

        public boolean getProtect();

        public boolean getPeace();
    }

    public static final class AllRecords {
        private static JSONObject allRecords = new JSONObject();

        public static void addRecord(WriterLockedChessAllInterface writerLockedChess, String name) {
            // 从指定游戏中添加记录，名字为name
            String recordTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            JSONObject json = new JSONObject(writerLockedChess.get_final_result());
            json.put("name", name);
            allRecords.put(recordTime, json);
        }

        public static void addRecord(WriterLockedChessAllInterface writerLockedChess) {
            // 从指定游戏中添加记录，名字为空
            addRecord(writerLockedChess, "");
        }

        public static void writeRecords(String filePath) throws IOException {
            // 将所有记录写入文件，文件名为filePath
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(allRecords.toString(2));
            } catch (IOException e) {
                throw new IOException(e.getMessage() + "please check the path");
            }
        }

        public static void readRecords(String filePath) throws IOException {
            // 从文件中读取所有记录，文件名为filePath
            try (FileReader reader = new FileReader(filePath)) {
                JSONTokener tokener = new JSONTokener(reader);
                allRecords = new JSONObject(tokener);
            } catch (IOException | JSONException e) {
                allRecords = new JSONObject();
                writeRecords(filePath);
            }
        }

        public static void deleteRecord(String recordTime, boolean isTime) {
            // 删除指定时间戳的记录
            if (isTime) {
                allRecords.remove(recordTime);
            } else {
                for (String time : allRecords.keySet()) {
                    if ((time + "\u2003" + allRecords.getJSONObject(time).getString("name")).equals(recordTime)) {
                        allRecords.remove(time);
                        break;
                    }
                }
            }
        }

        public static String getRecords() {
            return allRecords.toString();
        }

        private static final Map<String, String> getRecordsCache = new HashMap<>();
        private static final Map<String, Integer> getRecordsCacheCount = new HashMap<>();
        private static final Map<String, Long> getRecordsCacheTime = new HashMap<>();
        private static final ReentrantReadWriteLock getRecordsCacheLock = new ReentrantReadWriteLock();
        private static final ReentrantReadWriteLock getRecordCacheLock = new ReentrantReadWriteLock();

        static void clearReadRecordsCache() {
            getRecordCacheLock.writeLock().lock();
            getRecordsCacheLock.writeLock().lock();
            try {
                List<String> keysToRemove = new ArrayList<>(getRecordsCache.keySet());
                for (String key : keysToRemove) {
                    if (getRecordsCacheCount.get(key) < 10 && System.currentTimeMillis()
                            - getRecordsCacheTime.get(key) > 60000) {
                        getRecordsCache.remove(key);
                        getRecordsCacheCount.remove(key);
                        getRecordsCacheTime.remove(key);
                    }
                }
            } finally {
                getRecordCacheLock.writeLock().unlock();
                getRecordsCacheLock.writeLock().unlock();
            }
        }

        public static String readRecord(String boardRecord, String operationRecord, String timeRecord) {
            getRecordCacheLock.readLock().lock();
            try {
                if (getRecordsCache.containsKey(boardRecord + '&' + operationRecord + '&' + timeRecord)) {
                    getRecordsCacheLock.writeLock().lock();
                    try {
                        if (getRecordsCacheCount.containsKey(boardRecord + '&' + operationRecord + '&' + timeRecord)) {
                            getRecordsCacheCount.put(boardRecord + '&' + operationRecord + '&' + timeRecord,
                                    getRecordsCacheCount.get(boardRecord + '&' + operationRecord + '&' + timeRecord)
                                            + 1);
                        } else {
                            getRecordsCacheCount.put(boardRecord + '&' + operationRecord + '&' + timeRecord, 1);
                        }
                        getRecordsCacheTime.put(boardRecord + '&' + operationRecord + '&' + timeRecord,
                                System.currentTimeMillis());
                        return getRecordsCache.get(boardRecord + '&' + operationRecord + '&' + timeRecord);
                    } finally {
                        getRecordsCacheLock.writeLock().unlock();
                    }
                }
            } finally {
                getRecordCacheLock.readLock().unlock();
            }
            // 翻译记录
            String[] boardArray = boardRecord.split("#");
            String[] operationArray = operationRecord.split("#");
            String[] timeArray = timeRecord.split("#");
            JSONObject result = new JSONObject();
            int n = boardArray.length;
            if (n != operationArray.length || n != timeArray.length || n <= 2) {
                throw new IllegalArgumentException("Length of the record must be same and the length must be >2");
            }
            JSONObject oneStep;
            oneStep = new JSONObject();
            oneStep.put("board", new JSONObject(boardArray[0]));
            oneStep.put("operation", "无");
            oneStep.put("time", "0");
            result.put("0", oneStep);
            String[] timeStartAndEnd = timeArray[n - 1].split("\\$");
            String gameResult = operationArray[n - 1];
            result.put("startTime", timeStartAndEnd[0]);
            result.put("endTime", timeStartAndEnd[1]);
            result.put("gameResult", gameResult);
            int i;
            for (i = 1; i < n; i++) {
                oneStep = new JSONObject();
                oneStep.put("board", new JSONObject(boardArray[i]));
                oneStep.put("operation", operationArray[i - 1]);
                oneStep.put("time", timeArray[i - 1]);
                result.put(String.valueOf(i), oneStep);
            }
            result.put("step_number", i - 1);
            getRecordCacheLock.writeLock().lock();
            try {
                getRecordsCacheLock.writeLock().lock();
                try {
                    if (getRecordsCacheCount.containsKey(boardRecord + '&' + operationRecord + '&' + timeRecord)) {
                        getRecordsCacheCount.put(boardRecord + '&' + operationRecord + '&' + timeRecord,
                                getRecordsCacheCount.get(boardRecord + '&' + operationRecord + '&' + timeRecord) + 1);
                    } else {
                        getRecordsCacheCount.put(boardRecord + '&' + operationRecord + '&' + timeRecord, 1);
                    }
                    getRecordsCacheTime.put(boardRecord + '&' + operationRecord + '&' + timeRecord,
                            System.currentTimeMillis());
                    getRecordsCache.put(boardRecord + '&' + operationRecord + '&' + timeRecord, result.toString());
                    return result.toString();
                } finally {
                    getRecordsCacheLock.writeLock().unlock();
                }
            } finally {
                getRecordCacheLock.writeLock().unlock();
            }
        }

        public static void combineRecord() {
            if (OtherRecords.getHasCombined()) {
                return;
            }
            JSONObject combinedRecords = new JSONObject(OtherRecords.getRecords());
            for (String key : combinedRecords.keySet()) {
                allRecords.put(key, allRecords.getJSONObject(key));
            }
            OtherRecords.setHasCombined(true);
        }
    }

    public static void clearReadRecordsCache() {
        AllRecords.clearReadRecordsCache();
    }

    public static final class OtherRecords {
        private static JSONObject otherRecords = new JSONObject();
        private static boolean hasCombined = false;

        public static void readRecords(String filePath) throws IOException {
            // 从文件中读取其他记录，文件名为filePath
            try (FileReader reader = new FileReader(filePath)) {
                JSONTokener tokener = new JSONTokener(reader);
                otherRecords = new JSONObject(tokener);
                hasCombined = false;
            } catch (IOException | JSONException e) {
                throw new IOException("Failed to read records from file: " + filePath, e);
            }
        }

        public static String getRecords() {
            return otherRecords.toString();
        }

        public static boolean getHasCombined() {
            return hasCombined;
        }

        static void setHasCombined(boolean hasCombined) {
            OtherRecords.hasCombined = hasCombined;
        }
    }

    public static interface LockedChessRobotAllInterface {
        // 设置需要分析的游戏
        public void setGame(LockedChessAllInterface writerLockedChess);

        // 获取一个操作链，机器人会选择一个操作链执行
        public List<Object> getOperation();

        // 获取一个操作链，机器人会选择一个操作链执行。若usebetter为true，则会选择更好的操作链，否则执行的是最差的操作链
        public List<Object> getBetterOperation(boolean usebetter);
    }

    public static interface ChessPiece {
        @Override
        public boolean equals(Object o);

        @Override
        public int hashCode();

        @Override
        public String toString();

        // 返回棋子的x坐标
        public int getX();

        // 返回棋子的y坐标
        public int getY();

        // 返回棋子的颜色
        public String getColor();
    }

    public static LockedChessRobotAllInterface robot = new LockedChessRobot();

    public static LockedChessRobotAllInterface LockedChessRobotClass() {
        return new LockedChessRobot();
    }

    public static LockedChessRobotAllInterface LockedChessRobotClass(LockedChessAllInterface writerLockedChess) {
        LockedChessRobot result = new LockedChessRobot();
        result.setGame(writerLockedChess);
        return result;
    }

    public static class LockedChessClass implements LockedChessAllInterface {
        private final LockedChess lockedChessAllInterface = new LockedChess();

        @Override
        public void gameStart() {
            lockedChessAllInterface.gameStart();
        }

        @Override
        public void game_start() {
            lockedChessAllInterface.game_start();
        }

        @Override
        public List<Object> legalOperation() {
            return lockedChessAllInterface.legalOperation();
        }

        @Override
        public List<Object> legal_operation() {
            return lockedChessAllInterface.legal_operation();
        }

        @Override
        public void startOperation(Object x) {
            lockedChessAllInterface.startOperation(x);
        }

        @Override
        public void operation(Object x) {
            lockedChessAllInterface.operation(x);
        }

        @Override
        public String returnGame() {
            return lockedChessAllInterface.returnGame();
        }

        @Override
        public String return_game() {
            return lockedChessAllInterface.return_game();
        }

        @Override
        public void loadsGame(String x, boolean restart_game) {
            lockedChessAllInterface.loadsGame(x, restart_game);
        }

        @Override
        public void loads_game(String x, boolean restart_game) {
            lockedChessAllInterface.loads_game(x, restart_game);
        }

        @Override
        public void loadsGame(String x) {
            lockedChessAllInterface.loadsGame(x);
        }

        @Override
        public void loads_game(String x) {
            lockedChessAllInterface.loads_game(x);
        }

        @Override
        public void loadsGame(JSONObject x, boolean restart_game) {
            lockedChessAllInterface.loadsGame(x, restart_game);
        }

        @Override
        public void loads_game(JSONObject x, boolean restart_game) {
            lockedChessAllInterface.loads_game(x, restart_game);
        }

        @Override
        public void loadsGame(JSONObject x) {
            lockedChessAllInterface.loadsGame(x);
        }

        @Override
        public void loads_game(JSONObject x) {
            lockedChessAllInterface.loads_game(x);
        }

        @Override
        public List<List<Object>> calculateAllChains() {
            return lockedChessAllInterface.calculateAllChains();
        }

        @Override
        public List<List<Object>> calculate_all_chains() {
            return lockedChessAllInterface.calculate_all_chains();
        }

        @Override
        public boolean isEqualChain(List<Object> chain1, List<Object> chain2) {
            return lockedChessAllInterface.isEqualChain(chain1, chain2);
        }

        @Override
        public boolean is_equal_chain(List<Object> chain1, List<Object> chain2) {
            return lockedChessAllInterface.is_equal_chain(chain1, chain2);
        }

        @Override
        public Set<LockedChess.ChessPiece> getGame() {
            return lockedChessAllInterface.getGame();
        }

        @Override
        public Set<LockedChess.ChessPiece> get_game() {
            return lockedChessAllInterface.get_game();
        }

        @Override
        public int getOperationNumber() {
            return lockedChessAllInterface.getOperationNumber();
        }

        @Override
        public int get_operation_number() {
            return lockedChessAllInterface.get_operation_number();
        }

        @Override
        public String getOperationOppsite() {
            return lockedChessAllInterface.getOperationOppsite();
        }

        @Override
        public String get_operation_oppsite() {
            return lockedChessAllInterface.get_operation_oppsite();
        }

        @Override
        public LockedChess.ChessPiece getChooseChessLocate() {
            return lockedChessAllInterface.getChooseChessLocate();
        }

        @Override
        public LockedChess.ChessPiece get_choose_chess_locate() {
            return lockedChessAllInterface.get_choose_chess_locate();
        }

        @Override
        public String getOperationLastDirection() {
            return lockedChessAllInterface.getOperationLastDirection();
        }

        @Override
        public String get_operation_last_direction() {
            return lockedChessAllInterface.get_operation_last_direction();
        }

        @Override
        public List<Object> getAllOperation() {
            return lockedChessAllInterface.getAllOperation();
        }

        @Override
        public List<Object> get_all_operation() {
            return lockedChessAllInterface.get_all_operation();
        }

        @Override
        public boolean getHasOperated() {
            return lockedChessAllInterface.getHasOperated();
        }

        @Override
        public boolean get_has_operated() {
            return lockedChessAllInterface.get_has_operated();
        }

        @Override
        public void lockGame() {
            lockedChessAllInterface.lockGame();
        }

        @Override
        public void unlockGame() {
            lockedChessAllInterface.unlockGame();
        }
    }

    public static class WriterLockedChessClass implements WriterLockedChessAllInterface {
        private final WriterLockedChess writerLockedChess = new WriterLockedChess();

        @Override
        public void gameStart() {
            writerLockedChess.gameStart();
        }

        @Override
        public void game_start() {
            writerLockedChess.game_start();
        }

        @Override
        public List<Object> legalOperation() {
            return writerLockedChess.legalOperation();
        }

        @Override
        public List<Object> legal_operation() {
            return writerLockedChess.legal_operation();
        }

        @Override
        public void startOperation(Object x) {
            writerLockedChess.startOperation(x);
        }

        @Override
        public void operation(Object x) {
            writerLockedChess.operation(x);
        }

        @Override
        public String returnGame() {
            return writerLockedChess.returnGame();
        }

        @Override
        public String return_game() {
            return writerLockedChess.return_game();
        }

        @Override
        public void loadsGame(String x, boolean restart_game) {
            writerLockedChess.loadsGame(x, restart_game);
        }

        @Override
        public void loads_game(String x, boolean restart_game) {
            writerLockedChess.loads_game(x, restart_game);
        }

        @Override
        public void loadsGame(String x) {
            writerLockedChess.loadsGame(x);
        }

        @Override
        public void loads_game(String x) {
            writerLockedChess.loads_game(x);
        }

        @Override
        public void loadsGame(JSONObject x, boolean restart_game) {
            writerLockedChess.loadsGame(x, restart_game);
        }

        @Override
        public void loads_game(JSONObject x, boolean restart_game) {
            writerLockedChess.loads_game(x, restart_game);
        }

        @Override
        public void loadsGame(JSONObject x) {
            writerLockedChess.loadsGame(x);
        }

        @Override
        public void loads_game(JSONObject x) {
            writerLockedChess.loads_game(x);
        }

        @Override
        public List<List<Object>> calculateAllChains() {
            return writerLockedChess.calculateAllChains();
        }

        @Override
        public List<List<Object>> calculate_all_chains() {
            return writerLockedChess.calculate_all_chains();
        }

        @Override
        public boolean isEqualChain(List<Object> chain1, List<Object> chain2) {
            return writerLockedChess.isEqualChain(chain1, chain2);
        }

        @Override
        public boolean is_equal_chain(List<Object> chain1, List<Object> chain2) {
            return writerLockedChess.is_equal_chain(chain1, chain2);
        }

        @Override
        public Set<LockedChess.ChessPiece> getGame() {
            return writerLockedChess.getGame();
        }

        @Override
        public Set<LockedChess.ChessPiece> get_game() {
            return writerLockedChess.get_game();
        }

        @Override
        public int getOperationNumber() {
            return writerLockedChess.getOperationNumber();
        }

        @Override
        public int get_operation_number() {
            return writerLockedChess.get_operation_number();
        }

        @Override
        public String getOperationOppsite() {
            return writerLockedChess.getOperationOppsite();
        }

        @Override
        public String get_operation_oppsite() {
            return writerLockedChess.get_operation_oppsite();
        }

        @Override
        public LockedChess.ChessPiece getChooseChessLocate() {
            return writerLockedChess.getChooseChessLocate();
        }

        @Override
        public LockedChess.ChessPiece get_choose_chess_locate() {
            return writerLockedChess.get_choose_chess_locate();
        }

        @Override
        public String getOperationLastDirection() {
            return writerLockedChess.getOperationLastDirection();
        }

        @Override
        public String get_operation_last_direction() {
            return writerLockedChess.get_operation_last_direction();
        }

        @Override
        public List<Object> getAllOperation() {
            return writerLockedChess.getAllOperation();
        }

        @Override
        public List<Object> get_all_operation() {
            return writerLockedChess.get_all_operation();
        }

        @Override
        public boolean getHasOperated() {
            return writerLockedChess.getHasOperated();
        }

        @Override
        public boolean get_has_operated() {
            return writerLockedChess.get_has_operated();
        }

        @Override
        public void lockGame() {
            writerLockedChess.lockGame();
        }

        @Override
        public void unlockGame() {
            writerLockedChess.unlockGame();
        }

        @Override
        public void setProtect() {
            writerLockedChess.setProtect();
        }

        @Override
        public void setUnprotect() {
            writerLockedChess.setUnprotect();
        }

        @Override
        public String getFinalResult() {
            return writerLockedChess.getFinalResult();
        }

        @Override
        public String get_final_result() {
            return writerLockedChess.get_final_result();
        }

        @Override
        public void writeToRecord(String name) {
            writerLockedChess.writeToRecord(name);
        }

        @Override
        public void writeToRecord() {
            writerLockedChess.writeToRecord();
        }

        @Override
        public boolean getProtect() {
            return writerLockedChess.getProtect();
        }

        @Override
        public boolean getPeace() {
            return writerLockedChess.getPeace();
        }
    }

    // 创建一个ChessPiece对象，传入x、y坐标和颜色
    public static ChessPiece ChessPieceClass(int x, int y, String color) {
        // 返回一个LockedChess.ChessPiece对象，传入x、y坐标和颜色
        return new LockedChess.ChessPiece(x, y, color);
    }

    // 为机器人设置游戏
    public static void setGame(LockedChessAllInterface writerLockedChess) {
        robot.setGame(writerLockedChess);
    }

    public static void cleanLegalOperationCache() {
        LockedChess.cleanLegalOperationCache();
    }

    public static void cleanChainCache() {
        LockedChess.cleanChainCache();
    }
}

class LockedChess implements LockedChessCentre.LockedChessAllInterface {

    // Custom exception classes
    public static class GameHasNotStartedError extends RuntimeException {

        public GameHasNotStartedError(String message) {
            super(message);
        }
    }

    public static class IllegalLocationError extends RuntimeException {

        public IllegalLocationError(String message) {
            super(message);
        }
    }

    public static class IllegalOperationError extends RuntimeException {

        public IllegalOperationError(String message) {
            super(message);
        }
    }

    public static class GameIsOverError extends RuntimeException {

        public GameIsOverError(String message) {
            super(message);
        }
    }

    public static class IllegalChainError extends RuntimeException {

        public IllegalChainError(String message) {
            super(message);
        }
    }

    // Game state variables
    private Set<ChessPiece> game = new HashSet<>();
    private int operationNumber = 1;
    private String operationOppsite = "黑";
    private String operationLastDirection = "无";
    private List<Object> allOperation = new ArrayList<>();
    private ChessPiece chooseChessLocate = null;
    private boolean hasOperated = false;
    protected final ReentrantLock lock = new ReentrantLock();
    protected boolean inDfs = false;

    // Helper class for chess pieces
    public static class ChessPiece implements LockedChessCentre.ChessPiece {

        final int x;
        final int y;
        final String color;

        ChessPiece(int x, int y, String color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ChessPiece that = (ChessPiece) o;
            return x == that.x && y == that.y && color.equals(that.color);
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, color);
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + ",'" + color + "')";
        }

        @Override
        public int getX() {
            return x;
        }

        @Override
        public int getY() {
            return y;
        }

        @Override
        public String getColor() {
            return color;
        }
    }

    // Initialize the game
    @Override
    public void gameStart() {

        lock.lock();
        try {
            game = new HashSet<>(Arrays.asList(
                    new ChessPiece(8, 5, "白"),
                    new ChessPiece(5, 5, "黑"),
                    new ChessPiece(4, 4, "黑"),
                    new ChessPiece(9, 9, "黑"),
                    new ChessPiece(5, 8, "白"),
                    new ChessPiece(8, 8, "黑"),
                    new ChessPiece(9, 4, "白"),
                    new ChessPiece(4, 9, "白")));
            operationNumber = 1;
            operationOppsite = "黑";
            operationLastDirection = "无";
            allOperation = new ArrayList<>(Arrays.asList("u", "d", "l", "r"));
            chooseChessLocate = null;
            hasOperated = false;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void game_start() {
        gameStart();
    }

    private static final Map<String, List<Object>> legalOperationCache = new HashMap<>();
    private static final Map<String, Integer> legalOperationCacheCount = new HashMap<>();
    private static final Map<String, Long> legalOperationCacheTime = new HashMap<>();
    private static final ReentrantReadWriteLock legalOperationCacheCountLock = new ReentrantReadWriteLock();
    private static final ReentrantReadWriteLock legalOperationCacheLock = new ReentrantReadWriteLock();

    static void cleanLegalOperationCache() {
        legalOperationCacheLock.writeLock().lock();
        legalOperationCacheCountLock.writeLock().lock();
        try {
            List<String> keysToRemove = new ArrayList<>(legalOperationCache.keySet());
            for (String key : keysToRemove) {
                if (System.currentTimeMillis() - legalOperationCacheTime.get(key) > 60000 &&
                        legalOperationCacheCount.get(key) < 10) {
                    legalOperationCache.remove(key);
                    legalOperationCacheCount.remove(key);
                    legalOperationCacheTime.remove(key);
                }
            }
        } finally {
            legalOperationCacheCountLock.writeLock().unlock();
            legalOperationCacheLock.writeLock().unlock();
        }
    }

    // Get legal operations
    @Override
    public List<Object> legalOperation() {
        lock.lock();
        try {
            legalOperationCacheLock.readLock().lock();
            try {
                if (legalOperationCache.containsKey(returnGameWithoutAllOperation())) {
                    allOperation = new ArrayList<>(legalOperationCache.get(returnGameWithoutAllOperation()));
                    legalOperationCacheCountLock.writeLock().lock();
                    try {
                        if (legalOperationCacheCount.containsKey(returnGameWithoutAllOperation())) {
                            legalOperationCacheCount.put(returnGameWithoutAllOperation(),
                                    legalOperationCacheCount.get(returnGameWithoutAllOperation()) + 1);
                        } else {
                            legalOperationCacheCount.put(returnGameWithoutAllOperation(), 1);
                        }
                        legalOperationCacheTime.put(returnGameWithoutAllOperation(), System.currentTimeMillis());
                        return new ArrayList<>(allOperation);
                    } finally {
                        legalOperationCacheCountLock.writeLock().unlock();
                    }
                }
            } finally {
                legalOperationCacheLock.readLock().unlock();
            }
            allOperation = new ArrayList<>();

            // Check game state validity
            String checkResult = checkGame();
            if (!checkResult.equals("000")) {
                handleCheckError(checkResult);
            }

            List<String> directionSort = Arrays.asList("u", "d", "l", "r");

            switch (operationNumber) {
                case 1, 2 -> {
                    Map<String, Boolean> bloa = new HashMap<>();
                    Map<String, Boolean> wloa = new HashMap<>();
                    legalOperationAll(bloa, wloa);
                    if (operationOppsite.equals("黑")) {
                        for (String dir : bloa.keySet()) {
                            if (bloa.get(dir)
                                    && (operationNumber == 1
                                            || (operationNumber == 2
                                                    && ((Arrays.asList("u", "d").contains(dir)
                                                            && operationLastDirection.equals("左右"))
                                                            || (Arrays.asList("l", "r").contains(dir)
                                                                    && operationLastDirection.equals("上下")))))) {
                                allOperation.add(dir);
                            }
                        }
                    } else {
                        for (String dir : wloa.keySet()) {
                            if (wloa.get(dir)
                                    && (operationNumber == 1
                                            || (operationNumber == 2
                                                    && ((Arrays.asList("u", "d").contains(dir)
                                                            && operationLastDirection.equals("左右"))
                                                            || (Arrays.asList("l", "r").contains(dir)
                                                                    && operationLastDirection.equals("上下")))))) {
                                allOperation.add(dir);
                            }
                        }
                    }
                    allOperation.sort(Comparator.comparingInt(directionSort::indexOf));
                }
                case 4, 5 -> {
                    Map<ChessPiece, Map<String, Boolean>> bloo = new HashMap<>();
                    Map<ChessPiece, Map<String, Boolean>> wloo = new HashMap<>();
                    legalOperationOne(bloo, wloo);
                    if (operationOppsite.equals("黑")) {
                        for (String dir : bloo.get(chooseChessLocate).keySet()) {
                            if (bloo.get(chooseChessLocate).get(dir)
                                    && (operationNumber == 4
                                            || (operationNumber == 5
                                                    && ((Arrays.asList("u", "d").contains(dir)
                                                            && operationLastDirection.equals("左右"))
                                                            || (Arrays.asList("l", "r").contains(dir)
                                                                    && operationLastDirection.equals("上下")))))) {
                                allOperation.add(dir);
                            }
                        }
                    } else {
                        for (String dir : wloo.get(chooseChessLocate).keySet()) {
                            if (wloo.get(chooseChessLocate).get(dir)
                                    && (operationNumber == 4
                                            || (operationNumber == 5
                                                    && ((Arrays.asList("u", "d").contains(dir)
                                                            && operationLastDirection.equals("左右"))
                                                            || (Arrays.asList("l", "r").contains(dir)
                                                                    && operationLastDirection.equals("上下")))))) {
                                allOperation.add(dir);
                            }
                        }
                    }
                    allOperation.sort(Comparator.comparingInt(directionSort::indexOf));
                }
                case 3 -> {
                    for (ChessPiece piece : game) {
                        if (piece.color.equals(operationOppsite)) {
                            allOperation.add(piece);
                        }
                        Map<String, Integer> colourValue = new HashMap<>();
                        colourValue.put("黑", 0);
                        colourValue.put("白", 1);

                        allOperation.sort(Comparator.comparingInt(one_piece -> ((ChessPiece) one_piece).getX() * 26
                                + ((ChessPiece) one_piece).getY() * 2 +
                                colourValue.get(((ChessPiece) one_piece).getColor())));
                    }
                }
                default -> {
                }
            }
            legalOperationCacheLock.writeLock().lock();
            try {
                legalOperationCache.put(returnGameWithoutAllOperation(), new ArrayList<>(allOperation));
                legalOperationCacheCountLock.writeLock().lock();
                try {
                    if (legalOperationCacheCount.containsKey(returnGameWithoutAllOperation())) {
                        legalOperationCacheCount.put(returnGameWithoutAllOperation(),
                                legalOperationCacheCount.get(returnGameWithoutAllOperation()) + 1);
                    } else {
                        legalOperationCacheCount.put(returnGameWithoutAllOperation(), 1);
                    }
                    legalOperationCacheTime.put(returnGameWithoutAllOperation(),
                            System.currentTimeMillis());
                } finally {
                    legalOperationCacheCountLock.writeLock().unlock();
                }
            } finally {
                legalOperationCacheLock.writeLock().unlock();
            }
            return new ArrayList<>(allOperation);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<Object> legal_operation() {
        return legalOperation();
    }

    private String checkGame() {
        try {
            // Check basic game state
            if (game == null || operationNumber < 1 || operationNumber > 5
                    || !Arrays.asList("黑", "白").contains(operationOppsite)) {
                return "001";
            }

            // Check chess pieces
            int numberBlack = 0;
            int numberWhite = 0;
            Set<String> allLocate = new HashSet<>();

            for (ChessPiece piece : game) {
                // Check piece validity
                if (piece.x < 1 || piece.x > 12 || piece.y < 1 || piece.y > 12) {
                    return "003";
                }
                if (!piece.color.equals("黑") && !piece.color.equals("白")) {
                    return "003";
                }

                String locKey = piece.x + "," + piece.y;
                if (allLocate.contains(locKey)) {
                    return "003";
                }
                allLocate.add(locKey);

                if (piece.color.equals("黑")) {
                    numberBlack++;
                } else {
                    numberWhite++;
                }
            }

            if (numberBlack < 3 || numberWhite < 3) {
                return "002";
            }

            // Check operation state
            if ((operationNumber == 2 || operationNumber == 5)
                    && !Arrays.asList("上下", "左右").contains(operationLastDirection)) {
                return "002";
            }
            if ((operationNumber == 1 || operationNumber == 3 || operationNumber == 4)
                    && !operationLastDirection.equals("无")) {
                return "002";
            }

            if ((operationNumber == 4 || operationNumber == 5)
                    && (chooseChessLocate == null || !game.contains(chooseChessLocate))) {
                return "002";
            }
            if ((operationNumber == 1 || operationNumber == 2 || operationNumber == 3)
                    && chooseChessLocate != null) {
                return "002";
            }

            return "000";
        } catch (Exception e) {
            return "004";
        }
    }

    private static void handleCheckError(String errorCode) {
        String message;
        switch (errorCode) {
            case "001" -> {
                message = "The game has not started yet. Please start the game with 'gameStart()'.";
                throw new GameHasNotStartedError(message);
            }
            case "002" -> {
                message = "The game state is illegal.";
                throw new IllegalOperationError(message);
            }
            case "003" -> {
                message = "Chess location is illegal.";
                throw new IllegalLocationError(message);
            }
            case "004" -> {
                message = "Type error in game state.";
                throw new IllegalOperationError(message);
            }
            default -> {
                message = "Unknown error in game state.";
                throw new IllegalOperationError(message);
            }
        }
    }

    private void legalOperationAll(Map<String, Boolean> bloa, Map<String, Boolean> wloa) {
        // Initialize all directions to true
        for (String dir : Arrays.asList("u", "d", "l", "r")) {
            bloa.put(dir, true);
            wloa.put(dir, true);
        }

        for (ChessPiece piece : game) {
            if (piece.color.equals("黑")) {
                if (piece.y == 1) {
                    bloa.put("u", false);
                }
                if (piece.y == 12) {
                    bloa.put("d", false);
                }
                if (piece.x == 1) {
                    bloa.put("l", false);
                }
                if (piece.x == 12) {
                    bloa.put("r", false);
                }

                // Check for opponent pieces in adjacent positions
                for (ChessPiece other : game) {
                    if (other.color.equals("白")) {
                        if (other.x == piece.x && other.y == piece.y - 1) {
                            bloa.put("u", false);
                            wloa.put("d", false);
                        }
                        if (other.x == piece.x && other.y == piece.y + 1) {
                            bloa.put("d", false);
                            wloa.put("u", false);
                        }
                        if (other.x == piece.x - 1 && other.y == piece.y) {
                            bloa.put("l", false);
                            wloa.put("r", false);
                        }
                        if (other.x == piece.x + 1 && other.y == piece.y) {
                            bloa.put("r", false);
                            wloa.put("l", false);
                        }
                    }
                }
            } else {
                if (piece.y == 1) {
                    wloa.put("u", false);
                }
                if (piece.y == 12) {
                    wloa.put("d", false);
                }
                if (piece.x == 1) {
                    wloa.put("l", false);
                }
                if (piece.x == 12) {
                    wloa.put("r", false);
                }
            }
        }
    }

    private void legalOperationOne(Map<ChessPiece, Map<String, Boolean>> bloo,
            Map<ChessPiece, Map<String, Boolean>> wloo) {
        for (ChessPiece piece : game) {
            Map<String, Boolean> dirs = new HashMap<>();
            dirs.put("u", true);
            dirs.put("d", true);
            dirs.put("l", true);
            dirs.put("r", true);

            // Check boundaries
            if (piece.y == 1) {
                dirs.put("u", false);
            }
            if (piece.y == 12) {
                dirs.put("d", false);
            }
            if (piece.x == 1) {
                dirs.put("l", false);
            }
            if (piece.x == 12) {
                dirs.put("r", false);
            }

            // Check other pieces
            for (ChessPiece other : game) {
                if (other.x == piece.x && other.y == piece.y - 1) {
                    dirs.put("u", false);
                }
                if (other.x == piece.x && other.y == piece.y + 1) {
                    dirs.put("d", false);
                }
                if (other.x == piece.x - 1 && other.y == piece.y) {
                    dirs.put("l", false);
                }
                if (other.x == piece.x + 1 && other.y == piece.y) {
                    dirs.put("r", false);
                }
            }

            if (piece.color.equals("黑")) {
                bloo.put(piece, dirs);
            } else {
                wloo.put(piece, dirs);
            }
        }
    }

    // Perform an operation
    @Override
    public void startOperation(Object x) {
        lock.lock();
        try {
            List<Object> legalOps = legalOperation();
            if (legalOps.isEmpty()) {
                String msg = "The game is over, and operator '" + operationOppsite
                        + "' has failed. Please start a new game with 'gameStart()'";
                throw new GameIsOverError(msg);
            }

            if (x instanceof String && (x).equals("c")) {
                handleCancelOperation();
                return;
            }

            if (operationNumber == 3 && x instanceof String) {
                handleStringSelection((String) x);
                return;
            }

            if (!allOperation.contains(x)) {
                String msg = "Operation '" + x + "' is not in list 'allOperation'. Please check your operation.";
                throw new IllegalOperationError(msg);
            }

            if (operationNumber == 1 || operationNumber == 2 || operationNumber == 4 || operationNumber == 5) {
                if (x instanceof String string) {
                    changeGame(string);
                } else {
                    throw new IllegalArgumentException("Operation must be string if the operation number in (1,2,4,5)");
                }
            } else if (operationNumber == 3) {
                chooseChessLocate = (ChessPiece) x;
            }

            updateOperationState();
            hasOperated = true;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void operation(Object x) {
        startOperation(x);
    }

    private void handleCancelOperation() {
        if (operationNumber == 4 && hasOperated) {
            lock.lock();
            try {
                operationNumber = 3;
                chooseChessLocate = null;
            } finally {
                lock.unlock();
            }
        } else {
            String msg;
            if (operationNumber != 1 && hasOperated) {
                msg = "Can't be regret of the operation moving.";
            } else if (hasOperated) {
                msg = "Your turn has end, don't be regret.";
            } else {
                msg = "There is no operation has been taken. Can't be regret.";
            }
            throw new IllegalOperationError(msg);
        }
    }

    private void handleStringSelection(String x) {
        try {
            String[] parts = x.split(",");
            int xPos = Integer.parseInt(parts[0]);
            int yPos = Integer.parseInt(parts[1]);
            ChessPiece piece = new ChessPiece(xPos, yPos, operationOppsite);
            if (allOperation.contains(piece)) {
                startOperation(piece);
            } else {
                throw new IllegalOperationError("Invalid chess piece selection");
            }
        } catch (IllegalOperationError | NumberFormatException e) {
            throw new IllegalOperationError(
                    "operation '" + x + "' doesn't match the rule. Please check your operation");
        }
    }

    private void changeGame(String direction) {
        Set<ChessPiece> newGame = new HashSet<>();

        for (ChessPiece piece : game) {
            ChessPiece newPiece = new ChessPiece(piece.x, piece.y, piece.color);

            if (operationNumber == 1 || operationNumber == 2) {
                if (piece.color.equals(operationOppsite)) {
                    newPiece = movePiece(piece, direction);
                }
            } else if (operationNumber == 4 || operationNumber == 5) {
                if (piece.equals(chooseChessLocate)) {
                    newPiece = movePiece(piece, direction);
                }
            }

            newGame.add(newPiece);
        }

        game = newGame;

        if (operationNumber == 4) {
            chooseChessLocate = movePiece(chooseChessLocate, direction);
        }

        if (operationNumber == 1 || operationNumber == 4) {
            operationLastDirection = direction.equals("u") || direction.equals("d") ? "上下" : "左右";
        } else {
            operationLastDirection = "无";
        }
    }

    private static ChessPiece movePiece(ChessPiece piece, String direction) {
        int newX = piece.x;
        int newY = piece.y;

        switch (direction) {
            case "u" ->
                newY--;
            case "d" ->
                newY++;
            case "l" ->
                newX--;
            case "r" ->
                newX++;
        }

        return new ChessPiece(newX, newY, piece.color);
    }

    private void updateOperationState() {
        if (operationNumber != 5) {
            operationNumber++;
        } else {
            operationNumber = 1;
            chooseChessLocate = null;
            operationOppsite = operationOppsite.equals("黑") ? "白" : "黑";
        }
    }

    // Return game state as JSON string
    @Override
    public String returnGame() {
        lock.lock();
        try {
            legalOperation();
            int[][] state = new int[12][12];

            for (ChessPiece piece : game) {
                int row = piece.y - 1;
                int col = piece.x - 1;
                state[row][col] = piece.color.equals("黑") ? 1 : -1;
            }

            JSONObject json = new JSONObject();
            json.put("all_locate", new JSONArray(state));
            json.put("operation_number", operationNumber);
            json.put("operation_oppsite", operationOppsite);
            json.put("operation_last_direction", operationLastDirection);

            if (chooseChessLocate != null) {
                json.put("choose_chess_locate",
                        new JSONArray(Arrays.asList(
                                chooseChessLocate.x,
                                chooseChessLocate.y,
                                chooseChessLocate.color)));
            } else {
                json.put("choose_chess_locate", new JSONArray(Arrays.asList(-1, -1, "无")));
            }

            json.put("all_operation", new JSONArray(allOperation));

            return json.toString();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String return_game() {
        return returnGame();
    }

    private String returnGameWithoutAllOperation() {
        lock.lock();
        try {
            int[][] state = new int[12][12];
            for (ChessPiece piece : game) {
                int row = piece.y - 1;
                int col = piece.x - 1;
                state[row][col] = piece.color.equals("黑") ? 1 : -1;
            }
            JSONObject json = new JSONObject();
            json.put("all_locate", new JSONArray(state));
            json.put("operation_number", operationNumber);
            json.put("operation_oppsite", operationOppsite);
            json.put("operation_last_direction", operationLastDirection);

            if (chooseChessLocate != null) {
                json.put("choose_chess_locate",
                        new JSONArray(Arrays.asList(
                                chooseChessLocate.x,
                                chooseChessLocate.y,
                                chooseChessLocate.color)));
            } else {
                json.put("choose_chess_locate", new JSONArray(Arrays.asList(-1, -1, "无")));
            }

            return json.toString();
        } finally {
            lock.unlock();
        }

    }

    // Load game state from JSON string
    @Override
    public void loadsGame(String x, boolean restart_game) {
        lock.lock();
        try {
            JSONObject allInformation = new JSONObject(x);

            // Load basic operation info
            operationNumber = allInformation.getInt("operation_number");
            operationOppsite = allInformation.getString("operation_oppsite");
            operationLastDirection = allInformation.getString("operation_last_direction");

            JSONArray chooseLoc = allInformation.getJSONArray("choose_chess_locate");
            if (chooseLoc.getInt(0) != -1) {
                chooseChessLocate = new ChessPiece(
                        chooseLoc.getInt(0),
                        chooseLoc.getInt(1),
                        chooseLoc.getString(2));
            } else {
                chooseChessLocate = null;
            }

            // Load game state
            game = new HashSet<>();
            if (allInformation.has("all_locate")) {
                JSONArray allLocate = allInformation.getJSONArray("all_locate");
                if (allLocate.length() != 12) {
                    throw new JSONException("Invalid board size");
                }

                for (int i = 0; i < 12; i++) {
                    JSONArray row = allLocate.getJSONArray(i);
                    if (row.length() != 12) {
                        throw new JSONException("Invalid row size");
                    }

                    for (int j = 0; j < 12; j++) {
                        int val = row.getInt(j);
                        if (val == 1) {
                            game.add(new ChessPiece(j + 1, i + 1, "黑"));
                        } else if (val == -1) {
                            game.add(new ChessPiece(j + 1, i + 1, "白"));
                        }
                    }
                }
            } else if (allInformation.has("game")) {
                JSONArray gameArray = allInformation.getJSONArray("game");
                for (int i = 0; i < gameArray.length(); i++) {
                    JSONArray piece = gameArray.getJSONArray(i);
                    game.add(new ChessPiece(
                            piece.getInt(0),
                            piece.getInt(1),
                            piece.getString(2)));
                }
            } else {
                throw new JSONException("Missing game state");
            }

            // Validate the loaded game
            String checkResult = checkGame();
            if (!checkResult.equals("000")) {
                throw new IllegalOperationError("Invalid game state loaded");
            }

            if (restart_game) {
                hasOperated = false;
            }
        } catch (JSONException e) {
            throw new IllegalOperationError("Wrong JSON. Please check your input: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void loadsGame(String x) {
        loadsGame(x, false);
    }

    @Override
    public void loads_game(String x) {
        loadsGame(x);
    }

    @Override
    public void loads_game(String x, boolean restart_game) {
        loadsGame(x, restart_game);
    }

    @Override
    public void loadsGame(JSONObject x, boolean restart_game) {
        loadsGame(x.toString(), restart_game);
    }

    @Override
    public void loads_game(JSONObject x, boolean restart_game) {
        loadsGame(x.toString(), restart_game);
    }

    @Override
    public void loadsGame(JSONObject x) {
        loadsGame(x.toString());
    }

    @Override
    public void loads_game(JSONObject x) {
        loadsGame(x.toString());
    }

    // Calculate all possible operation chains

    private static final Map<String, List<List<Object>>> chainCache = new HashMap<>();
    private static final Map<String, Integer> chainCacheCount = new HashMap<>();
    private static final Map<String, Long> chainCacheTime = new HashMap<>();
    private static final ReentrantReadWriteLock chainCacheCountLock = new ReentrantReadWriteLock();
    private static final ReentrantReadWriteLock chainCacheLock = new ReentrantReadWriteLock();

    static void cleanChainCache() {
        chainCacheLock.writeLock().lock();
        chainCacheCountLock.writeLock().lock();
        try {
            List<String> keysToRemove = new ArrayList<>(chainCache.keySet());
            for (String key : keysToRemove) {
                if (System.currentTimeMillis() - chainCacheTime.get(key) > 60000 &&
                        chainCacheCount.get(key) < 10) {
                    chainCache.remove(key);
                    chainCacheCount.remove(key);
                    chainCacheTime.remove(key);
                }
            }
        } finally {
            chainCacheCountLock.writeLock().unlock();
            chainCacheLock.writeLock().unlock();
        }
    }

    @Override
    public List<List<Object>> calculateAllChains() {
        lock.lock();
        try {
            inDfs = true;
            chainCacheLock.readLock().lock();
            try {
                if (chainCache.containsKey(returnGame())) {
                    chainCacheCountLock.writeLock().lock();
                    try {
                        if (chainCacheCount.containsKey(returnGame())) {
                            chainCacheCount.put(returnGame(), chainCacheCount.get(returnGame()) + 1);
                        } else {
                            chainCacheCount.put(returnGame(), 1);
                        }
                        chainCacheTime.put(returnGame(), System.currentTimeMillis());
                    } finally {
                        chainCacheCountLock.writeLock().unlock();
                    }
                    return new ArrayList<>(chainCache.get(returnGame()));
                }
            } finally {
                chainCacheLock.readLock().unlock();
            }
            String initialState = returnGame();
            int requiredSteps = 6 - operationNumber;

            Set<String> visited = new HashSet<>();
            List<List<Object>> chains = new ArrayList<>();
            dfs(initialState, new ArrayList<>(), visited, chains, requiredSteps);
            chainCacheLock.writeLock().lock();
            try {
                chainCache.put(initialState, chains);
                chainCacheCountLock.writeLock().lock();
                try {
                    if (chainCacheCount.containsKey(initialState)) {
                        chainCacheCount.put(initialState, chainCacheCount.get(initialState) + 1);
                    } else {
                        chainCacheCount.put(initialState, 1);
                    }
                    chainCacheTime.put(initialState, System.currentTimeMillis());
                } finally {
                    chainCacheCountLock.writeLock().unlock();
                }
            } finally {
                chainCacheLock.writeLock().unlock();
            }
            return new ArrayList<>(chains);
        } finally {
            lock.unlock();
            inDfs = false;
        }
    }

    @Override
    public List<List<Object>> calculate_all_chains() {
        return calculateAllChains();
    }

    private void dfs(String stateJson, List<Object> path, Set<String> visited,
            List<List<Object>> chains, int requiredSteps) {

        LockedChess temp = new LockedChess();
        temp.loadsGame(stateJson);

        String stateKey = temp.getStateKey();
        if (visited.contains(stateKey))
            return;
        visited.add(stateKey);

        if (temp.getOperationNumber() == 1 && path.size() >= requiredSteps) {
            chains.add(new ArrayList<>(path.subList(0, requiredSteps)));
            return;
        }

        List<Object> legalOps = temp.legalOperation();
        if (legalOps.isEmpty() || path.size() >= requiredSteps + 5)
            return;

        for (Object op : legalOps) {
            LockedChess nextState = new LockedChess();
            nextState.loadsGame(stateJson);
            try {
                nextState.startOperation(op);
                path.add(op);
                dfs(nextState.returnGame(), path, visited, chains, requiredSteps);
                path.removeLast();
            } catch (Exception e) {
                // 跳过非法操作分支
                // 这里不需要处理异常，因为已经在dfs方法中处理了
            }
        }
    }

    private String getStateKey() {
        List<String> pieces = new ArrayList<>();
        for (ChessPiece piece : game) {
            pieces.add(piece.x + "," + piece.y + "," + piece.color);
        }
        Collections.sort(pieces);
        return String.join("|", pieces) + "|" + operationNumber + "|"
                + operationLastDirection + "|" + (chooseChessLocate != null ? chooseChessLocate.toString() : "null");
    }

    @Override
    // Check if two chains are equivalent
    public boolean isEqualChain(List<Object> chain1, List<Object> chain2) {
        lock.lock();
        try {
            String state1 = applyChain(chain1);
            String state2 = applyChain(chain2);
            return state1.equals(state2);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean is_equal_chain(List<Object> chain1, List<Object> chain2) {
        return isEqualChain(chain1, chain2);
    }

    private String applyChain(List<Object> chain) {
        String initialState = returnGame();
        try {
            inDfs = true;
            for (Object op : chain) {
                startOperation(op);
            }
            return returnGame();
        } catch (Exception e) {
            loadsGame(initialState);
            throw new IllegalChainError("Invalid chain for this game state");
        } finally {
            inDfs = false;
            loadsGame(initialState);
        }
    }

    // Getters for game state
    @Override
    public Set<ChessPiece> getGame() {
        legalOperation();
        return new HashSet<>(game);
    }

    @Override
    public Set<ChessPiece> get_game() {
        return getGame();
    }

    @Override
    public int getOperationNumber() {
        legalOperation();
        return operationNumber;
    }

    @Override
    public int get_operation_number() {
        return getOperationNumber();
    }

    @Override
    public String getOperationOppsite() {
        legalOperation();
        return operationOppsite;
    }

    @Override
    public String get_operation_oppsite() {
        return getOperationOppsite();
    }

    @Override
    public ChessPiece getChooseChessLocate() {
        legalOperation();
        return chooseChessLocate;
    }

    @Override
    public ChessPiece get_choose_chess_locate() {
        return getChooseChessLocate();
    }

    @Override
    public String getOperationLastDirection() {
        legalOperation();
        return operationLastDirection;
    }

    @Override
    public String get_operation_last_direction() {
        return getOperationLastDirection();
    }

    @Override
    public List<Object> getAllOperation() {
        return legalOperation();
    }

    @Override
    public List<Object> get_all_operation() {
        return getAllOperation();
    }

    @Override
    public boolean getHasOperated() {
        return hasOperated;
    }

    @Override
    public boolean get_has_operated() {
        return getHasOperated();
    }

    @Override
    public void lockGame() {
        lock.lock();
    }

    @Override
    public void unlockGame() {
        lock.unlock();
    }
}

class WriterLockedChess extends LockedChess implements LockedChessCentre.WriterLockedChessAllInterface {

    public static class GameIsNotOverError extends RuntimeException {

        public GameIsNotOverError(String message) {
            super(message);
        }
    }

    private boolean isProtected;
    private StringBuilder boardString;
    private StringBuilder operationString;
    private StringBuilder timeString;
    private String startTime;
    private long lastOperationTime;
    private static final Map<Integer, String> dictMap = Map.ofEntries(
            Map.entry(1, "1"),
            Map.entry(2, "2"),
            Map.entry(3, "3"),
            Map.entry(4, "4"),
            Map.entry(5, "5"),
            Map.entry(6, "6"),
            Map.entry(7, "7"),
            Map.entry(8, "8"),
            Map.entry(9, "9"),
            Map.entry(10, "A"),
            Map.entry(11, "B"),
            Map.entry(12, "C"));

    /*
     * All end strings
     * bw: black win
     * ww: white win
     * bf: black declare fail
     * wf: white declare fail
     * bo: black out of time
     * wo: white out of time
     * pp: peace
     */
    private static final List<String> allEndString = Arrays.asList(
            "bw", "ww", "bf", "wf", "bo", "wo", "pp");

    private boolean isEnd(String result) {
        if (result.length() < 2) {
            return false;
        }
        String finalString = result.substring(result.length() - 2);
        return allEndString.contains(finalString);
    }

    @Override
    public void gameStart() {
        lock.lock();
        try {
            if (isProtected) {
                return;
            }
            super.gameStart();
            boardString = new StringBuilder();
            boardString.append(return_game());
            operationString = new StringBuilder();
            startTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            timeString = new StringBuilder();
            lastOperationTime = System.currentTimeMillis();
            inDfs = false;
            isProtected = false;
            inPeace = false;
        } finally {
            lock.unlock();
        }

    }

    @Override
    public void setProtect() {
        lock.lock();
        try {
            isProtected = true;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void setUnprotect() {
        lock.lock();
        try {
            isProtected = false;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void startOperation(Object operation) {
        lock.lock();
        try {
            if (isProtected || isEnd(operationString.toString())) {
                return;
            }
            if (operation == null) {
                throw new IllegalOperationError("Operation cannot be null");
            }
            if (inPeace && (!(operation instanceof String) || (!operation.equals("y") && !operation.equals("n")))) {
                throw new IllegalOperationError("You can't use this operation when in peace.");
            }
            if ((operation instanceof String) && (operation.equals("f")
                    || operation.equals("o") || operation.equals("p") || operation.equals("y")
                    || operation.equals("n"))) {
                handleOtherOperation((String) operation);
                return;
            }
            super.startOperation(operation);
            if (!inDfs) {
                if ((getOperationOppsite().equals("黑") && getOperationNumber() != 1)
                        || (getOperationNumber() == 1 && getOperationOppsite().equals("白"))) {
                    operationString.append("b");
                } else {
                    operationString.append("w");
                }

                switch (operation) {
                    case LockedChess.ChessPiece chessPiece ->
                        operationString.append(dictMap.get(chessPiece.getX()))
                                .append(dictMap.get(chessPiece.getY()));
                    case String string -> {
                        if (getOperationNumber() == 4) {
                            String[] parts = string.split(",");
                            operationString.append(dictMap.get(Integer.valueOf(parts[0])))
                                    .append(dictMap.get(Integer.valueOf(parts[1])));
                        } else
                            operationString.append(string);
                    }
                    default ->
                        throw new IllegalOperationError("Invalid operation type");
                }
                normalWrite();
                if (legalOperation().isEmpty()) {
                    endWrite();
                    if (getOperationOppsite().equals("黑")) {
                        operationString.append("ww");
                    } else {
                        operationString.append("bw");
                    }
                }
            }
        } finally {
            lock.unlock();
        }
    }

    private void endWrite() {
        String endTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        timeString.append(startTime).append('$').append(endTime);
    }

    private void normalWrite() {
        operationString.append("#");
        long nextOperationTime = System.currentTimeMillis();
        timeString.append(
                String.format("%.3f", (double) (nextOperationTime - lastOperationTime) / 1000.0))
                .append("#");
        boardString.append('#').append(return_game());
        lastOperationTime = nextOperationTime;
    }

    private boolean inPeace = false;

    private void handleOtherOperation(String x) {
        switch (x) {
            case "f" -> {
                if (getOperationOppsite().equals("黑")) {
                    operationString.append("bf");
                } else {
                    operationString.append("wf");
                }
                endWrite();
            }
            case "o" -> {
                if (getOperationOppsite().equals("黑")) {
                    operationString.append("bo");
                } else {
                    operationString.append("wo");
                }
                endWrite();
            }
            case "p" -> {
                if (getOperationOppsite().equals("黑")) {
                    operationString.append("bp");
                } else {
                    operationString.append("wp");
                }
                normalWrite();
                inPeace = true;
            }
            case "y" -> {
                if (!inPeace) {
                    throw new IllegalOperationError("You can't use 'y' before 'p'.");
                }
                operationString.append("pp");
                endWrite();
                inPeace = false;
            }
            case "n" -> {
                if (!inPeace) {
                    throw new IllegalOperationError("You can't use 'n' before 'p'.");
                }
                if (getOperationOppsite().equals("黑")) {
                    operationString.append("wn");
                } else {
                    operationString.append("bn");
                }
                normalWrite();
                inPeace = false;
            }
            default -> throw new AssertionError();
        }
    }

    @Override
    public boolean getPeace() {
        lock.lock();
        try {
            return inPeace;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void loadsGame(String x, boolean restart_game) {
        lock.lock();
        try {
            if (isProtected || (isEnd(operationString.toString()) && !restart_game)) {
                return;
            }
            super.loadsGame(x, restart_game);
            if (restart_game) {
                boardString = new StringBuilder();
                boardString.append(return_game());
                operationString = new StringBuilder();
                startTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                timeString = new StringBuilder();
                lastOperationTime = System.currentTimeMillis();
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String get_final_result() {
        lock.lock();
        try {
            if (isEnd(operationString.toString())) {
                JSONObject json = new JSONObject();
                json.put("board", boardString.toString());
                json.put("operation", operationString.toString());
                json.put("time", timeString.toString());
                return json.toString();
            } else {
                throw new GameIsNotOverError("Game is not over yet");
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String getFinalResult() {
        return get_final_result();
    }

    @Override
    public void writeToRecord(String name) {
        if (isEnd(operationString.toString())) {
            LockedChessCentre.AllRecords.addRecord(this, name);
        } else {
            throw new GameIsNotOverError("Game is not over yet");
        }
    }

    @Override
    public void writeToRecord() {
        writeToRecord("");
    }

    @Override
    public boolean getProtect() {
        lock.lock();
        try {
            return isProtected;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<List<Object>> calculateAllChains() {
        lock.lock();
        try {
            boolean originalProtected = isProtected;
            setUnprotect(); // Temporarily unprotect to allow chain calculation
            try {
                return super.calculateAllChains();
            } finally {
                if (originalProtected) {
                    setProtect(); // Restore protection state
                }
            }

        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean isEqualChain(List<Object> chain1, List<Object> chain2) {
        lock.lock();
        try {
            boolean originalProtected = isProtected;
            setUnprotect(); // Temporarily unprotect to allow chain comparison
            try {
                return super.isEqualChain(chain1, chain2);
            } finally {
                if (originalProtected) {
                    setProtect(); // Restore protection state
                }
            }
        } finally {
            lock.unlock();
        }
    }
}

class LockedChessRobot implements LockedChessCentre.LockedChessRobotAllInterface {
    private LockedChessCentre.LockedChessAllInterface lockedChess;

    @Override
    public void setGame(LockedChessCentre.LockedChessAllInterface lockedChess) {
        this.lockedChess = lockedChess;
    }

    @Override
    public List<Object> getOperation() {
        lockedChess.lockGame();
        try {
            List<List<Object>> chains = lockedChess.calculate_all_chains();
            if (chains.isEmpty()) {
                return new ArrayList<>();
            } else {
                List<List<Object>> copychains = new ArrayList<>(chains);
                for (List<Object> chain : copychains) {
                    LockedChess temp = new LockedChess();
                    temp.loads_game(lockedChess.return_game(), true);
                    try {
                        for (Object operation : chain) {
                            temp.startOperation(operation);
                        }

                    } catch (Exception e) {
                        // Ignore this chain if it leads to an illegal state
                        chains.remove(chain);
                        continue;
                    }
                    if (temp.legal_operation().isEmpty()) {
                        return chain;
                    }
                }
                Random random = new Random();
                List<Object> returnChain;
                do {
                    returnChain = chains.get(random.nextInt(chains.size()));
                    chains.remove(returnChain);
                } while (dueToFail(returnChain));
                return returnChain;
            }
        } finally {
            lockedChess.unlockGame();
        }
    }

    private boolean dueToFail(List<Object> chain) {
        lockedChess.lockGame();
        try {
            LockedChess temp = new LockedChess();
            temp.loads_game(lockedChess.return_game(), true);
            for (Object operation : chain) {
                temp.startOperation(operation);
            }
            List<Integer> allX = new ArrayList<>();
            List<Integer> allY = new ArrayList<>();
            for (LockedChess.ChessPiece piece : temp.getGame()) {
                if (piece.getColor() == null ? lockedChess.get_operation_oppsite() == null
                        : piece.getColor().equals(lockedChess.get_operation_oppsite())) {
                    allX.add(piece.getX());
                    allY.add(piece.getY());
                }

            }
            int minusX = Collections.max(allX) - Collections.min(allX);
            int minusY = Collections.max(allY) - Collections.min(allY);
            return minusX >= 11 || minusY >= 11;
        } catch (Exception e) {
            // If any error occurs, treat it as a failure
            return true;
        } finally {
            lockedChess.unlockGame();
        }
    }

    private boolean justWin(List<Object> chain) {
        lockedChess.lockGame();
        try {
            LockedChess temp = new LockedChess();
            temp.loads_game(lockedChess.return_game(), true);
            try {
                for (Object operation : chain) {
                    temp.startOperation(operation);
                }
            } catch (Exception e) {
                return false;
            }
            List<List<Object>> newChains = temp.calculate_all_chains();
            List<List<Object>> newChainsCopy = new ArrayList<>(newChains);
            for (List<Object> newChain : newChainsCopy) {
                LockedChess temp2 = new LockedChess();
                temp2.loads_game(temp.return_game(), true);
                try {
                    for (Object operation : newChain) {
                        temp2.startOperation(operation);
                    }
                } catch (Exception e) {
                    // Ignore this chain if it leads to an illegal state
                    newChains.remove(newChain);
                    continue;
                }
                if (temp2.legal_operation().isEmpty()) {
                    return true;
                }
            }
            return false;
        } finally {
            lockedChess.unlockGame();
        }
    }

    @Override
    public List<Object> getBetterOperation(boolean usebetter) {
        lockedChess.lockGame();
        try {
            List<List<Object>> chains = lockedChess.calculate_all_chains();
            List<List<Object>> copyChains = new ArrayList<>(chains);
            if (chains.isEmpty()) {
                return new ArrayList<>();
            } else {
                for (List<Object> chain : copyChains) {
                    LockedChess temp = new LockedChess();
                    temp.loads_game(lockedChess.return_game(), true);
                    try {
                        for (Object operation : chain) {
                            temp.startOperation(operation);
                        }
                    } catch (Exception e) {
                        // Ignore this chain if it leads to an illegal state
                        chains.remove(chain);
                        continue;
                    }
                    if (temp.legal_operation().isEmpty()) {
                        return chain;
                    }
                }
            }
            List<List<Object>> copyChain = new ArrayList<>(chains);
            Random random = new Random();
            List<Object> returnChain;
            do {
                returnChain = copyChain.get(random.nextInt(copyChain.size()));
                copyChain.remove(returnChain);
                if (dueToFail(returnChain)) {
                    continue;
                }
                if (justWin(returnChain)) {
                    if (!usebetter) {
                        return returnChain;
                    }
                } else {
                    if (usebetter) {
                        return returnChain;
                    }
                }

            } while (!copyChain.isEmpty());
            do {
                returnChain = chains.get(random.nextInt(chains.size()));
                chains.remove(returnChain);
            } while (dueToFail(returnChain));
            return returnChain;
        } finally {
            lockedChess.unlockGame();
        }
    }
}
