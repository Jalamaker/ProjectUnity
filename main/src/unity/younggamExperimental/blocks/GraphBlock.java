package unity.younggamExperimental.blocks;

import arc.graphics.g2d.*;
import arc.scene.ui.layout.*;
import arc.util.io.*;
import mindustry.gen.*;
import mindustry.world.*;
import unity.graphics.*;
import unity.younggamExperimental.*;
import unity.younggamExperimental.graphs.*;
import unity.younggamExperimental.modules.*;

import static arc.Core.atlas;

//block that uses graph
public class GraphBlock extends Block implements GraphBlockBase{
    protected final Graphs graphs = new Graphs();
    protected TextureRegion heatRegion;//heatSprite

    public GraphBlock(String name){
        super(name);
        update = true;
    }

    @Override
    public void load(){
        super.load();
        if(graphs.hasGraph(GraphType.heat)) heatRegion = atlas.find(name + "-heat");
    }

    @Override
    public void setStats(){
        super.setStats();
        graphs.setStats(stats);
        setStatsExt(stats);
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid){
        graphs.drawPlace(x, y, size, rotation, valid);
        super.drawPlace(x, y, rotation, valid);
    }

    @Override
    public Graphs graphs(){
        return graphs;
    }

    public class GraphBuild extends Building implements GraphBuildBase{
        GraphModules gms;

        @Override
        public void created(){
            gms = new GraphModules(this);
            graphs.injectGraphConnector(gms);
            gms.created();
        }

        @Override
        public float efficiency(){
            return super.efficiency() * gms.efficiency();
        }

        @Override
        public void onRemoved(){
            gms.updateGraphRemovals();
            onDelete();
            super.onRemoved();
            onDeletePost();
        }

        @Override
        public void updateTile(){
            if(graphs.useOriginalUpdate()) super.updateTile();
            updatePre();
            gms.updateTile();
            updatePost();
            gms.prevTileRotation(rotation);
        }

        @Override
        public void onProximityUpdate(){
            super.onProximityUpdate();
            gms.onProximityUpdate();
            proxUpdate();
        }

        @Override
        public void display(Table table){
            super.display(table);
            gms.display(table);
            displayExt(table);
        }

        @Override
        public void displayBars(Table table){
            super.displayBars(table);
            gms.displayBars(table);
            displayBarsExt(table);
        }

        @Override
        public void write(Writes write){
            super.write(write);
            gms.write(write);
            writeExt(write);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            gms.read(read, revision);
            readExt(read, revision);
        }

        @Override
        public GraphModules gms(){
            return gms;
        }

        @Override
        public void drawSelect(){
            super.drawSelect();
            gms.drawSelect();
        }
        //laziness

        @Override
        public void draw(){
            if(graphs.hasGraph(GraphType.heat)){
                Draw.rect(region, x, y);
                UnityDrawf.drawHeat(heatRegion, x, y, 0, heat().getTemp());
                drawTeamTop();
            }
        }
    }
}
