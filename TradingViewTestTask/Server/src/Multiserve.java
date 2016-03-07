import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Multiserve
{
    public static void main (String[] args)
    {
        try
        {
            ServerSocket server = new ServerSocket(8050);
            System.out.println("server started");
            //server.setSoTimeout(60000);
            while (true)
            {
                try
                {
                    Socket socket = server.accept();
                    ServeOne serve = new ServeOne(socket);
                }
                catch (Exception ex) { ex.printStackTrace();}
            }
        }
        catch (IOException IOex) { IOex.printStackTrace();}
    }
}
