package tinesone.monstermaze.maze;

public abstract class MazeGeneratorBase implements MazeGenerator {
    @Override
    public Maze generate(int width, int height) {
        Cell[] cells = new Cell[height * width];
        generate(cells, width, height);

        return new Maze(cells, width, height);
    }

    protected abstract void generate(Cell[] cells, int width, int height);
}
