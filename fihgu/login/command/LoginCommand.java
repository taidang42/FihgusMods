package login.command;

import java.util.Iterator;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.NetServerHandler;
import net.minecraft.network.packet.Packet16BlockItemSwitch;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.network.packet.Packet202PlayerAbilities;
import net.minecraft.network.packet.Packet3Chat;
import net.minecraft.network.packet.Packet41EntityEffect;
import net.minecraft.network.packet.Packet4UpdateTime;
import net.minecraft.network.packet.Packet6SpawnPosition;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.EnumGameType;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import core.elements.CommandBase;
import core.events.PlayerLoginEvent;
import core.functions.Language;
import core.shortcut.Server;
import cpw.mods.fml.common.network.FMLNetworkHandler;

public class LoginCommand extends CommandBase
{
	public LoginCommand()
	{
		name = "login";
		usage = "log you in!";
	}
	
	@Override
	public void processPlayer(EntityPlayerMP player, String[] args)
	{
		NetLoginHandler handler = login.CommonProxy.waitMap.get(player);
		
		if(handler != null)
		{
			
			
			login.CommonProxy.waitMap.remove(handler);
			INetworkManager par1INetworkManager = handler.myTCPConnection;
	    	EntityPlayerMP par2EntityPlayerMP = player;
        	//////////////////////////////////////////////////////////////////////////////

        	Server.getConfigurationManager().readPlayerDataFromFile(par2EntityPlayerMP);
            //par2EntityPlayerMP.setWorld(Server.getServer().worldServerForDimension(par2EntityPlayerMP.dimension));
            //par2EntityPlayerMP.theItemInWorldManager.setWorld((WorldServer)par2EntityPlayerMP.worldObj);
            String var3 = "local";

            if (par1INetworkManager.getSocketAddress() != null)
            {
                var3 = par1INetworkManager.getSocketAddress().toString();
            }
            Server.getConfigurationManager().logger.info(par2EntityPlayerMP.username + "[" + var3 + "] logged in with entity id " + par2EntityPlayerMP.entityId + " at (" + par2EntityPlayerMP.posX + ", " + par2EntityPlayerMP.posY + ", " + par2EntityPlayerMP.posZ + ")");
            WorldServer var4 = Server.getServer().worldServerForDimension(par2EntityPlayerMP.dimension);
            
            //remove unloaded player
            var4.getPlayerManager().removePlayer(par2EntityPlayerMP);
            //set to the right GameType
            //par2EntityPlayerMP.theItemInWorldManager.setGameType();
            par2EntityPlayerMP.theItemInWorldManager.initializeGameType(var4.getWorldInfo().getGameType());
            ////////////////////////////////////////////////////////////////////////////////////////////////////  
            NetServerHandler var6 = par2EntityPlayerMP.playerNetServerHandler;
            Server.getConfigurationManager().sendPacketToAllPlayers(new Packet3Chat("\u00a7e" + par2EntityPlayerMP.username + " joined the game."));
            Server.getConfigurationManager().playerLoggedIn(par2EntityPlayerMP);
            FMLNetworkHandler.handlePlayerLogin(par2EntityPlayerMP, var6, par1INetworkManager);
            
            
			player.sendChatToPlayer(Language.translate("You have been logged in."));
			login.CommonProxy.waitMap.remove(par2EntityPlayerMP);
		}
	}
}
