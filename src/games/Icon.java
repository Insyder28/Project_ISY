package games;

/**
 * Represents an icon that can be placed on a {@link Board}.
 * @author Hindrik Kroes
 */
public enum Icon {
    CROSS('X'), NOUGHT('O'), NO_ICON(' ');

    // Private variable
    private final char icon;
    // Constructor (must be private)
    Icon(char icon) {
        this.icon = icon;
    }

    /**
     * Getter for char.
     * @return The char value that the Icon represents.
     */
    public char getChar() {
        return icon;
    }

    /**
     * Returns the Icon of the opponent.
     * @return The Icon of the opponent.
     */
    public Icon opponentIcon() {
        return this == Icon.CROSS ? Icon.NOUGHT : Icon.CROSS;
    }
}