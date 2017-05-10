package com.nwu.foodshake;

import com.nwu.foodshake.screen.Screen;

import java.util.ArrayDeque;
import java.util.Deque;

public class Navigator {
    //----------------------------------------------------------------------------------------------
    private Deque<Screen> screenStack;

    public Navigator() {
        screenStack = new ArrayDeque<>();
    }

    //----------------------------------------------------------------------------------------------
    public void setUp(Screen main) {
        if (screenStack.size() > 0) {
            screenStack.removeLast();
        }
        screenStack.addLast(main);
    }

    //----------------------------------------------------------------------------------------------
    public void show() {
        screenStack.getFirst().showPage((MainActivity) screenStack.getLast());
    }

    //----------------------------------------------------------------------------------------------
    public void goTo(Screen screen) {
        if (!screen.equals(screenStack.getFirst())) {
            screenStack.addFirst(screen);
        }
        show();
    }

    //----------------------------------------------------------------------------------------------
    public void pop() {
        if (screenStack.size() > 1) {
            screenStack.removeFirst();
        }
    }

    //----------------------------------------------------------------------------------------------
    public void back() {
        if (screenStack.size() > 1) {
            screenStack.removeFirst();
            show();
        }
    }

    //----------------------------------------------------------------------------------------------
}
