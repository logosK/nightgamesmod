package nightgames.characters;

import java.util.ArrayList;
import nightgames.status.Resistance;

/**By DSM - new implementation of Trait that makes it in line with other complex and widely-deployed and often-checked game mechanics. It carries its own equipment with it.
 * 
 * TODO: Move Trait to CharTrait, Convert all traits into Classes extended from CharTrait 
 * 
 * */
public abstract class CharTrait {
    
    protected String name;
    protected String descShort;         
    protected String descLong;
    protected CharTrait parentTrait;
    public enum TraitActivationTime {onRoundStart, onSkillUse, onEffectActivation, onTraitActivation, onTurnStart, onTurnEnd, onRoundEnd}   //In case we wish to activated this by phase of combat.
    protected TraitActivationTime TA_Time;
    protected boolean isFeat;
    protected boolean isHidden;
    protected ArrayList<Resistance> resistances;
    
    
    public String getName() {  return name;   }   public void setName(String name) {  this.name = name;   }
    public String getDescShort() {   return descShort;   }   public void setDescShort(String descShort) {  this.descShort = descShort;   }
    public String getDescLong() {  return descLong;   }   public void setDescLong(String descLong) {  this.descLong = descLong;   }
    public CharTrait getParentTrait() {   return parentTrait;  }  public void setParentTrait(CharTrait parentTrait) {  this.parentTrait = parentTrait;   }
    public TraitActivationTime getTA_Time() {  return TA_Time;  }  public void setTA_Time(TraitActivationTime tA_Time) {  TA_Time = tA_Time;  }
     public boolean isFeat() {   return isFeat;  }   public void setFeat(boolean isFeat) {  this.isFeat = isFeat;   }
    public boolean isHidden() {  return isHidden;  }  public void setHidden(boolean isHidden) {  this.isHidden = isHidden;  }
    public ArrayList<Resistance> getResistances() {   return resistances;  }  public void setResistances(ArrayList<Resistance> resistances) {  this.resistances = resistances;  }

    
    //Traits should carry their own shit with them.
    
    /**Returns true if the character meets the requiresments to take this Trait.*/
    abstract public boolean checkRequirements(Character user);
    
    /**Describes this trait - useful for the stringbuilder.*/
    abstract public void describe(Character c, StringBuilder b);
    
    
    //Many abstract classes should distinguish like this. They are checked, they spit out text, they do what the text says.
    
    /**Check if this trait activates its effect. Useful for some traits that trigger effect in combat.*/
    abstract public boolean checkIfActivates(Character user, Character opponent);
    
    /**displays any text relevant to the activation of this Trait.*/
    abstract public void sayActivation(Character user, Character opponent);
    
    /**Executes this trait's effects upon activation.*/
    abstract public void doActivation(Character user, Character opponent);


    public CharTrait(String n, String ds, String dl, CharTrait p) {
        this.name = n;
        this.descShort = ds;
        this.descLong = dl;
        this.parentTrait = p;
    }

    public boolean isOverridden(Character ch) {  
        return false;  
    }

    public boolean hasResistance(Resistance r){
        if (this.resistances.contains(r)){
            return true;
        } else {
            return false;
        }
    }

    
    /**Equals override method. Returns true if the names are the same.
     * 
     * @return 
     * Returns true if name are same, false otherwise.
     * */
    public boolean equals(Object obj){
        CharTrait otherItem = (CharTrait)obj;
        if (this.name == otherItem.getName()) {
            return true;
        } else {
            return false;
        }
    }

}
