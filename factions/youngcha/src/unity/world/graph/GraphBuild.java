package unity.world.graph;

import arc.func.*;
import arc.scene.ui.layout.*;
import arc.struct.ObjectMap.*;
import arc.struct.*;
import arc.util.io.*;
import mindustry.gen.*;

public interface GraphBuild{
    OrderedMap<Class<? extends Graph<?>>, GraphNode<?>> getNodes();

    Building getBuild();

    int getPrevRotation();

    void setPrevRotation(int t);

    default <T extends Graph<T>> GraphNode<T> getGraphNode(Class<T> c){
        return (GraphNode<T>)getNodes().get(c);
    }

    default <T extends Graph<T>> T getGraph(Class<T> c){
        return getGraph(c, 0);
    }

    default <T extends Graph<T>> T getGraph(Class<T> c, int index){
        return getGraphNode(c).connector.get(index).getGraph();
    }

    default void onInit(){}

    default void initGraph(){
        if(getNodes().size != 0){
            return;
        }
        setPrevRotation(getBuild().rotation);
        if(getBuild().block instanceof GraphBlock graphBlock){
            var cfg = graphBlock.getConfig();
            //fishes all relevant things from GraphBlockConfig in the GraphBlock
            //and populates the build using the settings.
            for(var connectionConfig : cfg.connections){
                if(!this.getNodes().containsKey(connectionConfig.getGraphType())){
                    var gNode = cfg.nodeConfig.get(connectionConfig.getGraphType()).get(this);
                    getNodes().put(connectionConfig.getGraphType(), gNode);
                }
                var node = getGraphNode(connectionConfig.getGraphType());
                node.connectors++;
            }
            for(var connectionConfig : cfg.connections){
                var node = getGraphNode(connectionConfig.getGraphType());
                node.connector.add(connectionConfig.getConnector(node));
            }
        }
        onInit();
    }


    default <T extends Graph<T>> void addNode(Class<T> c, Prov<GraphNode<T>> prov){
        getNodes().put(c, prov.get());
    }

    default void disconnectFromGraph(){
        for(var entry : getNodes()){
            entry.value.removeSelf();
        }
    }

    default void connectToGraph(){
        for(var entry : getNodes()){
            entry.value.onPlace();
        }
        onPlaced();
    }

    default void onPlaced(){

    }

    default void onRotate(){
        for(var entry : getNodes()){
            entry.value.onRotate();
        }
    }

    default void eachNode(Cons2<Class<? extends Graph<?>>, GraphNode<?>> cons){
        var graphIterator = new Entries<>(getNodes());
        for(var e : graphIterator){
            cons.get(e.key, e.value);
        }
    }

    default void updateGraphs(){
        if(getPrevRotation() == -1){
            connectToGraph();
            setPrevRotation(getBuild().rotation);
        }

        if(getPrevRotation() != getBuild().rotation && getBuild().block.rotate){
            onRotate();
            setPrevRotation(getBuild().rotation);
        }

        eachNode((cls, graphNode) -> {
            //change later.
            graphNode.update();
            for(var graphConn : graphNode.connector){
                graphConn.getGraph().update();
            }
        });
    }

    default void displayGraphBars(Table table){
        getNodes().each((cls, graphNode) -> graphNode.displayBars(table));
    }

    default void onConnectionChanged(GraphConnector<?> g){
    }

    ///I pray they are in the same order.
    default void writeGraphs(Writes write){
        getNodes().each((cls, graphNode) -> graphNode.write(write));
    }

    default void readGraphs(Reads read){
        getNodes().each((cls, graphNode) -> graphNode.read(read));
    }

    //conv for getting
    default TorqueGraphNode torqueNode(){
        return (TorqueGraphNode)getGraphNode(TorqueGraph.class);
    }

    default HeatGraphNode heatNode(){
        return (HeatGraphNode)getGraphNode(HeatGraph.class);
    }

    default CrucibleGraphNode crucibleNode(){
        return (CrucibleGraphNode)getGraphNode(CrucibleGraph.class);
    }

    //conv for drawing
    default float get2SpriteRotation(){
        return (getBuild().rotdeg() + 90f) % 180f - 90f;
    }

    default float get2SpriteRotationVert(){
        return (getBuild().rotdeg()) % 180f;
    }
}
