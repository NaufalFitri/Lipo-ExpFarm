<div align="center">

![GitHub](https://img.shields.io/github/license/NaufalFitri/Lipo-ExpFarm?style=flat-square)
[![Join us on Discord](https://img.shields.io/discord/1040175857462943788.svg?label=&logo=discord&logoColor=ffffff&color=7389D8&labelColor=6A7EC2&style=flat-square)](https://discord.com/invite/4DtDBGtST4)

</div>

<h2 align="center">
  <img src="lipomc.png" alt="LipoMC Logo" width="400">
  <br>
    <br>
  A Plugin Developed by LipoTeam
    <br>
    <h4 align="center">
    This plugin is licensed under the GNU General Public License v3.0. See the LICENSE file for details.
    </h4>
<br>
</h2>

**Compatible Minecraft Server Versions**

- Our plugin has been tested on Minecraft version 1.21.1, but it should be compatible with server versions up to 1.21.3.

---

## Offers

Our ExpFarm plugin offers a various configuration options for server owners
to customize the configuration file to their preferences.

**Configuration Options Include**

- Enable or Disable Multiplier of the specified mobs killed.
- Customize the Multiplier characteristics.
- Add or Remove mobs to be affected by ExpFarm plugin.
- Disable Damage taken in specified worlds.
- Customize the spawner spawn time.
- Enable or Disable particle to indicate radius of spawner to activate.
- Customize the particle characteristics.
- Customize the spawner radius
- Customize the radius type (circle or square).
- Customize the mobs characteristics
---
## Configuration
<details>
<summary><b>Default</b></summary>

```
# prefix for the plugin
prefix: '&8[&9&l!&8] &9&lExpFarm &8»&f'

ExpFarm:

  multiplier:

    # Enable or Disable to gain multiplier in the world specified and mobs specified
    enabled: true

    # Percentage to get multiplier by killing mobs specified
    percentage: 50

    # Range of multiplier can be gained, set both to same if you want to make fixed multiplier
    first-range: 2
    last-range: 4

  # Specified entity to gain multiplier, mob options and spawners (can not be specified but other event is not affecting those)
  entity-type:
    - ZOMBIE
    - SKELETON
    - CREEPER
    - ENDERMAN

  # List of spawners available in the server
  spawners: []

  # Set if the specified world can make player take damage
  damage-taken: true

  # Specified world to activate expfarm features
  worlds:
    - world

  # The options for the spawners
  spawner-options:

    # Set the spawn time in seconds, need to restart the server
    spawn-time: 5

    # y-offset of text-display spawned if admin make one
    y-offset: 1

    # particle spawned for displaying the radius of the spawner to activate
    particle:

      # Enable or Disable particle effects
      enabled: true

      # Type of particle to be used, you can check here https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Particle.html
      name: SMOKE

      # The quantity of particle spawned
      quantity: 1

      # The speed of particle spawned
      speed: 0

    # The radius for the spawner to activate
    radius: 5

    # Type of radius, can be square and circle only (if others, it will be set to default = square)
    radius-type: square

    # Randomize spawn location of the spawner
    random-range-spawn:

      # Because the location can be substract and added, so added -negative to make it substract
      first: -2
      last: 2

    # Mobs characteristics
    mobs:

      # Only for creeper
      creeper:

        # Time of ticks to make the creeper explodes, to make them freeze
        fuse-ticks-multiplier: 10

        # The explosion power creeper have
        explosion: 0

      # Option to make the mobs specified have tools or not
      no-tools: true

      # Option to make the mobs in world specified have drops or not
      no-drops: true
```

</details>


