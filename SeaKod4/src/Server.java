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
                        if ("Выход".equals(line)) {
                            out.println("Всего ХоРоШеГо!");
                            break;
                        }


                        System.out.println("Получено: " + line);
                        String[] parts = line.split(" ");
                        String command = parts[0];

                        switch (command) {
                            case "set":
                                if (parts.length != 3) {
                                    out.println("Какой-то Invalid ты ввел");
                                } else {
                                    data.put(parts[1], parts[2]);
                                    out.println("OK");
                                }
                                break;
                            case "get":
                                if (parts.length != 2) {
                                    out.println("какой-то Invalid ты ввел");
                                } else {
                                    String value = data.get(parts[1]);
                                    if (value == null) {
                                        out.println("Ключ не найден");
                                    } else {
                                        out.println(value);
                                    }
                                }
                                break;
                            case "del":
                                if (parts.length != 2) {
                                    out.println("какой-то Invalid ты ввел");
                                } else {
                                    if (data.delete(parts[1])) {
                                        out.println("OK");
                                    } else {
                                        out.println("Ключ не найден");
                                    }
                                }
                                break;
                            default:
                                out.println("Что это?");
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