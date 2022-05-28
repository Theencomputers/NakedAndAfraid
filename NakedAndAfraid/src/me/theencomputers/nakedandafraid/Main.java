/* 
	Title: NakedAndAfraid
	Author: Theencomputers
	Last Updated: 05-27-2022
	Version: 1.0.0
*/


package me.theencomputers.nakedandafraid;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener{
    public ProtocolManager pm;

    @Override
    public void onEnable() {
		getCommand("nakedandafraid").setExecutor(this);
    } 

	public void listenForPackets(){

            ProtocolLibrary.getProtocolManager().addPacketListener(
            	new PacketAdapter(this, PacketType.Play.Server.ENTITY_EQUIPMENT) {		//listten for the outgoing entity equipment packet

					@Override
					public void onPacketSending(PacketEvent event) {	
						PacketContainer packet = event.getPacket();
						Entity e = (Entity)packet.getEntityModifier(event).read(0);
						//ItemStack item = (ItemStack)packet.getItemModifier().read(0);		//left in so you can have fun
						int slot = packet.getIntegers().read(1);							//slot 0 is main hand and 1 - 4 is armor 5 is off hand for 1.9+
						if(e instanceof Player){											//only works for players so zombies with armour still work
							if(slot > 0 && slot < 5){										//if the slot is an armor slot
								event.setCancelled(true);									//cancel
							}
						}
					}
            });
	}
		public void stopListeningForPackets(){
			ProtocolLibrary.getProtocolManager().removePacketListeners(this);		//stop listening for packets
		}

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if(sender.hasPermission("uhc.staff")){
			if(args.length == 1){
				if(args[0].equalsIgnoreCase("enable") || args[0].equalsIgnoreCase("on")){
					stopListeningForPackets();
					listenForPackets();
					for (Player player : Bukkit.getOnlinePlayers())
						player.sendMessage("§a[Naked and Afraid]: Enabled");
				}
				else if(args[0].equalsIgnoreCase("disable") || args[0].equalsIgnoreCase("off")){
					stopListeningForPackets();
					for (Player player : Bukkit.getOnlinePlayers())
						player.sendMessage("§c[Naked and Afraid]: Disabled");
				}
				else{
					sender.sendMessage("§cError: Usage /nakedandafraid <enable|disable>");
				}
			}
			else{
				sender.sendMessage("§cError: Usage /nakedandafraid <enable|disable>");
			}
		}
		else{
			sender.sendMessage("§cYou do not have permission to use this command");
		}
			return true;
		}
}