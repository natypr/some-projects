import java.awt.*;

/**
 * Description the type of figure.
 *
 * @author Statkevich Nataliya.
 */

public enum TypeFigure {

    TypeI(new Color(0x00f0f0), 4, 4, 1, new boolean[][]{
            {
                    false, false, false, false,
                    true, true, true, true,
                    false, false, false, false,
                    false, false, false, false,
            },
            {false, false, true, false, false, false, true, false, false, false, true, false, false, false, true, false,},
            {false, false, false, false, false, false, false, false, true, true, true, true, false, false, false, false,},
            {false, true, false, false, false, true, false, false, false, true, false, false, false, true, false, false,}
    }),

    TypeJ(new Color(0x0000f0), 3, 3, 2, new boolean[][]{
            {
                    true, false, false,
                    true, true, true,
                    false, false, false,
            },
            {false, true, true, false, true, false, false, true, false,},
            {false, false, false, true, true, true, false, false, true,},
            {false, true, false, false, true, false, true, true, false,}
    }),

    TypeL(new Color(0xf0a000), 3, 3, 2, new boolean[][]{
            {
                    false, false, true,
                    true, true, true,
                    false, false, false,
            },
            {false, true, false, false, true, false, false, true, true,},
            {false, false, false, true, true, true, true, false, false,},
            {true, true, false, false, true, false, false, true, false,}
    }),

    TypeO(new Color(0xf0f000), 2, 2, 2, new boolean[][]{
            {
                    true, true,
                    true, true,
            },
            {true, true, true, true,},
            {true, true, true, true,},
            {true, true, true, true,}
    }),

    TypeS(new Color(0x00f000), 3, 3, 2, new boolean[][]{
            {
                    false, true, true,
                    true, true, false,
                    false, false, false,
            },
            {false, true, false, false, true, true, false, false, true,},
            {false, false, false, false, true, true, true, true, false,},
            {true, false, false, true, true, false, false, true, false,}
    }),

    TypeT(new Color(0xa000f0), 3, 3, 2, new boolean[][]{
            {
                    false, true, false,
                    true, true, true,
                    false, false, false,
            },
            {false, true, false, false, true, true, false, true, false,},
            {false, false, false, true, true, true, false, true, false,},
            {false, true, false, true, true, false, false, true, false,}
    }),

    TypeZ(new Color(0xf00000), 3, 3, 2, new boolean[][]{
            {
                    true, true, false,
                    false, true, true,
                    false, false, false,
            },
            {false, false, true, false, true, true, false, true, false,},
            {false, false, false, true, true, false, false, true, true,},
            {false, true, false, true, true, false, true, false, false,}
    });

    private Color baseColor;
    private Color lightColor;
    private Color darkColor;

    //Number of the central column for the appearance of the figure.
    private int centerColumn;
    private int centerRow;

    private int figureSize;
    //Number of the columns when rotating 0 or 2.
    private int cols;
    private int rows;


    private boolean[][] figure;

    /**
     * Creates a new TypeFigure.
     *
     * @param colorFigure Base color of the figure.
     * @param figureSize  Size of the figure array.
     * @param cols        Number of columns.
     * @param rows        Number of rows.
     * @param figure      Figure.
     */
    private TypeFigure(Color colorFigure, int figureSize, int cols, int rows, boolean[][] figure) {
        this.baseColor = colorFigure;
        this.lightColor = colorFigure.brighter();
        this.darkColor = colorFigure.darker();

        this.figureSize = figureSize;
        this.cols = cols;
        this.rows = rows;
        this.figure = figure;

        this.centerColumn = 5 - figureSize / 2;
        this.centerRow = getTopEmptyRow(0);
    }


    public Color getBaseColor() {
        return baseColor;
    }

    public Color getLightColor() {
        return lightColor;
    }

    public Color getDarkColor() {
        return darkColor;
    }

    public int getFigureSize() {
        return figureSize;
    }

    /**
     * Get number of rows when rotating 0 or 2 (use to preview).
     */
    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    public int getCenterColumn() {
        return centerColumn;
    }

    public int getCenterRow() {
        return centerRow;
    }


    /**
     * Checks if the coordinates and rotation of the tile exist.
     *
     * @param x        Coordinate of the tile.
     * @param y        Coordinate of the tile.
     * @param rotation Rotation to check.
     * @return Exists or doesn't exist.
     */
    public boolean isTile(int x, int y, int rotation) {
        return figure[rotation][y * figureSize + x];
    }


    /**
     * Number of empty columns on the LEFT side of the array for a given rotation.
     *
     * @param rotation Rotation.
     * @return Number of empty columns.
     */
    public int getLeftEmptyCol(int rotation) {
        for (int x = 0; x < figureSize; x++) {
            for (int y = 0; y < figureSize; y++) {
                if (isTile(x, y, rotation)) {
                    return x;
                }
            }
        }
        return -1;
    }

    public int getRightEmptyCol(int rotation) {
        for (int x = figureSize - 1; x >= 0; x--) {
            for (int y = 0; y < figureSize; y++) {
                if (isTile(x, y, rotation)) {
                    return figureSize - x;
                }
            }
        }
        return -1;
    }

    public int getTopEmptyRow(int rotation) {
        for (int y = 0; y < figureSize; y++) {
            for (int x = 0; x < figureSize; x++) {
                if (isTile(x, y, rotation)) {
                    return y;
                }
            }
        }
        return -1;
    }

    public int getBottomEmptyRow(int rotation) {
        for (int y = figureSize - 1; y >= 0; y--) {
            for (int x = 0; x < figureSize; x++) {
                if (isTile(x, y, rotation)) {
                    return figureSize - y;
                }
            }
        }
        return -1;
    }
}
