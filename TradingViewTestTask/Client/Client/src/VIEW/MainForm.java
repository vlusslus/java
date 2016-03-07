package VIEW;
import MODEL.LanguageClassifications.LANG;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


public class MainForm extends JFrame implements MainForm_I
{
    //region components
    private JProgressBar progressBar1;
    private JTextArea txtLog;
    private JButton Downloadbtn;
    private JButton Exitbtn;
    private JButton RemoveAllbtn;
    private JPanel pan;
    private JButton Applybtn;
    private JLabel ILlbl;
    private JComboBox ILcombo;
    private JButton Browsebtn;
    private JLabel LblDpathTitle;
    private JLabel LblDpath;
    private JCheckBox chkShowTT;
    private JTextField txtServerAddr;
    private JLabel ServerAddr;
    private JCheckBox SaveLogschk;
    private JCheckBox LoadLogschk;
    private JCheckBox chkShowSett;
    private JButton Connectbtn;
    private JButton MoveAllbtn;
    private JCheckBox chkConnectonstartup;
    private JScrollPane scroll;
    private JTable table;
    //endregion
    private ArrayList<String> columnNames;
    private DefaultTableModel model;
    private String[] Pathes;

    private boolean CONNECTED_flag;
    private boolean DOWNLOADING_flag;

