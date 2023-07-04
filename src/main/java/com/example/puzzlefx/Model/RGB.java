package com.example.puzzlefx.Model;

import java.util.ArrayList;
import java.util.List;

public class RGB {
    List<Integer> red;
    List<Integer> green;
    List<Integer> blue;

    public RGB() {
        red = new ArrayList<>();
        green = new ArrayList<>();
        blue = new ArrayList<>();
    }

    public RGB(List<Integer> red, List<Integer> green, List<Integer> blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public List<Integer> getRed() {
        return red;
    }

    public void setRed(List<Integer> red) {
        this.red = red;
    }

    public List<Integer> getGreen() {
        return green;
    }

    public void setGreen(List<Integer> green) {
        this.green = green;
    }

    public List<Integer> getBlue() {
        return blue;
    }

    public void setBlue(List<Integer> blue) {
        this.blue = blue;
    }

    public void addColor(int red, int green, int blue) {
        this.red.add(red);
        this.green.add(green);
        this.blue.add(blue);
    }
}
