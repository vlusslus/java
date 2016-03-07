package MODEL.SystemMSG;
import MODEL.LanguageClassifications.LANG;

public interface MessageService_I
{
    void ShowMessage(String message, LANG interface_language);
    void ShowExclamation(String exclamation, LANG interface_language);
    void ShowError(String error, LANG interface_language, Exception ex);
    boolean ShowQuestion(String question, LANG interface_language);
}