    public MainForm(LANG lang)
    {
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(1000,700);
        setLocationRelativeTo(null);
        //region adding components
        getContentPane().add(Connectbtn);
        getContentPane().add(Exitbtn);
        getContentPane().add(RemoveAllbtn);
        getContentPane().add(Downloadbtn);
        getContentPane().add(ILlbl);
        getContentPane().add(ILcombo);
        getContentPane().add(LblDpathTitle);
        getContentPane().add(LblDpath);
        getContentPane().add(Browsebtn);
        getContentPane().add(chkShowTT);
        getContentPane().add(chkShowSett);
        getContentPane().add(LoadLogschk);
        getContentPane().add(SaveLogschk);
        getContentPane().add(ServerAddr);
        getContentPane().add(txtServerAddr);
        getContentPane().add(Applybtn);
        getContentPane().add(MoveAllbtn);
        getContentPane().add(chkConnectonstartup);
        getContentPane().add(progressBar1);
        //endregion
        ILcombo.addItem("RUSSIAN");
        ILcombo.addItem("ENGLISH");

        Connectbtn.setEnabled(true);
        Downloadbtn.setEnabled(false);
        //region adjusting progBar
        progressBar1.setBounds(10, this.getHeight() - 55, this.getWidth() - 20, 20);
        progressBar1.setSize(this.getWidth() - 20, 20);
        progressBar1.setAutoscrolls(true);
        progressBar1.setVisible(false);
        //endregion
        //region listeners
        addWindowListener(new WindowAdapter() { @Override  public void windowClosing(WindowEvent e) { Exitbtn.doClick(); } } );
        chkShowSett.addChangeListener(new ChangeListener() { @Override public void stateChanged(ChangeEvent e) {
            if(chkShowSett.isSelected())
            {
                Browsebtn.setVisible(true);
                ILlbl.setVisible(true);
                ILcombo.setVisible(true);
                LblDpath.setVisible(true);
                LblDpathTitle.setVisible(true);
                LoadLogschk.setVisible(true);
                SaveLogschk.setVisible(true);
                chkShowTT.setVisible(true);
                ServerAddr.setVisible(true);
                txtServerAddr.setVisible(true);
                Applybtn.setVisible(true);
                chkConnectonstartup.setVisible(true);

            }
            else
            {
                Browsebtn.setVisible(false);
                ILlbl.setVisible(false);
                ILcombo.setVisible(false);
                LblDpath.setVisible(false);
                LblDpathTitle.setVisible(false);
                LoadLogschk.setVisible(false);
                SaveLogschk.setVisible(false);
                chkShowTT.setVisible(false);
                ServerAddr.setVisible(false);
                txtServerAddr.setVisible(false);
                Applybtn.setVisible(false);
                chkConnectonstartup.setVisible(false);
            } } } );

        chkShowTT.addChangeListener(e -> {
            if (chkShowTT.isSelected()) SetShowTT(true); else SetShowTT(false); });

        ILcombo.addActionListener(e -> {
            if (ILcombo.getSelectedItem() == "RUSSIAN") setInterfacelanguage(LANG.RUSSIAN);
            else if (ILcombo.getSelectedItem() == "ENGLISH") setInterfacelanguage(LANG.ENGLISH); });

        SaveLogschk.addChangeListener(e -> {
            if (SaveLogschk.isSelected()) SetSaveLOGS(true); else SetSaveLOGS(false); });

        LoadLogschk.addChangeListener(e -> {
            if (LoadLogschk.isSelected()) SetLoadLOGS(true); else SetLoadLOGS(false); });

        chkConnectonstartup.addChangeListener(e -> {
            if (chkConnectonstartup.isSelected()) SetConnectonStartup(true); else SetConnectonStartup(false); });
        //region tooltips
        chkShowTT.addMouseListener(new MouseAdapter() { @Override public void mouseEntered(MouseEvent e) {
            if (GetShowTT())
            {
                if(GetLang() == "RUSSIAN") chkShowTT.setToolTipText("поставьте галочку для отображения подсказок");
                else chkShowTT.setToolTipText("check to show tooltips");
            } } } );
        chkConnectonstartup.addMouseListener(new MouseAdapter() { @Override public void mouseClicked(MouseEvent e) {
            if (GetShowTT())
            {
                if(GetLang() == "RUSSIAN") chkConnectonstartup.setToolTipText("поставьте галочку для подключения к серверу при запуске программы");
                else chkConnectonstartup.setToolTipText("check to connect to server on startup");
            }
        } } );
        chkShowSett.addMouseListener(new MouseAdapter() { @Override public void mouseEntered(MouseEvent e) {
            if (GetShowTT())
            {
                if(GetLang() == "RUSSIAN") chkShowSett.setToolTipText("поставьте галочку для отображения настроек");
                else chkShowSett.setToolTipText("check to make settings panel visible");
            }
        } } );
        SaveLogschk.addMouseListener(new MouseAdapter() { @Override public void mouseEntered(MouseEvent e) {
            if (GetShowTT())
            {
                if(GetLang() == "RUSSIAN") SaveLogschk.setToolTipText("поставьте галочку для сохранения лога перед выходом");
                else SaveLogschk.setToolTipText("check to save log before exiting");
            }
        } } );
        LoadLogschk.addMouseListener(new MouseAdapter() { @Override public void mouseEntered(MouseEvent e) {
            if (GetShowTT())
            {
                if(GetLang() == "RUSSIAN") LoadLogschk.setToolTipText("поставьте галочку для загрузки лога перед выходом");
                else LoadLogschk.setToolTipText("check to load logs at startup");
            }
        } } );
        Browsebtn.addMouseListener(new MouseAdapter() { @Override public void mouseEntered(MouseEvent e) {
            if (GetShowTT())
            {
                if(GetLang() == "RUSSIAN") Browsebtn.setToolTipText("Нажмите для выбора пути сохранения");
                else Browsebtn.setToolTipText("click to shoose save path");
            }
        } } );
        LblDpath.addMouseListener(new MouseAdapter() { @Override public void mouseEntered(MouseEvent e) {
            if (GetShowTT())
            {
                if(GetLang() == "RUSSIAN") LblDpath.setToolTipText("нажмите на кнопку 'Обзор' для выбора пути сохранения");
                else LblDpath.setToolTipText("click 'Browse' button to choose save path");
            }
        } } );
        //endregion
        //endregion
        setInterfacelanguage(lang);
        //region adjusting logging area
        scroll = new JScrollPane(txtLog);
        scroll.setBounds(10, txtLog.getY(), txtLog.getWidth(), txtLog.getHeight());
        scroll.setAutoscrolls(true);
        scroll.setWheelScrollingEnabled(true);
        getContentPane().add(scroll);
        getContentPane().add(txtLog);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setViewportView(txtLog);
        txtLog.setEditable(false);
        txtLog.setLineWrap(true);
        //endregion
    }

