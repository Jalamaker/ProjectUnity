const lib = this.global.unity.exp;
const colorOfDirium = Color.valueOf("96f7c3");
const shieldBreakCircle = new Effect(40, e => {
    Draw.color(colorOfDirium);
    Lines.stroke(3 * e.fout());
    Lines.circle(e.x, e.y, e.rotation + e.fin());
});
const deflect = new Effect(12, e => {
    Draw.color(Pal.heal);

    Lines.stroke(2 * e.fout());
    Lines.square(e.x, e.y, 8 * e.fout(), 45);
})
const deflectGenerator = lib.extend(ForceProjector, ForceProjector.ForceBuild, "deflect-generator", {
    maxLevel: 30,
    expFields: [
        {
            type: "linear",
            field: "breakage",
            start: 775,
            intensity: 35
        }
    ],
    setStats(){
        this.super$setStats();
        this.stats.add(Stat.baseDeflectChance, this.levelf(), StatUnit.none);
        this.stats.remove(Stat.boostEffect); //ahhhhhhhhhhhhhhhhhh
    },
    drawPlace(x, y, rotation, valid){
        Draw.color(colorOfDirium);
        Lines.stroke(1.5);
        Lines.circle(x * Vars.tilesize + this.offset, y * Vars.tilesize + this.offset, this.radius);
        Draw.color();
    }
}, {
    deflectChance: 0.2,
    deflectDamage: 0,
    buildingRadius: 60,
    created(){
        this.super$created();
        this.buildingRadius = 60;
    },
    customUpdate(){
        //necessary for realRadius
        this.radscl = Mathf.lerpDelta(this.radscl, this.broken ? 0 : this.warmup, 0.05);
        this.warmup = Mathf.lerpDelta(this.warmup, this.efficiency(), 0.1);

        //shield is self-healing by cooldown
        var scale = !this.broken ? deflectGenerator.cooldownNormal : deflectGenerator.cooldownBrokenBase;
        var cons = deflectGenerator.consumes.get(ConsumeType.liquid);
        if(this.buildup > 0){
            if(cons.valid(this)){
                cons.update(this);
                scale *= (deflectGenerator.cooldownLiquid * (1 + (this.liquids.current().temperature - 0.4) * 0.9));
            }

            this.buildup -= this.delta() * scale;
        }
        //when shield is revived
        if(this.broken && this.buildup <= 0){
            this.broken = false;
        }

        //when shield is destroyed
        if(this.buildup >= deflectGenerator.breakage + deflectGenerator.phaseShieldBoost && !this.broken){
            this.broken = true;
            this.buildup = deflectGenerator.breakage;
            shieldBreakCircle.at(this.x, this.y, this.realRadius(), Pal.lancerLaser);
        }
        //when hit
        if(this.hit > 0){
            this.hit -= 1 / 5 * Time.delta;
        }

        //bullet intersect
        const customConsumer = trait => {
            if(trait.team != this.paramEntity.team && trait.type.absorbable && Mathf.dst(this.paramEntity.x, this.paramEntity.y, trait.x, trait.y) <= this.realRadius()){
                if(Mathf.chance(this.deflectChance)){
                    trait.trns(-trait.vel.x, -trait.vel.y);

                    var penX = Math.abs(this.paramEntity.x - trait.x);
                    var penY = Math.abs(this.paramEntity.y - trait.y);

                    if(penX > penY){
                        trait.vel.x *= -1;
                    }else{
                        trait.vel.y *= -1;
                    }
                    trait.owner = this.paramEntity;
                    trait.team = this.paramEntity.team;
                    trait.time += 1;
                    trait.damage += this.deflectDamage;

                    deflect.at(trait);
                    this.paramEntity.hit = 1;
                    this.paramEntity.buildup += trait.damage * this.paramEntity.warmup / 2;
                    if(cons.valid(this)){
                        this.incExp(scale / 12.5);
                    } else {
                        this.incExp(0.25);
                    }
                }else{
                    trait.absorb();
                    Fx.absorb.at(trait);
                    this.paramEntity.hit = 1;
                    this.paramEntity.buildup += trait.damage * this.paramEntity.warmup;
                    if(cons.valid(this)){
                        this.incExp(scale / 20);
                    } else {
                        this.incExp(0.1);
                    }
                }
            }
        };
        var realRadius = this.realRadius();

        if(realRadius > 0 && !this.broken){
            this.paramEntity = this;
            Groups.bullet.intersect(this.x - realRadius, this.y - realRadius, realRadius * 2, realRadius * 2, customConsumer);
        }
    },
    draw(){
        this.super$draw();
        
        if(this.drawer != null){
            this.drawer.set(this.x, this.y);
        }

        if(this.buildup > 0){
            Draw.alpha(this.buildup / this.breakage * 0.6);
            Draw.blend(Blending.additive);
            Draw.rect(deflectGenerator.topRegion, this.x, this.y);
            Draw.blend();
            Draw.reset();
        }
    },
    realRadius(){
        return (this.buildingRadius + this.phaseHeat * deflectGenerator.phaseRadiusBoost) * this.radscl;
    },
    drawShield(){
        if(!this.broken){
            var radius = this.realRadius();

            Draw.z(Layer.shields);

            Draw.color(colorOfDirium, Color.white.cpy(), Mathf.clamp(this.hit));

            if(Core.settings.getBool("animatedshields")){
                if(radius <= 1){
                    Draw.reset();
                } else {
                    Fill.poly(this.x, this.y, 40, radius);
                }
            } else {
                if(radius <= 1){
                    Draw.reset();
                } else {
                    Lines.stroke(1.5);
                    Draw.alpha(0.09 + Mathf.clamp(0.08 * this.hit));
                    Fill.circle(this.x, this.y, radius);
                    Draw.alpha(1);
                    Lines.circle(this.x, this.y, radius);
                    Draw.reset();
                }
            }
        }
        Draw.reset();
    },
    levelUp(int){
        deflectGenerator.consumes.power(2 + this.totalLevel());
        this.buildup = 0;
        this.broken = false;
        this.buildingRadius += 2;
        if(this.deflectChange <= 1){ //...to 8lv
            this.deflectChance += 0.1;
        }else if(this.deflectDamage < 15){//9 ~ 30, total +21 damage
            this.deflectDamage += 1;
        }else{

        }
    }
});
deflectGenerator.radius = 60;
deflectGenerator.buildVisibility = BuildVisibility.shown;
