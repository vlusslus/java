import MODEL.FileSystem.*;
import MODEL.LanguageClassifications.*;
import MODEL.Settings.*;
import MODEL.SystemMSG.*;
import VIEW.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;

public class Controller
{
    private final FileManager_I _manager;
    private final MainForm_I _view;
    private final MessageService_I _messageService;
    private final LOG_I _logger;
    private final INI_I _configer;
    private final Language_Service_I _langservice;

    Socket socket;
    DataOutputStream s_out = null;
    DataInputStream s_in = null;
    private final String delimiter = "#del#";

    public Controller(MainForm_I view, FileManager_I manager, MessageService_I service, LOG_I logger, INI_I configer, Language_Service_I language) throws Exception
    {
        //region Services init
        _view = view;
        _manager = manager;
        _messageService = service;
        _configer = configer;
        _langservice = language;
        _logger = logger;
        //endregion
        //region Settings init
        _configer.SetFilePath(System.getProperty("user.dir") + "\\ini.dat");

        if(SettingsLoaded())
        {
            if (_langservice.GetInterfaceLanguage() == LANG.RUSSIAN) _view.LOG(_logger.WriteToLog(" Настройки успешно загружены"));
            else _view.LOG(_logger.WriteToLog(" Settings loaded successfully "));
        }
        else
        {
            _view.LOG(_logger.WriteToLog(" Приложение запущено"));
            _view.LOG(_logger.WriteToLog(" Файл настроек не найден "));
            _view.LOG(_logger.WriteToLog(" Загружены настройки по умолчанию "));
        }

        if(_view.GetLoadLOGS()) LoadLOG();
        if(!_manager.Exists(_manager.GetDownloadPath().toString()))_manager.CreateNewFolder(_manager.GetDownloadPath());

        //endregion
        //region Events init
        _view.addApplyListener(new Listener(0));
        _view.addBrowseListener(new Listener(1));
        _view.addDownloadListener(new Listener(2));
        _view.addExitListener(new Listener(3));
        _view.addConnectbtnListener(new Listener(4));
        _view.addRemoveAllListener(new Listener(5));
        _view.addLangChangeListener(new Listener(6));
        _view.addMoveAllbtnListener(new Listener(7));
        //endregion
        _view.setvis(true);
        _view.enab(_manager.DownloadsFolderNotEmpty(_manager.GetDownloadPath()));
        if (_view.GetConnectonStartup()) ConnectToServer();
    }
    //region Events handling
    class Listener implements ActionListener
    {
        private int id;
        Listener(int ID) { id = ID; }
        @Override public void actionPerformed(ActionEvent e)
        {
            switch (id)
            {
                case 0: ApplyButtonPressed(); break;
                case 4: ConnectToServer(); break;
                case 1: BrowseButtonPressed(); break;
                case 2: DownloadButtonPressed(); break;
                case 3: ViewExitButtonPressed(); break;
                case 5: RemoveAllFDButtonPressed(); break;
                case 6: _langservice.SetInterfaceLanguage(_view.GetLang()); break;
                case 7: MoveAllFiles(); break;
            }
        }
    }
    //endregion
    //region    btns
    void MoveAllFiles()
    {
        File f = new File(_manager.GetDownloadPath().toString());

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        fileChooser.setCurrentDirectory(new File(_view.GetDpath()));

        if (_view.GetLang().equals("RUSSIAN")) fileChooser.setApproveButtonText("Выбрать");
        else fileChooser.setApproveButtonText("Choose");

        if (_view.GetLang().equals("RUSSIAN")) fileChooser.setDialogTitle("Выбрать папку назначения");
        else fileChooser.setDialogTitle("Select target path");

        fileChooser.showOpenDialog(null);
        File t = fileChooser.getSelectedFile();
        if(t == null)
        {
            if (_langservice.GetInterfaceLanguage() == LANG.RUSSIAN) _messageService.ShowMessage("Папка назначения не выбрана. Операция отменена", _langservice.GetInterfaceLanguage());
            else _messageService.ShowMessage("Target folder not chosen. Operation cancelled", _langservice.GetInterfaceLanguage());
            return;
        }
        else
        {
            if(t.toString().equals(f.toString()))
            {
                if (_langservice.GetInterfaceLanguage() == LANG.RUSSIAN) _messageService.ShowMessage("Выбрана папка источника. Операция отменена", _langservice.GetInterfaceLanguage());
                else _messageService.ShowMessage("Source folder chosen. Operation cancelled", _langservice.GetInterfaceLanguage());
                return;
            }
            try
            {
                if (_view.GetLang().equals("RUSSIAN")) _view.LOG(_logger.WriteToLog("Начато перемещение файлов"));
                else _view.LOG(_logger.WriteToLog("File transfer started"));
                _manager.MoveAll(f.toPath(), t.toPath());
                if (_langservice.GetInterfaceLanguage() == LANG.RUSSIAN) _view.LOG(_logger.WriteToLog("Операция успешно завершена"));
                else _view.LOG(_logger.WriteToLog("Operation completed"));
                _view.enab(_manager.DownloadsFolderNotEmpty(_manager.GetDownloadPath()));
            }
            catch (IOException IOex) { _messageService.ShowError(null, _langservice.GetInterfaceLanguage(), IOex); return; }
        }
    }
    void RemoveAllFDButtonPressed()
    {
        try
        {
            if (_view.GetLang().equals("RUSSIAN")) { _view.LOG(_logger.WriteToLog("Начато удаление файлов")); }
            else { _view.LOG(_logger.WriteToLog("Started deleting files")); }
            _manager.DeleteAllFilesAndSubfolders(_manager.GetDownloadPath());
        }
        catch (IOException IOex) { _messageService.ShowError(null, _langservice.GetInterfaceLanguage(), IOex); return; }
        if (_view.GetLang().equals("RUSSIAN")) { _view.LOG(_logger.WriteToLog("Удаление файлов завершено")); }
        else  { _view.LOG(_logger.WriteToLog("Finished deleting files")); }
        _view.enab(_manager.DownloadsFolderNotEmpty(_manager.GetDownloadPath()));
    }
    void DownloadButtonPressed()
    {
        try
        {
            ArrayList<String> ch = _view.GetChosenFiles();
            String[] chosen = new String[ch.size()];
            for(int i = 0; i < ch.size(); i++) { chosen[i] = ch.get(i); }
            s_out.writeUTF(buildMSG("download", chosen));
            s_out.flush();
            if (_langservice.GetInterfaceLanguage() == LANG.RUSSIAN) _view.LOG(_logger.WriteToLog(" начинается загрузка файлов"));
            else _view.LOG(_logger.WriteToLog(" starting downloading"));
            _view.setDOWNLOADING_flag(true);
            download();
            CloseConnection();
        }
        catch (Exception ex) { _messageService.ShowError(null, _langservice.GetInterfaceLanguage(), ex); }
        _view.setCONNECTED_flag(false);
    }
    void ViewExitButtonPressed()
    {
        if (_view.GetSaveLOGS()) SaveLOG(_view.GetLOG());
        if (SettingsChanged())
        {
            String question;
            if (_langservice.GetInterfaceLanguage() == LANG.RUSSIAN) question = "Сохранить настройки перед выходом?";
            else question = "Do you want to save settings changes before exit?";

            if(_messageService.ShowQuestion(question, _langservice.GetInterfaceLanguage())) SaveSettings();
            System.exit(0);
        }
        else System.exit(0);
    }
    void ApplyButtonPressed()
    {
        while (_view.GetHost().contains(" ")) _view.SetHost(_view.GetHost().replace(" ", ""));
        Pattern reg = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
        Matcher match = reg.matcher(_view.GetHost());
        if (!match.matches())
        {
            String message;
            if (_view.GetLang().equals("RUSSIAN")) message = "Неверный формат данных";
            else  message = "Wrong data format";
            _view.LOG(_logger.WriteToLog(message));
            _messageService.ShowError(message, _langservice.GetInterfaceLanguage(), new DataFormatException());
        }
        else
        {
            _view.SetHost(match.group());
            SaveSettings();
        }
    }
    void BrowseButtonPressed()
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (_view.GetLang().equals("RUSSIAN")) fileChooser.setApproveButtonText("Выбрать");
        else fileChooser.setApproveButtonText("Choose");

