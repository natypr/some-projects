import javax.swing.*;
import java.awt.*;

/**
 * Description of the playing field.
 *
 * @author Statkevich Nataliya.
 */
public class BoardPanel extends JPanel {

    static final int TILE_SIZE = 28;
    static final int SHADOW_WIDTH = 5;
    static final int COL_COUNT = 10;
    private static final int SHADOW_GHOST_FIGURE = 30;
    private static final int BORDER_WIDTH = 7;
    private static final int PANEL_WIDTH = COL_COUNT * TILE_SIZE + BORDER_WIDTH * 2;
    private static final int VISIBLE_ROW_COUNT = 20;
    static final int PANEL_HEIGHT = VISIBLE_ROW_COUNT * TILE_SIZE + BORDER_WIDTH * 2;
    private static final int HIDDEN_ROW_COUNT = 2;
    static final int ROW_COUNT = VISIBLE_ROW_COUNT + HIDDEN_ROW_COUNT;
    private static final int CENTER_X = COL_COUNT * TILE_SIZE / 2;
    private static final Font LARGE_FONT = new Font("Verdana", Font.BOLD, 20);
    private static final Font MIDDLE_FONT = new Font("Verdana", Font.BOLD, 16);

    private static final Color BOARD_COLOR = new Color(0x000000);
    private static final Color TEXT_COLOR = new Color(0xffffff);

    private Tetris tetris;
    private TypeFigure[][] tiles;


    BoardPanel(Tetris tetris) {
        this.tetris = tetris;
        this.tiles = new TypeFigure[ROW_COUNT][COL_COUNT];

        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(BOARD_COLOR);
    }

    /**
     * Clears all tiles of board.
     */
    void clear() {
        for (int i = 0; i < ROW_COUNT; i++) {
            for (int j = 0; j < COL_COUNT; j++) {
                tiles[i][j] = null;
            }
        }
    }

