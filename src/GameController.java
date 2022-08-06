import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GameController {
    static class Pair<T> {
        private final T pairX;
        private final T pairY;

        public Pair(T x, T y) {
            this.pairX = x;
            this.pairY = y;
        }

        public T getX() {
            return this.pairX;
        }

        public T getY() {
            return this.pairY;
        }
    }

    public int moveAimInMenu(int y, String command) {
        if (command.equals("up")) y -= 40;
        if (command.equals("down")) y += 40;
        return y;
    }
    public int getPlayerShip(ModelPlayer player) {
        return player.step == 0 ? 0 : player.ships[player.step - 1];
    }
    public void nextShip(ModelPlayer player) {
        if (player.step != -1) {
            player.step -= 1;
        }
        player.numberShips[player.ships[player.step] - 1] -= 1;
    }
    private void writeShip(int[] x, int[] y, ModelPlayer player) {
        for (int i = 0; i < x.length; i++) {
            if (x[i] != 0 && y[i] != 0) player.mapToPlayer.get(x[i]).put(y[i], RenderBox.SHIP.ordinal());
        }
    }
    public void writeHitOrMissPlayers(int x, int y, boolean isHit, ModelPlayer player, ModelPlayer opponent){
        try {
            if(isHit){
                player.mapToOpponent.get(x).put(y,RenderBox.HIT.ordinal());
                opponent.mapToPlayer.get(x).put(y,RenderBox.HIT.ordinal());
            }
            else {
                player.mapToOpponent.get(x).put(y,RenderBox.MISS.ordinal());
                opponent.mapToPlayer.get(x).put(y,RenderBox.MISS.ordinal());
            }

        }catch (NullPointerException exs){
            System.out.println(exs.getMessage());
        }
    }
    public Pair<int[]> newShip(int[] x, int[] y, int length) {
        int boxStep = 25;
        for (int i = 0; i < length; i++) {
            x[i] = 40;
            y[i] = boxStep;
            boxStep += 28;
        }
        return new Pair<>(x, y);
    }
    public boolean checkShipInThePlayerMap(int x, int y, ModelPlayer player){
        return checkInTheMap(x,y, player.mapToPlayer, RenderBox.SHIP.ordinal());
    }
    public boolean checkEmptyBoxInTheOpponentMAp(int x, int y , ModelPlayer player){
        return checkInTheMap(x,y,player.mapToOpponent,RenderBox.EMPTY.ordinal());
    }
    private boolean checkInTheMap(int x, int y , Map<Integer, HashMap<Integer, Integer>> map , int box){
        try {
            if (map.isEmpty()) return false;
            else return map.get(x).get(y) == box;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public int getContentBoxInThePlayer(int x, int y, Map<Integer, HashMap<Integer, Integer>> map){
        if(map.isEmpty())return 0;
        else return map.get(x).get(y);
    }

    public ArrayList<ArrayList<Integer>> searchIsKill(int x, int y, ModelPlayer player) {
        int content = getContentBoxInThePlayer(x,y,player.mapToPlayer);
        ArrayList<ArrayList<Integer>> resaltList = new ArrayList<>();
        ArrayList<Integer> contentList = new ArrayList<>();
        ArrayList<Integer> xList = new ArrayList<>();
        ArrayList<Integer> yList = new ArrayList<>();

        if(content != 3) {
            xList.add(x);
            yList.add(y);
            for (int i = y - 28; i > 24; i -= 28) {    //up
                content = getContentBoxInThePlayer(x, i, player.mapToPlayer);
                if (content == 0 || content == 3) break;
                else {
                    contentList.add(content);
                    xList.add(x);
                    yList.add(i);
                }
            }
            for (int i = y + 28; i < 278; i += 28) { //down
                content = getContentBoxInThePlayer(x, i, player.mapToPlayer);
                if (content == 0 || content == 3) break;
                else {
                    contentList.add(content);
                    xList.add(x);
                    yList.add(i);
                }
            }
            for (int i = x - 28; i > 39; i -= 28) { // left
                content = getContentBoxInThePlayer(i, y, player.mapToPlayer);
                if (content == 0 || content == 3) break;
                else {
                    contentList.add(content);
                    xList.add(i);
                    yList.add(y);
                }
            }
            for (int i = x + 28; i < 299; i += 28) {  //right
                content = getContentBoxInThePlayer(i, y, player.mapToPlayer);
                if (content == 0 || content == 3) break;
                else {
                    contentList.add(content);
                    xList.add(i);
                    yList.add(y);
                }
            }


            boolean isKilling = contentList.stream().allMatch(s -> s == 2);
            if(isKilling){
                resaltList.add(xList);
                resaltList.add(yList);
            }
        }
        return resaltList;
    }

    public void checkDeadShip(int x, int y,ModelPlayer player, ModelPlayer opponent) {
        ArrayList<ArrayList<Integer>> list = searchIsKill(x,y,opponent);
        if (!list.isEmpty()) {

            List<Integer> xList =  list.get(0).stream().sorted().collect(Collectors.toList());
            List<Integer> yList =  list.get(1).stream().sorted().collect(Collectors.toList());

            if (xList.get(0).equals(xList.get(xList.size()-1))){
                for (int i = yList.get(0)-28; i < yList.get(yList.size()-1)+29; i+=28) {
                    writeHitOrMissPlayers(xList.get(0)-28,i,false,player,opponent);
                    writeHitOrMissPlayers(xList.get(0)+28,i,false,player,opponent);
                }
                writeHitOrMissPlayers(xList.get(0),yList.get(0)-28,false,player,opponent);
                writeHitOrMissPlayers(xList.get(0),yList.get(yList.size()-1)+28,false,player,opponent);
            }
            if(yList.get(0).equals(yList.get(yList.size()-1))){
                for (int i = xList.get(0)-28; i < xList.get(yList.size()-1)+29; i+=28) {
                    writeHitOrMissPlayers(i,yList.get(0)-28,false,player,opponent);
                    writeHitOrMissPlayers(i,yList.get(0)+28,false,player,opponent);
                }
                writeHitOrMissPlayers(xList.get(0)-28,yList.get(0),false,player,opponent);
                writeHitOrMissPlayers(xList.get(xList.size()-1)+28,yList.get(0),false,player,opponent);
            }

        }

    }
    public boolean checkCollision(int[] x, int[] y, ModelPlayer player) {
        int endShip = getPlayerShip(player)-1;
        if (x[0] == 0) return false;
        for (int i = 0; i < endShip; i++) if (checkShipInThePlayerMap(x[i],y[i], player)) return false;

        if (x[0] == x[endShip]) {
            for (int i = y[0] - 28; i < y[endShip] + 29; i += 28) {
                if (checkShipInThePlayerMap(x[0] - 28, i, player)) return false;
                if (checkShipInThePlayerMap(x[0] + 28, i,player)) return false;
            }
            if (checkShipInThePlayerMap(x[0], y[0] - 28, player)) return false;
            if (checkShipInThePlayerMap(x[0], y[endShip] + 28, player)) return false;
        }
        if (y[0] == y[endShip]) {
            for (int i = x[0] - 28; i < x[endShip] + 29; i += 28) {
                if (checkShipInThePlayerMap(i, y[0] - 28, player)) return false;
                if (checkShipInThePlayerMap(i, y[0] + 28, player)) return false;
            }
            if (checkShipInThePlayerMap(x[0] - 28, y[0], player)) return false;
            if (checkShipInThePlayerMap(x[endShip] + 28, y[0], player)) return false;
        }
        return true;
    }
    public void restartPLayers(ModelPlayer[] players){
        for(ModelPlayer player:players){
            player.mapToPlayer.clear();
            player.mapToOpponent.clear();
            player.numberOfActions = 0;
            player.readyToPlay = false;
            player.onlyUser = false;
            player.init();
        }
    }
    public Pair<int[]> moveShips(String command, int[] shipX, int[] shipY, ModelPlayer player) {
        int lengthShip = getPlayerShip(player);
        if (lengthShip > 0) {
            if ("left".equals(command) && shipX[0] > 40)
                for (int i = 0; i < lengthShip; i++) shipX[i] -= 28;
            if ("right".equals(command) && shipX[0] < 292 && shipX[lengthShip - 1] < 292)
                for (int i = 0; i < lengthShip; i++) shipX[i] += 28;
            if ("up".equals(command) && shipY[0] > 40 && shipY[lengthShip - 1] > 40)
                for (int i = 0; i < lengthShip; i++) shipY[i] -= 28;
            if ("down".equals(command) && shipY[lengthShip - 1] < 256 && shipY[0] < 256)
                for (int i = 0; i < lengthShip; i++) shipY[i] += 28;

            if ("enter".equals(command)) {
                nextShip(player);
                writeShip(shipX, shipY, player);
                for (int i = 0; i < shipY.length; i++) {
                    shipY[i] = 0;
                    shipX[i] = 0;
                }
            }

            if ("space".equals(command)) {
                for (int i = 1; i < lengthShip; i++) {
                    if (shipX[0] == shipX[lengthShip - 1]) {
                        shipY[i] = shipY[0];
                        shipX[i] = shipX[0] + (i * 28);
                        if (shipX[i] > 292) for (int j = lengthShip - 1; j != -1; j--) shipX[j] = shipX[j] - 28;

                    } else {
                        shipX[i] = shipX[0];
                        shipY[i] = shipY[0] + (i * 28);
                        if (shipY[i] > 277) for (int j = lengthShip - 1; j != -1; j--) shipY[j] = shipY[j] - 28;
                    }
                }
            }
        }
        return new Pair<>(shipX, shipY);
    }

}
