package action.message;

/**
 * Techniques for formatting "action messages" with placeholders for pronouns so those pronouns are correctly replaced.
 * Usage is simple: create Being values to represent the subject (user) of an action and the object (target) of that
 * action, using either the constructors for that inner class or the static being() methods, then pass the user to
 * user() along with the "action message" as a string, storing the result, then passing it as the message for target()
 * if there is, one along with the target Being, and finally calling capitalize() on the result. A series of calls
 * looks, if Phrasing has been imported with {@code import static}, like this:
 * <br>
 * {@code
 * String message = capitalize(
 *     target(
 *         user("@being jumped with @my spear at ~being!", 2, being("rogue", Gender.FEMALE, "Brunhilda")),
 *         3, being("goblin", Gender.MALE));}
 * <br>
 * Which would give "You jumped with your spear at the goblin!". You could change the 2 that indicates referring to the
 * user (using the action) in the second person; if it was 1 for first person, that would give "I jumped with my spear
 * at the goblin!", and if it was 3 for third person, that would give "Brunhilda jumped with her spear at the goblin!"
 *
 * A nested class is used for Beings, and a nested enum is used for genders. In case an enum isn't enough for a complex
 * gender-handling function, there is a Gender.OTHER value that conjugates to unpronounceable substitute pronouns like
 * "qge" and "qgim" instead of "he" and "him", which are meant to be replaced in some customized way before being shown
 * to a user. If you are replacing these values, which you only need to do if there is a gender in use with the value
 * Gender.OTHER, you need to search for "qge" (he/she), "qgim" (him/her), "qgis" (his/her), "qgims" (his/hers), and
 * "qgimself" (himself/herself). Typically those would be searched with regex word boundaries around them, using the
 * regex escape "\b" (which may need the backslash escaped again, "\\b"), to avoid the replacement order mattering. If
 * the string was already capitalized, as in the example, the search would need to be done for both the capitalized "Q"
 * and the lower-case "q", since they probably need to be replaced differently. An exception is when the "other" gender
 * is used for a deity, which is a clear case for its use since pronouns regarding gods and goddesses often are
 * capitalized regardless of where they are placed in a sentence, so replacing "qgis" with "His" is an easy way to
 * ensure this rule is followed. If you're always capitalizing such a pronoun, you only need to search for something
 * that matches either "Qg" or "qg" at the start, like "\\b[Qq]gimself\\b", and replace it once with "Himself".
 * <br>
 * Currently, this only handles past-tense messages correctly because it is drastically simpler to conjugate verbs when
 * using only the past tense. Compare in present tense, "you bite, he bites", "you scratch, he scratches", and "you hit,
 * he hits", to their past tense counterparts, "you bit, he bit", "you scratched, he scratched", and "you hit, he hit".
 * It's clearly difficult to conjugate from present to past tense with so many irregular verbs in English, from hit to
 * bite to swim to pay, and covering all of them seems essentially impossible to automate. This means either the library
 * consumer would need to supply a separate present-tense and past-tense version of any messages that could need to
 * change tense, or the library can cover one tense at a time and not automate changes between them. Present-tense
 * support also requires some special support for verbs that may not be irregular, but may need to go from, for instance
 * "preach" to "preaches" instead of simply to "preachs", and that special support may be present here in the future.
 * Created by Tommy Ettinger on 2/26/2016.
 */
