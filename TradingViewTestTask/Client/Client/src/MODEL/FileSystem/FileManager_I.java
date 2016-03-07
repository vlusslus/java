package MODEL.FileSystem;
import java.io.IOException;
import java.nio.file.Path;
        
public interface FileManager_I
{
    Path GetDownloadPath();
    void SetDownloadPath(Path p);

    void CreateNewFolder(Path path) throws IOException;
    boolean Exists(String filePath);
    boolean DownloadsFolderNotEmpty(Path path);
    void DeleteAllFilesAndSubfolders(Path parentDir) throws IOException;
    void MoveAll(Path s, Path t) throws IOException;
    void AppendContent(String content, String path) throws IOException;
    String ReadContent(String path) throws IOException;
}