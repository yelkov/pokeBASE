package edu.badpals.pokebase.controller;

import java.util.Stack;

public class ScenePile {
    private static Stack<String> stack = new Stack<>();

    public static void addToPile(String view){
        stack.push(view);
        System.out.println(stack.size());
    }

    public static String goBack(){
        stack.pop();
        System.out.println(stack.size());
        return stack.pop();
    }
}
