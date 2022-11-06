package games;
public enum Icon {   // to save as "Seed.java"
    CROSS('X'), NOUGHT('O'), NO_ICON(' ');

    // Private variable
    private final char icon;
    // Constructor (must be private)
    Icon(char icon) {
        this.icon = icon;
    }
    // Public Getter
    public char getChar() {
        return icon;
    }

    public Icon opponentIcon() {
        return this == Icon.CROSS ? Icon.NOUGHT : Icon.CROSS;
    }
}