public class Phrasing {
    /**
     * Takes a format string fmt that should be a sentence in past-tense, containing a formatted indicator of the
     * subject of the sentence (the user of the action), {@code @being}, and changes any references to the user to match
     * the basic String value of being if the grammatical person (1, 2, or 3 representing first, second, or third person
     * respectively) requires that Being to be addressed by something other than "you" or "I". Also changes any pronouns
     * that are replaced differently based on gender, like {@code @my} being replaced with "your", "his", or "her" and
     * {@code @mine} being replaced with "yours", "his", or "hers", to be replaced with the correct pronoun for being's
     * gender. Returns a new String that in many cases should be passed to other methods in this class to replace any
     * other indicators. Specifically using {@code @being} is not required; any "@thing" terms will be replaced,
     * including {@code @was} to replace the past-tense of "is" with "was" or "were", like "I was, you were, she was,
     * they were" as fits the gender or genders. A Being object can represent a group of people of any gender using the
     * Gender.PLURAL value; this should not be confused with the gender-non-specific singular usage of "they" with the
     * Gender.THEY value.
     * <br>
     * Valid indicator strings:
     * <ul>
     *     <li>"@being" (replaced with "I", "we", "you", being's specific name or being's general name)</li>
     *     <li>"@general" (replaced with "I", "we", "you", or being's general name)</li>
     *     <li>"@I" (replaced with "I", "we", "you", "they", "he", "she", "it", "xe", or "qge")</li>
     *     <li>"@me" (replaced with "me", "us", "you", "them", "him", "her", "it", "xim", or "qgim")</li>
     *     <li>"@my" (replaced with "my", "our", "your", "their" "his", "her", "its", "their", "xis", or "qgis")</li>
     *     <li>"@mine" (replaced with "mine", "ours", "yours", "theirs" "his", "hers", "its", "theirs", "xis", or "qgims")</li>
     *     <li>"@myself" (replaced with "myself", "ourselves", "yourself", "yourselves", "themselves", "themself", "himself", "herself", "itself", "ximself", or "qgimself")</li>
     *     <li>"@was" (replaced with "was" or "were")</li>
     * </ul>
     * @param fmt a format string using the conventions of this class.
     * @param person grammatical person for this Being; 1 for first-person, 2 for second-person, and 3 for third-person
     * @param being a Being that is using the action described in the format string
     * @return a (not necessarily complete) conversion of fmt to a new String with terms like {@code @being} replaced
     */
    public static String user(String fmt, int person, Being being)
    {
        return alter(fmt, person, being, "[@]", -1);
    }
    /**
     * Takes a format string fmt that should be a sentence in past-tense, containing a formatted indicator of the
     * subject of the sentence (the user of the action), {@code @being}, and changes any references to the user to match
     * the basic String value of being if the grammatical person (1, 2, or 3 representing first, second, or third person
     * respectively) requires that Being to be addressed by something other than "you" or "I". Also changes any pronouns
     * that are replaced differently based on gender, like {@code @my} being replaced with "your", "his", or "her" and
     * {@code @mine} being replaced with "yours", "his", or "hers", to be replaced with the correct pronoun for being's
     * gender. Returns a new String that in many cases should be passed to other methods in this class to replace any
     * other indicators. Specifically using {@code @being} is not required; any "@thing" terms will be replaced,
     * including {@code @was} to replace the past-tense of "is" with "was" or "were", like "I was, you were, she was,
     * they were" as fits the gender or genders. A Being object can represent a group of people of any gender using the
     * Gender.PLURAL value; this should not be confused with the gender-non-specific singular usage of "they" with the
     * Gender.THEY value.
     * <br>
     * Valid indicator strings:
     * <ul>
     *     <li>"@being" (replaced with "I", "we", "you", being's specific name or being's general name)</li>
     *     <li>"@general" (replaced with "I", "we", "you", or being's general name)</li>
     *     <li>"@I" (replaced with "I", "we", "you", "they", "he", "she", "it", "xe", or "qge")</li>
     *     <li>"@me" (replaced with "me", "us", "you", "them", "him", "her", "it", "xim", or "qgim")</li>
     *     <li>"@my" (replaced with "my", "our", "your", "their" "his", "her", "its", "their", "xis", or "qgis")</li>
     *     <li>"@mine" (replaced with "mine", "ours", "yours", "theirs" "his", "hers", "its", "theirs", "xis", or "qgims")</li>
     *     <li>"@myself" (replaced with "myself", "ourselves", "yourself", "yourselves", "themselves", "themself", "himself", "herself", "itself", "ximself", or "qgimself")</li>
     *     <li>"@was" (replaced with "was" or "were")</li>
     * </ul>
     * @param fmt a format string using the conventions of this class.
     * @param person grammatical person for this Being; 1 for first-person, 2 for second-person, and 3 for third-person
     * @param being a Being that is using the action described in the format string
     * @param alwaysGeneralName true if this should use the general name even if the being has a (potentially unknown?)
     *                          specific name; "@general" can be used in any case to force a general name
     * @return a (not necessarily complete) conversion of fmt to a new String with terms like {@code @being} replaced
     */
    public static String user(String fmt, int person, Being being, boolean alwaysGeneralName)
    {
        return alter(fmt, person, being, "[@]", alwaysGeneralName ? 0 : -1);
    }
    /**
     * Takes a format string fmt that should be a sentence in past-tense, containing a formatted indicator of the
     * object of the sentence (the target of the action), {@code ~being}, and changes any references to the target to
     * match the basic String value of being if the grammatical person (1, 2, or 3 representing first, second, or third
     * person respectively) requires that Being to be addressed by something other than "you" or "I". Also changes any
     * pronouns that are replaced differently based on gender, like {@code ~my} being replaced with "your", "his", or
     * "her" and {@code ~mine} being replaced with "yours", "his", or "hers", to be replaced with the correct pronoun
     * for being's gender. Returns a new String that in many cases should be passed to other methods in this class to
     * replace any other indicators. Specifically using {@code ~being} is not required; any "~thing" terms will be
     * replaced, including {@code ~was} to replace the past-tense of "is" with "was" or "were", like "I was, you were,
     * she was, they were" as fits the gender or genders. A Being object can represent a group of people of any gender
     * using the Gender.PLURAL value; this should not be confused with the gender-non-specific singular usage of "they"
     * with the Gender.THEY value.
     * <br>
     * Valid indicator strings:
     * <ul>
     *     <li>"~being" (replaced with "I", "we", "you", being's specific name or being's general name)</li>
     *     <li>"~general" (replaced with "I", "we", "you", or being's general name)</li>
     *     <li>"~I" (replaced with "I", "we", "you", "they", "he", "she", "it", "xe", or "qge")</li>
     *     <li>"~me" (replaced with "me", "us", "you", "them", "him", "her", "it", "xim", or "qgim")</li>
     *     <li>"~my" (replaced with "my", "our", "your", "their" "his", "her", "its", "their", "xis", or "qgis")</li>
     *     <li>"~mine" (replaced with "mine", "ours", "yours", "theirs" "his", "hers", "its", "theirs", "xis", or "qgims")</li>
     *     <li>"~myself" (replaced with "myself", "ourselves", "yourself", "yourselves", "themselves", "themself", "himself", "herself", "itself", "ximself", or "qgimself")</li>
     *     <li>"~was" (replaced with "was" or "were")</li>
     * </ul>
     * @param fmt a format string using the conventions of this class.
     * @param person grammatical person for this Being; 1 for first-person, 2 for second-person, and 3 for third-person
     * @param being a Being that is using the action described in the format string
     * @return a (not necessarily complete) conversion of fmt to a new String with terms like {@code ~being} replaced
     */
    public static String target(String fmt, int person, Being being)
    {
        return alter(fmt, person, being, "[~]", -1);
    }
    /**
     * Takes a format string fmt that should be a sentence in past-tense, containing a formatted indicator of the
     * object of the sentence (the target of the action), {@code ~being}, and changes any references to the target to
     * match the basic String value of being if the grammatical person (1, 2, or 3 representing first, second, or third
     * person respectively) requires that Being to be addressed by something other than "you" or "I". Also changes any
     * pronouns that are replaced differently based on gender, like {@code ~my} being replaced with "your", "his", or
     * "her" and {@code ~mine} being replaced with "yours", "his", or "hers", to be replaced with the correct pronoun
     * for being's gender. Returns a new String that in many cases should be passed to other methods in this class to
     * replace any other indicators. Specifically using {@code ~being} is not required; any "~thing" terms will be
     * replaced, including {@code ~was} to replace the past-tense of "is" with "was" or "were", like "I was, you were,
     * she was, they were" as fits the gender or genders. A Being object can represent a group of people of any gender
     * using the Gender.PLURAL value; this should not be confused with the gender-non-specific singular usage of "they"
     * with the Gender.THEY value.
     * <br>
     * Valid indicator strings:
     * <ul>
     *     <li>"~being" (replaced with "I", "we", "you", being's specific name or being's general name)</li>
     *     <li>"~general" (replaced with "I", "we", "you", or being's general name)</li>
     *     <li>"~I" (replaced with "I", "we", "you", "they", "he", "she", "it", "xe", or "qge")</li>
     *     <li>"~me" (replaced with "me", "us", "you", "them", "him", "her", "it", "xim", or "qgim")</li>
     *     <li>"~my" (replaced with "my", "our", "your", "their" "his", "her", "its", "their", "xis", or "qgis")</li>
     *     <li>"~mine" (replaced with "mine", "ours", "yours", "theirs" "his", "hers", "its", "theirs", "xis", or "qgims")</li>
     *     <li>"~myself" (replaced with "myself", "ourselves", "yourself", "yourselves", "themselves", "themself", "himself", "herself", "itself", "ximself", or "qgimself")</li>
     *     <li>"~was" (replaced with "was" or "were")</li>
     * </ul>
     * @param fmt a format string using the conventions of this class.
     * @param person grammatical person for this Being; 1 for first-person, 2 for second-person, and 3 for third-person
     * @param being a Being that is using the action described in the format string
     * @param alwaysGeneralName true if this should use the general name even if the being has a (potentially unknown?)
     *                          specific name; "~general" can be used in any case to force a general name
     * @return a (not necessarily complete) conversion of fmt to a new String with terms like {@code ~being} replaced
     */
    public static String target(String fmt, int person, Being being, boolean alwaysGeneralName)
    {
        return alter(fmt, person, being, "[~]", alwaysGeneralName ? 0 : -1);
    }
    /**
     * Internal use.
     * @param fmt a format string using the conventions of this class.
     * @param person grammatical person for this Being; 1 for first-person, 2 for second-person, and 3 for third-person
     * @param being a Being that is using the action described in the format string
     * @param seek search for this in the format string before a pronoun
     * @return a (not necessarily complete) conversion of fmt to a new String with terms like {@code @being} replaced
     */
    private static String alter(String fmt, int person, Being being, String seek, int specificNameBehavior)
    {
        String working = fmt;
        switch (person) {
            case 1:
                if (being.gender == Gender.PLURAL) {
                    working = working.replaceAll(seek + "user", "we")
                            .replaceAll(seek + "general", "we")
                            .replaceAll(seek + "I", "we")
                            .replaceAll(seek + "was", "were")
                            .replaceAll(seek + "me", "us")
                            .replaceAll(seek + "my", "our")
                            .replaceAll(seek + "mine", "ours")
                            .replaceAll(seek + "myself", "ourselves");
                } else {
                    working = working.replaceAll(seek + "user", "I")
                            .replaceAll(seek + "general", "I")
                            .replaceAll(seek + "I", "I")
                            .replaceAll(seek + "was", "was")
                            .replaceAll(seek + "me", "me")
                            .replaceAll(seek + "my", "my")
                            .replaceAll(seek + "mine", "mine")
                            .replaceAll(seek + "myself", "myself");
                }
                break;
            case 2:
                if (being.gender == Gender.PLURAL) {
                    working = working.replaceAll(seek + "user", "you")
                            .replaceAll(seek + "general", "you")
                            .replaceAll(seek + "I", "you")
                            .replaceAll(seek + "was", "were")
                            .replaceAll(seek + "me", "you")
                            .replaceAll(seek + "my", "your")
                            .replaceAll(seek + "mine", "yours")
                            .replaceAll(seek + "myself", "yourselves");
                } else {
                    working = working.replaceAll(seek + "user", "you")
                            .replaceAll(seek + "general", "you")
                            .replaceAll(seek + "I", "you")
                            .replaceAll(seek + "was", "were")
                            .replaceAll(seek + "me", "you")
                            .replaceAll(seek + "my", "your")
                            .replaceAll(seek + "mine", "yours")
                            .replaceAll(seek + "myself", "yourself");
                }
                break;
            default:
                String general = "the " + being.generalName, name;
                switch (specificNameBehavior)
                {
                    case 0: name = general;
                        break;
                    case 1: name = being.specificName == null ? general : being.specificName;
                        break;
                    default:
                            name = being.toString();
                }
                switch (being.gender)
                {
                    case PLURAL:
                        working = working.replaceAll(seek + "user", name)
                                .replaceAll(seek + "general", general)
                                .replaceAll(seek + "I", "they")
                                .replaceAll(seek + "was", "were")
                                .replaceAll(seek + "me", "them")
                                .replaceAll(seek + "my", "their")
                                .replaceAll(seek + "mine", "theirs")
                                .replaceAll(seek + "myself", "themselves");
                        break;
                    case THEY:
                        working = working.replaceAll(seek + "user", name)
                                .replaceAll(seek + "general", general)
                                .replaceAll(seek + "I", "they")
                                .replaceAll(seek + "was", "were")
                                .replaceAll(seek + "me", "them")
                                .replaceAll(seek + "my", "their")
                                .replaceAll(seek + "mine", "theirs")
                                .replaceAll(seek + "myself", "themself");
                        break;
                    case GENDERLESS:
                        working = working.replaceAll(seek + "user", name)
                                .replaceAll(seek + "general", general)
                                .replaceAll(seek + "I", "it")
                                .replaceAll(seek + "was", "was")
                                .replaceAll(seek + "me", "it")
                                .replaceAll(seek + "my", "its")
                                .replaceAll(seek + "mine", "its")
                                .replaceAll(seek + "myself", "itself");
                        break;
                    case MALE:
                        working = working.replaceAll(seek + "user", name)
                                .replaceAll(seek + "general", general)
                                .replaceAll(seek + "I", "he")
                                .replaceAll(seek + "was", "was")
                                .replaceAll(seek + "me", "him")
                                .replaceAll(seek + "my", "his")
                                .replaceAll(seek + "mine", "his")
                                .replaceAll(seek + "myself", "himself");
                        break;
                    case FEMALE:
                        working = working.replaceAll(seek + "user", name)
                                .replaceAll(seek + "general", general)
                                .replaceAll(seek + "I", "she")
                                .replaceAll(seek + "was", "was")
                                .replaceAll(seek + "me", "her")
                                .replaceAll(seek + "my", "her")
                                .replaceAll(seek + "mine", "hers")
                                .replaceAll(seek + "myself", "herself");
                        break;
                    case ADDITIONAL:
                        working = working.replaceAll(seek + "user", name)
                                .replaceAll(seek + "general", general)
                                .replaceAll(seek + "I", "xe")
                                .replaceAll(seek + "was", "was")
                                .replaceAll(seek + "me", "xim")
                                .replaceAll(seek + "my", "xis")
                                .replaceAll(seek + "mine", "xis")
                                .replaceAll(seek + "myself", "ximself");
                        break;
                    case OTHER:
                    default:
                        working = working.replaceAll(seek + "user", name)
                                .replaceAll(seek + "general", general)
                                .replaceAll(seek + "I", "qge")
                                .replaceAll(seek + "was", "was")
                                .replaceAll(seek + "me", "qgim")
                                .replaceAll(seek + "my", "qgis")
                                .replaceAll(seek + "mine", "qgis")
                                .replaceAll(seek + "myself", "qgimself");
                        break;

                }

        }
        return working;
    }

