package com.github.ustc_zzzz.fmltutor.block;

import com.github.ustc_zzzz.fmltutor.FMLTutor;
import com.google.common.base.Function;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLoader
{
    public static Block grassBlock = new BlockGrassBlock();
    public static Block fluidMercury = new BlockFluidMercury();
    public static Block metalFurnace = new BlockMetalFurnace();

    public BlockLoader(FMLPreInitializationEvent event)
    {
        register(grassBlock, "grass_block");
        register(fluidMercury, "fluid_mercury");
        register(metalFurnace, new ItemMultiTexture(metalFurnace, metalFurnace, new Function<ItemStack, String>()
        {
            @Override
            public String apply(ItemStack input)
            {
                return BlockMetalFurnace.EnumMaterial.values()[input.getMetadata() >> 3].getName();
            }
        }), "metal_furnace");
    }

    @SideOnly(Side.CLIENT)
    public static void registerRenders()
    {
        OBJLoader.instance.addDomain(FMLTutor.MODID);

        registerStateMapper(metalFurnace, new StateMap.Builder().withName(BlockMetalFurnace.MATERIAL)
                .withSuffix("_furnace").ignore(BlockMetalFurnace.FACING).build());

        registerRender(grassBlock);
        registerRender(metalFurnace, 0, "iron_furnace");
        registerRender(metalFurnace, 8, "gold_furnace");
    }

    private static void register(Block block, ItemBlock itemBlock, String name)
    {
        GameRegistry.registerBlock(block, null, name);
        GameRegistry.registerItem(itemBlock, name);
        GameData.getBlockItemMap().put(block, itemBlock);
    }

    private static void register(Block block, String name)
    {
        GameRegistry.registerBlock(block, name);
    }

    @SideOnly(Side.CLIENT)
    private static void registerStateMapper(Block block, IStateMapper mapper)
    {
        ModelLoader.setCustomStateMapper(block, mapper);
    }

    @SideOnly(Side.CLIENT)
    private static void registerRender(Block block, int meta, String name)
    {
        Item item = Item.getItemFromBlock(block);
        ResourceLocation location = new ResourceLocation(FMLTutor.MODID, name);
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(location, "inventory"));
        ModelLoader.registerItemVariants(item, location);
    }

    @SideOnly(Side.CLIENT)
    private static void registerRender(Block block)
    {
        registerRender(block, 0, GameData.getBlockRegistry().getNameForObject(block).getResourcePath());
    }
}
