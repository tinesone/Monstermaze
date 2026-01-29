package tinesone.monstermaze.levelbuilder;


public enum CellType {
    CROSS,
    STRAIGHT,
    CORNER,
    DEADEND,
    T_CROSS,
    WALL;

    public String getStructureLocation(String resourceFolderName)
    {
        String fileName = this.toString().toLowerCase();
        return "structures/" + resourceFolderName + "/" + fileName + ".nbt";
    }
}