    /**
     * Sentence capitalization method; GWT-2.6.0-compatible, uses only some regular-expression-based standard library
     * methods to work within GWT's and JavaScript's limits. This is recommended to call after you finish with user()
     * and target() calls, since those may have replaced the first letter with a lower-case one.
     * @param text the sentence to capitalize
     * @return the capitalized sentence
     */
    // documentation for GWT 2.6.1, for future reference:
    // https://github.com/gwtproject/gwt-site/blob/408ba32d25e032026148e06287db0d3e64cead4e/src/main/markdown/doc/latest/RefJreEmulation.md
    public static String capitalize(String text)
    {
        String t2 = " " + text;
        String[] parts = t2.split("^[^A-Za-z"
                + "\u00B5\u00C0-\u00D6\u00D8-\u00F6\u00F8-\u01BA\u01BC-\u01BF\u01C4\u01C6-\u01C7\u01C9-\u01CA\u01CC-\u01F1"
                + "\u01F3-\u0293\u0295-\u02AF\u0370-\u0373\u0376-\u0377\u037B-\u037D\u0386\u0388-\u038A\u038C\u038E-\u03A1"
                + "\u03A3-\u03F5\u03F7-\u0481\u048A-\u0527\u0531-\u0556\u0561-\u0587\u10A0-\u10C5\u10C7\u10CD\u1D00-\u1D2B"
                + "\u1D6B-\u1D77\u1D79-\u1D9A\u1E00-\u1F15\u1F18-\u1F1D\u1F20-\u1F45\u1F48-\u1F4D\u1F50-\u1F57\u1F59\u1F5B"
                + "\u1F5D\u1F5F-\u1F7D\u1F80-\u1F87\u1F90-\u1F97\u1FA0-\u1FA7\u1FB0-\u1FB4\u1FB6-\u1FBB\u1FBE\u1FC2-\u1FC4"
                + "\u1FC6-\u1FCB\u1FD0-\u1FD3\u1FD6-\u1FDB\u1FE0-\u1FEC\u1FF2-\u1FF4\u1FF6-\u1FFB\u2102\u2107\u210A-\u2113"
                + "\u2115\u2119-\u211D\u2124\u2126\u2128\u212A-\u212D\u212F-\u2134\u2139\u213C-\u213F\u2145-\u2149\u214E"
                + "\u2183-\u2184\u2C00-\u2C2E\u2C30-\u2C5E\u2C60-\u2C7B\u2C7E-\u2CE4\u2CEB-\u2CEE\u2CF2-\u2CF3\u2D00-\u2D25"
                + "\u2D27\u2D2D\uA640-\uA66D\uA680-\uA697\uA722-\uA76F\uA771-\uA787\uA78B-\uA78E\uA790-\uA793\uA7A0-\uA7AA"
                + "\uA7FA\uFB00-\uFB06\uFB13-\uFB17\uFF21-\uFF3A\uFF41-\uFF5A]+", 2);
        if(parts.length < 2 || parts[1].isEmpty())
            return text;
        char firstLetter = parts[1].charAt(0), replacer = Character.toUpperCase(firstLetter);
        return text.replaceFirst(String.valueOf(firstLetter), String.valueOf(replacer));
    }

