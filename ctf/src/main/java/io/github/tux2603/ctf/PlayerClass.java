package io.github.tux2603.ctf;

public enum PlayerClass {
    ASSASSIN("assassin", 1),
    MAGE("mage", 2),
    MEDIC("medic", 3),
    NORMAL("normal", 4),
    SCOUT("scout", 5),
    TANK("tank", 6),
    NONE("none", 0);

    private final String name;
    private final int id;

    PlayerClass(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public static PlayerClass getById(int id) {
        for (PlayerClass type : values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        return NONE;
    }

    public static PlayerClass getByName(String name) {
        for (PlayerClass playerClass : values()) {
            if (playerClass.name().equalsIgnoreCase(name)) {
                return playerClass;
            }
        }
        return NONE;
    }

    @Override
    public String toString() {
        return name;
    }
}
