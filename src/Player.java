import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Player {
    public String name;
    public int[] numberShips;
    public int step;
    private final int[] ships = new int[]{1, 1, 1, 1, 2, 2, 2, 3, 3, 4};
    public int allShips;
    int numberOfActions;
    public boolean onlyUser = false;
    public boolean readyToPlay = false;
    public HashMap<Integer, HashMap<Integer, Integer>> playerMap = new HashMap<>();
    public HashMap<Integer, HashMap<Integer, Integer>> opponentMap = new HashMap<>();

    public Player(String s, int n) {
        name = s;
        numberOfActions = n;

        init();
    }

    public void init() {
        allShips = Arrays.stream(ships).sum();
        numberShips = new int[]{4, 3, 2, 1};
        step = 10;

        for (int i = 40; i < 293; i += 28) {
            HashMap<Integer, Integer> line = new HashMap<>();
            HashMap<Integer, Integer> lineOpponent = new HashMap<>();
            for (int j = 25; j < 278; j += 28) {
                line.put(j, 0);
                lineOpponent.put(j, 0);
            }
            playerMap.put(i, line);
            opponentMap.put(i, lineOpponent);
        }
    }

    public void restart() {
        playerMap.clear();
        opponentMap.clear();
        numberOfActions = 0;
        readyToPlay = false;
        onlyUser = false;
        init();
    }

    private boolean check(Integer x, Integer y, int number, HashMap<Integer, HashMap<Integer, Integer>> map) {
        try {
            if (map.isEmpty()) return false;
            else return map.get(x).get(y) == number;
        } catch (NullPointerException e) {
            return false;
        }

    }

    public boolean checkShip(Integer x, Integer y) {
        return check(x, y, 1, playerMap);
    }

    public boolean checkShipOpponent(Integer x, Integer y) {
        return check(x, y, 0, opponentMap);
    }

    private boolean checkHit(Integer x, Integer y) {
        return check(x, y, 2, playerMap);
    }

    public int renderHitAndMiss(Integer x, Integer y, HashMap<Integer, HashMap<Integer, Integer>> map) {
        if (map.isEmpty()) return 0;
        else return map.get(x).get(y);
    }

    public void writeShip(int[] x, int[] y, Integer ship) {
        for (int i = 0; i < x.length; i++) {
            if (x[i] != 0 && y[i] != 0) playerMap.get(x[i]).put(y[i], ship);
        }
    }

    public void writeMissOrHit(int x, int y, boolean hit, boolean flag) {
        HashMap<Integer, HashMap<Integer, Integer>> dict = flag ? playerMap : opponentMap;
        try {
            if (hit) dict.get(x).put(y, 2);
            else dict.get(x).put(y, 3);
        }catch (NullPointerException exs) {
            System.out.println(exs);
        }
    }

    public ArrayList<ArrayList<Integer>> searchIsKill(int x, int y) {
        int content = renderHitAndMiss(x,y,playerMap);
        ArrayList<ArrayList<Integer>> resaltList = new ArrayList<>();
        ArrayList<Integer> contentList = new ArrayList<>();
        ArrayList<Integer> xList = new ArrayList<>();
        ArrayList<Integer> yList = new ArrayList<>();

        if(content != 3) {
            xList.add(x);
            yList.add(y);
            for (int i = y - 28; i > 24; i -= 28) {    //up
                content = renderHitAndMiss(x, i, playerMap);
                if (content == 0 || content == 3) break;
                else {
                    contentList.add(content);
                    xList.add(x);
                    yList.add(i);
                }
            }
            for (int i = y + 28; i < 278; i += 28) { //down
                content = renderHitAndMiss(x, i, playerMap);
                if (content == 0 || content == 3) break;
                else {
                    contentList.add(content);
                    xList.add(x);
                    yList.add(i);
                }
            }
            for (int i = x - 28; i > 39; i -= 28) { // left
                content = renderHitAndMiss(i, y, playerMap);
                if (content == 0 || content == 3) break;
                else {
                    contentList.add(content);
                    xList.add(i);
                    yList.add(y);
                }
            }
            for (int i = x + 28; i < 299; i += 28) {  //right
                content = renderHitAndMiss(i, y, playerMap);
                if (content == 0 || content == 3) break;
                else {
                    contentList.add(content);
                    xList.add(i);
                    yList.add(y);
                }
            }


            boolean isKilling = contentList.stream().allMatch(s -> s == 2);
            if(isKilling){
                System.out.println(contentList);
                resaltList.add(xList);
                resaltList.add(yList);
            }
        }
        return resaltList;
    }

    public void nextShip() {
        if (step != -1) {
            step -= 1;
        }
        numberShips[ships[step] - 1] -= 1;
    }

    public int getShip() {
        return step == 0 ? 0 : ships[step - 1];
    }


}
