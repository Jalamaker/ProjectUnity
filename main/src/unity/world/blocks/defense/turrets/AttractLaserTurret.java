package unity.world.blocks.defense.turrets;

import arc.audio.*;
import arc.math.*;
import arc.util.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.consumers.*;
import unity.util.*;

import java.lang.reflect.*;

import static mindustry.Vars.*;

public class AttractLaserTurret extends LaserTurret{
    public float chargeWarmup = 0.002f;
    public float chargeCooldown = 0.01f;

    public Sound chargeSound = Sounds.none;
    public float chargeSoundVolume = 1f;

    public float attractionStrength = 0.85f;

    public AttractLaserTurret(String name){
        super(name);
    }

    public class ChargeLaserTurretBuild extends LaserTurretBuild{
        public float charge;

        protected PitchedSoundLoop sound = new PitchedSoundLoop(chargeSound, chargeSoundVolume);

        // thanks anuke for making the fields package-private
        private Field bulletf;
        private Field bulletLifef;

        {
            try{
                bulletf = LaserTurretBuild.class.getDeclaredField("bullet");
                bulletf.setAccessible(true);

                bulletLifef = LaserTurretBuild.class.getDeclaredField("bulletLife");
                bulletLifef.setAccessible(true);
            }catch(NoSuchFieldException e){
                throw new RuntimeException(e);
            }
        }

        @Override
        public void updateTile(){
            if(!validateTarget() || !consValid()){
                charge = Mathf.lerpDelta(charge, 0f, chargeCooldown);
            }
            if(bulletLife() <= 0f && bullet() == null){
                attractUnits();
            }

            super.updateTile();

            float prog = charge * 1.5f + 0.5f;
            sound.update(x, y, charge, prog);

            if(isControlled()){
                Log.info("Bullet: @, Life: @", bullet(), bulletLife());
            }
        }

        @Override
        protected void updateShooting(){
            if(bulletLife() > 0f && bullet() != null){
                return;
            }

            if(charge >= 1f && (consValid() || cheating())){
                BulletType type = peekAmmo();

                shoot(type);

                charge = 0f;
            }
        }

        @Override
        protected void updateCooling(){
            if(!isShooting() || (bulletLife() > 0f && bullet() != null)){
                charge = Mathf.lerpDelta(charge, 0f, chargeCooldown);
            }else if(isControlled() && isShooting()){
                float maxUsed = consumes.<ConsumeLiquidBase>get(ConsumeType.liquid).amount;

                Liquid liquid = liquids.current();

                float used = Math.min(Math.min(liquids.get(liquid), maxUsed * Time.delta), Math.max(0f, ((reloadTime - reload) / coolantMultiplier) / liquid.heatCapacity)) * baseReloadSpeed();
                charge = Mathf.clamp(charge + chargeWarmup * (1f - used), 0f, 1f);
                liquids.remove(liquid, used);

                if(Mathf.chance(0.06f * used)){
                    coolEffect.at(x + Mathf.range(size * tilesize / 2f), y + Mathf.range(size * tilesize / 2f));
                }
            }
        }

        @Override
        public void remove(){
            sound.stop();
            super.remove();
        }

        protected boolean hasEnemyInProximity(){
            return Units.closestTarget(team, x, y, range(), unit -> !unit.dead()) != null;
        }

        protected void attractUnits(){
            Units.nearby(x - range() * 2f, y - range() * 2f, range() * 4f, range() * 4f, unit -> {
                if(!unit.dead() && unit.within(x, y, range() * 2f)){
                    Tmp.v1.set(x - unit.x, y - unit.y)
                        .rotate(10f * (1f - charge))
                        .setLength(attractionStrength * charge * Time.delta)
                        .scl(unit.dst(this) / range() / 2);

                    unit.vel.add(Tmp.v1);
                }
            });
        }

        protected float bulletLife(){
            try{
				return bulletLifef.getFloat(this);
			}catch(Exception e){
                throw new RuntimeException(e);
            }
        }

        protected Bullet bullet(){
            try{
                return (Bullet)bulletf.get(this);
            }catch(Exception e){
                throw new RuntimeException(e);
            }
        }
    }
}
