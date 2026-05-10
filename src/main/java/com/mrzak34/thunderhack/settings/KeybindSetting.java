package com.mrzak34.thunderhack.settings;

import com.google.common.base.Converter;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.lwjgl.input.Keyboard;

public class KeybindSetting extends Setting{

    public int code;

    public KeybindSetting(int code) {
        this.name = "keybind";
        this.code = code;
    }

    public int getKeyCode(int i) {
        return code;
    }

    public static KeybindSetting none() {
        return new KeybindSetting(-1);
    }

    public int setKeyCode(int code) {
        this.code = code;
        return code;
    }
    public static class BindConverter
            extends Converter<KeybindSetting, JsonElement> {
        public JsonElement doForward(KeybindSetting bind) {
            return new JsonPrimitive(bind.toString());
        }

        public KeybindSetting doBackward(JsonElement jsonElement) {
            String s = jsonElement.getAsString();
            if (s.equalsIgnoreCase("None")) {
                return KeybindSetting.none();
            }
            int key = -1;
            try {
                key = Keyboard.getKeyIndex(s.toUpperCase());
            } catch (Exception exception) {
                // empty catch block
            }
            if (key == 0) {
                return KeybindSetting.none();
            }
            return new KeybindSetting(key);
        }
    }

}
