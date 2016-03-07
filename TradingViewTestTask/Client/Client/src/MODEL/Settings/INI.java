package MODEL.Settings;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
        
public class INI implements INI_I
{
    private static String path;
        
    public void SetFilePath(String fpath)
    {
        path = fpath;
    }
    public void IniWriteValue(String[] Key, String[] Value) throws IOException
    {
        Properties properties = new Properties();
        FileOutputStream fos;
        
        File file = new File(path);
        fos = new FileOutputStream(file);
        Path p = Paths.get(path);
        File dir = new File(p.getParent().toString());
        
        if (Files.notExists(Paths.get(dir.toString()))) dir.mkdirs();
        if (!file.exists())
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.close();
        }
        for (int i = 0; i < Key.length; i++)
        {
            properties.put(Key[i], Value[i]);
        }
        properties.store(fos, null);
        fos.close();
    }
    public String IniReadValue(String Key) throws IOException
    {
        Properties properties = new Properties();
        File file = new File(path);
        FileInputStream inputStream = new FileInputStream(file);
        properties.load(inputStream);
        inputStream.close();
        return properties.getProperty(Key);
    }
}