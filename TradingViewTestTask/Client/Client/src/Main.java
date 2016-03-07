import MODEL.FileSystem.FileManager;
import MODEL.LanguageClassifications.LANG;
import MODEL.LanguageClassifications.Language;
import MODEL.Settings.INI;
import MODEL.SystemMSG.LOG;
import MODEL.SystemMSG.MessageService;
import VIEW.MainForm;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        MainForm mainform = new MainForm(LANG.RUSSIAN);
        MessageService service = new MessageService();
        FileManager manager = new FileManager();
        LOG logger = new LOG();
        INI configer = new INI();
        Language language = new Language(LANG.RUSSIAN);

        new Controller(mainform, manager, service, logger, configer, language);
    }
}