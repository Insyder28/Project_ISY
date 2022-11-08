public class Main {

    public static void main(String[] args) {

        GUI gui = new GUI(); //creates the frame
        gui.MainFrame();
        //gui.endGame("Test");
        gui.setCurrentPlayer('X');
        gui.getMove();


    }
}