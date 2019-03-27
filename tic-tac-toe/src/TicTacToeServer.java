import java.net.ServerSocket;

public class TicTacToeServer {

    public static void main(String[] args) {
        ServerSocket listener = null;
        try {
            listener = new ServerSocket(9090);
            System.out.println("- Server is Running -");
            System.out.println(listener.getLocalPort());

            while (true) {
                Game game = new Game();
                Game.Player playerX = game.new Player(listener.accept(), 'X');
                Game.Player playerO = game.new Player(listener.accept(), 'O');
                playerX.setOpponent(playerO);
                playerO.setOpponent(playerX);
                game.currentPlayer = playerX;
                playerX.start();
                playerO.start();
            }
        } catch (Exception ignored) {

        } finally {
            if (listener != null) {
                try {
                    listener.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}

