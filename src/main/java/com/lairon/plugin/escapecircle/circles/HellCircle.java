package com.lairon.plugin.escapecircle.circles;

import com.lairon.plugin.escapecircle.registered.Circle;
import com.lairon.plugin.escapecircle.registered.CircleRegistered;
import com.lairon.plugin.escapecircle.utils.EffectUtils;
import com.lairon.plugin.escapecircle.utils.ItemStackUtils;
import com.lairon.plugin.escapecircle.utils.LocationUtils;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Giant;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class HellCircle extends Circle {

    private BossBar cristalBar;
    private Location bossLocation = new Location(Bukkit.getWorld("world"), -185.64, 138.00, -188.75);
    private Giant giant;
    private boolean isStarted = false;
    private Location chestLocation = new Location(Bukkit.getWorld("world"), -227.5, 156.5, -188.5);
    private boolean isWin = false;
    private Location[] lines = new Location[]{
            new Location(Bukkit.getWorld("world"), -283.32, 139.00, -200.52),
            new Location(Bukkit.getWorld("world"), -283.22, 139.00, -197.32),
            new Location(Bukkit.getWorld("world"), -282.83, 139.00, -194.53),
            new Location(Bukkit.getWorld("world"), -283.06, 139.00, -191.14),
            new Location(Bukkit.getWorld("world"), -282.66, 139.00, -188.85),
            new Location(Bukkit.getWorld("world"), -282.57, 139.00, -183.05),
            new Location(Bukkit.getWorld("world"), -282.57, 139.00, -179.74),
            new Location(Bukkit.getWorld("world"), -281.68, 139.00, -182.58),
            new Location(Bukkit.getWorld("world"), -281.68, 139.00, -182.58),
            new Location(Bukkit.getWorld("world"), -281.20, 139.06, -188.91),
    };

    private Location[] expls = new Location[]{
            new Location(Bukkit.getWorld("world"), -278.39, 139.00, -186.04),
            new Location(Bukkit.getWorld("world"), -279.45, 139.00, -192.35),
            new Location(Bukkit.getWorld("world"), -276.74, 139.06, -188.60),
            new Location(Bukkit.getWorld("world"), -282.33, 139.00, -188.78),
            new Location(Bukkit.getWorld("world"), -277.52, 139.06, -189.42),
    };
    private ArrayList<Player> god = new ArrayList<>();

    public HellCircle(Plugin main, CircleRegistered circleRegistered) {
        super(main, circleRegistered);
    }

    @Override
    public void start(Player player) {
        isStarted = true;
        getCircleRegistered().getCircle(BossBarCircle.class).start(player);
        getCircleRegistered().getCircle(BossBarCircle.class).setProgress(1);
        giant = bossLocation.getWorld().spawn(bossLocation, Giant.class);

        giant.setPersistent(false);
        giant.setRemoveWhenFarAway(false);

        Bukkit.dispatchCommand(player, "scale set 2.3 @e[type=minecraft:giant]");

        cristalBar = Bukkit.createBossBar("§eПопади в 4 кристалла", BarColor.RED, BarStyle.SOLID);
        cristalBar.setProgress(0);
        cristalBar.addPlayer(player);
        cristalBar.setVisible(true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30, 255));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 30, 1));

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!isStarted) {
                    this.cancel();
                    return;
                }

                if (giant.isDead()) {
                    this.cancel();
                    return;
                }

                if (isWin) {
                    this.cancel();
                    return;
                }
                SmallFireball fireball = giant.launchProjectile(SmallFireball.class);
                fireball.setMetadata("giantfireball", new FixedMetadataValue(getMain(), true));
                fireball.setDirection(giant.getLocation().getDirection());
            }
        }.runTaskTimer(getMain(), 5, 5);
        new BukkitRunnable() {

            private int pos = 0;

            @Override
            public void run() {
                if (!isStarted) {
                    this.cancel();
                    return;
                }

                if (giant.isDead()) {
                    this.cancel();
                    return;
                }
                if (isWin) {
                    this.cancel();
                    return;
                }

                if (pos > vectors.size() - 1) pos = 0;
                Vector vector = vectors.get(pos);
                pos++;
                Location location = giant.getLocation().clone().setDirection(vector);
                giant.teleport(location);

            }
        }.runTaskTimer(getMain(), 1, 1);


        Chest chest = (Chest) chestLocation.getBlock().getState();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (isWin) {
                    this.cancel();
                    return;
                }

                if (!chest.getInventory().contains(Material.PLAYER_HEAD, 4)) return;
                this.cancel();
                chestLocation.getBlock().breakNaturally();
                for (int i = 0; i < 30; i++) {
                    Bukkit.getScheduler().runTaskLater(getMain(), () -> chestLocation.getWorld().spawnParticle(Particle.LAVA, chestLocation, 1), i);

                }
                ItemStack bow = new ItemStack(Material.BOW);
                bow.setDurability((short) 377);
                ItemStackUtils.addEnchantment(bow, Enchantment.ARROW_INFINITE, 1);
                ItemStackUtils.giveItem(player, bow);
                ItemStackUtils.giveItem(player, new ItemStack(Material.ARROW));
                player.sendActionBar("§cИспользуйте лук в вашем инвентаре, чтобы сломать в эндер кристаллы");
                return;
            }
        }.runTaskTimer(getMain(), 1, 1);

    }

    @EventHandler
    public void onCristal(ProjectileHitEvent e) {
        if (!isStarted) return;
        if (e.getHitEntity() == null) return;
        if (e.getHitEntity().getType() != EntityType.ENDER_CRYSTAL) return;
        double progress = cristalBar.getProgress();
        if (progress == 0.75f) {
            Player player = cristalBar.getPlayers().get(0);
            isWin = true;
            cristalBar.setProgress(1);
            cristalBar.removeAll();
            cristalBar.setVisible(false);

            giant.setGliding(true);
            Bukkit.getScheduler().runTaskLater(getMain(), ()->{

                for (Location line : lines) {
                    EffectUtils.drawParticleLine(giant.getLocation(), line, 1, Particle.EXPLOSION_LARGE);
                }
                god.add(player);

                Bukkit.getScheduler().runTaskLater(getMain(), ()->{
                    for (Location expl : expls) {
                        expl.getWorld().createExplosion(expl, 10);
                        LocationUtils.blockRadiusSphere(expl, 3).forEach(b -> b.breakNaturally());
                    }
                    god.remove(player);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if(player.getLocation().getY() > 112) return;
                            this.cancel();
                            isStarted = false;
                            getCircleRegistered().getCircle(AmogusCircle.class).start(player);
                            return;
                        }
                    }.runTaskTimer(getMain(), 1, 1);


                }, 10);


            }, 200);

        } else {
            cristalBar.setProgress(progress + 0.25f);
        }

    }

    @EventHandler
    public void onGod(EntityDamageEvent e){
        if(e.getEntity().getType() != EntityType.PLAYER) return;
        Player player = (Player) e.getEntity();

        if(god.contains(player)) e.setCancelled(true);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if (!e.getEntity().hasMetadata("giantfireball")) return;
        e.setCancelled(true);
        e.getEntity().remove();
        e.getEntity().getWorld().spawnParticle(Particle.EXPLOSION_HUGE, e.getEntity().getLocation(), 2);
        e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1, 1);
        if (e.getHitEntity() != null) {
            if (e.getHitEntity().getType() == EntityType.PLAYER) {
                ((Player) e.getHitEntity()).damage(1);
            }
        }
    }

    @Override
    public void onDisable() {

        if (giant != null) giant.remove();
    }


    private ArrayList<Vector> vectors = new ArrayList<Vector>() {{
        add(new Vector(-0.9956048797380385, -0.09363295180783218, -0.0019477621924005042));
        add(new Vector(-0.9956048797380385, -0.09363295180783218, -0.0019477621924005042));
        add(new Vector(-0.9956048797380385, -0.09363295180783218, -0.0019477621924005042));
        add(new Vector(-0.9946726588929541, -0.10306558579098518, -0.001945938432238239));
        add(new Vector(-0.9943972136889061, -0.10568415475867236, 0.0022451816596680465));
        add(new Vector(-0.9943715899911206, -0.10568415475867236, 0.0074833449389705604));
        add(new Vector(-0.9940858114683525, -0.10830199107875535, 0.008004883864511775));
        add(new Vector(-0.99345868492073, -0.11301219225798265, 0.01637332454385807));
        add(new Vector(-0.9922754228720249, -0.11405856263794083, 0.04878657041759802));
        add(new Vector(-0.9901111924747791, -0.11405856263794083, 0.08167295038834903));
        add(new Vector(-0.9898033000008908, -0.11405856263794083, 0.08532333559064337));
        add(new Vector(-0.9892069064150797, -0.11353539318948516, 0.092625108874521));
        add(new Vector(-0.9873713083752026, -0.10777848329772319, 0.11611071412872677));
        add(new Vector(-0.983022403143136, -0.10201795712869345, 0.15250997128706636));
        add(new Vector(-0.9815687851080231, -0.09730224460798409, 0.16448402139967092));
        add(new Vector(-0.9814154388032227, -0.08943791938223165, 0.1697780758969047));
        add(new Vector(-0.9805290788652364, -0.08261763946663007, 0.17814895775348474));
        add(new Vector(-0.9794065665404807, -0.07736861896549595, 0.18648558715557295));
        add(new Vector(-0.978225236089548, -0.0757934878368539, 0.1932116318420624));
        add(new Vector(-0.978277758004672, -0.07369301943571206, 0.19375749554481805));
        add(new Vector(-0.9781233216484887, -0.0757934878368539, 0.19372690791124436));
        add(new Vector(-0.9768094111316822, -0.08209283810851327, 0.19774766814267591));
        add(new Vector(-0.9724954502877138, -0.08943791938223165, 0.21506617062261132));
        add(new Vector(-0.9671815616090577, -0.09572984953374594, 0.23536274724711898));
        add(new Vector(-0.965525719424507, -0.10620778121530586, 0.23765519632255017));
        add(new Vector(-0.9638086132391326, -0.1151048065054266, 0.2404658823313787));
        add(new Vector(-0.9624531614160764, -0.12451511681935229, 0.24120509481282304));
        add(new Vector(-0.961564080526834, -0.13547961389119836, 0.23883005099960966));
        add(new Vector(-0.9617631011507254, -0.14486462696195562, 0.23243488791511693));
        add(new Vector(-0.963983228226157, -0.15007294862474335, 0.21957788092098865));
        add(new Vector(-0.967793496760793, -0.15267555792647872, 0.20016473625330616));
        add(new Vector(-0.9711531143203893, -0.15683752380932026, 0.17962076625816337));
        add(new Vector(-0.9755440783881493, -0.15735757535145414, 0.1534677314660249));
        add(new Vector(-0.9801668305885468, -0.15527710881617499, 0.12313408826029029));
        add(new Vector(-0.9821068449391464, -0.15735757535145414, 0.103463706695569));
        add(new Vector(-0.9850383725501223, -0.15891746759218875, 0.06666815655087668));
        add(new Vector(-0.9856931333082201, -0.16203605836291823, 0.04640433965948165));
        add(new Vector(-0.9862525480312379, -0.16463364969288954, 0.014201158075655735));
        add(new Vector(-0.9860351519874584, -0.1661916578139304, -0.010724360965919673));
        add(new Vector(-0.9859355545399396, -0.1615164047241805, -0.04293638665510167));
        add(new Vector(-0.9849270771648374, -0.15527710881617499, -0.07620808451352211));
        add(new Vector(-0.9836283270747478, -0.1469484457629735, -0.1043142773736146));
        add(new Vector(-0.9812469900905908, -0.13860935118266796, -0.13394697534052716));
        add(new Vector(-0.9800982985880856, -0.13287048014987943, -0.14748817108324397));
        add(new Vector(-0.9796095600288117, -0.12869394457882324, -0.15428213937102572));
        add(new Vector(-0.979076215439977, -0.12503759988272764, -0.16054707401662172));
        add(new Vector(-0.9786149764488808, -0.12033409580578946, -0.16683055252728543));
        add(new Vector(-0.9785131733600981, -0.11667394082279282, -0.16999753261041722));
        add(new Vector(-0.9799269155803636, -0.11353540145805067, -0.16386870578885473));
        add(new Vector(-0.9824717194997625, -0.11353540145805067, -0.1478480064084029));
        add(new Vector(-0.9852615925562792, -0.11562788890089311, -0.12605469266071465));
        add(new Vector(-0.9880026577687586, -0.11719691832622126, -0.1005764911731672));
        add(new Vector(-0.9899647436687065, -0.11771986333127214, -0.07817825829614496));
        add(new Vector(-0.9916262974618539, -0.11928850190609293, -0.04947261358666067));
        add(new Vector(-0.9923066689625695, -0.11981131546761248, -0.031188514205230344));
        add(new Vector(-0.9927956966986101, -0.11981131546761248, -0.001397606039436652));
        add(new Vector(-0.9922620752007059, -0.11981131546761248, 0.03257641484738478));
        add(new Vector(-0.9912328249266154, -0.11562788890089311, 0.06393495207102774));
        add(new Vector(-0.9898761184456051, -0.10882547710089914, 0.09111687914335147));
        add(new Vector(-0.9897692030757009, -0.09625401624830847, 0.10531898688727574));
        add(new Vector(-0.9901769735688951, -0.08943792767125729, 0.10747287149701806));
        add(new Vector(-0.9914953032855743, -0.08786437729916963, 0.09600476428009368));
        add(new Vector(-0.9926117578239964, -0.09048683749062532, 0.0808333499893533));
        add(new Vector(-0.9940691067368117, -0.09520567284187498, 0.05255940344223105));
        add(new Vector(-0.9948929376501962, -0.09835038160687265, 0.02269900992434395));
        add(new Vector(-0.9947277082900797, -0.10254179395562307, -0.0014024451249714002));
        add(new Vector(-0.9943884105047697, -0.10254179395562307, -0.026016716667562202));
        add(new Vector(-0.9933130552087631, -0.11248896825807146, -0.02598858157089614));
        add(new Vector(-0.9902055043230464, -0.11458170872584196, -0.07977525452048016));
        add(new Vector(-0.9881924935228394, -0.11510481477248907, -0.1011260468984314));
        add(new Vector(-0.9862209172260357, -0.11458170872584196, -0.11932868243345054));
        add(new Vector(-0.98425089060772, -0.11039572870445531, -0.1380687054394381));
        add(new Vector(-0.9826390329736351, -0.10620778949061253, -0.15211980912477155));
        add(new Vector(-0.9821505181256429, -0.10306559406904328, -0.1573462521470609));
        add(new Vector(-0.9821963464882181, -0.10097022358907974, -0.15841512204826763));
        add(new Vector(-0.9827076417710803, -0.0957298578179026, -0.1584975871326949));
        add(new Vector(-0.9839097152570493, -0.08891343548752594, -0.15497120123622307));
        add(new Vector(-0.985696888486166, -0.08996239505410152, -0.14250758402562555));
        add(new Vector(-0.9859575666593285, -0.10097022358907974, -0.13301387407179327));
        add(new Vector(-0.9855076286809771, -0.1140585709060076, -0.1255601696975458));
        add(new Vector(-0.9830585792832492, -0.12869394457882324, -0.13051321130959218));
        add(new Vector(-0.9785032912587841, -0.13704465766917573, -0.15411057977979808));
        add(new Vector(-0.9735521034094876, -0.14173769653413984, -0.17918350183027013));
        add(new Vector(-0.9669153980100323, -0.13965228825278378, -0.2134756460977741));
        add(new Vector(-0.963352555931279, -0.1260824453316549, -0.23675951925923133));
        add(new Vector(-0.9637158200744419, -0.11667394082279282, -0.24008125639275837));
        add(new Vector(-0.9672009436891857, -0.11196570479622217, -0.22800441986118095));
        add(new Vector(-0.9731760511256393, -0.11301220052704457, -0.2003911576081697));
        add(new Vector(-0.9790211655324134, -0.11458170872584196, -0.16848913752827263));
        add(new Vector(-0.9832212652929592, -0.11562788890089311, -0.1410891022865874));
        add(new Vector(-0.9856583002961725, -0.11615093096600705, -0.12242008124891103));
        add(new Vector(-0.9886002026776174, -0.11562788890089311, -0.09643562917354018));
        add(new Vector(-0.9903317116008639, -0.11562788890089311, -0.0766374080069654));
        add(new Vector(-0.9917519352545029, -0.11615093096600705, -0.05419465060942938));
        add(new Vector(-0.9926094003329218, -0.11876565526620557, -0.024925840004025052));
        add(new Vector(-0.9926059274978485, -0.12137955623226293, 6.899456464106354E-4));
        add(new Vector(-0.9921667636415864, -0.12347007202723824, 0.01898036982171342));
        add(new Vector(-0.9914692009477545, -0.12660481568508533, 0.030984580316132056));
        add(new Vector(-0.9903230741127996, -0.12764945092562058, 0.054459402844340324));
        add(new Vector(-0.9889621260962573, -0.12503759988272764, 0.07949535686275806));
        add(new Vector(-0.9867910254915135, -0.11719691832622126, 0.11184075439768863));
        add(new Vector(-0.9838039919671236, -0.1035893656026626, 0.14628379515034992));
        add(new Vector(-0.9806668974147846, -0.09468146146558067, 0.17125378001551375));
        add(new Vector(-0.9822481743874549, -0.08996239505410152, 0.16460647432176492));
        add(new Vector(-0.9846409959706763, -0.08733981158544557, 0.15117495283972612));
        add(new Vector(-0.987190487391026, -0.09048683749062532, 0.13140423830917272));
        add(new Vector(-0.990304922792521, -0.09520567284187498, 0.10115354542291119));
        add(new Vector(-0.9920259938491786, -0.09887440512910353, 0.07815548309565697));
        add(new Vector(-0.9928606327779091, -0.10097022358907974, 0.06350415599172277));
        add(new Vector(-0.9927895142578497, -0.10725495390439034, 0.05352901309224681));
        add(new Vector(-0.9929994873239142, -0.11353540145805067, 0.03258421075003578));
        add(new Vector(-0.9931583672821349, -0.11667394082279282, 0.004863026872567167));
        add(new Vector(-0.9930702651766671, -0.11719691832622126, -0.008736747494403068));
        add(new Vector(-0.9934304086679788, -0.11144241028659624, -0.026011772782463683));
        add(new Vector(-0.9936094030282859, -0.10568416303444335, -0.03963851532893621));
        add(new Vector(-0.9940514086219929, -0.09992236977547492, -0.04332801674534611));
        add(new Vector(-0.9947426249733348, -0.09625401624830847, -0.034961041420840805));
        add(new Vector(-0.9953284524790091, -0.09310867038875734, -0.02553521459041689));
        add(new Vector(-0.9957847601516043, -0.0883889186483477, -0.02449715309964797));
        add(new Vector(-0.9961761848680897, -0.08471662263999082, -0.021356557544021233));
        add(new Vector(-0.9965013573531865, -0.08156802228092853, -0.018212702557146406));
        add(new Vector(-0.9967805490377093, -0.07894356530366456, -0.014016082092685156));
        add(new Vector(-0.9968450307997322, -0.07894356530366456, -0.008240028338959251));
        add(new Vector(-0.9968450307997322, -0.07894356530366456, -0.008240028338959251));
        add(new Vector(-0.996866095457463, -0.07894356530366456, -0.0050893245660238295));
        add(new Vector(-0.9968780846625183, -0.07894356530366456, -0.0014134414509049647));
        add(new Vector(-0.9968783496789085, -0.07894356530366456, 0.0012122040264342313));
        add(new Vector(-0.9968735824641785, -0.07894356530366456, 0.003312715227873699));
        add(new Vector(-0.9968695391202084, -0.07894356530366456, 0.004362965895990798));
        add(new Vector(-0.9968695391202084, -0.07894356530366456, 0.004362965895990798));
        add(new Vector(-0.9968695391202084, -0.07894356530366456, 0.004362965895990798));
        add(new Vector(-0.9968695391202084, -0.07894356530366456, 0.004362965895990798));
        add(new Vector(-0.9968695391202084, -0.07894356530366456, 0.004362965895990798));
        add(new Vector(-0.9968751892026522, -0.07894356530366456, 0.00278758844208831));
        add(new Vector(-0.9968751892026522, -0.07894356530366456, 0.00278758844208831));
        add(new Vector(-0.9968358637541433, -0.0794685010630114, 0.0017372600061760564));
        add(new Vector(-0.9951503376692379, -0.09835038160687265, 0.0017343225144954852));
    }};
}
