package action.message;

/**
 * Static utility methods for working with String values in a way that is compatible with GWT 2.6.1 .
 * <br>
 * GWT's documentation for old versions is... well it exists, but that's about all I can say in its favor. Finding this
 * page that documents the Java standard library support for a slightly-older version was a hassle. No idea if there are
 * any changes between 2.6.0 and 2.6.1, or what changed between 2.7.0 and 2.8.0 beta 1.
 * https://github.com/gwtproject/gwt-site/blob/408ba32d25e032026148e06287db0d3e64cead4e/src/main/markdown/doc/latest/RefJreEmulation.md
 * Created by Tommy Ettinger on 2/26/2016.
 */
public class StringTools {
    public static String matchLetter = "[A-Za-z"
            + "\u00B5\u00C0-\u00D6\u00D8-\u00F6\u00F8-\u01BA\u01BC-\u01BF\u01C4\u01C6-\u01C7\u01C9-\u01CA\u01CC-\u01F1"
            + "\u01F3-\u0293\u0295-\u02AF\u0370-\u0373\u0376-\u0377\u037B-\u037D\u0386\u0388-\u038A\u038C\u038E-\u03A1"
            + "\u03A3-\u03F5\u03F7-\u0481\u048A-\u0527\u0531-\u0556\u0561-\u0587\u10A0-\u10C5\u10C7\u10CD\u1D00-\u1D2B"
            + "\u1D6B-\u1D77\u1D79-\u1D9A\u1E00-\u1F15\u1F18-\u1F1D\u1F20-\u1F45\u1F48-\u1F4D\u1F50-\u1F57\u1F59\u1F5B"
            + "\u1F5D\u1F5F-\u1F7D\u1F80-\u1F87\u1F90-\u1F97\u1FA0-\u1FA7\u1FB0-\u1FB4\u1FB6-\u1FBB\u1FBE\u1FC2-\u1FC4"
            + "\u1FC6-\u1FCB\u1FD0-\u1FD3\u1FD6-\u1FDB\u1FE0-\u1FEC\u1FF2-\u1FF4\u1FF6-\u1FFB\u2102\u2107\u210A-\u2113"
            + "\u2115\u2119-\u211D\u2124\u2126\u2128\u212A-\u212D\u212F-\u2134\u2139\u213C-\u213F\u2145-\u2149\u214E"
            + "\u2183-\u2184\u2C00-\u2C2E\u2C30-\u2C5E\u2C60-\u2C7B\u2C7E-\u2CE4\u2CEB-\u2CEE\u2CF2-\u2CF3\u2D00-\u2D25"
            + "\u2D27\u2D2D\uA640-\uA66D\uA680-\uA697\uA722-\uA76F\uA771-\uA787\uA78B-\uA78E\uA790-\uA793\uA7A0-\uA7AA"
            + "\uA7FA\uFB00-\uFB06\uFB13-\uFB17\uFF21-\uFF3A\uFF41-\uFF5A]",
            matchNonLetter = "[^A-Za-z"
                    + "\u00B5\u00C0-\u00D6\u00D8-\u00F6\u00F8-\u01BA\u01BC-\u01BF\u01C4\u01C6-\u01C7\u01C9-\u01CA\u01CC-\u01F1"
                    + "\u01F3-\u0293\u0295-\u02AF\u0370-\u0373\u0376-\u0377\u037B-\u037D\u0386\u0388-\u038A\u038C\u038E-\u03A1"
                    + "\u03A3-\u03F5\u03F7-\u0481\u048A-\u0527\u0531-\u0556\u0561-\u0587\u10A0-\u10C5\u10C7\u10CD\u1D00-\u1D2B"
                    + "\u1D6B-\u1D77\u1D79-\u1D9A\u1E00-\u1F15\u1F18-\u1F1D\u1F20-\u1F45\u1F48-\u1F4D\u1F50-\u1F57\u1F59\u1F5B"
                    + "\u1F5D\u1F5F-\u1F7D\u1F80-\u1F87\u1F90-\u1F97\u1FA0-\u1FA7\u1FB0-\u1FB4\u1FB6-\u1FBB\u1FBE\u1FC2-\u1FC4"
                    + "\u1FC6-\u1FCB\u1FD0-\u1FD3\u1FD6-\u1FDB\u1FE0-\u1FEC\u1FF2-\u1FF4\u1FF6-\u1FFB\u2102\u2107\u210A-\u2113"
                    + "\u2115\u2119-\u211D\u2124\u2126\u2128\u212A-\u212D\u212F-\u2134\u2139\u213C-\u213F\u2145-\u2149\u214E"
                    + "\u2183-\u2184\u2C00-\u2C2E\u2C30-\u2C5E\u2C60-\u2C7B\u2C7E-\u2CE4\u2CEB-\u2CEE\u2CF2-\u2CF3\u2D00-\u2D25"
                    + "\u2D27\u2D2D\uA640-\uA66D\uA680-\uA697\uA722-\uA76F\uA771-\uA787\uA78B-\uA78E\uA790-\uA793\uA7A0-\uA7AA"
                    + "\uA7FA\uFB00-\uFB06\uFB13-\uFB17\uFF21-\uFF3A\uFF41-\uFF5A]";
    public static StringBuilder join(String... texts)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < texts.length; i++) {
            sb.append(texts[i]);
        }
        return sb;
    }
    public String capitalize(String text)
    {
        String t2 = " " + text;
        String[] parts = t2.split("^" + matchNonLetter + "+", 2);
        if(parts.length < 2 || parts[1].isEmpty())
            return text;
        char firstLetter = parts[1].charAt(0), replacer = Character.toUpperCase(firstLetter);
        return text.replaceFirst(String.valueOf(firstLetter), String.valueOf(replacer));
    }
}
