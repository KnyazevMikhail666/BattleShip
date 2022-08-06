import java.util.Arrays;
import java.util.HashMap;

public class ModelPlayer {
    public String name;
    public int[] numberShips;
    public int step;
    public final int[] ships = new int[]{1, 1, 1, 1, 2, 2, 2, 3, 3, 4};
    public int allShips;
    int numberOfActions;
    public boolean onlyUser = false;
    public boolean readyToPlay = false;
    public HashMap<Integer, HashMap<Integer, Integer>> mapToPlayer = new HashMap<>();
    public HashMap<Integer, HashMap<Integer, Integer>> mapToOpponent = new HashMap<>();


    public ModelPlayer(String s, int n) {
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
                lineOpponent.put(j, RenderBox.EMPTY.ordinal());
            }
            mapToPlayer.put(i, line);
            mapToOpponent.put(i, lineOpponent);
        }
    }
}
