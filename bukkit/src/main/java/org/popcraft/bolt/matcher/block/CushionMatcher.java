package org.popcraft.bolt.matcher.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.popcraft.bolt.matcher.Match;
import org.popcraft.bolt.util.EnumUtil;
import org.popcraft.bolt.util.FoliaUtil;

import java.util.HashSet;
import java.util.Set;

public class CushionMatcher implements BlockMatcher {
    private static final EntityType CUSHION = EnumUtil.valueOf(EntityType.class, "CUSHION").orElse(null);
    private boolean enabled;

    @Override
    public void initialize(Set<Material> protectableBlocks, Set<EntityType> protectableEntities) {
        enabled = protectableEntities.stream().anyMatch(entityType -> entityType.equals(CUSHION));
    }

    @Override
    public boolean enabled() {
        return enabled;
    }

    @Override
    public boolean canMatch(Block block) {
        return enabled;
    }

    @Override
    public Match findMatch(Block block) {
        final Set<Entity> entities = new HashSet<>();
        FoliaUtil.getNearbyEntities(block, block.getBoundingBox().expand(0.5, 0.5, 0.5, 0.5, 0.5, 0.5), ItemFrame.class::isInstance).forEach(entity -> {
            if (entity.getType().equals(CUSHION) && entity.getLocation().getBlock().getRelative(BlockFace.UP).getLocation().equals(block.getLocation())) {
                entities.add(entity);
            }
        });
        return Match.ofEntities(entities);
    }
}
