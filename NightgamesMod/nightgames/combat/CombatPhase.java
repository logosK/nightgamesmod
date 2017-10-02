package nightgames.combat;

import java.util.Observable;

/**For Combat rewrite.*/
public abstract class CombatPhase extends Observable implements Cloneable {
    
    protected String m_Name;
    
    /*
    private enum CombatPhase {
        START,
        PRETURN,
        SKILL_SELECTION,
        PET_ACTIONS,
        DETERMINE_SKILL_ORDER,
        P1_ACT_FIRST,
        P2_ACT_FIRST,
        P1_ACT_SECOND,
        P2_ACT_SECOND,
        UPKEEP,
        LEVEL_DRAIN,
        RESULTS_SCENE,
        FINISHED_SCENE,
        ENDED,
    }*/
    
    protected CombatPhase m_PhaseType;
    
    protected boolean m_isComplete;     //Indicates if this phase has completed and is ready to move on. 
    protected boolean m_isStepping;     //Indicates if this Phase steps out when written to the GUI's text window or if it just displays all at once. Defaults to False.
    protected boolean m_isSilent;       //Indicates if this Phase is written to the GUI at all. Defaults to true.
    
    public String getM_Name() {  return m_Name;   }   public void setM_Name(String m_Name) {  this.m_Name = m_Name;   }
    public CombatPhase getM_PhaseType() {   return m_PhaseType;    }   public void setM_PhaseType(CombatPhase m_PhaseType) { this.m_PhaseType = m_PhaseType;  }
    public boolean isM_isComplete() {   return m_isComplete;   }    public void setM_isComplete(boolean m_isComplete) {  this.m_isComplete = m_isComplete;    }
    public boolean isM_isStepping() {       return m_isStepping;    }    public void setM_isStepping(boolean m_isStepping) {  this.m_isStepping = m_isStepping;    }
    public boolean isM_isSilent() {        return m_isSilent;    }    public void setM_isSilent(boolean m_isSilent) {     this.m_isSilent = m_isSilent;  }
    
    
    abstract public void beginPhase(CombatData CD);
    abstract public void processPhase(CombatData CD);
    abstract public void finishPhase(CombatData CD);
    abstract public void nextPhase(CombatData CD);

    public void determineWinCondition(CombatData CD){
        
    }
    
    

}
