import java.io.*;
import java.net.*;

public class Server {
    private static HashTable data = new HashTable(10);

        public static void main(String[] args) {
            startServer();
        }

        private static void startServer() {
            try (ServerSocket serverSocket = new ServerSocket(8080)) {
                System.out.println("Сервер запущен на порту 8080");
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Клиент присоединен: " + clientSocket.getInetAddress());
                    handleClient(clientSocket);
                }
            } catch (IOException e) {
                System.err.println("Ошибка при запуске сервера: " + e.getMessage());
            }
        }

        private static void handleClient(Socket clientSocket) {
            new Thread(() -> {
                try (
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
                ) {
                    String line;
                    while ((line = in.readLine()) != null) {
                        if ("Выход".equals(line)) {
                            out.println("Всего ХоРоШеГо");
                            break;
                        }
                        processCommand(line, out);
                    }
                } catch (IOException e) {
                    System.err.println("Ошибка обработки клиента: " + e.getMessage());
                } finally {
                    closeSocket(clientSocket);
                }
            }).start();
        }

        private static void processCommand(String commandLine, PrintWriter out) {
            String[] parts = commandLine.split(" ");
            String command = parts[0].toLowerCase();

            try {
                switch (command) {
                    case "set":
                        handleSetCommand(parts, out);
                        break;
                    case "get":
                        handleGetCommand(parts, out);
                        break;
                    case "del":
                        handleDeleteCommand(parts, out);
                        break;
                    default:
                        out.println("Неизвестная команда");
                }
            } catch (IllegalArgumentException e) {
                out.println("Неверный формат команды: " + e.getMessage());
            }
        }

        private static void handleSetCommand(String[] parts, PrintWriter out) {
            if (parts.length != 3) {
                out.println("Необходимо указать ключ и значение (set key value)");
            }
            else {
            data.put(parts[1], parts[2]);
            out.println("OK");
        }}

        private static void handleGetCommand(String[] parts, PrintWriter out) {
            if (parts.length != 2) {
                out.println("Необходимо указать ключ (get key)");
            }else {
                String value = data.get(parts[1]);
                if (value == null) {
                    out.println("Ключ не найден");
                } else {
                    out.println(value);
                }
            }
        }

        private static void handleDeleteCommand(String[] parts, PrintWriter out) {
            if (parts.length != 2) {
                out.println("Необходимо указать ключ (del key)");
            } else {
                if (data.delete(parts[1])) {
                    out.println("OK");
                } else {
                    out.println("Ключ не найден");
                }
            }

        }

        private static void closeSocket(Socket socket) {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Ошибка при закрытии сокета: " + e.getMessage());
            }
        }
    }