        if (_view.GetLang().equals("RUSSIAN")) fileChooser.setDialogTitle("Выбрать путь для скачивания");
        else fileChooser.setDialogTitle("Select download path");
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        fileChooser.setCurrentDirectory(new File(_view.GetDpath()));
        fileChooser.showOpenDialog(null);
        File f = fileChooser.getSelectedFile();
        if (f != null)
        {
            _view.SetDpath(f.getAbsolutePath());
            _manager.SetDownloadPath(Paths.get(_view.GetDpath()));
        }
    }
    //endregion
    //region handling connection
    void ConnectToServer()
    {
        try
        {
            socket = new Socket(_view.GetHost(), 8050);
            s_in = new DataInputStream(socket.getInputStream());
            s_out = new DataOutputStream(socket.getOutputStream());

            if (_langservice.GetInterfaceLanguage() == LANG.RUSSIAN) _view.LOG(_logger.WriteToLog(" Соединение с сервером установлено"));
            else _view.LOG(_logger.WriteToLog(" Server connection established "));
            _view.setCONNECTED_flag(true);

            ReadMSG();
            if (_langservice.GetInterfaceLanguage() == LANG.RUSSIAN) _view.LOG(_logger.WriteToLog(" список доступных файлов получен"));
            else _view.LOG(_logger.WriteToLog(" available files list received"));
        }
        catch (IOException IOex) { _messageService.ShowError(null, _langservice.GetInterfaceLanguage(), IOex); }
    }
    String[] ExtractFilePathsFromMSG(String message)
    {
        message = message.substring(message.indexOf(delimiter) + delimiter.length());
        ArrayList<String> extracted = new ArrayList<>();
        String temp = "";
        for (int i = 0; i < message.length(); i++)
        {
            if (message.charAt(i) != '#') temp += message.charAt(i);
            else
            {
                extracted.add(temp);
                temp = "";
                i += 4;
            }
            if (i == message.length() - 1) extracted.add(temp);
        }
        String[] filename = new String[extracted.size()];
        for (int i = 0; i < extracted.size(); i++)
        {
            filename[i] = extracted.get(i);
        }
        return filename;
    }
    public String buildMSG(String mess, String[] m)
    {
        mess += delimiter;
        for(int i = 0; i < m.length; i++)
        {
            mess += m[i];
            if (i != m.length - 1) mess += delimiter;
        }
        return mess;
    }
    public void ReadMSG() throws IOException
    {
        String message = s_in.readUTF();
        if (message.contains("listfiles" + delimiter)) _view.ListFiles(ExtractFilePathsFromMSG(message));
        else if (message.contains("!exit!")) CloseConnection();
    }
    void download()
    {
        try
        {
            String path =_manager.GetDownloadPath().toString() + "\\";
            new File(path).mkdirs();

            int filesCount = s_in.readInt();

            if (filesCount > 1) _view.SetMaxProg(filesCount);

            for (int j = 0; j < filesCount; j++)
            {
                if (filesCount > 1) _view.SetCurProg((j + 1)*100 /filesCount);

                long fileSize = s_in.readLong();
                String fileName = s_in.readUTF();

                byte[] buffer = new byte[64*1024];
                FileOutputStream outF = new FileOutputStream(path + fileName.substring(fileName.lastIndexOf('\\') + 1));
                long left = fileSize;
                int read = 0;
                System.out.println(left);
                if (filesCount == 1) _view.SetMaxProg((int) fileSize);
                while (true)
                {
                    int nextPackSize = (int)Math.min(left, buffer.length);
                    int count = s_in.read(buffer, 0, nextPackSize);

                    read += count;
                    if (filesCount == 1) _view.SetCurProg((read));

                    if (count <= 0)
                    {
                        String message;
                        if (_langservice.GetInterfaceLanguage() == LANG.RUSSIAN) message = "Что-то пошло не так!" ;
                        else message = "opps, something's wrong..it's gotta be wrong buffer filling";
                        throw new RuntimeException(message);
                    }
                    outF.write(buffer, 0, count);
                    left -= count;
                    if (left == 0) { break; }
                }
                outF.flush();
                outF.close();
                _view.removerow(fileName);
            }
            if (_langservice.GetInterfaceLanguage() == LANG.RUSSIAN) _view.LOG(_logger.WriteToLog(" загрузка завершена"));
            else _view.LOG(_logger.WriteToLog(" finished downloading"));
        }
        catch (IOException e){_messageService.ShowError(null, _langservice.GetInterfaceLanguage(),e);  }
        _view.enab(_manager.DownloadsFolderNotEmpty(_manager.GetDownloadPath()));
        _view.enabprogress(false);
    }
    void CloseConnection()
    {
        try
        {
            s_out.writeUTF("exit");
            s_out.flush();
            s_in.close(); s_out.close(); socket.close();
            if (_langservice.GetInterfaceLanguage() == LANG.RUSSIAN) _view.LOG(_logger.WriteToLog(" соединение закрыто"));
            else _view.LOG(_logger.WriteToLog(" connection closed"));
        }
        catch (IOException IOex) { _messageService.ShowError(null, _langservice.GetInterfaceLanguage(), IOex); }
    }
    //endregion
    //region Logging
    void LoadLOG()
    {
        try
        {
            if (_logger.OpenLog(_langservice.GetInterfaceLanguage()).equals(""))
            {
                if(_langservice.GetInterfaceLanguage() == LANG.RUSSIAN)  _view.LOG(_logger.WriteToLog(" Лог файл не найден"));
                else _view.LOG(_logger.WriteToLog(" Log file not found"));
            }
            else
            {
                String s = _logger.OpenLog(_langservice.GetInterfaceLanguage()) + _view.GetLOG();
                _view.SetLOG(s);
            }
        }
        catch (IOException IOex) { _messageService.ShowError(null, _langservice.GetInterfaceLanguage(), IOex); }
    }
    void SaveLOG(String content)
    {
        try
        {
            _logger.SaveLog(content);
        }
        catch (IOException IOex) { _messageService.ShowError(null, _langservice.GetInterfaceLanguage(), IOex); }
    }
    //endregion
    //region Settings
    boolean SettingsLoaded()
    {
        try
        {
            _langservice.SetInterfaceLanguage(_configer.IniReadValue("INTERFACELANGUAGE"));
            _view.setInterfacelanguage(_langservice.GetInterfaceLanguage());
            if (_langservice.GetInterfaceLanguage() == LANG.RUSSIAN) _view.LOG(_logger.WriteToLog(" Приложение запущено"));
            else _view.LOG(_logger.WriteToLog(" App started "));

            _manager.SetDownloadPath(Paths.get(_configer.IniReadValue("DOWNLOADPATH")));
            _view.SetDpath(_manager.GetDownloadPath().toString());

            _view.SetHost(_configer.IniReadValue("HOST"));

            _view.SetConnectonStartup(Boolean.valueOf(_configer.IniReadValue("CONNECTONSTARTUP")));

            _view.SetLoadLOGS(Boolean.valueOf(_configer.IniReadValue("LOADLOGS")));

            _view.SetSaveLOGS(Boolean.valueOf(_configer.IniReadValue("SAVELOGS")));

            _view.SetShowTT(Boolean.valueOf(_configer.IniReadValue("SHOWTT")));

            _view.SetShowSett(Boolean.valueOf(_configer.IniReadValue("SHOWSETT")));
        }
        catch (Exception IOex)
        {
            LoadDefaultSettings();
            return false;
        }
        return true;
    }
    void LoadDefaultSettings()
    {
        _langservice.SetInterfaceLanguage(LANG.RUSSIAN);
        _manager.SetDownloadPath(Paths.get(System.getProperty("user.dir") + "\\Downloads"));

        _view.SetHost("127.0.0.1");
        _view.SetDpath(_manager.GetDownloadPath().toString());
        _view.SetShowTT(false);
        _view.SetLoadLOGS(false);
        _view.SetSaveLOGS(true);
        _view.SetShowSett(true);
        _view.SetConnectonStartup(false);

        SaveSettings();
    }
    void SaveSettings()
    {
        try
        {
            String[] Props = new String[8];
            Props[0] = "INTERFACELANGUAGE";
            Props[1] = "DOWNLOADPATH";
            Props[3] = "HOST";
            Props[4] = "SHOWTT";
            Props[5] = "LOADLOGS";
            Props[6] = "SAVELOGS";
            Props[7] = "SHOWSETT";
            Props[2] = "CONNECTONSTARTUP";

            String [] Values = new String[9];
            Values[0] = _langservice.GetInterfaceLanguage().toString();
            Values[1] = _manager.GetDownloadPath().toString();
            Values[3] = _view.GetHost();
            Values[4] = String.valueOf(_view.GetShowTT());
            Values[5] = String.valueOf(_view.GetLoadLOGS());
            Values[6] = String.valueOf(_view.GetSaveLOGS());
            Values[7] = String.valueOf(_view.GetShowSett());
            Values[2] = String.valueOf(_view.GetConnectonStartup());

            _configer.IniWriteValue(Props, Values);
            if(_langservice.GetInterfaceLanguage() == LANG.RUSSIAN)_logger.WriteToLog("Настройки успешно сохранены");
            else _logger.WriteToLog("Settings saved successfully");
        }
        catch (IOException IOex)
        { _messageService.ShowError(null,_langservice.GetInterfaceLanguage(),IOex); }
    }
    boolean SettingsChanged()
    {
        try
        {
            return !(_view.GetLang().equals(_configer.IniReadValue("INTERFACELANGUAGE")) &&
                    _view.GetDpath().equals(_configer.IniReadValue("DOWNLOADPATH")) &&
                    _view.GetHost().equals(_configer.IniReadValue("HOST")) &&
                    _view.GetShowTT() == (Boolean.valueOf(_configer.IniReadValue("SHOWTT"))) &&
                    _view.GetLoadLOGS() == (Boolean.valueOf(_configer.IniReadValue("LOADLOGS"))) &&
                    _view.GetSaveLOGS() == (Boolean.valueOf(_configer.IniReadValue("SAVELOGS"))) &&
                    _view.GetShowSett() == Boolean.valueOf(_configer.IniReadValue("SHOWSETT")) &&
                    _view.GetConnectonStartup() == Boolean.valueOf(_configer.IniReadValue("CONNECTONSTARTUP")));
        }
        catch (IOException IOex) { _messageService.ShowError(null, _langservice.GetInterfaceLanguage(), IOex); return false; }
    }
    //endregion
}