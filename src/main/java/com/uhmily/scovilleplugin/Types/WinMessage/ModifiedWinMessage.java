package com.uhmily.scovilleplugin.Types.WinMessage;

import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import org.bukkit.entity.Player;

public class ModifiedWinMessage {

    private WinMessage message;
    private boolean rainbow, times, time;

    public ModifiedWinMessage() {
        this.message = WinMessage.DEFAULT;
        this.rainbow = false;
        this.times = false;
        this.time = false;
    }

    public WinMessage getMessage() {
        return message;
    }

    public void setMessage(WinMessage message) {
        this.message = message;
    }

    public boolean isRainbow() {
        return rainbow;
    }

    public void setRainbow(boolean rainbow) {
        this.rainbow = rainbow;
    }

    public boolean isTimes() {
        return times;
    }

    public void setTimes(boolean times) {
        this.times = times;
    }

    public boolean isTime() {
        return time;
    }

    public void setTime(boolean time) {
        this.time = time;
    }

    public String toKey() {
        return "win_message_" + this.message.getIdent() + (this.rainbow ? "_rainbow" : "") + (this.times ? "_times" : "") + (this.time ? "_time" : "");
    }

}
