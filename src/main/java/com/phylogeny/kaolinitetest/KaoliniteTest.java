package com.phylogeny.kaolinitetest;

import com.phylogeny.kaolinitetest.proxy.ProxyCommon;
import com.phylogeny.kaolinitetest.reference.Reference;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = Reference.MOD_ID, version = Reference.VERSION)
public class KaoliniteTest
{
	@SidedProxy(clientSide = Reference.CLIENT_CLASSPATH, serverSide = Reference.COMMON_CLASSPATH)
	public static ProxyCommon proxy;
	
	public static SimpleNetworkWrapper packetNetwork = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);
	
	@EventHandler
	public void preinit(FMLPreInitializationEvent event)
	{
		proxy.preInit();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init();
	}
	
}