import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ServeOne extends Thread
{
    private Socket socket;
    private OutputStream os;
    private InputStream is;
    private Path path = Paths.get(System.getProperty("user.dir") + "\\DownloadableFiles");
    private final String delimiter = "#del#";
    ArrayList<String> q = new ArrayList<>();

    public ServeOne(Socket s) throws IOException
    {
        socket = s;
        os = s.getOutputStream();
        is = s.getInputStream();
        start();
    }
    public void run()
    {
        try
        {
            System.out.println("waiting for connection...");
            DataOutputStream outD = new DataOutputStream(os);
            System.out.println("sending available files list...");
            q = ListFiles(path);
            String[] b = new String[q.size()];
            for (int i = 0; i < b.length; i++) { b[i] = q.get(i); }
            outD.writeUTF(msg("listfiles", b));

            outD.flush();
            String[] readyfiles = null;
//region waiting for filelist
            DataInputStream inD = new DataInputStream(is);
            while(true)
            {
                System.out.println("waiting for chosen files list...");
                String message = inD.readUTF();
                if (message.equals("!exit!")) break;
                else if (message.contains("download"))
                {
                    ArrayList<String> chosenfiles = new ArrayList<>();
                    String temp = "";
                    message = message.substring(message.indexOf(delimiter) + delimiter.length());
                    for (int i = 0; i < message.length(); i++)
                    {
                        if (message.charAt(i) != '#') temp += message.charAt(i);
                        else
                        {
                            chosenfiles.add(temp);
                            temp = "";
                            i += 4;
                        }
                        if (i == message.length() - 1) chosenfiles.add(temp);
                    }

                    readyfiles = new String[chosenfiles.size()];
                    System.out.println("chosen files list received");
                    for (int i = 0; i < readyfiles.length; i++) { System.out.println(readyfiles[i]); readyfiles[i] = chosenfiles.get(i); }
                    break;
                }
            }
//endregion
//region sending files
            int numFiles = readyfiles.length;
            outD.writeInt(numFiles);
            for (int i = 0; i < numFiles; i++)
            {
                System.out.println("sending " + readyfiles[i]);
                File f = new File(readyfiles[i]);
                outD.writeLong(f.length());
                outD.writeUTF(f.getName());
                FileInputStream in = new FileInputStream(f);
                byte [] buffer = new byte[64*1024];
                int count;

                try
                {
                    while((count = in.read(buffer)) != -1) { outD.write(buffer, 0, count); }
                }
                catch (SocketException Sex) { in.close(); inD.close();outD.close(); socket.close(); }

                outD.flush();
                in.close();
            }
            System.out.println("finished sending files..\n exiting");
//endregion
            socket.close();
            System.out.println("connection closed");

            return;
        }
        catch (Exception e){ e.printStackTrace(); }
    }
    ArrayList<String> ListFiles(Path parentfolder)
    {
        if (parentfolder == null) parentfolder = Paths.get(System.getProperty("user.dir") + "\\DownloadableFiles");
        File f = new File(parentfolder.toString());
        for(File s : f.listFiles())
        {
            if (s.isFile()) q.add(s.getAbsolutePath());
            else ListFiles(Paths.get(s.getAbsolutePath()));
        }
        return q;
    }
    String msg(String mtype, String[] message)
    {
        mtype += delimiter;
        for(int i = 0; i < message.length; i++)
        {
            mtype += message[i];
            if (i != message.length - 1) mtype += delimiter;
        }
        System.out.println(mtype);
        return mtype;
    }
}