    /**
     * Gender and pronoun preference can be incredibly hard to handle universally.
     * This at least tries to provide pronoun handling for the common cases, such as "you" not needing a gendered
     * pronoun at all, and supports male, female, genderless (using "it" and related forms; preferred especially for
     * things that aren't alive, and in most cases not recommended for people), "they" (using "they" in place of "he" or
     * "she"; preferred in some cases when describing someone with a non-specific gender or an unknown gender) pronouns,
     * and plural for when a group of individuals, regardless of gender or genders, is referred to as a single Being,
     * plus some support for uncommon situations, like additional gender (as in, a gender that is in addition to male
     * and female but that is not genderless, which has a clear use case when describing non-human species, and a more
     * delicate use for humans who use non-binary gender pronouns; hopefully "xe" is suitable), and finally an "other"
     * pronoun that is unpronounceable and can be used as a replacement target for customized pronouns. Here, the
     * non-binary gendered pronouns are modified from the male pronouns by replacing 'h' with 'x' (he becomes xe, his
     * becomes xis). The "other" pronouns replace the 'h' in the male pronouns with 'qg', except for in one case. Where,
     * if the female pronoun were used, it would be "hers", but the male pronoun in that case would be "his", changing
     * the male pronoun would lead to a difficult-to-replace case because "his" is also used in the case where the
     * female pronoun is the usefully distinct "her". Here, the "other" gender diverges from what it usually does, and
     * uses "qgims" in place of "his" or "hers". The "other" pronouns should be replaced before being displayed, since
     * they are unpronounceable and so are probably confusing out of context.
     * <br>
     * The actual implementation of the described behavior is in the parent Phrasing class, and relies on whether the
     * pronoun in question applies in the first person, second person, or third person.
     */
    public enum Gender {
        MALE,
        FEMALE,
        GENDERLESS,
        THEY,
        ADDITIONAL,
        OTHER,
        PLURAL
    }

