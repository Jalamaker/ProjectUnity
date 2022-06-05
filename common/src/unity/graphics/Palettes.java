package unity.graphics;

import arc.graphics.*;

import static mindustry.graphics.Pal.*;

/**
 * Shared access for commonly used palettes. Specific palettes should be defined in the corresponding submodules, optionally
 * copying/referencing colors from this class to avoid recompilation as much as possible.
 * @author GlennFolker
 */
public final class Palettes{
    public static final Color
        monolith = new Color(0x87ceebff),

        outline = darkerMetal,
        darkOutline = new Color(0x38383dff),
        darkerOutline = new Color(0x2e3142ff);

    private Palettes(){
        throw new AssertionError();
    }
}
