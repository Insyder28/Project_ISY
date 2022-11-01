package games;

import networking.GameSocket;
import players.Player;

public class TicTacToe {
    private Player[] players = new Player[2];
    private Board board = new Board(3, 3);

    public void startLocalGame(Player xPlayer, Player oPlayer) {
        // Setup
        players[0] = xPlayer;
        xPlayer.setIcon(Icon.CROSS);

        players[1] = oPlayer;
        oPlayer.setIcon(Icon.NOUGHT);

        boolean loop = true;
        Player winner = null;
        int counter = 0;

        // Start game
        while (loop) {
            for (Player player : players) {
                // Check if board is full
                if (counter >= 9){
                    loop = false;
                    break;
                }

                // Get move from player
                int pos = player.move(board);

                // Validate
                if (!validateMove(pos)) {
                    if (player.getIcon() == Icon.CROSS) winner = players[1];
                    else winner = player;

                    loop = false;
                    break;
                }

                // Set move on board
                board.set(pos, player.getIcon());

                // Check for winner
                if (checkWinner(player.getIcon())) {
                    winner = player;
                    loop = false;
                    break;
                }

                // Increment move counter
                counter++;
            }
        }

        // Game finished
        if (winner == null) {
            System.out.println("draw");
            return;
        }

        System.out.println("\nWinner is: '" + winner.getIcon() + "'");
        System.out.println(board);
    }

    public void startOnlineGame(Player player) {
        // ...
    }

    private boolean validateMove(int pos) {
        if (pos < 0 || pos > 8) return false;

        int row = pos / 3;
        int col = pos % 3;

        return board.data[row][col] == Icon.NO_ICON;
    }

    private boolean checkWinner(Icon icon) {
        // Check rows
        for (int i = 0; i < board.height; i++)
            if (checkRow(i, icon)) return true;

        // Check columns
        for (int i = 0; i < board.width; i++) {
            if (checkCol(i, icon)) return true;
        }

        // Check top-left to bottom-right
        if (board.data[0][0] == icon && board.data[1][1] == icon && board.data[2][2] == icon)
            return true;

        // Check bottom-left to top right
        return board.data[2][0] == icon && board.data[1][1] == icon && board.data[0][2] == icon;
    }

    private boolean checkRow(int index, Icon icon) {
        for (Icon col : board.data[index])
            if (col != icon) return false;
        return true;
    }

    private boolean checkCol(int index, Icon icon) {
        for (int i = 0; i < board.height; i++)
            if (board.data[i][index] != icon) return false;
        return true;
    }
}
