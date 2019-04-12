package by.naty.chat.client;

import by.naty.chat.view.Controller;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class ClientDo {
    private Socket socket;
    // read stream from socket
    private BufferedReader inSocket;
    private BufferedWriter outSocket;

    private String userName;

    private Optional<Controller> controller = Optional.empty();

    public ClientDo(String address, int port) {
        try {
            this.socket = new Socket(address, port);
        } catch (IOException e) {
            System.err.println("- Socket not running! -");
        }

        try {
            inSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outSocket = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            new ReadMessages().start();

        } catch (IOException e) {
            ClientDo.this.closeSocket();
        }
    }

    public void setController(Controller controller) {
        this.controller = Optional.of(controller);
    }

    public void registration(String name) {
        userName = name;
    }

    private void closeSocket() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                inSocket.close();
                outSocket.close();
            }
        } catch (IOException ignored) {
        }
    }

    public void sendMsg(String userStr) {
        System.out.println(userStr);
        try {
            Date date = new Date();
            SimpleDateFormat dtFormat = new SimpleDateFormat("HH:mm:ss '-' dd.MM.yy");
            String dtime = dtFormat.format(date);

            if (userStr.equals("/exit")) {
                String finalStr = "- User " + userName + " left chat! -";
                sendMsg(finalStr);
                outSocket.write("/exit" + "\n");

                ClientDo.this.closeSocket();
            } else {
                outSocket.write("[(" + dtime + ") " + userName + "]: " + userStr + "\n");
            }
            //push everything out of the buffer
            outSocket.flush();

        } catch (IOException e) {
            ClientDo.this.closeSocket();
            e.printStackTrace();
        }
    }

    /**
     * Thread reading messages from the socket.
     */
    private class ReadMessages extends Thread {
        @Override
        public void run() {
            String str;
            try {
                while (true) {
                    str = inSocket.readLine();
                    if (str.equals("/exit")) {
                        ClientDo.this.closeSocket();
                        break;
                    }
                    System.out.println(str);
                    String finalStr = str;

                    controller.ifPresent(controller -> controller.showMessage(finalStr));

                }
            } catch (IOException e) {
                ClientDo.this.closeSocket();
            }
        }
    }
}
