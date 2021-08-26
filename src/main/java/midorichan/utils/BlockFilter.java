package midorichan.utils;

import org.bukkit.Material;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public class BlockFilter {

    private static Set<Material> stairsTypeSet;

    static {
        stairsTypeSet = EnumSet.noneOf(Material.class);

        Arrays.stream(Material.values())
                .filter(m -> !m.name().startsWith("LEGACY_"))
                .forEach(m -> {
                    String name = m.name();
                    if (name.endsWith("_STAIRS"))
                        stairsTypeSet.add(m);
                });
    }

    public static boolean isStairsBlock(Material type) {
        return stairsTypeSet.contains(type);
    }

}
