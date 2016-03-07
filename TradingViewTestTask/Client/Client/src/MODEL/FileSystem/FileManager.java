package MODEL.FileSystem;
import org.apache.commons.io.FileUtils;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;

public  class FileManager implements FileManager_I
{
    private Path downloadpath = null;

    public boolean DownloadsFolderNotEmpty(Path path)
    {
        boolean result = false;
        File s = new File(path.toString());
        ArrayList<String> check = new ArrayList<>();
        for (File q : s.listFiles()) { check.add(s.getAbsolutePath()); }
        if (check.size() > 0) result = true;
        return result;
    }
    public void AppendContent(String content, String path) throws IOException
    {
        FileWriter fw = new FileWriter(path, true);
        fw.write(content);
        fw.close();
    }
    public String ReadContent(String path) throws IOException
    {
        Path p = Paths.get(path);
        return new String(Files.readAllBytes(p));
    }
    public boolean Exists(String filePath)
    {
        Path p = Paths.get(filePath);
        return Files.exists(p);
    }
    public void DeleteAllFilesAndSubfolders(Path parentDir) throws IOException
    {
        File f = new File(parentDir.toString());
        for(File s : f.listFiles())
        {
            if (s.isFile()) Files.delete(Paths.get(s.getAbsolutePath()));
            else
            {
                DeleteAllFilesAndSubfolders(Paths.get(s.getAbsolutePath()));
                Files.deleteIfExists(Paths.get(s.getAbsolutePath()));
            }
        }
    }
    public void MoveAll(Path source, Path destination) throws IOException
    {
        FileUtils.moveToDirectory(source.toFile(), destination.toFile(), true);
    }
    public void CreateNewFolder(Path path) throws IOException
    {
        new File(path.toString()).mkdirs();
    }
    public Path GetDownloadPath()
    {
        return downloadpath;
    }
    public void SetDownloadPath(Path p)
    {
        downloadpath = p;
    }
}