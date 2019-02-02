package cn.wode490390.nukkit.feedback;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Feedback extends PluginBase {

    private Config config;

    private int cooldown = 0;

    private final Map<Player, Integer> cooldowns = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("fb")) {
            if (sender.isPlayer()) {
                if (sender.hasPermission("fb.send")) {
                    if (args.length > 0) {
                        Player p = (Player) sender;
                        if (cooldowns.containsKey(p) && cooldowns.get(p) > getServer().getTick()) {
                            sender.sendMessage("Cooling...");
                            return true;
                        }
                        cooldowns.put(p, getServer().getTick() + cooldown);
                        List<String> l;
                        try {
                            l = config.getStringList(p.getName());
                        } catch (Exception e) {
                            l = new ArrayList<>();
                        }
                        String t = "";
                        for (String arg : args) {
                            t += arg + " ";
                        }
                        l.add(t);
                        config.set(p.getName(), l);
                        config.save();
                        sender.sendMessage("Thx :)");
                        return true;
                    }
                } else {
                    sender.sendMessage("Permission denied!");
                    return true;
                }
            } else {
                sender.sendMessage("Non-player!");
                return true;
            }
        }
        return false;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = getConfig();
        try {
            cooldown = config.getInt("cooldown-tick");
        } catch (Exception e) {}
    }
}
