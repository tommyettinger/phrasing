package action.message;

/**
 * Gender and pronoun preference can be incredibly hard to handle universally.
 * This at least tries to provide pronoun handling for the common cases, such as "you" not needing a gendered pronoun
 * at all, and supports male, female, genderless (using "it" and related forms; preferred especially for things that
 * aren't alive, and in most cases not recommended for people), "they" (using "they" in place of "he" or "she";
 * preferred in some cases when describing someone with a non-specific gender or an unknown gender) pronouns, and plural
 * for when a group of individuals, regardless of gender or genders, is referred to as a single Being, plus some support
 * for uncommon situations, like additional gender (as in, a gender that is in addition to male and female but that is
 * not genderless, which has a clear use case when describing non-human species, and a more delicate use for humans who
 * use non-binary gender pronouns; hopefully "xe" is suitable), and finally an "other" pronoun that is unpronounceable
 * and can be used as a replacement target for customized pronouns. Here, the non-binary gendered pronouns are modified
 * from the male pronouns by replacing 'h' with 'x' (he becomes xe, his becomes xis) and the "other" pronouns replace
 * the 'h' in the male pronouns with two backtick characters, '``'.
 *
 * The actual implementation of the described behavior is in the Phrasing class in this package, and relies on whether
 * the pronoun in question applies in the first person, second person, or third person.
 *
 * Created by Tommy Ettinger on 2/26/2016.
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
