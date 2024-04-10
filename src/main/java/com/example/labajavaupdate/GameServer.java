package com.example.labajavaupdate;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class GameServer {
    private ServerSocket serverSocket;

    public GameServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public static void main(String[] args) {
        int port = 3124;
        try{
            GameServer server = new GameServer(port);
            server.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void run() {
        while (true) {
            try {
                Socket client = serverSocket.accept();
                System.out.println("Просто подключается к " + client.getRemoteSocketAddress());

                DataInputStream in = new DataInputStream(client.getInputStream());
                String nickname = in.readUTF();
                System.out.println("Получен никнейм: " + nickname);

                DataOutputStream out = new DataOutputStream(client.getOutputStream());
                System.out.println("Я В GAMESERVER");
                out.writeUTF("OK");


            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
