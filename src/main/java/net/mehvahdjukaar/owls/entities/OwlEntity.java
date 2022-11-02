package net.mehvahdjukaar.owls.entities;

import net.mehvahdjukaar.owls.OwlMod;
import net.mehvahdjukaar.owls.entities.controller.OwlBodyRotationControl;
import net.mehvahdjukaar.owls.entities.controller.OwlMoveController;
import net.mehvahdjukaar.owls.entities.goals.*;
import net.mehvahdjukaar.owls.entities.navigation.FlyingNavigator;
import net.mehvahdjukaar.owls.entities.navigation.OwlNavigator;
import net.mehvahdjukaar.owls.entities.navigation.VanillaNavigator;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;

public class OwlEntity extends TamableAnimal implements FlyingAnimal {

    private static final EntityDataAccessor<Byte> DATA_VARIANT_ID = SynchedEntityData.defineId(OwlEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> DATA_ACTION = SynchedEntityData.defineId(OwlEntity.class, EntityDataSerializers.BYTE);

    public static final Predicate<LivingEntity> PREY_SELECTOR = (e) -> {
        EntityType<?> type = e.getType();
        return type == EntityType.RABBIT;
    };

    public static final Predicate<LivingEntity> TAMED_PREY_SELECTOR = (e) -> {
        EntityType<?> type = e.getType();
        return type == EntityType.SPIDER || type == EntityType.SILVERFISH || type == EntityType.ENDERMITE;
    };

    private static final Ingredient TAME_FOOD = Ingredient.of(Items.RABBIT_FOOT, Items.RABBIT, Items.COOKED_RABBIT);

    public float interestedAngle;
    public float interestedAngleO;
    private boolean interestedSide;

    public float flap;
    public float flapSpeed = 0;
    public float oFlapSpeed = 0;
    public float oFlap;
    private float flapping = 1.0F;
    private float nextFlap = 1.0F;

    public OwlEntity(EntityType<? extends OwlEntity> entityType, Level world) {
        super(entityType, world);

        this.moveControl = new OwlMoveController(this, false);


        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.COCOA, -1.0F);

        //3 elements: navigator, movement controller, goals
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new OwlBodyRotationControl(this);
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        //vanilla flying navigator
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, pLevel);

        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(true);
        //navigation = new OwlNavigator(this, level);
        return navigation;
    }

    @Override
    protected void registerGoals() {
       // this.goalSelector.addGoal(3, new MoveToPerchGoal(this,3,10));
       // this.goalSelector.addGoal(1, new OwlSleepGoal(this));
        //this.goalSelector.addGoal(2, new SitGoal(this));
       // this.goalSelector.addGoal(5, new OwlInterestedGoal(this, 8.0F));

        //look at prey
      //  this.goalSelector.addGoal(6, new OwlLookAtPreyGoal(this, 9.0F, 80, 0.015f));
        //look at shit
      //  this.goalSelector.addGoal(7, new GazeGoal(this, Player.class, 8.0F, 40, 0.015f));
       // this.goalSelector.addGoal(7, new GazeGoal(this, Mob.class, 8.0F, 40, 0.007f));

        goalSelector.addGoal(8, new FlyWanderGoal(this, 1));

      //  this.goalSelector.addGoal(0, new SwimGoal(this));



      //  this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 1.0D, 15.0F, 6F, true));

        //this.goalSelector.addGoal(2, new WaterAvoidingRandomFlyingGoal(this, 1.0D));




      //  this.goalSelector.addGoal(5, new FollowParentFlyingGoal(this, 1D, 20,5));


      //  this.goalSelector.addGoal(3, new DiveAtTargetGoal(this, 0.4F));

    //    this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, true));
        //this.goalSelector.addGoal(5, new OcelotAttackGoal(this));



        //this.goalSelector.addGoal(7, new LookRandomlyGoal(this));

        this.goalSelector.addGoal(7, new BreedGoal(this, 1.0D));

       // this.targetSelector.addGoal(5, new NonTamedTargetGoal<>(this, AnimalEntity.class, false, PREY_SELECTOR));
     //   this.targetSelector.addGoal(7, new NearestAttackableTargetGoal<>(this, MonsterEntity.class, 10, false, false, TAMED_PREY_SELECTOR));


    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 12.0D)
                .add(Attributes.FLYING_SPEED, 2.1F)
                .add(Attributes.MOVEMENT_SPEED, 0.2F)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_VARIANT_ID, (byte) 0);
        this.entityData.define(DATA_ACTION, (byte) 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putByte("Variant", (byte) this.getOwlType().ordinal());
        nbt.putByte("State", this.getState());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setOwlType(OwlType.fromNBT(nbt.getByte("Variant")));
        this.setState(nbt.getByte("State"));
    }

    //----spawn stuff----

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance instance, MobSpawnType reason, @Nullable SpawnGroupData data, @Nullable CompoundTag nbt) {
        this.setOwlType(OwlType.getOwlType(world.getBiome(this.blockPosition()).value(), world.getRandom()));
        this.setState((byte) 0);
        this.populateDefaultEquipmentSlots(instance);
        return super.finalizeSpawn(world, instance, reason, data, nbt);
    }

    public static boolean checkParrotSpawnRules(EntityType<OwlEntity> entityType, LevelAccessor accessor, MobSpawnType spawnType, BlockPos pos, Random random) {
        BlockState blockstate = accessor.getBlockState(pos.below());
        return (blockstate.is(BlockTags.LEAVES) || blockstate.is(Blocks.GRASS_BLOCK) || blockstate.is(BlockTags.LOGS) || blockstate.is(Blocks.AIR)) && accessor.getRawBrightness(pos, 0) > 8;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob father) {
        OwlEntity owl = OwlMod.OWL.get().create(level);
        if (owl != null) {
            OwlType variant = random.nextBoolean() ? this.getOwlType() : ((OwlEntity) father).getOwlType();
            owl.setOwlType(variant);
            UUID uuid = this.getOwnerUUID();
            if (uuid != null) {
                owl.setOwnerUUID(uuid);
                owl.setTame(true);
            }
        }
        return owl;
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance p_180481_1_) {
        super.populateDefaultEquipmentSlots(p_180481_1_);
        //this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
    }

    public OwlType getOwlType() {
        return OwlType.values()[this.entityData.get(DATA_VARIANT_ID) % 7];
    }

    public void setOwlType(OwlType variant) {
        this.entityData.set(DATA_VARIANT_ID, (byte) variant.ordinal());
    }

    private void setState(byte state) {
        this.entityData.set(DATA_ACTION, state);
    }

    private byte getState() {
        return this.entityData.get(DATA_ACTION);
    }

    public void setInterested(boolean interested) {
        this.setState(interested ? (byte) 1 : 0);
    }

    public boolean isInterested() {
        return this.getState() == 1;
    }

    public void setDiving(boolean diving) {
        this.setState(diving ? (byte) 2 : 0);
    }

    public boolean isDiving() {
        return this.getState() == 2;
    }

    public void setSleeping(boolean sleeping) {
        this.setState(sleeping ? (byte) 3 : 0);
    }

    public boolean isSleeping() {
        return this.getState() == 3;
    }

    private void setFlag(int id, boolean value) {
        if (value) {
            this.entityData.set(DATA_FLAGS_ID, (byte) (this.entityData.get(DATA_FLAGS_ID) | id));
        } else {
            this.entityData.set(DATA_FLAGS_ID, (byte) (this.entityData.get(DATA_FLAGS_ID) & ~id));
        }
    }


    @Override
    protected float getStandingEyeHeight(Pose pPose, EntityDimensions size) {
        return size.height * 0.8F;
    }

    @Override
    public void tick() {
        super.tick();
        this.interestedAngleO = this.interestedAngle;
        if (this.isInterested()) {
            if (this.random.nextFloat() < 0.02 && Math.abs(this.interestedAngleO) > 0.8)
                this.interestedSide = !interestedSide;

            this.interestedAngle += (this.interestedSide ? -1 : 1 * 1.0F - this.interestedAngle) * 0.3F;
            this.interestedAngle = Mth.clamp(this.interestedAngle, -1, 1);
        } else {
            this.interestedAngle += (0.0F - this.interestedAngle) * 0.4F;
        }
    }


    //----taming logic----

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        Item item = itemstack.getItem();
        if (this.level.isClientSide) {
            if (this.isTame() && this.isOwnedBy(player)) {
                return InteractionResult.SUCCESS;
            } else {
                return !this.isFood(itemstack) || !(this.getHealth() < this.getMaxHealth()) && this.isTame() ? InteractionResult.PASS : InteractionResult.SUCCESS;
            }
        } else {
            if (this.isTame()) {
                if (this.isOwnedBy(player)) {

                    if (item.isEdible() && this.isFood(itemstack) && this.getHealth() < this.getMaxHealth()) {
                        this.usePlayerItem(player, hand, itemstack);
                        this.heal((float) item.getFoodProperties().getNutrition());
                        return InteractionResult.CONSUME;
                    }

                    InteractionResult result = super.mobInteract(player, hand);
                    if (!result.consumesAction() || this.isBaby()) {
                        this.setOrderedToSit(!this.isOrderedToSit());
                    }
                    return result;
                }
            } else if (isTameFood(itemstack)) {
                this.usePlayerItem(player, hand, itemstack);
                this.setSleeping(false);
                if (this.random.nextInt(3) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player)) {
                    this.tame(player);
                    this.setOrderedToSit(true);
                    this.level.broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.level.broadcastEntityEvent(this, (byte) 6);
                }
                this.setPersistenceRequired();
                return InteractionResult.CONSUME;
            }

            InteractionResult result = super.mobInteract(player, hand);
            if (result.consumesAction()) {
                this.setPersistenceRequired();
            }
            return result;
        }
    }

    public boolean isTameFood(ItemStack stack) {
        return TAME_FOOD.test(stack);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        Item item = stack.getItem();
        return item.isEdible() && item.getFoodProperties().isMeat();
    }

    @Override
    public boolean canMate(Animal entity) {
        if (entity == this) {
            return false;
        } else if (!this.isTame()) {
            return false;
        } else if (!(entity instanceof OwlEntity owlEntity)) {
            return false;
        } else {
            if (!owlEntity.isTame()) {
                return false;
            } else if (owlEntity.isInSittingPose()) {
                return false;
            } else {
                return this.isInLove() && owlEntity.isInLove();
            }
        }
    }

    private float getAttackDamage() {
        return (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        boolean flag = entity.hurt(DamageSource.mobAttack(this), (float) ((int) this.getAttackDamage()));
        if (flag) {
            this.doEnchantDamageEffects(this, entity);
        }
        return flag;
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.PARROT_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.PARROT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PARROT_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
        this.playSound(SoundEvents.PARROT_STEP, 0.15F, 1.0F);
    }

    //-----flapping and flying logic----

    @Override
    public boolean isFlying() {
        return !this.onGround;
    }

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        return false;
    }

    @Override
    protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pos) {
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.calculateFlapping();
    }

    private void calculateFlapping() {
        this.oFlap = this.flap;
        this.oFlapSpeed = this.flapSpeed;
        this.flapSpeed = (float) ((double) this.flapSpeed + (double) (!this.onGround && !this.isPassenger() ? 2.5f : -1) * 0.3D);
        this.flapSpeed = Mth.clamp(this.flapSpeed, 0.0F, 1.2F);
        if (!this.onGround && this.flapping < 1.0F) {
            this.flapping = 1.0F;
        }

        this.flapping = (float) ((double) this.flapping * 0.9D);
        Vec3 vector3d = this.getDeltaMovement();
        if (!this.onGround && vector3d.y < 0.0D) {
            this.setDeltaMovement(vector3d.multiply(1.0D, 0.6D, 1.0D));
        }

        this.flap += this.flapping * 2.0F;
    }

    @Override
    protected boolean isFlapping() {
        return this.flyDist > this.nextFlap;
    }

    @Override
    protected void onFlap() {
        this.playSound(SoundEvents.PARROT_FLY, 0.15F, 1.0F);
        this.nextFlap = this.flyDist + this.flapSpeed / 2.0F;
    }

    //probably skulk stuff
    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return MovementEmission.EVENTS;
    }

    @Override
    public float getVoicePitch() {
        return getPitch(this.random);
    }

    public static float getPitch(Random random) {
        return (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F;
    }

    @Override
    public boolean hurt(DamageSource source, float p_70097_2_) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            this.setOrderedToSit(false);
            this.setSleeping(false);
            return super.hurt(source, p_70097_2_);
        }
    }


    @Override
    public Vec3 getLeashOffset() {
        return new Vec3(0.0D, 0.5F * this.getEyeHeight(), this.getBbWidth() * 0.4F);
    }

    @Override
    public int getMaxHeadXRot() {
        return this.isFlying() ? 35 : super.getMaxHeadXRot();
    }

    @Override
    public int getMaxHeadYRot() {
        return this.isFlying() ? super.getMaxHeadYRot() : 155;
    }

    public int getMaxHeadToBodyStableYRot() {
        return getMaxHeadYRot() / 2;
    }

    @Override
    public int getHeadRotSpeed() {
        return 24;
    }

    public boolean isOnTree(int minHeight) {
        if (this.onGround) {
            int x = this.blockPosition().getX();
            int y = this.blockPosition().getY();
            int z = this.blockPosition().getZ();
            return y - this.level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z) >= minHeight;
        }
        return false;
    }

    public int getFlightTime() {
        return this.fallFlyTicks;
    }

    public Predicate<LivingEntity> getPreys() {
        return this.isTame() ? TAMED_PREY_SELECTOR : PREY_SELECTOR;
    }


}
