package com.uhmily.scovilleplugin.Types;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Language {

    ENGLISH("en"),
    SPANISH("es"),
    GERMAN("de"),
    JAPANESE("jp");

    private final String abbrev;

    Language(String abbrev) {
        this.abbrev = abbrev;
    }

    @JsonValue
    public String getAbbrev() {
        return this.abbrev;
    }

    public static Language getLang(String abbrev) {
        switch (abbrev) {
            case "es": return SPANISH;
            case "de": return GERMAN;
            case "jp": return JAPANESE;
            default: return ENGLISH;
        }
    }

    public int toInt() {
        return this.ordinal() + 1;
    }

    public String getItemTexture() {
        switch (this) {
            case ENGLISH:
                return "http://textures.minecraft.net/texture/4cac9774da1217248532ce147f7831f67a12fdcca1cf0cb4b3848de6bc94b4";
            case SPANISH:
                return "http://textures.minecraft.net/texture/32bd4521983309e0ad76c1ee29874287957ec3d96f8d889324da8c887e485ea8";
            case GERMAN:
                return "http://textures.minecraft.net/texture/5e7899b4806858697e283f084d9173fe487886453774626b24bd8cfecc77b3f";
            default:
                return "http://textures.minecraft.net/texture/d640ae466162a47d3ee33c4076df1cab96f11860f07edb1f0832c525a9e33323";
        }
    }

    public Language nextLang() {
        if (this == Language.JAPANESE) {
            return ENGLISH;
        }
        return Language.values()[toInt()];
    }

    public Language prevLang() {
        if (this == Language.ENGLISH) {
            return JAPANESE;
        }
        return Language.values()[this.ordinal() - 1];
    }

}
