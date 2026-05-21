package com.mrzak34.thunderhack;

import com.mrzak34.thunderhack.Hud.HudManager;
import com.mrzak34.thunderhack.event.events.NetworkHandler;
import com.mrzak34.thunderhack.manager.*;
import com.mrzak34.thunderhack.module.Module;
import com.mrzak34.thunderhack.module.ModuleManager;
import com.mrzak34.thunderhack.module.modules.client.ClickguiToggle;
import com.mrzak34.thunderhack.module.modules.client.HudeditorToggle;
import com.mrzak34.thunderhack.ui.HudOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION)
public class Main {
    private static Minecraft mc = Minecraft.getMinecraft();

    public static Main INSTANCE = new Main();
    public static ModuleManager moduleManager;
    public static HudManager hudManager;
    public static final String MODID = "thunderhack";
    public static final String NAME = "Thunder Hack";
    public static final String VERSION = "1.0";
    public static final Logger LOGGER = LogManager.getLogger("ThunderHack");
    public static HudOverlay hud;
    public ClickguiToggle clickGUI;
    public HudeditorToggle hudEditor;
    public static NetworkHandler networkHandler;
    public static RotationManager rotationManager;
    public static TotemPopManager totemPopManager;
    public static TimerManager timerManager;
    public static ServerManager servermanager;
    public static FileManager fileManager;
    public static CommandManager commandManager;
    public static ConfigManager configManager;
    private static boolean unloaded;

    static {
        unloaded = false;
    }

    public static void load() {
        LOGGER.info("\n\nLoading ThunderHack by MrZak34");
    }

    public static boolean nullCheck() {
        return Main .mc.player == null;
    }

    public static boolean fullNullCheck() {
        return Main.mc.player == null || Main.mc.world == null;
    }

    public static ModuleManager getModules() {
        return Main.moduleManager;
    }
    public static NetworkHandler getNetworkHandler() {
        return Main.networkHandler;
    }

    @Mod.Instance
    public Main instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {}

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // some example code
        MinecraftForge.EVENT_BUS.register(instance);
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
        moduleManager = new ModuleManager();
        timerManager = new TimerManager();
        hudManager = new HudManager();
        hud = new HudOverlay();
        rotationManager = new RotationManager();
        MinecraftForge.EVENT_BUS.register(new HudOverlay());
        totemPopManager = new TotemPopManager();
        servermanager = new ServerManager();
        fileManager = new FileManager();
        configManager = new ConfigManager();
        commandManager = new CommandManager();
    }

    @SubscribeEvent
    public void keyPress(InputEvent.KeyInputEvent e) {
        if(Minecraft.getMinecraft().world == null || Minecraft.getMinecraft().player == null)
            return;
        try {
            if(Keyboard.isCreated()) {
                if(Keyboard.getEventKeyState()) {
                    int keyCode = Keyboard.getEventKey();
                    if(keyCode<= 0)
                        return;
                    for(Module m : moduleManager.modules) {
                        if(m.getKey() == keyCode && keyCode > 0) {
                            m.toggle();
                        }
                    }
                }
            }
        } catch (Exception q) {q.printStackTrace(); }
    }
}
