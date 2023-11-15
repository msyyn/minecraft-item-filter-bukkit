package com.example.demo;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import java.util.HashSet;

public class BlockPickupGUI {

  public void openInventory(Player player, HashSet<Material> selectedOres) {
    if (selectedOres == null) {
      selectedOres = new HashSet<>();
    }
    Inventory inv = Bukkit.createInventory(null, 9, ChatColor.GREEN + "Select Blocks");

    addItem(inv, Material.GOLD_ORE, Material.GOLD_INGOT, selectedOres);
    addItem(inv, Material.IRON_ORE, Material.IRON_INGOT, selectedOres);
    addItem(inv, Material.DIAMOND_ORE, Material.DIAMOND, selectedOres);

    player.openInventory(inv);
  }

  private void addItem(Inventory inv, Material ore, Material smelted, HashSet<Material> selectedOres) {

    if (selectedOres.contains(ore)) {
      inv.addItem(new ItemStack(smelted));
    } else {
      inv.addItem(new ItemStack(ore));
    }
  }
}
