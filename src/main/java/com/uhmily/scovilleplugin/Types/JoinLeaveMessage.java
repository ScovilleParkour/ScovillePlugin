package com.uhmily.scovilleplugin.Types;

import java.util.Arrays;
import java.util.Optional;

public enum JoinLeaveMessage {

    MRCOOL("mr_cool"),
    SHAKESPEARE("shakespeare"),
    SUPERHERO("superhero"),
    AVIATION("aviation"),
    MONARCH("monarch");

    private String ident;

    JoinLeaveMessage(String ident) {
        this.ident = ident;
    }

    public String getIdent() {
        return ident;
    }

    public static JoinLeaveMessage fromIdent(String ident) {
        Optional<JoinLeaveMessage> message = Arrays.stream(JoinLeaveMessage.values()).filter(joinLeaveMessage -> joinLeaveMessage.getIdent().equalsIgnoreCase(ident)).findFirst();
        if (message.isPresent()) return message.get();
        return null;
    }

}