    /**
     * Constructs a Being given only a general name; primarily meant for non-living things. Gender will be assigned
     * GENDERLESS and specificName will be null.
     * @param generalName the name of the general type of Being, like "store manager" or "copper dragon"
     * @return a new Being with only a general name and GENDERLESS for its gender.
     */
    public static Being being(String generalName)
    {
        return new Being(generalName);
    }

    /**
     * Constructs a Being given a general name and a Gender; useful in most cases when a Being doesn't need or have a
     * specific name or title of some kind. specificName will be null.
     * @param generalName the name of the general type of Being, like "store manager" or "copper dragon"
     * @param gender a Gender enum; should not be null
     * @return a new Being with a general name and the specified gender
     */
    public static Being being(String generalName, Gender gender)
    {
        return new Being(generalName, gender);
    }

    /**
     * Constructs a Being taking a general name, gender, and specific name; useful primarily when a being's specific
     * name is important in some way, and there is a reason to display it. The specific name can include a title if one
     * is appropriate, and if the specific name is used, then there won't be a generic "the" before the name (there will
     * be if the general name is used).
     * @param generalName the name of the general type of Being, like "store manager" or "copper dragon"
     * @param gender a Gender enum; should not be null
     * @param specificName the name of this specific individual, like "Mary Jane Johnson-Turner" or "Duke Graham"
     * @return a new Being with the specified general and specific names, and the specified gender
     */
    public static Being being(String generalName, Gender gender, String specificName)
    {
        return new Being(generalName, gender, specificName);
    }
    /**
     * Simple data class storing a generalName, gender, and potentially a specificName. Can be constructed with the
     * constructors in this class, or using the static being() method on Phrasing.
     */
    public static class Being {
        /**
         * A Gender enum; defaults to GENDERLESS if not supplied to make constructing inanimate Beings a little easier.
         */
        public Gender gender = Gender.GENDERLESS;
        /**
         * The given name of the Being, which may be null if the Being doesn't have one (this is the default, and is meant
         * for inanimate Beings). If the specificName is null, the generalName will always be used instead.
         */
        public String specificName = null;
        /**
         * The name of the general type of Being if you don't know its specificName or it doesn't have one. Examples include
         * "stone idol", "thug", or "cyber-goblin". When a generalName is used, "the" is generally prefixed, but this is
         * never the case with a specificName.
         */
        public String generalName;

