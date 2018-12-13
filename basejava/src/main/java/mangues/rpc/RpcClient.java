package mangues.rpc;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class RpcClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8080);
        OutputStream outputStream = socket.getOutputStream();
        String request = "mangues.tomcat.ProjectUtil";
        byte[] bytes = request.getBytes();
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                try {
                    outputStream.write(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        System.in.read();
    }
}
