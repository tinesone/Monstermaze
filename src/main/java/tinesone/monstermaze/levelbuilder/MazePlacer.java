package tinesone.monstermaze.levelbuilder;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.util.Vector;

import java.util.Random;

public final class MazePlacer {
    public static void placePiece(MazePiece mazePiece, Location cellOrigin, StructureRotation rotation, Random rn)
    {
        Vector size = mazePiece.structure().getSize();
        Vector offSet = baseOffset(mazePiece.anchorPoint(), size, rotation);
        Vector rotatedAnchor = rotateLocal(new Vector(mazePiece.anchorPoint().x(), 0, mazePiece.anchorPoint().z()), size, rotation);



        //Location placeLocation = cellOrigin.clone().subtract(offSet);
        Location placeLocation = cellOrigin.clone().subtract(rotatedAnchor);
        //cellOrigin.getBlock().setType(Material.EMERALD_BLOCK);
        //placeLocation.getBlock().setType(Material.REDSTONE_BLOCK);

        mazePiece.structure().place(placeLocation, false, rotation, Mirror.NONE, 0, 1, rn);
    }

    private static Vector rotateLocal(Vector v, Vector size, StructureRotation r)
    {
        int x = v.getBlockX();
        int z = v.getBlockZ();

        int n = size.getBlockX();

        return switch (r) {
            case NONE -> new Vector(x, 0, z);
            case CLOCKWISE_90 -> new Vector(n - 1 - z, 0, x);
            case CLOCKWISE_180 -> new Vector(n - 1 - x, 0, n - 1 - z);
            case COUNTERCLOCKWISE_90 -> new Vector(z, 0, n - 1 - x);
        };
    }
    private static Vector baseOffset(
            StructureAnchorPoint anchor,
            Vector size,
            StructureRotation rotation
    ) {
        int x = anchor.x();
        int z = anchor.z();

        int n = size.getBlockX(); // square

        // bounding-box shift in local space
        int ox = 0;
        int oz = 0;

        switch (rotation) {
            case CLOCKWISE_90 -> oz = n - 1;
            case CLOCKWISE_180 -> {
                ox = n - 1;
                oz = n - 1;
            }
            case COUNTERCLOCKWISE_90 -> ox = n - 1;
        }

        // apply bbox shift BEFORE rotation
        return rotateLocal(
                new Vector(x + ox, 0, z + oz),
                size,
                rotation
        );
    }
}
