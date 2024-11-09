package dev.lipoteam.expFarm;

import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;
import java.util.logging.Logger;

public class DataManager {

    private final Plugin plugin;

    public DataManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public static Logger logger = Logger.getLogger("");

    public void setdata(Object what, String whatkey, Object data) {
        NamespacedKey key = new NamespacedKey(plugin, whatkey);

        if (what instanceof ItemStack item) {
            ItemMeta itemMeta = item.getItemMeta();

            if (!Objects.isNull(itemMeta)) {
                PersistentDataContainer itemData = itemMeta.getPersistentDataContainer();

                switch (data) {
                    case String s -> itemData.set(key, PersistentDataType.STRING, s);
                    case Integer i -> itemData.set(key, PersistentDataType.INTEGER, i);
                    case Boolean b -> itemData.set(key, PersistentDataType.BOOLEAN, b);
                    case Double d -> itemData.set(key, PersistentDataType.DOUBLE, d);
                    case null, default -> itemData.set(key, PersistentDataType.STRING, serialize(data));
                }
                item.setItemMeta(itemMeta);

            }
        } else if (what instanceof Player) {

            PersistentDataContainer playerData = ((Player) what).getPersistentDataContainer();
            switch (data) {
                case String s -> playerData.set(key, PersistentDataType.STRING, s);
                case Integer i -> playerData.set(key, PersistentDataType.INTEGER, i);
                case Boolean b -> playerData.set(key, PersistentDataType.BOOLEAN, b);
                case Double d -> playerData.set(key, PersistentDataType.DOUBLE, d);
                case null, default -> playerData.set(key, PersistentDataType.STRING, serialize(data));
            }

        } else if (what instanceof TextDisplay) {
            PersistentDataContainer displayData = ((TextDisplay) what).getPersistentDataContainer();
            switch (data) {
                case String s -> displayData.set(key, PersistentDataType.STRING, s);
                case Integer i -> displayData.set(key, PersistentDataType.INTEGER, i);
                case Boolean b -> displayData.set(key, PersistentDataType.BOOLEAN, b);
                case Double d -> displayData.set(key, PersistentDataType.DOUBLE, d);
                case null, default -> displayData.set(key, PersistentDataType.STRING, serialize(data));
            }
            System.out.println("test");
        }

    }

    public void unsetdata(Object what, String whatkey) {
        NamespacedKey key = new NamespacedKey(plugin, whatkey);

        if (what instanceof ItemStack item) {
            ItemMeta itemMeta = item.getItemMeta();

            if (!Objects.isNull(itemMeta)) {
                PersistentDataContainer itemData = itemMeta.getPersistentDataContainer();

                itemData.remove(key);
                item.setItemMeta(itemMeta);

            }
        } else if (what instanceof Player) {

            PersistentDataContainer playerData = ((Player) what).getPersistentDataContainer();
            playerData.remove(key);

        } else if (what instanceof  TextDisplay) {

            PersistentDataContainer displayData = ((TextDisplay) what).getPersistentDataContainer();
            displayData.remove(key);

        }

    }

    public Object getdata(Object what, String whatkey, boolean deep) {
        NamespacedKey key = new NamespacedKey(plugin, whatkey);
        Object thedata = null;

        if (what instanceof ItemStack item) {
            ItemMeta itemMeta = item.getItemMeta();
            if (!Objects.isNull(itemMeta)) {
                PersistentDataContainer itemData = itemMeta.getPersistentDataContainer();

                if (itemData.has(key, PersistentDataType.STRING)) {
                    thedata = itemData.get(key, PersistentDataType.STRING);

                    if (deep) {
                        thedata = deserialize((String) thedata);
                    }

                } else if (itemData.has(key, PersistentDataType.INTEGER)) {
                    thedata = itemData.get(key, PersistentDataType.INTEGER);
                } else if (itemData.has(key, PersistentDataType.BOOLEAN)) {
                    thedata = itemData.get(key, PersistentDataType.BOOLEAN);
                } else if (itemData.has(key, PersistentDataType.DOUBLE)) {
                    thedata = itemData.get(key, PersistentDataType.DOUBLE);
                }
            }

        } else if (what instanceof Player p) {
            PersistentDataContainer playerData = p.getPersistentDataContainer();

            if (playerData.has(key, PersistentDataType.STRING)) {
                thedata = playerData.get(key, PersistentDataType.STRING);

                if (deep) {
                    thedata = deserialize((String) thedata);
                }

            } else if (playerData.has(key, PersistentDataType.INTEGER)) {
                thedata = playerData.get(key, PersistentDataType.INTEGER);
            } else if (playerData.has(key, PersistentDataType.BOOLEAN)) {
                thedata = playerData.get(key, PersistentDataType.BOOLEAN);
            } else if (playerData.has(key, PersistentDataType.DOUBLE)) {
                thedata = playerData.get(key, PersistentDataType.DOUBLE);
            }

        }
        return thedata;
    }

    public boolean hasData(Object what, String keyvalue) {

        NamespacedKey key = new NamespacedKey(plugin, keyvalue);

        if (what instanceof ItemStack) {
            if (((ItemStack) what).hasItemMeta()) {
                ItemMeta whatMeta = ((ItemStack) what).getItemMeta();
                assert whatMeta != null;
                PersistentDataContainer data = whatMeta.getPersistentDataContainer();
                return data.has(key);
            }

        } else if (what instanceof Player) {

            PersistentDataContainer data = ((Player) what).getPersistentDataContainer();
            return data.has(key);

        } else if (what instanceof  TextDisplay) {

            PersistentDataContainer data = ((TextDisplay) what).getPersistentDataContainer();
            return data.has(key);

        }

        return false;
    }

    public void CalcData(Object what, String whatkey, double num, String operation) {

        Object data = getdata(what, whatkey, false);
        switch (operation) {
            case "+" -> setdata(what, whatkey, (double) data + num);
            case "-" -> setdata(what, whatkey, (double) data - num);
            case "*" -> setdata(what, whatkey, (double) data * num);
            case "/" -> setdata(what, whatkey, (double) data / num);
        }

    }

    public String serialize(Object item) {

        String encodedObject;

        try {

            ByteArrayOutputStream io = new ByteArrayOutputStream();
            BukkitObjectOutputStream os = new BukkitObjectOutputStream(io);
            os.writeObject(item);
            os.flush();

            byte[] serializedObject = io.toByteArray();

            encodedObject = new String(Base64.getEncoder().encode(serializedObject));

            return encodedObject;


        } catch (IOException ex) {
            logger.warning(ex.toString());
        }

        return null;
    }

    public Object deserialize(String encoded) {

        try {

            byte[] serializedObject;
            serializedObject = Base64.getDecoder().decode(encoded);
            ByteArrayInputStream in = new ByteArrayInputStream(serializedObject);

            BukkitObjectInputStream is = new BukkitObjectInputStream(in);

            return is.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            logger.warning(ex.toString());
        }
        return null;
    }

}
