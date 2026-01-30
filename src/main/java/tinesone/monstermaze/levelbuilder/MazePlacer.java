package tinesone.monstermaze.levelbuilder;

import org.bukkit.Location;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.structure.Structure;
import org.bukkit.util.Vector;

import java.util.Random;

public final class MazePlacer {
    public static void placePiece(Structure structure, Location cellOrigin, StructureRotation rotation, Random rn)
    {
        Vector size = structure.getSize();
        Vector rotatedAnchor = rotateAnchor(new StructureAnchorPoint(0, 0), size, rotation);

        Location placeLocation = cellOrigin.clone().subtract(rotatedAnchor);

        structure.place(placeLocation, false, rotation, Mirror.NONE, 0, 1, rn);
    }

    private static Vector rotateAnchor(
            StructureAnchorPoint anchorPoint,
            Vector size,
            StructureRotation rotation
    ) {
        int x = anchorPoint.x();
        int z = anchorPoint.z();

        int w = size.getBlockX();
        int d = size.getBlockZ();

        return switch (rotation) {
            case NONE -> new Vector(x, 0, z);

            case CLOCKWISE_90 ->
                    new Vector(d - 1 - z, 0, x);

            case CLOCKWISE_180 ->
                    new Vector(w - 1 - x, 0, d - 1 - z);

            case COUNTERCLOCKWISE_90 ->
                    new Vector(z, 0, w - 1 - x);
        };
    }
}
