package gui;

public class Main {

    public static void main(String[] args) {

        GUI gui = new GUI(); //creates the frame
        gui.MainFrame();
        gui.setCurrentPlayer('X');

        System.out.println(gui.getMove());
        System.out.println(gui.getMove());

        gui.setCurrentPlayer('O');

        char[][] board = {
                {' ', 'X', ' '},
                {' ', ' ', 'O'},
                {'X', ' ', ' '},
        };

        gui.updateBoard(board);

        gui.endGame("You won!");
    }
}