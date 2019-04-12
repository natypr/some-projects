package by.naty.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

public class Server {
    //list of users
    static LinkedList<ServerDo> serverListThread = new LinkedList<>();
    static Story story;

    public static void main(String[] args) throws IOException {
        ServerSocket server;
        while (true) {
            Scanner inPortServer = new Scanner(System.in);
            System.out.print("Input port (Server): ");
            int port = inPortServer.nextInt();
            try {
                server = new ServerSocket(port, 10);
                break;
            } catch (Exception e) {
                System.out.println("Port is busy");
            }
        }

        story = new Story();
        System.out.println("- Server Started -");
        try {
            while (true) {
                Socket socket = server.accept();
                try {
                    serverListThread.add(new ServerDo(socket));
                } catch (IOException e) {
                    socket.close();
                }
            }
        } finally {
            server.close();
        }
    }
}
