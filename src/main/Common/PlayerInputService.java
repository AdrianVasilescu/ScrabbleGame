package main.Common;

import java.util.Scanner;

public class PlayerInputService {
    Scanner scanner;

    public PlayerInputService()
    {
        scanner = new Scanner(System.in);
    }

    public String getInput(String prompt)
    {
        System.out.println(prompt);
        return scanner.nextLine();
    }
}
