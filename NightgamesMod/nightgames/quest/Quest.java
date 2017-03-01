package nightgames.quest;

import java.util.HashMap;
import java.util.Map;
import nightgames.characters.Character;

public abstract class Quest {
    private String name;
    private int numPointTypes;
    protected Map<Character,int[]> points;
    
    public Quest(String name, int numPointTypes) {
        this.name=name;
        this.numPointTypes=numPointTypes;
        this.points=new HashMap<Character, int[]>();
    }
    
    protected Quest(Map<Character, int[]> points, String name, int numPointTypes) {
        this.name=name;
        this.numPointTypes=numPointTypes;
        this.points=points;
    }
    
    public void pointTo(Character charto, int pointType) {
        if (!points.containsKey(charto)) {points.put(charto, new int[numPointTypes]);}
        points.get(charto)[pointType]+=1;
    }
    
    public int getPointsForOfType(Character charFor, int pointType) {
        if (points.containsKey(charFor)) {return points.get(charFor)[pointType];}
        return 0;
    }
    
    public String getName() {return name;}
}
