package nightgames.quest;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;

import nightgames.characters.Character;
import nightgames.global.Global;
import nightgames.json.JsonUtils;

public abstract class Quest {
    private String name;
    private int numPointTypes;
    protected Map<String,int[]> points;
    
    public Quest() {
        this.name="ERROR";
        this.numPointTypes=0;
        this.points=new HashMap<String, int[]>();
        System.out.println("0-arg constructor called");throw new RuntimeException();
    }
    
    public Quest(String name, int numPointTypes) {
        this.name=name;
        this.numPointTypes=numPointTypes;
        this.points=new HashMap<String, int[]>();
        //System.out.println("2-arg constructor called");throw new RuntimeException();
    }
    
    protected Quest(Map<String, int[]> points, String name, int numPointTypes) {
        this.name=name;
        this.numPointTypes=numPointTypes;
        this.points=points;
        System.out.println("3-arg constructor called");//throw new RuntimeException();
    }
    
    public void pointTo(Character charto, int pointType) {
        if (!points.containsKey(charto.getClass().getName())) {points.put(charto.getClass().getName(), new int[numPointTypes]);}
        points.get(charto.getClass().getName())[pointType]+=1;
    }
    
    public int getPointsForOfType(Character charFor, int pointType) {
        if (points.containsKey(charFor.getClass().getName())) {return points.get(charFor.getClass().getName())[pointType];}
        return 0;
    }
    
    public String getName() {return name;}
    
    public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", name);
        obj.addProperty("numPointTypes", numPointTypes);
        obj.add("points", JsonUtils.JsonFromMap(points));
        return obj;
    }
    
    public static Quest load(JsonObject object) {
        String name = object.get("name").getAsString();
        System.out.println(name);
//        int numPointTypes = object.get("numPointTypes").getAsInt();
        System.out.println(object.toString());
        System.out.println(object.get("points").toString());
        Map<String, int[]> points = JsonUtils.mapFromJson(object.getAsJsonObject("points"), String.class, int[].class);
        System.out.println(points);
        //return new Quest(name, numPointTypes, points);
        if (name.equals("Buttslut Training")) {System.out.println("Is a BSQ");return new ButtslutQuest(points);}
        return null;
    }
}
