package MODEL.Settings;
import java.io.IOException;

public interface INI_I
{
    void SetFilePath(String fpath);
    void IniWriteValue(String[] Key, String[] Value) throws IOException;
    String IniReadValue(String Key) throws IOException;
}