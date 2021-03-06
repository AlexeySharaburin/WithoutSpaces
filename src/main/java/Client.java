import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {

        InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1",
                23456);

        SocketChannel socketChannel = SocketChannel.open();

        socketChannel.connect(socketAddress);

        try (Scanner scanner = new Scanner(System.in)) {

            final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);

            String inputString;
            while (true) {
                System.out.println("Введите, пожалуйста, строку символов с пробелами " +
                        "или 'end' для завершения работы:");
                inputString = scanner.nextLine();
                if ("end".equals(inputString)) {
                    System.out.println("Работа завершена!");
                    break;
                }

                socketChannel.write(
                        ByteBuffer.wrap(
                                inputString.getBytes(StandardCharsets.UTF_8)));

                Thread.sleep(3000);

                int bytesCount = socketChannel.read(inputBuffer);

                System.out.println("Ответ от сервера -> " + new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8));

                inputBuffer.clear();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            socketChannel.close();
        }
    }
}

