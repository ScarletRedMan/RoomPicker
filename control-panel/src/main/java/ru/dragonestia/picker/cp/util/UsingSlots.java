package ru.dragonestia.picker.cp.util;

public class UsingSlots {

    private UsingSlots() {}

    public static int getUsingPercentage(int slots, int usedSlots) {
        if (slots == -1) return -1;
        double percent = usedSlots / (double) slots * 100;
        return (int) percent;
    }
}
