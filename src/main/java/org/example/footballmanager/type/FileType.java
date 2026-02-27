package org.example.footballmanager.type;

import lombok.Getter;

@Getter
public enum FileType {
    TEAMS("teams"),
    PLAYERS("players");

    private final String folderName;

    FileType(String folderName) {
        this.folderName = folderName;
    }

    public static FileType fromString(String folder) {
        for (FileType type : values()) {
            if (type.folderName.equalsIgnoreCase(folder)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown file type: " + folder);
    }
}
