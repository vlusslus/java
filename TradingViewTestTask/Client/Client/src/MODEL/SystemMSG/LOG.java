package MODEL.SystemMSG;
import MODEL.FileSystem.FileManager;
import MODEL.LanguageClassifications.LANG;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
        
public class LOG implements LOG_I
{
    private final String path = System.getProperty("user.dir") + "\\log.txt";
        
    public String WriteToLog(String message)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy | HH:mm:ss");
        Date now = new Date();
        String s = sdf.format(now);
        return (s + " - " + message + "\r\n");
}
    public void SaveLog(String content) throws IOException
    {
        FileManager manager = new FileManager();
        manager.AppendContent(content + "\r\n", path);
    }
    public String OpenLog(LANG lang) throws IOException
    {
        String content = "";
        FileManager manager = new FileManager();
        SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy | HH:mm:ss");
        Date now = new Date();
        String s = sdf.format(now);
        
        if (manager.Exists(path))
        {
            if (lang == LANG.RUSSIAN) content = ("=============история=============\r\n" + manager.ReadContent(path) +
                    "===========конец истории==========\r\n" + s + " - лог файл успешно загружен\r\n");
            else content = ("=============LOG=============\r\n" + manager.ReadContent(path) +
                    "===========END OF LOG==========\r\n" + s + " - LOG FILE loaded\r\n");
        }
        return  content;
    }
}