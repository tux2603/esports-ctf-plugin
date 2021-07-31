package io.github.tux2603.ctf;

import org.bukkit.ChatColor;

public enum PlayerTeam {
    RED (0, "red", ChatColor.RED),
    BLUE (1, "blue", ChatColor.BLUE),
    NONE (-1, "none", ChatColor.GRAY);

    private final int id;
    private final String name;
    private final ChatColor color;

    PlayerTeam(int id, String name, ChatColor color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public static PlayerTeam getById(int id) {
        for (PlayerTeam team : values()) {
            if (team.getId() == id) {
                return team;
            }
        }
        return NONE;
    }

    public String getName() {
        return name;
    }

    public static PlayerTeam getByName(String name) {
        for (PlayerTeam team : values()) {
            if (team.getName().equalsIgnoreCase(name)) {
                return team;
            }
        }
        return NONE;
    }

    public ChatColor getColor() {
        return color;
    }

    public String getColorCode() {
        return color.toString();
    }
}
