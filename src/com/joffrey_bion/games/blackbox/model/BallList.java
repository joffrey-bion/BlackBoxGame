package com.joffrey_bion.games.blackbox.model;

import java.util.LinkedList;

public class BallList extends LinkedList<Ball> {

    public BallList() {
        super();
    }
    
    public void add(int i, int j) {
        add(new Ball(i, j));
    }

    public void remove(int i, int j) {
        for (Ball b : this) {
            if (b.i == i && b.j == j) {
                remove(b);
                return;
            }
        }
        throw new RuntimeException("The specified ball is not in the list.");
    }
    
    public boolean contains(int i, int j) {
        for (Ball b : this) {
            if (b.i == i && b.j == j) {
                return true;
            }
        }
        return false;
    }
}
