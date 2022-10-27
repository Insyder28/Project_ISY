package games;

import players.Player;

public class TicTacToe {
    private final Player[] players = new Player[2];
    private final Board board = new Board(3, 3);

    public TicTacToe (Player xPlayer, Player oPlayer) {
        players[0] = xPlayer;
        xPlayer.setIcon(Icon.CROSS);

        players[1] = oPlayer;
        oPlayer.setIcon(Icon.NOUGHT);
    }

    public void startGame() {
        boolean loop = true;
        Player winner = null;
        int counter = 0;

        while (loop) {
            for (Player player : players) {
                if (counter >= 9){
                    loop = false;
                    break;
                }

                board.set(player.move(board), player.getIcon());

                if (checkWinner(player.getIcon())) {
                    winner = player;
                    loop = false;
                    break;
                }

                counter++;
            }
        }

        // Game finished
        if (winner == null) {
            System.out.println("draw");
            return;
        }

        System.out.println("\nWinner is: " + winner.getIcon());
        System.out.println(board);
    }

    private boolean checkWinner(Icon player) {
        // Check rows
        for (int i = 0; i < board.height; i++)
            if (checkRow(i, player)) return true;

        // Check columns
        for (int i = 0; i < board.width; i++) {
            if (checkCol(i, player)) return true;
        }

        // Check top-left to bottom-right
        if (board.data[0][0] == player && board.data[1][1] == player && board.data[2][2] == player)
            return true;

        // Check bottom-left to top right
        return board.data[2][0] == player && board.data[1][1] == player && board.data[0][2] == player;
    }

    private boolean checkRow(int index, Icon player) {
        for (Icon col : board.data[index])
            if (col != player) return false;
        return true;
    }

    private boolean checkCol(int index, Icon player) {
        for (int i = 0; i < board.height; i++)
            if (board.data[i][index] != player) return false;
        return true;
    }
}
