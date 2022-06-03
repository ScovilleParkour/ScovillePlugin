package com.uhmily.scovilleplugin.Types.WinMessage;

import com.uhmily.scovilleplugin.Types.JoinLeaveMessage;

import java.util.Arrays;
import java.util.Optional;

public enum WinMessage {
    DEFAULT("default"),
    LEET("leet"),
    CONQUISTADOR("conq"),
    SPICY("spicy"),
    DISBELIEF("disbelief"),
    REVERSED("reverse"),
    HYPE("hype"),
    DAILYNEWS("daily"),
    ROBOTIC("robotic");

    private String ident;

    WinMessage(String ident) { this.ident = ident; }

    public String getIdent() {
        return ident;
    }

    public static WinMessage fromIdent(String ident) {
        Optional<WinMessage> message = Arrays.stream(WinMessage.values()).filter(joinLeaveMessage -> joinLeaveMessage.getIdent().equalsIgnoreCase(ident)).findFirst();
        if (message.isPresent()) return message.get();
        return null;
    }

}
