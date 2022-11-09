package gui;

public class InstanceAlreadyExistsException extends RuntimeException {
    public InstanceAlreadyExistsException() {
        super("There is already an instance of GUI");
    }
}
