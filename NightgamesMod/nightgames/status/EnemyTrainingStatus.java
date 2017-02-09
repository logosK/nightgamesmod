package nightgames.status;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;

public abstract class EnemyTrainingStatus extends Status {

    public EnemyTrainingStatus(Character affected) {
        super("EnemyTrainingStatus", affected);
    }

    private Trait trainingTrait;
    private double trainingLevel;
    
    public void tick(Combat c) {
        trainingLevel -= 0.01;
    }

    public String initialMessage(Combat c, boolean replaced) {
        return Global.format("Your enemies have started to train you to be a "+trainingTrait.getDesc(), affected, c.getOpponent(affected));
    }

    @Override
    public String describe(Combat c) {
        return Global.format("You are being trained to be a "+trainingTrait.getDesc(), affected, c.getOpponent(affected));
    }

    @Override
    public int mod(Attribute a) {
        return 0;
    }

    @Override
    public int regen(Combat c) {
        return 0;
    }

    @Override
    public int damage(Combat c, int x) {
        return 0;
    }

    @Override
    public double pleasure(Combat c, BodyPart withPart, BodyPart targetPart, double x) {
        return 0;
    }

    @Override
    public int weakened(Combat c, int x) {
        return 0;
    }

    @Override
    public int tempted(Combat c, int x) {
        return 0;
    }

    @Override
    public int evade() {
        return 0;
    }

    @Override
    public int escape() {
        return 0;
    }

    @Override
    public int gainmojo(int x) {
        return 0;
    }

    @Override
    public int spendmojo(int x) {
        return 0;
    }

    @Override
    public int counter() {
        return 0;
    }

    @Override
    public int value() {
        return 0;
    }

    @Override
    public boolean lingering() {
        return true;
    }
    
    @Override
    public Status instance(Character newAffected, Character newOther) {
        throw new RuntimeException("trying to instantiate abstract class enemyTrainingStatus");
    }

    @Override
    public JsonObject saveToJson() {
        return null;
    }

    @Override
    public Status loadFromJson(JsonObject obj) {
        return null;
    }
    
}
