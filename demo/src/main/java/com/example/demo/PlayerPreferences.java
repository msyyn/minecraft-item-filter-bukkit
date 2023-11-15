package com.example.demo;

import org.bukkit.Material;

import java.util.HashSet;

public class PlayerPreferences {
  private HashSet<Material> selectedOres;
  private HashSet<Material> ignoredItems;

  public PlayerPreferences() {
    selectedOres = new HashSet<>();
    ignoredItems = new HashSet<>();
  }

  public HashSet<Material> getSelectedOres() {
    return selectedOres;
  }

  public HashSet<Material> getIgnoredItems() {
    return ignoredItems;
  }
}
