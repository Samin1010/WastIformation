package app;

import java.util.ArrayList;

public class Persona {
    ArrayList<String> needs = new ArrayList<String>();
    ArrayList<String> goals = new ArrayList<String>();
    ArrayList<String> skills = new ArrayList<String>();
    String background;
    String attributes;

    Persona(String attributes, String background){
        this.attributes = attributes;
        this.background = background;
    }

    public void addNeed (String need){
        this.needs.add(need);
    }

    public void addGoal (String goal){
        this.goals.add(goal);
    }

    public void addSkill (String skill){
        this.skills.add(skill);
    }
        
}
