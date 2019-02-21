import java.util.Random;

/**
 * The main logic of the game Tetris.
 * In particular, description the key logic, game update, creation and rotation of figures.
 *
 * @author Statkevich Nataliya.
 */

class Tetris {

    //Number of pieces that exist.
    private static final int TYPE_COUNT = TypeFigure.values().length;

    //BoardPanel instance.
    private BoardPanel board;
    private SidePanel side;

    private boolean isPaused;
    private boolean isNewGame;
    private boolean isGameOver;

    private int score;
    private Random random;
    private TimerClock logicTimer;

    private float gameSpeed;

    private TypeFigure currentType;
    private TypeFigure nextType;

    private int currentCol;
    private int currentRow;
    private int currentRotation;

    //Delayed fall of the figure.
    private int delayFall;


    //Creates a new Tetris instance.
    Tetris() {
        this.board = new BoardPanel(this);
        this.side = new SidePanel(this);
    }

    void onKeyDownPressed() {
        if (!isPaused && delayFall == 0) {
            logicTimer.setCyclesPerSecond(25.0f);
        }
    }

    void onKeyDownReleased() {
        logicTimer.setCyclesPerSecond(gameSpeed);
        logicTimer.reset();
    }

    void onLeft() {
        if (!isPaused && board.isValidAndEmpty(currentType, currentCol - 1, currentRow, currentRotation)) {
            currentCol--;
        }
    }

    void onRight() {
        if (!isPaused && board.isValidAndEmpty(currentType, currentCol + 1, currentRow, currentRotation)) {
            currentCol++;
        }
    }

    void onRotate() {
        if (!isPaused) {
            rotatePiece((currentRotation == 3) ? 0 : currentRotation + 1);
        }
    }

    void onPause() {
        if (!isGameOver && !isNewGame) {
            isPaused = !isPaused;
            logicTimer.setPaused(isPaused);
        }
    }

    void onStart() {
        if (isGameOver || isNewGame) {
            resetGame();
        }
    }

    //Initializes everything and enters the game loop.
    void startGame() {
        this.random = new Random();
        this.isNewGame = true;
        this.gameSpeed = 1.0f;

        this.logicTimer = new TimerClock(gameSpeed);
        logicTimer.setPaused(true);
    }

    //Updates the game and handles the bulk of it's logic.
    private void updateGame() {
        if (board.isValidAndEmpty(currentType, currentCol, currentRow + 1, currentRotation)) {
            currentRow++;
        } else {
            board.addPiece(currentType, currentCol, currentRow, currentRotation);

            int cleared = board.checkLines();
            switch (cleared) {
                case 1:
                    score += 100;
                    break;
                case 2:
                    score += 300;
                    break;
                case 3:
                    score += 700;
                    break;
                case 4:
                    score += 1500;
                    break;
            }

            gameSpeed += 0.040f;
            logicTimer.setCyclesPerSecond(gameSpeed);
            logicTimer.reset();

            delayFall = 25;
            createsFigure();
        }
    }


    void renderGame() {
        board.repaint();
        side.repaint();
    }

    //At the beginning of a new game, resets game variables to default values.
    private void resetGame() {
        this.score = 0;
        this.gameSpeed = 1.0f;
        this.nextType = TypeFigure.values()[random.nextInt(TYPE_COUNT)];
        this.isNewGame = false;
        this.isGameOver = false;
        board.clear();
        logicTimer.reset();
        logicTimer.setCyclesPerSecond(gameSpeed);
        createsFigure();
    }

    //Creates a shape and resets variables to default values.
    private void createsFigure() {
        this.currentType = nextType;
        this.currentCol = currentType.getCenterColumn();
        this.currentRow = currentType.getCenterRow();
        this.currentRotation = 0;
        this.nextType = TypeFigure.values()[random.nextInt(TYPE_COUNT)];

        if (!board.isValidAndEmpty(currentType, currentCol, currentRow, currentRotation)) {
            this.isGameOver = true;
            logicTimer.setPaused(true);
        }
    }

    /**
     * Attempts to set the rotation of the current figure to newRotation.
     *
     * @param newRotation Rotation of the new piece.
     */
    private void rotatePiece(int newRotation) {
        int newColumn = currentCol;
        int newRow = currentRow;

        //Determine the number of empty rows or columns on this side.
        int left = currentType.getLeftEmptyCol(newRotation);
        int right = currentType.getRightEmptyCol(newRotation);
        int top = currentType.getTopEmptyRow(newRotation);
        int bottom = currentType.getBottomEmptyRow(newRotation);

        //If the current figure is too far to the left or right, move the figure away from the edges
        //so that the figure doesn't clip out of the map and automatically become invalid.
        if (currentCol < -left) {
            newColumn -= currentCol - left;
        } else if (currentCol + currentType.getFigureSize() - right >= BoardPanel.COL_COUNT) {
            newColumn -= (currentCol + currentType.getFigureSize() - right) - BoardPanel.COL_COUNT + 1;
        }
        if (currentRow < -top) {
            newRow -= currentRow - top;
        } else if (currentRow + currentType.getFigureSize() - bottom >= BoardPanel.ROW_COUNT) {
            newRow -= (currentRow + currentType.getFigureSize() - bottom) - BoardPanel.ROW_COUNT + 1;
        }

        if (board.isValidAndEmpty(currentType, newColumn, newRow, newRotation)) {
            currentRotation = newRotation;
            currentRow = newRow;
            currentCol = newColumn;
        }
    }


    boolean isPaused() {
        return isPaused;
    }

    boolean isGameOver() {
        return isGameOver;
    }

    boolean isNewGame() {
        return isNewGame;
    }

    int getScore() {
        return score;
    }


    TypeFigure getPieceType() {
        return currentType;
    }

    TypeFigure getNextPieceType() {
        return nextType;
    }

    int getPieceCol() {
        return currentCol;
    }

    int getPieceRow() {
        return currentRow;
    }

    int getPieceRotation() {
        return currentRotation;
    }

    BoardPanel getBoard() {
        return board;
    }

    SidePanel getSide() {
        return side;
    }

    void updateTimer() {
        logicTimer.update();
        if (logicTimer.hasElapsedCycle()) {
            updateGame();
        }

    }

    //Decrement the drop cool down if necessary.
    void decrementDelayFall() {
        if (delayFall > 0) {
            delayFall--;
        }
    }
}
