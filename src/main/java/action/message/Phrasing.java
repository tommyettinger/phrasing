package action.message;

/**
 * Created by Tommy Ettinger on 2/26/2016.
 */
public class Phrasing {

    /**
     * Takes a format string fmt that should be a sentence in past-tense, containing a formatted indicator of the
     * subject of the sentence (the user of the action), {@code @user}, and changes any references to the user to match
     * the basic String value of being if the grammatical person (1, 2, or 3 representing first, second, or third person
     * respectively) requires that Being to be addressed by something other than "you" or "I". Also changes any pronouns
     * that are replaced differently based on gender, like {@code @my} being replaced with "your", "his", or "her" and
     * {@code @mine} being replaced with "yours", "his", or "hers", to be replaced with the correct pronoun for being's
     * gender. Returns a new String that in many cases should be passed to other methods in this class to replace any
     * other indicators. Specifically using {@code @user} is not required; any "@thing" terms will be replaced,
     * including {@code @was} to replace the past-tense of "is" with "was" or "were", like "I was, you were, she was,
     * they were" as fits the gender or genders. A Being object can represent a group of people of any gender using the
     * Gender.PLURAL value; this should not be confused with the gender-non-specific singular usage of "they" with the
     * Gender.THEY value.
     * @param fmt a format string using the conventions of this class.
     * @param person grammatical person for this Being; 1 for first-person, 2 for second-person, and 3 for third-person
     * @param being a Being that is using the action described in the format string
     * @return a (not necessarily complete) conversion of fmt to a new String with terms like {@code @user} replaced
     */
    public static String user(String fmt, int person, Being being)
    {
        return alter(fmt, person, being, "[@]");
    }
    /**
     * Takes a format string fmt that should be a sentence in past-tense, containing a formatted indicator of the
     * object of the sentence (the target of the action), {@code ~target}, and changes any references to the target to
     * match the basic String value of being if the grammatical person (1, 2, or 3 representing first, second, or third
     * person respectively) requires that Being to be addressed by something other than "you" or "I". Also changes any
     * pronouns that are replaced differently based on gender, like {@code ~my} being replaced with "your", "his", or
     * "her" and {@code ~mine} being replaced with "yours", "his", or "hers", to be replaced with the correct pronoun
     * for being's gender. Returns a new String that in many cases should be passed to other methods in this class to
     * replace any other indicators. Specifically using {@code ~target} is not required; any "~thing" terms will be
     * replaced, including {@code ~was} to replace the past-tense of "is" with "was" or "were", like "I was, you were,
     * she was, they were" as fits the gender or genders. A Being object can represent a group of people of any gender
     * using the Gender.PLURAL value; this should not be confused with the gender-non-specific singular usage of "they"
     * with the Gender.THEY value.
     * @param fmt a format string using the conventions of this class.
     * @param person grammatical person for this Being; 1 for first-person, 2 for second-person, and 3 for third-person
     * @param being a Being that is using the action described in the format string
     * @return a (not necessarily complete) conversion of fmt to a new String with terms like {@code ~target} replaced
     */
    public static String target(String fmt, int person, Being being)
    {
        return alter(fmt, person, being, "[~]");
    }
    /**
     * Internal use.
     * @param fmt a format string using the conventions of this class. {@code @user} must be present in the string
     * @param person grammatical person for this Being; 1 for first-person, 2 for second-person, and 3 for third-person
     * @param being a Being that is using the action described in the format string
     * @param seek search for this in the format string before a pronoun
     * @return a (not necessarily complete) conversion of fmt to a new String with at least the {@code @user} replaced
     */
    private static String alter(String fmt, int person, Being being, String seek)
    {
        String working = fmt;
        switch (person) {
            case 1:
                if (being.gender == Gender.PLURAL) {
                    working = working.replaceAll(seek + "user", "we")
                            .replaceAll(seek + "I", "we")
                            .replaceAll(seek + "was", "were")
                            .replaceAll(seek + "me", "us")
                            .replaceAll(seek + "my", "our")
                            .replaceAll(seek + "mine", "ours")
                            .replaceAll(seek + "myself", "ourselves");
                } else {
                    working = working.replaceAll(seek + "user", "I")
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
                            .replaceAll(seek + "I", "you")
                            .replaceAll(seek + "was", "were")
                            .replaceAll(seek + "me", "you")
                            .replaceAll(seek + "my", "your")
                            .replaceAll(seek + "mine", "yours")
                            .replaceAll(seek + "myself", "yourselves");
                } else {
                    working = working.replaceAll(seek + "user", "you")
                            .replaceAll(seek + "I", "you")
                            .replaceAll(seek + "was", "were")
                            .replaceAll(seek + "me", "you")
                            .replaceAll(seek + "my", "your")
                            .replaceAll(seek + "mine", "yours")
                            .replaceAll(seek + "myself", "yourself");
                }
                break;
            default:
                switch (being.gender)
                {
                    case PLURAL:
                        working = working.replaceAll(seek + "user", being.toString())
                                .replaceAll(seek + "I", "they")
                                .replaceAll(seek + "was", "were")
                                .replaceAll(seek + "me", "them")
                                .replaceAll(seek + "my", "their")
                                .replaceAll(seek + "mine", "theirs")
                                .replaceAll(seek + "myself", "themselves");
                        break;
                    case THEY:
                        working = working.replaceAll(seek + "user", being.toString())
                                .replaceAll(seek + "I", "they")
                                .replaceAll(seek + "was", "were")
                                .replaceAll(seek + "me", "them")
                                .replaceAll(seek + "my", "their")
                                .replaceAll(seek + "mine", "theirs")
                                .replaceAll(seek + "myself", "themself");
                        break;
                    case GENDERLESS:
                        working = working.replaceAll(seek + "user", being.toString())
                                .replaceAll(seek + "I", "it")
                                .replaceAll(seek + "was", "was")
                                .replaceAll(seek + "me", "it")
                                .replaceAll(seek + "my", "its")
                                .replaceAll(seek + "mine", "its")
                                .replaceAll(seek + "myself", "itself");
                        break;
                    case MALE:
                        working = working.replaceAll(seek + "user", being.toString())
                                .replaceAll(seek + "I", "he")
                                .replaceAll(seek + "was", "was")
                                .replaceAll(seek + "me", "him")
                                .replaceAll(seek + "my", "his")
                                .replaceAll(seek + "mine", "his")
                                .replaceAll(seek + "myself", "himself");
                        break;
                    case FEMALE:
                        working = working.replaceAll(seek + "user", being.toString())
                                .replaceAll(seek + "I", "she")
                                .replaceAll(seek + "was", "was")
                                .replaceAll(seek + "me", "her")
                                .replaceAll(seek + "my", "her")
                                .replaceAll(seek + "mine", "hers")
                                .replaceAll(seek + "myself", "herself");
                        break;
                    case ADDITIONAL:
                        working = working.replaceAll(seek + "user", being.toString())
                                .replaceAll(seek + "I", "xe")
                                .replaceAll(seek + "was", "was")
                                .replaceAll(seek + "me", "xim")
                                .replaceAll(seek + "my", "xis")
                                .replaceAll(seek + "mine", "xis")
                                .replaceAll(seek + "myself", "ximself");
                        break;
                    case OTHER:                     
                    default:
                        working = working.replaceAll(seek + "user", being.toString())
                                .replaceAll(seek + "I", "``e")
                                .replaceAll(seek + "was", "was")
                                .replaceAll(seek + "me", "``im")
                                .replaceAll(seek + "my", "``is")
                                .replaceAll(seek + "mine", "``is")
                                .replaceAll(seek + "myself", "``imself");
                        break;

                }

        }
        return working;
    }
}
