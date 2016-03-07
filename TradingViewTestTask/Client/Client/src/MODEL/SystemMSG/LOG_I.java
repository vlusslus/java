package MODEL.SystemMSG;
import MODEL.LanguageClassifications.LANG;
import java.io.IOException;

public interface LOG_I
{
    String WriteToLog(String message);
    void SaveLog(String content) throws IOException;
    String OpenLog(LANG lang) throws IOException;
}