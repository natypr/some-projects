import javax.swing.*;
import java.awt.*;

/**
 * Description of the information panel.
 *
 * @author Statkevich Nataliya.
 */
public class SidePanel extends JPanel {

    private static final int TILE_SIZE_PREVIEW = (BoardPanel.TILE_SIZE + 12) >> 1;
    private static final int SHADOW_WIDTH_PREVIEW = (BoardPanel.SHADOW_WIDTH + 3) >> 1;

    private static final int TILE_COUNT_IN_PREVIEW = 5;

    //Coordinate X and Y for preview box
    private static final int CENTER_X_BOX = 90;
    private static final int CENTER_Y_BOX = 80;

    private static final int SIZE_BOX = (TILE_SIZE_PREVIEW * TILE_COUNT_IN_PREVIEW >> 1);

    //Indents on coordinates X(to the right) and Y(between the lines).
    private static final int INDENT_X = 40;
    private static final int INDENT_Y = 20;

    private static final int COORD_Y_NEXT_FIGURE = CENTER_Y_BOX + SIZE_BOX + INDENT_Y;
    private static final int COORD_Y_SCORE = COORD_Y_NEXT_FIGURE + INDENT_Y + INDENT_Y + INDENT_Y;
    private static final int COORD_Y_KEY = 390;

    private static final Font SMALL_FONT = new Font("Tahoma", Font.BOLD, 11);
    private static final Font MIDDLE_FONT = new Font("Tahoma", Font.BOLD, 16);

    private static final Color TEXT_COLOR = new Color(0xcccccc);
    private static final Color SIDE_COLOR = new Color(0x000000);

    //Tetris instance.
    private Tetris tetris;


    SidePanel(Tetris tetris) {
        this.tetris = tetris;
        setPreferredSize(new Dimension(180, BoardPanel.PANEL_HEIGHT));
        setBackground(SIDE_COLOR);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(TEXT_COLOR);

        //Coordinate Y of current row.
        int tempSpace;

        g.setFont(MIDDLE_FONT);
        g.drawString("Score:  " + tetris.getScore(), INDENT_X, COORD_Y_SCORE);

        g.setFont(SMALL_FONT);
        g.drawString("P - Pause Game", INDENT_X, tempSpace = COORD_Y_KEY);
        g.drawString("A __ Move Left __ <-", INDENT_X, tempSpace += INDENT_Y + INDENT_Y);
        g.drawString("D _ Move Right __ ->", INDENT_X, tempSpace += INDENT_Y);
        g.drawString("Q ___ Rotate ___ Up", INDENT_X, tempSpace += INDENT_Y);
        g.drawString("S ____ Drop ____ Dn", INDENT_X, tempSpace += INDENT_Y);


        //Draw the next figure in preview box.
        g.setFont(MIDDLE_FONT);
        g.drawString("Next Piece:", INDENT_X, COORD_Y_NEXT_FIGURE);
        g.drawRect(CENTER_X_BOX - SIZE_BOX, CENTER_Y_BOX - SIZE_BOX, SIZE_BOX * 2, SIZE_BOX * 2);

        TypeFigure type = tetris.getNextPieceType();
        if (!tetris.isGameOver() && type != null) {
            int cols = type.getCols();
            int rows = type.getRows();
            int dimension = type.getFigureSize();

            int startX = (CENTER_X_BOX - (cols * TILE_SIZE_PREVIEW / 2));
            int startY = (CENTER_Y_BOX - (rows * TILE_SIZE_PREVIEW / 2));

            //In the preview, the default rotation is zero.
            int top = type.getTopEmptyRow(0);
            int left = type.getLeftEmptyCol(0);

            for (int row = 0; row < dimension; row++) {
                for (int col = 0; col < dimension; col++) {
                    if (type.isTile(col, row, 0)) {
                        drawTile(type, startX + ((col - left) * TILE_SIZE_PREVIEW), startY + ((row - top) * TILE_SIZE_PREVIEW), g);
                    }
                }
            }
        }
    }

    /**
     * Draws a figure onto the preview window.
     *
     * @param type Type of figure to draw.
     * @param x    Coordinate of the figure.
     * @param y    Coordinate of the figure.
     * @param g    Graphics object.
     */
    private void drawTile(TypeFigure type, int x, int y, Graphics g) {
        g.setColor(type.getBaseColor());
        g.fillRect(x, y, TILE_SIZE_PREVIEW, TILE_SIZE_PREVIEW);

        g.setColor(type.getDarkColor());
        g.fillRect(x, y + TILE_SIZE_PREVIEW - SHADOW_WIDTH_PREVIEW, TILE_SIZE_PREVIEW, SHADOW_WIDTH_PREVIEW);
        g.fillRect(x + TILE_SIZE_PREVIEW - SHADOW_WIDTH_PREVIEW, y, SHADOW_WIDTH_PREVIEW, TILE_SIZE_PREVIEW);

        g.setColor(type.getLightColor());
        for (int i = 0; i < SHADOW_WIDTH_PREVIEW; i++) {
            g.drawLine(x, y + i, x + TILE_SIZE_PREVIEW - i - 1, y + i);
            g.drawLine(x + i, y, x + i, y + TILE_SIZE_PREVIEW - i - 1);
        }
    }
}
