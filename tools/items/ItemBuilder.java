package fr.tetemh.shop.tools.items;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ItemBuilder {
    private final ItemStack item;

    public ItemBuilder(ItemStack item) {
        this.item = item;
    }
    public ItemBuilder(Material material) {
        this.item = new ItemStack(material, 1, (short) 0);
    }
    public ItemBuilder(Material material, int amount) {
        this.item = new ItemStack(material, amount, (short) 0);
    }
    public ItemBuilder(Material material, int amount, int data) {
        this.item = new ItemStack(material, amount, (short) data);
    }

    public ItemBuilder setName(@Nonnull String name) {
        ItemMeta meta = this.meta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        return this.apply(meta);
    }
    public ItemBuilder setLore(@Nonnull List<String> lore) {
        List<String> ls = new ArrayList<>();
        lore.forEach(l -> ls.add(ChatColor.translateAlternateColorCodes('&', l)));

        ItemMeta meta = this.meta();
        meta.setLore(ls);
        return this.apply(meta);
    }
    public ItemBuilder setLore(@Nonnull String... lore) {
        return this.setLore(Arrays.asList(lore));
    }
    public ItemBuilder addEnchantment(Enchantment enchantment, int level, boolean unsafe) {
        if (unsafe) {
            this.item.addUnsafeEnchantment(enchantment, level);
            return this;
        }

        ItemMeta meta = this.meta();
        meta.addEnchant(enchantment, level, true);
        return this.apply(meta);
    }
    public ItemBuilder removeEnchantments(Enchantment... enchantments) {
        ItemMeta meta = this.meta();
        Arrays.asList(enchantments).forEach(meta::removeEnchant);
        return this.apply(meta);
    }
    public ItemBuilder unEnchant() {
        ItemMeta meta = this.meta();
        meta.getEnchants().keySet().forEach(meta::removeEnchant);
        return this.apply(meta);
    }
    public ItemBuilder addItemFlags(ItemFlag... flags) {
        ItemMeta meta = this.meta();
        meta.addItemFlags(flags);
        return this.apply(meta);
    }
    public ItemBuilder removeItemFlags(ItemFlag... flags) {
        ItemMeta meta = this.meta();
        meta.removeItemFlags(flags);
        return this.apply(meta);
    }
    public ItemBuilder setUnbreakable(boolean state) {
        ItemMeta meta = this.meta();
        meta.spigot().setUnbreakable(state);
        this.apply(meta);
        return this.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
    }

    public ItemBuilder setSkullOwner(String owner) {
        SkullMeta meta = this.skullMeta();
        meta.setOwner(owner);
        return this.apply(meta);
    }
    public ItemBuilder setSkullOwningPlayer(Player player) {
        return this.setSkullOwner(player.getName());
    }
    public ItemBuilder setSkullTexture(String texture) {
        SkullMeta meta = this.skullMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);

        try {
            profile.getProperties().put("textures", new Property("textures", texture));
            Field f = meta.getClass().getDeclaredField("profile");
            f.setAccessible(true);
            f.set(meta, profile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this.apply(meta);
    }

    public ItemBuilder setArmorColor(Color color) {
        LeatherArmorMeta meta = this.leatherArmorMeta();
        meta.setColor(color);
        return this.apply(meta);
    }

    private ItemMeta meta() {
        return this.item.getItemMeta();
    }
    private SkullMeta skullMeta() {
        return (SkullMeta) this.meta();
    }
    private LeatherArmorMeta leatherArmorMeta() {
        return (LeatherArmorMeta) this.meta();
    }
    private ItemBuilder apply(ItemMeta meta) {
        this.item.setItemMeta(meta);
        return this;
    }
    public ItemStack build() {
        return this.item;
    }
}
