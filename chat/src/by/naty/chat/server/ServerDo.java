package by.naty.chat.server;

import java.io.*;
import java.net.Socket;

public class ServerDo extends Thread {
    private Socket socket;
    // read stream from socket
    private BufferedReader inSocket;
    // write stream to socket
    private BufferedWriter outSocket;


    ServerDo(Socket socket) throws IOException {
        this.socket = socket;
        inSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        outSocket = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        Server.story.printStory(outSocket);
        start();
    }

    @Override
    public void run() {
        String word;
        try {
            try {
                while (true) {
                    word = inSocket.readLine();
                    if (word.equals("/exit")) {
                        this.closeServer();
                        break;
                    }
                    System.out.println("Echo: " + word);
                    Server.story.addStory(word);
                    for (ServerDo m : Server.serverListThread) {
                        m.send(word);
                    }
                }
            } catch (NullPointerException ignored) {
            }
        } catch (IOException e) {
            this.closeServer();
        }
    }

    private void send(String msg) {
        try {
            outSocket.write(msg + "\n");
            outSocket.flush();
        } catch (IOException ignored) {
        }
    }

    private void closeServer() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                inSocket.close();
                outSocket.close();
                for (ServerDo m : Server.serverListThread) {
                    if (m.equals(this)) m.interrupt();
                    Server.serverListThread.remove(this);
                }
            }
        } catch (IOException ignored) {
        }
    }
}
