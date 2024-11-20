import java.io.*;
import java.net.*;

public class Server {
    private static HashTable data = new HashTable(10);

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Сервер запущен на порту 8080");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Клиент присоединен: " + clientSocket.getInetAddress());

            new Thread(() -> {
                try (
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                ) {
                    String line;
                    while ((line = in.readLine()) != null) {
                        if (".".equals(line)) {
                            out.println("good bye");
                            break;
                        }


                        System.out.println("Received: " + line);
                        String[] parts = line.split(" ");
                        String command = parts[0];

                        switch (command) {
                            case "set":
                                if (parts.length != 3) {
                                    out.println("Invalid command format");
                                } else {
                                    data.put(parts[1], parts[2]);
                                    out.println("OK");
                                }
                                break;
                            case "get":
                                if (parts.length != 2) {
                                    out.println("Invalid command format");
                                } else {
                                    String value = data.get(parts[1]);
                                    if (value == null) {
                                        out.println("Key not found");
                                    } else {
                                        out.println(value);
                                    }
                                }
                                break;
                            case "del":
                                if (parts.length != 2) {
                                    out.println("Invalid command format");
                                } else {
                                    if (data.delete(parts[1])) {
                                        out.println("OK");
                                    } else {
                                        out.println("Key not found");
                                    }
                                }
                                break;
                            default:
                                out.println("Unknown command");
                                break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}