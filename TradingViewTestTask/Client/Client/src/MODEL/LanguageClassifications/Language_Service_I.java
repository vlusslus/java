package MODEL.LanguageClassifications;

public interface Language_Service_I
{
    LANG GetInterfaceLanguage();
    void SetInterfaceLanguage(String language);
    void SetInterfaceLanguage(LANG language);
    @Override String toString();
}