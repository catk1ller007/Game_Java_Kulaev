package com.example.labajavaupdate;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerHandler extends Thread {
    private final Gson gson = new Gson();
    private final GameClient gameClient;
    private Socket clientSocket;
    private final DataInputStream in;
    private final DataOutputStream out;
    public ServerSocket serverSocket;
    public ServerHandler(GameClient gameClient, Socket socket,
                         DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
        this.gameClient = gameClient;
        clientSocket = socket;
        in = dataInputStream;
        out = dataOutputStream;
        setDaemon(true);
        start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                clientSocket = serverSocket.accept();
                System.out.println("Просто подключается к " + clientSocket.getRemoteSocketAddress());

                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                String nickname = in.readUTF();
                System.out.println("Получен никнейм: " + nickname);

                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                System.out.println("Я В SERVERHendler");
                out.writeUTF("OK");

            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
