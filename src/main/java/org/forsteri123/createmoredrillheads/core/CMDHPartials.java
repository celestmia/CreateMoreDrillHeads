package org.forsteri123.createmoredrillheads.core;

import com.jozufozu.flywheel.core.PartialModel;
import net.minecraft.resources.ResourceLocation;
import org.forsteri123.createmoredrillheads.CreateMoreDrillHeads;

import java.util.ArrayList;
import java.util.List;

public class CMDHPartials {
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final List<String> paths = new ArrayList<>();

    protected static PartialModel blockPartial(String path) {
        return new PartialModel(new ResourceLocation(CreateMoreDrillHeads.MOD_ID, ("block/" + path)));
    }

    public static void register() {
        for (String path : paths) {
            blockPartial(path);
        }
    }
}
