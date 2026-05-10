package com.mrzak34.thunderhack.module;

public enum Category {

    COMBAT("Combat", 10, 10, false, true),
    EXPLOIT("Exploit", 120, 10, false, true),
    MISC("Misc", 230, 10, false, true),
    MOVEMENT("Movement", 340, 10, false, true),
    RENDER("Render", 450, 10, false, true),
    CLIENT("Client", 560, 10, false, true);

    public String name;
    public int posX, posY;
    public boolean mouseClicked;
    public boolean showModules;

    Category(String name, int posX, int posY,boolean mouseClicked, boolean showModules) {
        this.name = name;
        this.posX = posX;
        this.posY = posY;
        this.mouseClicked = mouseClicked;
        this.showModules = showModules;
    }

    public String getName() {
        return this.name;
    }

    }

