package action.message;

/**
 * Created by Tommy Ettinger on 2/26/2016.
 */
public class Being {
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

    @Override
    public String toString() {
        if(specificName != null)
            return specificName;

        return "the " + generalName;
    }
}
