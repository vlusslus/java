package MODEL.SystemMSG;
import MODEL.LanguageClassifications.LANG;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Objects;
import java.util.zip.DataFormatException;
        
public class MessageService implements MessageService_I
{
    private String GetExceptionMSG(Exception ex, LANG interfacelanguage)
    {
        String result = "";
        if (ex instanceof IOException)
        {
            if(interfacelanguage == LANG.RUSSIAN) result = "Ошибка записи/чтения файла: " + Arrays.toString(ex.getStackTrace());
            else result = "IO error: " + Arrays.toString(ex.getStackTrace());
        }
        else if (ex instanceof DataFormatException)
        {
            if(interfacelanguage == LANG.RUSSIAN) result = "Неверный формат данных: " + Arrays.toString(ex.getStackTrace());
            else result = "Wrong data format: " + Arrays.toString(ex.getStackTrace());
        }
        else if (ex instanceof UnknownHostException)
        {
            if(interfacelanguage == LANG.RUSSIAN) result = "Неизвестный хост: " + Arrays.toString(ex.getStackTrace());
            else result = "Unknown host: " + Arrays.toString(ex.getStackTrace());
        }
        else if (ex instanceof SocketException)
        {
            if(interfacelanguage == LANG.RUSSIAN) result = "ошибка подкючения: " + Arrays.toString(ex.getStackTrace());
            else result = "socket connection exception: " + Arrays.toString(ex.getStackTrace());
        }
        else
        {
            if(interfacelanguage == LANG.RUSSIAN) result = "необработанная ошибка: " + Arrays.toString(ex.getStackTrace());
            else result = "Unknown error: " + Arrays.toString(ex.getStackTrace());
        }
        return result;
    }
    public void ShowMessage(String message, LANG interface_language)
    {
        String title;
        if (interface_language == LANG.RUSSIAN) title = "Сообщение";
        else title = "Message";
        
        MyOptionPane mop = new MyOptionPane();
        mop.showOkMessage(title, message);
    }
    public void ShowExclamation(String exclamation, LANG interface_language)
    {
        String title;
        
        if (interface_language == LANG.RUSSIAN) title = "Предупреждение";
        else title = "Warning!";
        
        MyOptionPane mop = new MyOptionPane();
        mop.showOkMessage(title, exclamation);
    }
    public void ShowError(String error, LANG interface_language, Exception ex)
    {
        String title;
        String text;

        if (interface_language == LANG.RUSSIAN) title = "Ошибка";
        else title = "Error";
        text = GetExceptionMSG(ex, interface_language);
        if ((text.equals("необработанная ошибка") || text.equals("Unknown error")) && (error != null && !Objects.equals(error, "")))
            text = error;

        MyOptionPane mop = new MyOptionPane();
        mop.showOkMessage(title, text);
    }
    public boolean ShowQuestion(String question, LANG interface_language)
    {
        MyOptionPane mop = new MyOptionPane();
        boolean result;

        if (interface_language == LANG.RUSSIAN) result = mop.showYesNoMessage("Внимание", question, "Да", "Нет") == 0;
        else result = mop.showYesNoMessage("Attention", question, "Yes", "No") == 0;
        return result;
    }
    class MyOptionPane
    {
        public static final int YES = 0;
        public static final int NO = -1;
        
        private int choice = NO;
        
        public void showOkMessage(String title, String message)
        {
            JLabel label = new JLabel(message);
        
            JButton okButton = new JButton("OK");
            okButton.addActionListener
            (e -> { choice = YES; JButton button = (JButton)e.getSource(); SwingUtilities.getWindowAncestor(button).dispose(); });
        
            JPanel buttons = new JPanel();
            buttons.add(okButton);
        
            JPanel content = new JPanel(new BorderLayout(8, 8));
            content.add(label, BorderLayout.CENTER);
            content.add(buttons, BorderLayout.SOUTH);

            JDialog dialog = new JDialog();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setModal(true);
            dialog.setTitle(title);
            dialog.getContentPane().add(content);
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        }
        public int showYesNoMessage(String title, String message, String Yes, String No)
        {
            JLabel label = new JLabel(message);
        
            JButton okButton = new JButton(Yes);
            okButton.addActionListener
            (e -> { choice = YES; JButton button = (JButton)e.getSource(); SwingUtilities.getWindowAncestor(button).dispose(); });
        
            JButton noButton = new JButton(No);
            noButton.addActionListener
            (e-> { choice = NO; JButton button = (JButton)e.getSource(); SwingUtilities.getWindowAncestor(button).dispose(); } );
        
            JPanel buttons = new JPanel();
            buttons.add(okButton);
            buttons.add(noButton);
        
            JPanel content = new JPanel(new BorderLayout(8, 8));
            content.add(label, BorderLayout.CENTER);
            content.add(buttons, BorderLayout.SOUTH);
        
            JDialog dialog = new JDialog();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setModal(true);
            dialog.setTitle(title);
            dialog.getContentPane().add(content);
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
            return choice;
        }
    }
}