        /**
         * Constructor given only a general name; primarily meant for non-living things. Gender will be assigned GENDERLESS
         * and specificName will be null.
         * @param generalName the name of the general type of Being, like "store manager" or "copper dragon"
         */
        public Being(String generalName) {
            this.generalName = generalName;
        }

        /**
         * Constructor given a general name and a Gender; useful in most cases when a Being doesn't need or have a specific
         * name or title of some kind. specificName will be null.
         * @param generalName the name of the general type of Being, like "store manager" or "copper dragon"
         * @param gender a Gender enum; should not be null
         */
        public Being(String generalName, Gender gender) {
            this.gender = gender;
            this.generalName = generalName;
        }

        /**
         * Constructor taking a general name, gender, and specific name; useful primarily when a being's specific name is
         * important in some way, and there is a reason to display it. The specific name can include a title if one is
         * appropriate, and if the specific name is used, then there won't be a generic "the" before the name (there will be
         * if the general name is used).
         * @param generalName the name of the general type of Being, like "store manager" or "copper dragon"
         * @param gender a Gender enum; should not be null
         * @param specificName the name of this specific individual, like "Mary Jane Johnson-Turner" or "Duke Graham"
         */
        public Being(String generalName, Gender gender, String specificName) {
            this.gender = gender;
            this.specificName = specificName;
            this.generalName = generalName;
        }

        /**
         * Gets the specific name of this Being if it is non-null, otherwise gets the general name prefixed by "the ".
         * @return either this Being's specific name, or "the " followed by this Being's general name
         */
        @Override
        public String toString() {
            if(specificName != null)
                return specificName;

            return "the " + generalName;
        }
    }

}
