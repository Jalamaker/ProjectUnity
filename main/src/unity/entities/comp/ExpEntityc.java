package unity.entities.comp;

import arc.math.*;
import mindustry.ctype.*;
import unity.annotations.Annotations.*;
import unity.type.*;

public abstract interface ExpEntityc<T extends UnlockableContent, E extends ExpType<T>>{
    @Initialize(eval = "0f")
    float exp();

    void exp(float exp);

    default float expf(){
        int level = level();
        if(level >= maxLevel()) return 1f;

        float last = expType().requiredExp(level);
        float next = expType().requiredExp(level + 1);

        return (exp() - last) / (next - last);
    }

    default int level(){
        return Math.min(Mathf.floorPositive(Mathf.sqrt(exp() * 0.1f)), maxLevel());
    }

    default float levelf(){
        return level() / (float)(maxLevel());
    }

    default int maxLevel(){
        return expType().maxLevel;
    }

    @MustInherit
    E expType();

    @MustInherit
    void upgrade(int i);
}
