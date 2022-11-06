package games;

import players.Player;

public class TicTacToe {
    private Board board;
    private boolean gameRunning = false;

    public void startGame(Player xPlayer, Player oPlayer) {
        if (gameRunning) throw new RuntimeException("Game already running"); //TODO: create custom exception.
        gameRunning = true;

        // Setup
        Player[] players = new Player[2];
        board = new Board(3, 3);

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
                System.out.println(board);
                System.out.println("\n" + player.getIcon() + "'s turn\n" + board);
                int pos = player.move(board);

                // Validate
//                if (!validateMove(pos)) {
//                    if (player.getIcon() == Icon.CROSS) winner = players[1];
//                    else winner = player;
//
//                    loop = false;
//                    break;
//                }

                // Set move on board
                board.set(pos, player.getIcon());

                // Check for winner
                if (hasWon(player.getIcon())) {
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
            System.out.println("\nDraw");
            System.out.println(board);
            return;
        }

        System.out.println("\nWinner is: '" + winner.getIcon() + "'");
        System.out.println(board);
        gameRunning = false;
    }

    public Board getBoard() {
        return this.board;
    }

    private boolean validateMove(int pos) {
        if (pos < 0 || pos > 8) return false;

        int row = pos / 3;
        int col = pos % 3;

        return board.data[row][col] == Icon.NO_ICON;
    }

    private boolean hasWon(Icon icon) {
        //Check all columns for 3 in a row of icon
        for (int row = 0; row < board.height-2; row++) {
            for (int col = 0; col < board.width; col++) {
                if (board.data[row][col] == icon
                        && board.data[row][col] == board.data[row + 1][col]
                        && board.data[row][col] == board.data[row + 2][col]) {
                    return true;
                }
            }
        }
        //Check all rows for 3 in a row of icon
        for (int row = 0; row < board.height; row++) {
            for (int col = 0; col < board.width-2; col++) {
                if (board.data[row][col] == icon
                        && board.data[row][col] == board.data[row][col + 1]
                        && board.data[row][col] == board.data[row][col + 2]) {
                    return true;
                }
            }
        }
        //Check diagonals down-right
        for (int row = 0; row < board.height-2; row++) {
            for (int col = 0; col < board.width-2; col++) {
                if (board.data[row][col] == icon
                        && board.data[row][col] == board.data[row + 1][col + 1]
                        && board.data[row][col] == board.data[row + 2][col + 2]) {
                    return true;
                }
            }
        }
        //Check diagonals down-left
        for (int row = 0; row < board.height-2; row++) {
            for (int col = 2; col < board.width; col++) {
                if (board.data[row][col] == icon
                        && board.data[row][col] == board.data[row + 1][col - 1]
                        && board.data[row][col] == board.data[row + 2][col - 2]) {
                    return true;
                }
            }
        }
        return false;
    }
}
