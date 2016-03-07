package VIEW;
import MODEL.LanguageClassifications.LANG;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public interface MainForm_I
{
    void setInterfacelanguage(LANG interface_language);

    void SetCurProg(int i);
    void SetMaxProg(int i);
    int GetMaxProg();
    void enabprogress(boolean b);

    String GetLOG();
    void SetLOG(String log);
    void LOG(String content);
    void SetDpath(String path);
    String GetDpath();
    void ListFiles(String[] filenames);
    void addLangChangeListener(ActionListener listen4langChange);
    void addDownloadListener(ActionListener listen4DownLoad);
    void addExitListener(ActionListener listen4Exit);
    void addRemoveAllListener(ActionListener listen4RemoveAll);
    void addApplyListener(ActionListener listen4Apply);
    void addBrowseListener(ActionListener listen4Browse);
    void addConnectbtnListener(ActionListener listen4Connect);
    void addMoveAllbtnListener(ActionListener listen4MoveAll);

    ArrayList<String> GetChosenFiles();
    void setvis(boolean b);
    String GetLang();
    boolean GetShowTT();
    boolean GetSaveLOGS();
    boolean GetLoadLOGS();
    void SetConnectonStartup(boolean b);
    boolean GetConnectonStartup();
    void SetShowSett(boolean b);
    boolean GetShowSett();
    void SetLoadLOGS(boolean b);
    void SetSaveLOGS(boolean b);
    void SetShowTT(boolean b);
    String GetHost();
    void SetHost(String host);
    void removerow(String value);
    void setCONNECTED_flag(boolean b);
    void setDOWNLOADING_flag(boolean b);
    void enab(boolean b);
}