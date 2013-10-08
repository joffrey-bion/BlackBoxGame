package com.joffrey_bion.games.blackbox;

import java.util.InputMismatchException;
import java.util.Scanner;

import com.joffrey_bion.games.blackbox.model.Grid;
import com.joffrey_bion.games.blackbox.model.Side;

public class Main {

    public static void main(String[] args) {
        int n = 5;//getNumberOfBalls();
        System.out.println("Starting " + n + "-ball game...");
        Grid grid = new Grid(n);
        System.out.println(grid);
        grid.shoot(Side.TOP, 2);
        System.out.println(grid);
        grid.shoot(Side.TOP, 5);
        System.out.println(grid);
        grid.shoot(Side.LEFT, 0);
        System.out.println(grid);
        grid.shoot(Side.RIGHT, 2);
        System.out.println(grid);
        grid.shoot(Side.RIGHT, 5);
        System.out.println(grid);
        grid.shoot(Side.BOTTOM, 3);
        System.out.println(grid);
    }
    
    private static int getNumberOfBalls() {
        System.out.println("Enter number of balls to find: ");
        Scanner sc = new Scanner(System.in);
        try {
            int n = sc.nextInt();
            sc.close();
            return n;
        } catch(InputMismatchException e) {
            System.err.println("Integer expected.");
            return getNumberOfBalls();
        }
    }
    
}
