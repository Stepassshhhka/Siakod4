import java.io.*;
import java.net.*;

public class Client {

    public static void main(String[] args) throws IOException {
        Socket clientSocket = new Socket("localhost", 8080);
        System.out.println("Клиент подключился к серверу");

        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        ) {
            while (true) {
                System.out.print("Введите команду: ");
                String command = console.readLine();
                out.println(command);
                System.out.println("Отправелно: " + command);

                String response = in.readLine();
                System.out.println("Получено: " + response);
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
    }
}