    /**
     * Determines whether or not a tile can be placed at the coordinates.
     *
     * @param type     Type of piece to use.
     * @param x        Coordinate of the tile.
     * @param y        Coordinate of the tile.
     * @param rotation The rotation of the figure.
     * @return Whether or not the position is valid.
     */
    boolean isValidAndEmpty(TypeFigure type, int x, int y, int rotation) {

        //Is the tile in a valid column?
        if (x < -type.getLeftEmptyCol(rotation) || x + type.getFigureSize() - type.getRightEmptyCol(rotation) >= COL_COUNT) {
            return false;
        }

        //Is the tile in a valid row?
        if (y < -type.getTopEmptyRow(rotation) || y + type.getFigureSize() - type.getBottomEmptyRow(rotation) >= ROW_COUNT) {
            return false;
        }

        //Scroll through each tile of figure and see if it conflicts with existing tile.
        for (int col = 0; col < type.getFigureSize(); col++) {
            for (int row = 0; row < type.getFigureSize(); row++) {
                if (type.isTile(col, row, rotation) && isOccupied(x + col, y + row)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Adds a tile to the game board, if the tile exists.
     *
     * @param type     Type of figure to place.
     * @param x        Coordinate of the tile.
     * @param y        Coordinate of the tile.
     * @param rotation Rotation of the figure.
     */
    void addPiece(TypeFigure type, int x, int y, int rotation) {
        for (int col = 0; col < type.getFigureSize(); col++) {
            for (int row = 0; row < type.getFigureSize(); row++) {
                if (type.isTile(col, row, rotation)) {
                    setTile(col + x, row + y, type);
                }
            }
        }
    }


    /**
     * Checks the board to see if any lines have been cleared.
     *
     * @return Number of lines that were cleared.
     */
    int checkLines() {
        int fullLines = 0;

        for (int row = 0; row < ROW_COUNT; row++) {
            if (checkLine(row)) {
                fullLines++;
            }
        }
        return fullLines;
    }

    /**
     * Checks whether or not row is full.
     * And if the line is full, then we shift all the lines above it by one down.
     *
     * @param line Row to check.
     * @return Whether or not this row is full.
     */
    private boolean checkLine(int line) {
        for (int col = 0; col < COL_COUNT; col++) {
            if (!isOccupied(col, line)) {
                return false;
            }
        }
        for (int row = line - 1; row >= 0; row--) {
            for (int col = 0; col < COL_COUNT; col++) {
                setTile(col, row + 1, getTile(col, row));
            }
        }
        return true;
    }

    // Checks if a tile is occupied
    private boolean isOccupied(int x, int y) {
        return tiles[y][x] != null;
    }

    //Sets a tile located at the desired column and row.
    private void setTile(int x, int y, TypeFigure type) {
        tiles[y][x] = type;
    }

    //Gets a tile by it's column and row.
    private TypeFigure getTile(int x, int y) {
        return tiles[y][x];
    }


    /**
     * Drawing the board depending on the current state of the game.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.translate(BORDER_WIDTH, BORDER_WIDTH);

        if (tetris.isPaused()) {
            g.setFont(LARGE_FONT);
            g.setColor(TEXT_COLOR);
            String msg = "PAUSED";

            g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, 170);
            g.setFont(MIDDLE_FONT);
            msg = "Press P to Play";
            g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, 330);

        } else if (tetris.isNewGame() || tetris.isGameOver()) {
            g.setFont(LARGE_FONT);
            g.setColor(TEXT_COLOR);

            String msg = tetris.isNewGame() ? "TETRIS" : "GAME OVER";
            g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, 170);
            g.setFont(MIDDLE_FONT);
            msg = "Press Enter to Play" + (tetris.isNewGame() ? "" : " Again");
            g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, 330);
        } else {


            TypeFigure type = tetris.getPieceType();
            int pieceCol = tetris.getPieceCol();
            int pieceRow = tetris.getPieceRow();
            int rotation = tetris.getPieceRotation();

            //Draw the piece onto the board (when falling).
            for (int col = 0; col < type.getFigureSize(); col++) {
                for (int row = 0; row < type.getFigureSize(); row++) {
                    if (pieceRow + row >= 2 && type.isTile(col, row, rotation)) {
                        drawTile(type, (pieceCol + col) * TILE_SIZE, (pieceRow + row - HIDDEN_ROW_COUNT) * TILE_SIZE, g);
                    }
                }
            }
            //Draw the figure onto the board (when it landed on board).
            for (int x = 0; x < COL_COUNT; x++) {
                for (int y = HIDDEN_ROW_COUNT; y < ROW_COUNT; y++) {
                    TypeFigure tile = getTile(x, y);
                    if (tile != null) {
                        drawTile(tile, x * TILE_SIZE, (y - HIDDEN_ROW_COUNT) * TILE_SIZE, g);
                    }
                }
            }


            //Draw a ghost of figure one row higher than the collision.
            Color base = type.getBaseColor();
            base = new Color(base.getRed(), base.getGreen(), base.getBlue(), SHADOW_GHOST_FIGURE);
            for (int lowest = pieceRow; lowest < ROW_COUNT; lowest++) {

                if (isValidAndEmpty(type, pieceCol, lowest, rotation)) {
                    continue;
                }

                lowest--;
                for (int col = 0; col < type.getFigureSize(); col++) {
                    for (int row = 0; row < type.getFigureSize(); row++) {
                        if (lowest + row >= 2 && type.isTile(col, row, rotation)) {
                            drawTile(base, base.brighter(), base.darker(), (pieceCol + col) * TILE_SIZE, (lowest + row - HIDDEN_ROW_COUNT) * TILE_SIZE, g);
                        }
                    }
                }
                break;
            }


            // Draw the background grid
            g.setColor(Color.DARK_GRAY);
            for (int x = 0; x < COL_COUNT; x++) {
                for (int y = 0; y < VISIBLE_ROW_COUNT; y++) {
                    g.drawLine(0, y * TILE_SIZE, COL_COUNT * TILE_SIZE, y * TILE_SIZE);
                    g.drawLine(x * TILE_SIZE, 0, x * TILE_SIZE, VISIBLE_ROW_COUNT * TILE_SIZE);
                }
            }
        }
        // Draw the outline.
        g.setColor(Color.WHITE);
        g.drawRect(0, 0, TILE_SIZE * COL_COUNT, TILE_SIZE * VISIBLE_ROW_COUNT);
    }

    private void drawTile(TypeFigure type, int x, int y, Graphics g) {
        drawTile(type.getBaseColor(), type.getLightColor(), type.getDarkColor(), x, y, g);
    }

    /**
     * Draws a tile onto the board.
     *
     * @param base  Base color of tile.
     * @param light Light color of the tile.
     * @param dark  Dark color of the tile.
     * @param x     Column.
     * @param y     Row.
     * @param g     TGraphics object.
     */
    private void drawTile(Color base, Color light, Color dark, int x, int y, Graphics g) {

        g.setColor(base);
        g.fillRect(x, y, TILE_SIZE, TILE_SIZE);

        //Fill the bottom and right edges of the tile with the dark shading color.
        g.setColor(dark);
        g.fillRect(x, y + TILE_SIZE - SHADOW_WIDTH, TILE_SIZE, SHADOW_WIDTH);
        g.fillRect(x + TILE_SIZE - SHADOW_WIDTH, y, SHADOW_WIDTH, TILE_SIZE);

        //Fill the top and left edges of the tile with the light shading
        //(draw one row for each row or column to get a diagonal).
        g.setColor(light);
        for (int i = 0; i < SHADOW_WIDTH; i++) {
            g.drawLine(x, y + i, x + TILE_SIZE - i - 1, y + i);
            g.drawLine(x + i, y, x + i, y + TILE_SIZE - i - 1);
        }
    }
}