/**
 *  KumarLanguage
 *  Copyright 15.07.2017 by Michael Peter Christen, @0rb1t3r
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *  
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *  
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program in the file lgpl21.txt
 *  If not, see <http://www.gnu.org/licenses/>.
 */

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
