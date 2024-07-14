package com.example.lab6;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Scanner;

public class MathInterpretator {
    public static void main(String[] args) {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine js = factory.getEngineByName("graal.js");

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter equation to compute or exit to quit the program:");
        while (true) {
            try {
                String input = scanner.nextLine();
                if (input.trim().equalsIgnoreCase("exit")) return;
                js.eval("print( " + input + " );");
            } catch (ScriptException e) {
                e.printStackTrace();
                System.out.println("Error: try again!");
            }
        }

    }
}
