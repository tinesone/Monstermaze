package tinesone.monstermaze.levelbuilder;


public enum CellType {
    CROSS,
    STRAIGHT,
    CORNER,
    DEADEND,
    T_CROSS,
    WALL;

    public String getStructureLocation(String resourceFolderName, Rotation rotation)
    {
        String number = rotation.toString().toLowerCase().split("_")[1];
        return "structures/" + resourceFolderName // structures/test/0/wall_0.nbt
                + "/" + number
                + "/" + this.toString().toLowerCase()
                + "_" + number
                + ".nbt";
    }
}
