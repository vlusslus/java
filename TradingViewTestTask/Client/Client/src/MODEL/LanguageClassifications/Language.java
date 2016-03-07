package MODEL.LanguageClassifications;

public final class Language implements Language_Service_I
{
    private LANG interface_language;
        
    public Language()
    {

    }
    public Language(LANG language)
    {
        interface_language = language;
    }
    public void Language(LANG lang)
    {
        interface_language = lang;
    }
    public LANG GetInterfaceLanguage()
    {
        return StringToLANG(interface_language.toString());
    }
    public void SetInterfaceLanguage(LANG language)
    {
        interface_language = language;
    }
    public void SetInterfaceLanguage(String language)
    {
        interface_language = StringToLANG(language.toUpperCase());
    }
    public String toString()
    {
        return interface_language.toString().toUpperCase();
    }
    private LANG StringToLANG(String language)
    {
        LANG lang = null;
        switch(language)
        {
            case "RUSSIAN":
                lang = LANG.RUSSIAN;
                break;
            case "ENGLISH":
                lang = LANG.ENGLISH;
                break;
        }
        return lang;
    }
}