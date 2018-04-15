import javax.websocket.DeploymentException;
import java.util.Scanner;

public class MyServer {

    public static void main(String[] args) {
        org.glassfish.tyrus.server.Server server =
                new org.glassfish.tyrus.server.Server("localhost", 8025, "/ws", null, MyServerEndpoint.class);
        try {
            server.start();
            System.out.println("Press any key to stop the server..");
            new Scanner(System.in).nextLine();
        } catch (DeploymentException e) {
            throw new RuntimeException(e);
        } finally {
            server.stop();
        }
    }

}