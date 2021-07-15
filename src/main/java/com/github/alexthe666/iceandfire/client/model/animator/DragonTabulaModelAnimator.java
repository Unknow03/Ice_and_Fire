package com.github.alexthe666.iceandfire.client.model.animator;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ITabulaModelAnimator;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.github.alexthe666.iceandfire.client.model.util.DragonAnimationsLibrary;
import com.github.alexthe666.iceandfire.client.model.util.EnumDragonAnimations;
import com.github.alexthe666.iceandfire.client.model.util.EnumDragonModelTypes;
import com.github.alexthe666.iceandfire.client.model.util.EnumDragonPoses;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;

public abstract class DragonTabulaModelAnimator<T extends EntityDragonBase> extends IceAndFireTabulaModelAnimator implements ITabulaModelAnimator<T> {

    protected TabulaModel[] walkPoses;
    protected TabulaModel[] flyPoses;
    protected TabulaModel[] swimPoses;
    protected AdvancedModelBox[] neckParts;
    protected AdvancedModelBox[] tailParts;
    protected AdvancedModelBox[] tailPartsWBody;
    protected AdvancedModelBox[] toesPartsL;
    protected AdvancedModelBox[] toesPartsR;
    protected AdvancedModelBox[] clawL;
    protected AdvancedModelBox[] clawR;
    protected ModelAnimator bakedAnimation;

    public DragonTabulaModelAnimator(TabulaModel baseModel) {
        super(baseModel);
    }

    public void init(TabulaModel model) {
        neckParts = new AdvancedModelBox[]{model.getCube("Neck1"), model.getCube("Neck2"), model.getCube("Neck3"), model.getCube("Neck3"), model.getCube("Head")};
        tailParts = new AdvancedModelBox[]{model.getCube("Tail1"), model.getCube("Tail2"), model.getCube("Tail3"), model.getCube("Tail4")};
        tailPartsWBody = new AdvancedModelBox[]{model.getCube("BodyLower"), model.getCube("Tail1"), model.getCube("Tail2"), model.getCube("Tail3"), model.getCube("Tail4")};
        toesPartsL = new AdvancedModelBox[]{model.getCube("ToeL1"), model.getCube("ToeL2"), model.getCube("ToeL3")};
        toesPartsR = new AdvancedModelBox[]{model.getCube("ToeR1"), model.getCube("ToeR2"), model.getCube("ToeR3")};
        clawL = new AdvancedModelBox[]{model.getCube("ClawL")};
        clawR = new AdvancedModelBox[]{model.getCube("ClawR")};
    }

    @Override
    public void setRotationAngles(TabulaModel model, T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float scale) {

    }

    protected boolean isWing(TabulaModel model, AdvancedModelBox modelRenderer) {
        return model.getCube("armL1") == modelRenderer || model.getCube("armR1") == modelRenderer || model.getCube("armL1").childModels.contains(modelRenderer) || model.getCube("armR1").childModels.contains(modelRenderer);
    }

    protected boolean isHorn(AdvancedModelBox modelRenderer) {
        return modelRenderer.boxName.contains("Horn");
    }


    public void animate(TabulaModel model, T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float scale) {
        if(bakedAnimation == null)
            bakeAnimation(model, entity, limbSwing, limbSwingAmount, ageInTicks, rotationYaw, rotationPitch, scale);
        else
            model.llibAnimator = bakedAnimation;
    }

    protected void genderMob(T entity, AdvancedModelBox cube) {
        if (!entity.isMale()) {
            TabulaModel maleModel = getModel(EnumDragonPoses.MALE);
            TabulaModel femaleModel = getModel(EnumDragonPoses.FEMALE);
            AdvancedModelBox femaleModelCube = femaleModel.getCube(cube.boxName);
            AdvancedModelBox maleModelCube = maleModel.getCube(cube.boxName);
            if(maleModelCube == null || femaleModelCube == null)
                return;
            float x = femaleModelCube.rotateAngleX;
            float y = femaleModelCube.rotateAngleY;
            float z = femaleModelCube.rotateAngleZ;
            if (x != maleModelCube.rotateAngleX || y != maleModelCube.rotateAngleY || z != maleModelCube.rotateAngleZ) {
                this.setRotateAngle(cube, 1F, x, y, z);
            }
        }
    }

    protected abstract TabulaModel getModel(EnumDragonPoses pose);

