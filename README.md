# GlitchInventoryAPI [![CodeFactor](https://www.codefactor.io/repository/github/gliczdev/glitchinventoryapi/badge)](#)

<div align="center">
<a href="https://discord.gg/ZRuaXh3P63"><img alt="discord-plural" height="40" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/social/discord-plural_46h.png"></a>
<a href="#"><img alt="paper" height="40" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/supported/paper_46h.png"></a>
</div>

## Supported versions

This API supports all versions >= 1.17.1!

## Adding dependency

<details><summary>Maven</summary>
<p>

### Repository

#### Releases

```xaml
<repository>
  <id>roxymc-releases</id>
  <name>RoxyMC Repository</name>
  <url>https://repo.roxymc.net/releases</url>
</repository>
```

#### Snapshots

```xaml
<repository>
  <id>roxymc-snapshots</id>
  <name>RoxyMC Repository</name>
  <url>https://repo.roxymc.net/snapshots</url>
</repository>
```

### Dependency

```xaml
<dependency>
  <groupId>me.glicz</groupId>
  <artifactId>glitchinventoryapi-api</artifactId>
  <version>VERSION</version>
</dependency>
```

</p>
</details>
<details><summary>Gradle</summary>
<p>

### Repository

#### Releases

```kts
maven {
    name = "roxymcReleases"
    url = uri("https://repo.roxymc.net/releases")
}

```

#### Snapshots

```kts
maven {
    name = "roxymcSnapshots"
    url = uri("https://repo.roxymc.net/snapshots")
}
```

### Dependency

```kts
dependencies {
    implementation("me.glicz:glitchinventoryapi-api:VERSION")
}
```

</p>
</details>

## Shading

If you want to shade this API into your plugin, you have to load it in `onEnable()`

```java
@Override
public void onEnable() {
    GlitchInventoryAPI.init(new GlitchInventoryAPIImpl(GlitchInventoryAPIConfig.create(this)));
}
```

## Usage

### Basic example

```java
GlitchInventory.simple(3) // Create simple inventory with 3 rows (Bukkit InventoryType can be also used)
    .title(Component.text("GlitchInventoryAPI")) // Set title to GlitchInventoryAPI
    .item(10, ItemBuilder.itemBuilder(Material.STONE).asGuiItem(e -> {
        e.player().sendMessage("Simple GUI created with GlitchInventoryAPI!")
    })) // Set slot to stone item with click action
    .open(player); // Open inventory to player
```