    //region table
    public void setvis(boolean b)
    {
        setVisible(b);
    }
    public void removerow(String filename)
    {
        for (int i = 0; i < model.getRowCount(); i++)
        {
            if ( filename.equals(model.getValueAt(i, 0))) { model.removeRow(i); table.updateUI(); }
        }
    }
    private void createTablemodel(ArrayList<String> rowdata)
    {
        columnNames = new ArrayList<>();
        columnNames.add("Доступные файлы");
        columnNames.add("Выбрать");

        model = new DefaultTableModel()
        {
            /*public int getRowCount() { return rows; } видимо эти три строки перекрывали аналогичные методы
            public int getColumnCount() { return cols;}         класса модель и это не давало правильно считывать значения и удалять строки
            public String getColumnName(int c) { return columnNames.get(c); }*/
            public Class getColumnClass(int columnn) { return types[columnn]; }
            Class[] types = new Class[] {String.class, Boolean.class };

        };
        model.addColumn(columnNames.get(0));
        model.addColumn(columnNames.get(1));

        for (int i = 0; i < rowdata.size(); i++)
        {
            if(model.getRowCount() < rowdata.size())
            {
                Object[] row = new Object[] { rowdata.get(i), true };
                model.addRow(row);
            }
            model.setValueAt(rowdata.get(i), i, 0);
            model.setValueAt(true, i, 1);
        }

        table = new JTable(model);

        table.setAutoCreateColumnsFromModel(true);
        table.setIgnoreRepaint(false);
        table.setBounds(10, 10, 680, 500);
        table.setSize(680, 500);

        JScrollPane tablescroll = new JScrollPane();
        tablescroll.setBounds(10, table.getY(), table.getWidth(), table.getHeight());
        tablescroll.setAutoscrolls(true);
        tablescroll.setWheelScrollingEnabled(true);
        getContentPane().add(tablescroll);
        getContentPane().add(table);
        tablescroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        tablescroll.setViewportView(table);
        table.setVisible(true);
        table.updateUI();
    }
    public void ListFiles(String[] filenames)
    {
        ArrayList<String> files = new ArrayList<>();
        Pathes = filenames;

        for (String filename : filenames) {
            String s = filename.substring(filename.lastIndexOf("\\") + 1);
            if (s.contains(".") && s.lastIndexOf(".") == s.length() - 4) files.add(s);
        }
        createTablemodel(files);
    }
    public ArrayList<String> GetChosenFiles()
    {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < table.getRowCount(); i++)
        {
            boolean checked = (boolean) table.getValueAt(i, 1);
            if (checked) result.add(Pathes[i]);
        }
        return result;
    }
    //endregion
    //region components' language and location
    public String GetLang()
    {
        return ILcombo.getSelectedItem().toString();
    }
    public void setInterfacelanguage(LANG interface_language)
    {
        txtLog.setBounds(10, getHeight() - 160, getWidth() - 20, 100);
        switch (interface_language)
        {
            case RUSSIAN:
                ILcombo.setSelectedItem("RUSSIAN");
                ILlbl.setBounds(this.getWidth() - 300, 40, 130, 20);
                ILcombo.setBounds(this.getWidth() - 140, 40, 130 , 20);

                LblDpathTitle.setBounds(ILlbl.getX(), ILlbl.getY() + 30, 90, 20);
                LblDpath.setBounds(ILlbl.getX(), LblDpathTitle.getY() + LblDpathTitle.getHeight() + 3, 290, 20);
                Browsebtn.setBounds(this.getWidth() - 90, LblDpath.getY() + 30, 80, 20);

                chkShowSett.setBounds(ILlbl.getX() + 50, 10, 280, 20);
                chkShowTT.setBounds(ILlbl.getX(), Browsebtn.getY() + 30, 280, 20);

                LoadLogschk.setBounds(ILlbl.getX(), chkShowTT.getY() + 30, 280, 20);
                SaveLogschk.setBounds(ILlbl.getX(), LoadLogschk.getY() + 30, 280, 20);
                chkConnectonstartup.setBounds(ILlbl.getX(), SaveLogschk.getY() + 30, 280, 20);

                ServerAddr.setBounds(ILlbl.getX(), chkConnectonstartup.getY() + 30, 140, 20);
                txtServerAddr.setBounds(ILlbl.getX() + ServerAddr.getWidth() + 10, ServerAddr.getY(), 140, 20);

                Applybtn.setBounds(ILlbl.getX() + 70, txtServerAddr.getY() + 60, 150, 20);

                Connectbtn.setBounds(Applybtn.getX() - 15, Applybtn.getY() + 30, 180, 20);

                Downloadbtn.setBounds(Applybtn.getX() - 15, Connectbtn.getY() + 30, 180, 20);
                RemoveAllbtn.setBounds(Applybtn.getX() - 15,  Downloadbtn.getY() + 30, 180, 20);
                MoveAllbtn.setBounds(Applybtn.getX()-15, RemoveAllbtn.getY() + 30, 180, 20);
                Exitbtn.setBounds(Applybtn.getX() - 15, MoveAllbtn.getY() + 30, 180, 20);

                try
                {
                    table.getColumnModel().getColumn(0).setHeaderValue("Доступные файлы");
                    table.getColumnModel().getColumn(1).setHeaderValue("выбрать");
                }
                catch (Exception e) { /*e.printStackTrace(); */}
                if (!txtLog.getText().equals("")) txtLog.setText(txtLog.getText() + "изменен язык интерфейса\n");

                chkConnectonstartup.setText("Подключаться к серверу при запуске");
                MoveAllbtn.setText("Переместить загрузки");
                Connectbtn.setText("Подключиться к серверу");
                Downloadbtn.setText("Скачать файлы");
                RemoveAllbtn.setText("Удалить все файлы");
                Exitbtn.setText("Выход");
                ILlbl.setText("Язык интерфейса:");
                LblDpathTitle.setText("Скачивать в:");
                Browsebtn.setText("Обзор");
                chkShowTT.setText("Показывать подсказки");
                chkShowSett.setText("Показать настройки");
                LoadLogschk.setText("Загружать логи");
                SaveLogschk.setText("Сохранять лог");
                ServerAddr.setText("IP и порт сервера:");
                Applybtn.setText("Применить");
                break;
            case ENGLISH:
                try
                {
                    table.getColumnModel().getColumn(0).setHeaderValue("Available files");
                    table.getColumnModel().getColumn(1).setHeaderValue("choose");
                }
                catch (Exception e) { /*e.printStackTrace();*/ }
                ILcombo.setSelectedItem("ENGLISH");
                ILlbl.setBounds(this.getWidth() - 300, 40, 130, 20);
                ILcombo.setBounds(this.getWidth() - 140, 40, 130 , 20);

                LblDpathTitle.setBounds(ILlbl.getX(), ILlbl.getY() + 30, 120, 20);
                LblDpath.setBounds(ILlbl.getX(), LblDpathTitle.getY() + LblDpathTitle.getHeight() + 3, 290, 20);
                Browsebtn.setBounds(this.getWidth() - 90, LblDpath.getY() + 30, 80, 20);

                chkShowSett.setBounds(ILlbl.getX() + 50, 10, 280, 20);
                chkShowTT.setBounds(ILlbl.getX(), Browsebtn.getY() + 30, 280, 20);

                LoadLogschk.setBounds(ILlbl.getX(), chkShowTT.getY() + 30, 280, 20);
                SaveLogschk.setBounds(ILlbl.getX(), LoadLogschk.getY() + 30, 280, 20);
                chkConnectonstartup.setBounds(ILlbl.getX(), SaveLogschk.getY() + 30, 280, 20);

                ServerAddr.setBounds(ILlbl.getX(), chkConnectonstartup.getY() + 30, 140, 20);
                txtServerAddr.setBounds(ILlbl.getX() + ServerAddr.getWidth() + 10, ServerAddr.getY(), 130, 20);

                Applybtn.setBounds(ILlbl.getX() + 60, txtServerAddr.getY() + 60, 150, 20);

                Connectbtn.setBounds(Applybtn.getX(), Applybtn.getY() + 30, 150, 20);

                Downloadbtn.setBounds(Applybtn.getX() - 15, Connectbtn.getY() + 30, 180, 20);
                RemoveAllbtn.setBounds(Applybtn.getX() - 15,  Downloadbtn.getY() + 30, 180, 20);
                MoveAllbtn.setBounds(Applybtn.getX()-15, RemoveAllbtn.getY() + 30, 180, 20);
                Exitbtn.setBounds(Applybtn.getX() - 15, MoveAllbtn.getY() + 30, 180, 20);

                if (!txtLog.getText().equals("")) txtLog.setText(txtLog.getText() + "Interface language changed\n");
                chkConnectonstartup.setText("Connect on startup");
                MoveAllbtn.setText("Move all files");
                Connectbtn.setText("Connect to server");
                Downloadbtn.setText("Download");
                RemoveAllbtn.setText("Delete all files");
                Exitbtn.setText("Exit");
                ILlbl.setText("Interface language:");
                LblDpathTitle.setText("Downloads path:");
                Browsebtn.setText("Browse");
                chkShowTT.setText("Show tooltips");
                chkShowSett.setText("Show settings");
                ServerAddr.setText("Server ip address & port:");
                LoadLogschk.setText("Load log file");
                SaveLogschk.setText("Save log file");
                Applybtn.setText("Apply settings");
                break;
        }
    }
    //endregion
    //region throwable events
    public void addLangChangeListener(ActionListener listen4langChange) { ILcombo.addActionListener(listen4langChange);}
    public void addConnectbtnListener(ActionListener listen4Connect) { Connectbtn.addActionListener(listen4Connect); }
    public void addDownloadListener(ActionListener listen4Download) { Downloadbtn.addActionListener(listen4Download); }
    public void addExitListener(ActionListener listen4Exit) { Exitbtn.addActionListener(listen4Exit); }
    public void addRemoveAllListener(ActionListener listen4RemoveAll) { RemoveAllbtn.addActionListener(listen4RemoveAll); }
    public void addApplyListener(ActionListener listen4Apply)
    {
        Applybtn.addActionListener(listen4Apply);
    }
    public void addBrowseListener(ActionListener listen4Browse) { Browsebtn.addActionListener(listen4Browse); }
    public void addMoveAllbtnListener(ActionListener listen4MoveAll) { MoveAllbtn.addActionListener(listen4MoveAll); }
    //endregion
    //region progress
    public void enabprogress(boolean b)
    {
        progressBar1.setVisible(b);
    }
    public void SetCurProg(int i)
    {
        progressBar1.setValue(i);
        DOWNLOADING_flag = i > 0 && i <= GetMaxProg();
        Rectangle progressRect = progressBar1.getBounds();
        progressRect.x = 0;
        progressRect.y = 0;
        progressBar1.paintImmediately( progressRect );
    }
    public void SetMaxProg(int i)
    {
        progressBar1.setMaximum(i);
        if (i > 0) progressBar1.setVisible(true);
        else progressBar1.setVisible(false);
    }
    public int GetMaxProg()
    {
        return progressBar1.getMaximum();
    }
    //endregion
    //region logging
    public void LOG(String content)
    {
        txtLog.append(content);
    }
    public String GetLOG()
    {
        return txtLog.getText();
    }
    public void SetLOG(String log)
    {
        txtLog.setText(log);
        txtLog.setCaretPosition(txtLog.getText().length());
    }
    //endregion
    //region settings
    public boolean GetShowTT()
    {
        return chkShowTT.isSelected();
    }
    public void SetShowTT(boolean b)
    {
        chkShowTT.setSelected(b);
    }

    public void SetShowSett(boolean b)
    {
        chkShowSett.setSelected(b);
    }
    public boolean GetShowSett()
    {
        return chkShowSett.isSelected();
    }

    public void SetConnectonStartup(boolean b)
    {
        chkConnectonstartup.setSelected(b);
    }
    public boolean GetConnectonStartup()
    {
        return chkConnectonstartup.isSelected();
    }

    public String GetHost()
    {
        return txtServerAddr.getText();
    }
    public void SetHost(String host)
    {
        txtServerAddr.setText(host);
    }
    public void SetDpath(String path)
    {
        LblDpath.setText(path);
    }
    public String GetDpath()
    {
        return  LblDpath.getText();
    }

    public void SetLoadLOGS(boolean b)
    {
        LoadLogschk.setSelected(b);
    }
    public void SetSaveLOGS(boolean b)
    {
        SaveLogschk.setSelected(b);
    }
    public boolean GetSaveLOGS()
    {
        return SaveLogschk.isSelected();
    }
    public boolean GetLoadLOGS()
    {
        return LoadLogschk.isSelected();
    }
    //endregion
    //region downloading/connected/OP flags
    public void setCONNECTED_flag(boolean b)
    {
        CONNECTED_flag = b;
        if(b) { Connectbtn.setEnabled(false); setDOWNLOADING_flag(false); }
        else { Connectbtn.setEnabled(true); setDOWNLOADING_flag(true); }
    }
    public void setDOWNLOADING_flag(boolean b)
    {
        DOWNLOADING_flag = b;
        if(b) Downloadbtn.setEnabled(false);
        else Downloadbtn.setEnabled(true);
    }
    //endregion
    public void enab(boolean b)
    {
        if(b) { RemoveAllbtn.setEnabled(true); MoveAllbtn.setEnabled(true); }
        else { RemoveAllbtn.setEnabled(false); MoveAllbtn.setEnabled(false); }
    }
}