    //Doesn't actually bake any animations, but maybe that will change
    protected void bakeAnimation(TabulaModel model, T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float scale) {
        AdvancedModelBox modelCubeJaw = model.getCube("Jaw");
        AdvancedModelBox modelCubeBodyUpper = model.getCube("BodyUpper");
        model.llibAnimator.update(entity);
//Firecharge
        model.llibAnimator.setAnimation(T.ANIMATION_FIRECHARGE);
        model.llibAnimator.startKeyframe(10);
        moveToPose(model, getModel(EnumDragonPoses.BLAST_CHARGE1));
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(10);
        moveToPose(model, getModel(EnumDragonPoses.BLAST_CHARGE2));
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(5);
        moveToPose(model, getModel(EnumDragonPoses.BLAST_CHARGE3));
        model.llibAnimator.endKeyframe();
        model.llibAnimator.resetKeyframe(5);
//Speak
        model.llibAnimator.setAnimation(T.ANIMATION_SPEAK);
        model.llibAnimator.startKeyframe(5);
        this.rotate(model.llibAnimator, modelCubeJaw, 18, 0, 0);
        model.llibAnimator.move(modelCubeJaw, 0, 0, 0.2F);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.setStaticKeyframe(5);
        model.llibAnimator.startKeyframe(5);
        this.rotate(model.llibAnimator, modelCubeJaw, 18, 0, 0);
        model.llibAnimator.move(modelCubeJaw, 0, 0, 0.2F);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.resetKeyframe(5);
//Bite
        model.llibAnimator.setAnimation(T.ANIMATION_BITE);
        model.llibAnimator.startKeyframe(10);
        moveToPoseSameModel(model, getModel(EnumDragonPoses.BITE1));
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(5);
        moveToPoseSameModel(model, getModel(EnumDragonPoses.BITE2));
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(5);
        moveToPoseSameModel(model, getModel(EnumDragonPoses.BITE3));
        model.llibAnimator.endKeyframe();
        model.llibAnimator.resetKeyframe(10);
//Shakeprey
        model.llibAnimator.setAnimation(T.ANIMATION_SHAKEPREY);
        model.llibAnimator.startKeyframe(15);
        moveToPoseSameModel(model, getModel(EnumDragonPoses.GRAB1));
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(10);
        moveToPoseSameModel(model, getModel(EnumDragonPoses.GRAB2));
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(10);
        moveToPoseSameModel(model, getModel(EnumDragonPoses.GRAB_SHAKE1));
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(10);
        moveToPoseSameModel(model, getModel(EnumDragonPoses.GRAB_SHAKE2));
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(10);
        moveToPoseSameModel(model, getModel(EnumDragonPoses.GRAB_SHAKE3));
        model.llibAnimator.endKeyframe();
        model.llibAnimator.resetKeyframe(10);
//Tailwhack
        model.llibAnimator.setAnimation(T.ANIMATION_TAILWHACK);
        model.llibAnimator.startKeyframe(10);
        moveToPoseSameModel(model, getModel(EnumDragonPoses.TAIL_WHIP1));
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(10);
        moveToPoseSameModel(model, getModel(EnumDragonPoses.TAIL_WHIP2));
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(10);
        moveToPoseSameModel(model, getModel(EnumDragonPoses.TAIL_WHIP3));
        model.llibAnimator.endKeyframe();
        model.llibAnimator.resetKeyframe(10);
//Wingblast
        model.llibAnimator.setAnimation(T.ANIMATION_WINGBLAST);
        model.llibAnimator.startKeyframe(10);
        moveToPoseSameModel(model, getModel(EnumDragonPoses.WING_BLAST1));
        model.llibAnimator.move(modelCubeBodyUpper, 0, -4F, 0);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(5);
        moveToPoseSameModel(model, getModel(EnumDragonPoses.WING_BLAST2));
        model.llibAnimator.move(modelCubeBodyUpper, 0, -4F, 0);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(5);
        moveToPoseSameModel(model, getModel(EnumDragonPoses.WING_BLAST3));
        model.llibAnimator.move(modelCubeBodyUpper, 0, -4F, 0);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(5);
        moveToPoseSameModel(model, getModel(EnumDragonPoses.WING_BLAST4));
        model.llibAnimator.move(modelCubeBodyUpper, 0, -4F, 0);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(5);
        moveToPoseSameModel(model, getModel(EnumDragonPoses.WING_BLAST5));
        model.llibAnimator.move(modelCubeBodyUpper, 0, -4F, 0);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(5);
        moveToPoseSameModel(model, getModel(EnumDragonPoses.WING_BLAST6));
        model.llibAnimator.move(modelCubeBodyUpper, 0, -4F, 0);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(5);
        moveToPoseSameModel(model, getModel(EnumDragonPoses.WING_BLAST7));
        model.llibAnimator.move(modelCubeBodyUpper, 0, -4F, 0);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.resetKeyframe(10);
//Roar
        model.llibAnimator.setAnimation(T.ANIMATION_ROAR);
        model.llibAnimator.startKeyframe(10);
        moveToPoseSameModel(model, getModel(EnumDragonPoses.ROAR1));
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(10);
        moveToPoseSameModel(model, getModel(EnumDragonPoses.ROAR2));
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(10);
        moveToPoseSameModel(model, getModel(EnumDragonPoses.ROAR3));
        model.llibAnimator.endKeyframe();
        model.llibAnimator.resetKeyframe(10);
//Epicroar
        model.llibAnimator.setAnimation(T.ANIMATION_EPIC_ROAR);
        model.llibAnimator.startKeyframe(10);
        moveToPoseSameModel(model, getModel(EnumDragonPoses.EPIC_ROAR1));
        model.llibAnimator.move(modelCubeBodyUpper, 0, -6.8F, 0);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(10);
        moveToPoseSameModel(model, getModel(EnumDragonPoses.EPIC_ROAR2));
        model.llibAnimator.move(modelCubeBodyUpper, 0, -6.8F, 0);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(10);
        moveToPoseSameModel(model, getModel(EnumDragonPoses.EPIC_ROAR3));
        model.llibAnimator.move(modelCubeBodyUpper, 0, -6.8F, 0);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(10);
        moveToPoseSameModel(model, getModel(EnumDragonPoses.EPIC_ROAR2));
        model.llibAnimator.move(modelCubeBodyUpper, 0, -6.8F, 0);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(10);
        moveToPoseSameModel(model, getModel(EnumDragonPoses.EPIC_ROAR3));
        model.llibAnimator.move(modelCubeBodyUpper, 0, -6.8F, 0);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.resetKeyframe(10);
    }
}
