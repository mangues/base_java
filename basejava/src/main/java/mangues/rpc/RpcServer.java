package mangues.rpc;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class RpcServer {
    public static void main(String[] args) throws IOException {
        ServerSocket socketServer = new ServerSocket(8080);
        Socket accept = socketServer.accept();
        InputStream inputStream = accept.getInputStream();
        while (true) {
            byte[] bu = new byte[1024];
            inputStream.read(bu);
            System.out.println(new String(bu));
        }
    }
}
