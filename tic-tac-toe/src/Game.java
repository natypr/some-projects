import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class Game {

    Player currentPlayer;
    private Player[] board = {
            null, null, null,
            null, null, null,
            null, null, null};

    private boolean winner() {
        return
                (board[0] != null && board[0] == board[1] && board[0] == board[2])
                        || (board[3] != null && board[3] == board[4] && board[3] == board[5])
                        || (board[6] != null && board[6] == board[7] && board[6] == board[8])

                        || (board[0] != null && board[0] == board[3] && board[0] == board[6])
                        || (board[1] != null && board[1] == board[4] && board[1] == board[7])
                        || (board[2] != null && board[2] == board[5] && board[2] == board[8])

                        || (board[0] != null && board[0] == board[4] && board[0] == board[8])
                        || (board[2] != null && board[2] == board[4] && board[2] == board[6]);
    }

    private boolean noEmptySquares() {
        for (Player player : board) {
            if (player == null) {
                return false;
            }
        }
        return true;
    }

    private synchronized boolean legalMove(int location, Player player) {
        if (player == currentPlayer && board[location] == null) {
            board[location] = currentPlayer;
            currentPlayer = currentPlayer.opponent;
            currentPlayer.otherPlayerMoved(location);
            return true;
        }
        return false;
    }

    class Player extends Thread {
        char mark;
        Player opponent;
        Socket socket;
        BufferedReader input;
        PrintWriter output;

        Player(Socket socket, char mark) {
            this.socket = socket;
            this.mark = mark;
            try {
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);
                output.println("WELCOME " + mark);
                output.println("MESSAGE Ожидание подключения противника");
            } catch (IOException e) {
                System.out.println("Player died: " + e);
            }
        }

        void setOpponent(Player opponent) {
            this.opponent = opponent;
        }

        void otherPlayerMoved(int location) {
            output.println("OPPONENT_MOVED " + location);
            output.println(winner() ? "DEFEAT" : noEmptySquares() ? "TIE" : "");
        }

        public void run() {
            try {
                output.println("MESSAGE Оба игрока подключено");

                if (mark == 'X') {
                    output.println("MESSAGE Ваш ход");
                }

                while (true) {
                    String command = input.readLine();
                    if (command.startsWith("MOVE")) {
                        int location = Integer.parseInt(command.substring(5));
                        if (legalMove(location, this)) {
                            output.println("VALID_MOVE");
                            output.println(winner() ? "VICTORY"
                                    : noEmptySquares() ? "TIE"
                                    : "");
                        } else {
                            output.println("MESSAGE Что???");
                        }
                    } else if (command.startsWith("QUIT")) {
                        return;
                    }
                }
            } catch (IOException e) {
                System.out.println("Player died: " + e);
            } finally {
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}