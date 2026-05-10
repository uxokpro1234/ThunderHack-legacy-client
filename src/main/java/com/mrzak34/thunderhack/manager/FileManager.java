package com.mrzak34.thunderhack.manager;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.mrzak34.thunderhack.Main;
import com.mrzak34.thunderhack.module.Category;

public class FileManager
        extends Main {
    private final Path base = this.getMkDirectory(this.getRoot(), "thunderhack");
    private final Path config = this.getMkDirectory(this.base, "config");
    private final Path notebot = this.getMkDirectory(this.base, "notebot");
    public Category category;

    private String[] expandPath(String fullPath) {
        return fullPath.split(":?\\\\\\\\|\\/");
    }

    private Stream<String> expandPaths(String... paths) {
        return Arrays.stream(paths).map(this::expandPath).flatMap(Arrays::stream);
    }

    private Path lookupPath(Path root, String... paths) {
        return Paths.get(root.toString(), paths);
    }

    private Path getRoot() {
        return Paths.get("", new String[0]);
    }

    private void createDirectory(Path dir) {
        try {
            if (!Files.isDirectory(dir, new LinkOption[0])) {
                if (Files.exists(dir, new LinkOption[0])) {
                    Files.delete(dir);
                }
                Files.createDirectories(dir, new FileAttribute[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Path getMkDirectory(Path parent, String... paths) {
        if (paths.length < 1) {
            return parent;
        }
        Path dir = this.lookupPath(parent, paths);
        this.createDirectory(dir);
        return dir;
    }

public FileManager() {
        this.getMkDirectory(this.base, "config");
        //phobos
        //JSONObject PhobosAWp = new JSONObject();
        //PhobosAWp.put("spoofs", PhobosAWP.spoofs.getValue());
        //PhobosAWp.put("Timeout", PhobosAWP.Timeout.getValue());
        //PhobosAWp.put("Bows", PhobosAWP.Bows.isEnabled());
        //PhobosAWp.put("pearls", PhobosAWP.pearls.isEnabled());
        //PhobosAWp.put("eggs", PhobosAWP.pearls.isEnabled());
        //PhobosAWp.put("snowballs", PhobosAWP.snowballs.isEnabled());
        //PhobosAWp.put("bypass", PhobosAWP.bypass.isEnabled());

        //JSONObject PhobosAWP1 = new JSONObject();
        //PhobosAWP1.put("PhobosAWP", PhobosAWp);

//speed
        //JSONObject SpeeD = new JSONObject();
        //SpeeD.put("Mode", Speed.mode.getMode());
        //SpeeD.put("Jump", Speed.strafeJump.isEnabled());
        //SpeeD.put("NoShake", Speed.noShake.isEnabled());
        //SpeeD.put("Timer", Speed.useTimer.isEnabled());

        //JSONObject Speed1 = new JSONObject();
        //Speed1.put("Speed", SpeeD);
//Packetmine
    //for (Module category : Main.moduleManager.getModuleList()) {

        //if(category.getName().getInstance().toggled){

        //JSONObject mo = new JSONObject();
        //mo.put(category.getName(), true);

        //JSONObject modul = new JSONObject();
        //modul.put("Enabled", mo);

        //в лист их
        //JSONArray Lis = new JSONArray();
        //Lis.add(modul);
        //Lis.add(Speed1);
        //Lis.add(PacketMine1);


        //Write JSON file
        //try (FileWriter file = new FileWriter("C:\\Users\\Markuss\\AppData\\Roaming\\.minecraft\\thunderhack\\config\\config.json")) {
            //We can write any JSONArray or JSONObject instance to the file
            //file.write(Lis.toJSONString());
            //file.flush();

        //} catch (IOException e) {
            //e.printStackTrace();
        //}
    //}

}

    public Path getBasePath() {
        return this.base;
    }

    public Path getBaseResolve(String ... paths) {
        String[] names = (String[])this.expandPaths(paths).toArray(String[]::new);
        if (names.length < 1) {
            throw new IllegalArgumentException("missing path");
        }
        return this.lookupPath(this.getBasePath(), names);
    }

    public Path getMkBaseResolve(String ... paths) {
        Path path = this.getBaseResolve(paths);
        this.createDirectory(path.getParent());
        return path;
    }

    public Path getConfig() {
        return this.getBasePath().resolve("config");
    }

    public Path getCache() {
        return this.getBasePath().resolve("cache");
    }

    public Path getNotebot() {
        return this.getBasePath().resolve("notebot");
    }

    public Path getMkBaseDirectory(String ... names) {
        return this.getMkDirectory(this.getBasePath(), this.expandPaths(names).collect(Collectors.joining(File.separator)));
    }

    public Path getMkConfigDirectory(String ... names) {
        return this.getMkDirectory(this.getConfig(), this.expandPaths(names).collect(Collectors.joining(File.separator)));
    }

    public static boolean appendTextFile(String data, String file) {
        try {
            Path path = Paths.get(file, new String[0]);
            Files.write(path, Collections.singletonList(data), StandardCharsets.UTF_8, Files.exists(path, new LinkOption[0]) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
        }
        catch (IOException e) {
            System.out.println("WARNING: Unable to write file: " + file);
            return false;
        }
        return true;
    }

    public static List<String> readTextFileAllLines(String file) {
        try {
            Path path = Paths.get(file, new String[0]);
            return Files.readAllLines(path, StandardCharsets.UTF_8);
        }
        catch (IOException e) {
            System.out.println("WARNING: Unable to read file, creating new file: " + file);
            FileManager.appendTextFile("", file);
            return Collections.emptyList();
        }
    }
}