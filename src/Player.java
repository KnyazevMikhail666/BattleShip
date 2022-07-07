import java.util.HashMap;

public class Player {
    public String name;
    public int[] numberShips = new int[]{4, 3, 2, 0};//number of player ships
    public int step = 10;
    private int[] ships = new int[]{1, 1, 1, 1, 2, 2, 2, 3, 3, 4};
    int numberOfActions = 0;
    public boolean onlyUser = false;
    public boolean readyToPlay = false;
    public HashMap<Integer, HashMap<Integer, Integer>> playerMap = new HashMap<>();
    public HashMap<Integer, HashMap<Integer, Integer>> opponentMap;

    public Player(String s) {
        name = s;
        init();
    }

    public void init() {
        for (int i = 40; i < 293; i += 28) {
            HashMap<Integer, Integer> line = new HashMap<>();
            for (int j = 25; j < 278; j += 28) line.put(j, 0);
            playerMap.put(i, line);
        }
        //opponentMap.putAll(playerMap);
    }

    public boolean checkShip(Integer x, Integer y) {
        if (playerMap.isEmpty()) return false;
        else return playerMap.get(x).get(y) > 0;
    }

    public void writeShip(int[] x, int[] y, Integer ship) {
        for (int i = 0; i < x.length; i++) {
            if (x[i] != 0 && y[i] != 0) playerMap.get(x[i]).put(y[i], ship);
        }
    }

    public void nextShip() {
        if (step != -1) {
            step-=1;
        }
        numberShips[ships[step-1]-1] -=1 ;
    }
    public int getShip() {
        return step == 0 ? 0 : ships[step-1];
    }




}
