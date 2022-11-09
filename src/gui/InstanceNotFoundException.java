package gui;

public class InstanceNotFoundException extends RuntimeException {
    public InstanceNotFoundException() {
        super("There is no instance of GUI");
    }
}
