package unity.content;

import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.gen.*;
import mindustry.type.*;
import unity.annotations.Annotations.*;
import unity.type.*;
import unity.type.sector.*;
import unity.type.sector.objectives.*;

import static mindustry.Vars.*;
import static unity.content.UnityPlanets.*;

public class UnitySectorPresets implements ContentList{
    public static @FactionDef("monolith") SectorPreset
    accretion, salvagedLab;

    @Override
    public void load(){
        accretion = new ScriptedSector("accretion", megalith, 200){{
            alwaysUnlocked = true;
            addStartingItems = true;
            difficulty = 3f;
            captureWave = 15;

            objectives.addAll(
                new ResourceAmountObjective(ItemStack.with(
                    Items.copper, 6000,
                    Items.lead, 5600,
                    Items.silicon, 3200,
                    UnityItems.monolite, 4800
                ), state.rules.defaultTeam, this, name + "-resource", (ResourceAmountObjective objective) -> {
                    int win = Math.max((state.wave / 5 + 1) * 5, captureWave);

                    state.rules.winWave = Math.max(captureWave, win);
                    if(state.hasSector()){
                        state.getSector().info.winWave = win;
                        GlobalObjective.fire(GlobalObjective.sectorAccretionComplete);
                    }

                    Sounds.unlock.play();
                }).init((ResourceAmountObjective objective) -> {
                    if(state.getSector() != null){
                        if(!GlobalObjective.reached(GlobalObjective.sectorAccretionComplete)){
                            state.rules.winWave = -1;
                            if(state.hasSector()){
                                state.getSector().info.winWave = -1;
                            }
                        }else{
                            objective.stop();
                        }
                    }else{
                        state.rules.winWave = -1;
                    }
                })
            );
        }};

        salvagedLab = new ScriptedSector("salvaged-laboratory", megalith, 100){{
            difficulty = 4f;
            captureWave = 30;
        }};
    }
}
