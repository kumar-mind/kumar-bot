
package ai.kumar.mind;

/**
 * ISO-639-1 language codes as enum
 */
public enum KumarLanguage {

    unknown("unknown", "unknown"), // this is a helper object; i.e. for a kumar dream where the language is unknown
    en("English", "English");

    private final String internationalName, nativeName;
    
    private KumarLanguage(String internationalName, String nativeName) {
        this.internationalName = internationalName;
        this.nativeName = nativeName;
    }
    
    public String getInternationalName() {
        return internationalName;
    }

    public String getNativeName() {
        return nativeName;
    }
    
    /**
     * Parse a string containing a language name.
     * This is convenience method which returns KumarLanguage.unknown in case that
     * the parsing fails.
     * @param language the string representatio of a ISO-639-1 language name
     * @return the language object or KumarLanguage.unknown if the language cannot be parsed
     */
    public static KumarLanguage parse(String language) {
        try {
            if (language.length() > 2) language = language.substring(0, 2);
            return KumarLanguage.valueOf(language.toLowerCase());
        } catch (IllegalArgumentException e) {
            return KumarLanguage.unknown;
        }
    }
    
    public float likelihoodCanSpeak(KumarLanguage other) {
        if (other == en) return 0.5f;
        return 0.0f;
    }
}
