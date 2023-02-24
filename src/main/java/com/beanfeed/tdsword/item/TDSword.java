package com.beanfeed.tdsword.item;

import com.beanfeed.tdsword.TransDimensionalSword;
import com.beanfeed.tdsword.Utils;
import com.beanfeed.tdsword.screen.TDSscreenhandler;
import com.beanfeed.tdsword.sound.TDSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.List;

public class TDSword extends Item {
    public TDSword(Settings settings) {
        super(settings);
    }

    public final DefaultedList<ItemStack> itemHandler = DefaultedList.ofSize(4, ItemStack.EMPTY);
    private Vec3d lastWaypoint = null;
    private boolean isActivated = false;
    private int lapisSlot = 0;
    private int lapisAmount = 0;
    private int goldSlot = 1;
    private int goldAmount = 0;
    //protected final ContainerData data;
    private List<Vec3d> Waypoints;
    private float lastWaypointYRotation = 0.0f;
    private RegistryKey<World> lastDim = null;


    //Returns saved waypoint
    public Vec3d getLastWaypoint(ItemStack stack) {
        updateItemHandler(stack);
        ItemStack rune = itemHandler.get(2);
        NbtCompound nbt = rune.getOrCreateNbt();
        //TransDimensionalSword.LOGGER.info(String.valueOf(rune));
        if(!nbt.contains("waypoint")) return null;
        BlockPos pos = NbtHelper.toBlockPos(nbt.getCompound("waypoint"));
        return Utils.BlockPosToVec3(pos);
    }
    public float getLastWaypointRotation(ItemStack stack) {
        updateItemHandler(stack);
        ItemStack rune = itemHandler.get(2);
        NbtCompound nbt = rune.getOrCreateNbt();
        if(!nbt.contains("rotation")) return 0.0f;
        return nbt.getFloat("rotation");
    }
    public RegistryKey<World> getLastDimension(ItemStack stack) {
        updateItemHandler(stack);
        ItemStack rune = itemHandler.get(2);
        NbtCompound nbt = rune.getOrCreateNbt();
        if(!nbt.contains("dimension")) return null;
        Identifier keyLocation = Identifier.tryParse(nbt.getString("dimension"));
        return RegistryKey.of(Registry.WORLD_KEY, keyLocation);
    }
    public int getGoldAmount(ItemStack stack) {
        updateItemHandler(stack);
        return itemHandler.get(0).getCount();
    }
    public void setGoldAmount(ItemStack stack, int amount) {
        updateItemHandler(stack);
        itemHandler.get(0).setCount(amount);
        Inventories.writeNbt(stack.getOrCreateNbt(), itemHandler);
    }
    public boolean isActivated(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        if(!nbt.contains("active")) {
            nbt.putBoolean("active", false);
            return false;
        }
        return nbt.getBoolean("active");
    }
    private void updateActive(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        if(!nbt.contains("active")) {
            nbt.putBoolean("active", false);
            isActivated = false;
        }
        isActivated = nbt.getBoolean("active");
    }
    //public SimpleContainer inv = new SimpleContainer(2);
    private void setActivated(ItemStack stack, boolean value) {
        isActivated = value;
        stack.getOrCreateNbt().putBoolean("active", value);
    }
    @Override
    public TypedActionResult<ItemStack> use(World pLevel, PlayerEntity pPlayer, Hand pUsedHand) {
        if(pLevel.isClient()) return TypedActionResult.fail(pPlayer.getStackInHand(pUsedHand));
        //TransDimensionalSword.LOGGER.info(pUsedHand.toString());
        updateActive(pPlayer.getStackInHand(pUsedHand));
        if(!pPlayer.isSneaking())
        {
            /*
            if(pPlayer.getOffhandItem().is(Items.LAPIS_LAZULI)) {
                //If item is lapis
                pPlayer.getOffhandItem().shrink(1);
                if(inv.getItem(lapisSlot) == ItemStack.EMPTY) {
                    inv.setItem(lapisSlot, new ItemStack(Items.LAPIS_LAZULI, 1));
                }
            } else {
                //If item is gold ingot
                pPlayer.getOffhandItem().shrink(1);
                if(inv.getItem(goldSlot) == ItemStack.EMPTY) {
                    inv.setItem(goldSlot, new ItemStack(Items.GOLD_INGOT, 1));
                }
            }
            */
            ItemStack stack = pPlayer.getStackInHand(pUsedHand);
            NbtCompound nbt = stack.getOrCreateNbt();
            NbtCompound storedItemNBT = new NbtCompound();
            if(!nbt.contains("Items")) {
                nbt.put("Items", storedItemNBT);
            }
            Inventories.readNbt(nbt, itemHandler);
            if(!pPlayer.world.isClient()) {
                updateItemHandler(stack);
                SimpleInventory inv = new SimpleInventory(4);
                for(int i = 0; i < itemHandler.size(); i++) {
                    inv.setStack(i, itemHandler.get(i));
                }
                pPlayer.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, playerInventory, playerx) -> {
                    return new TDSscreenhandler(syncId, playerInventory, inv, playerInventory.player.getStackInHand(pUsedHand));
                }, Text.translatable("menu.title.tdsword.tdswordmenu")));
                //TransDimensionalSword.LOGGER.info("Open");
            }
        } else if(isActivated){
            updateItemHandler(pPlayer.getStackInHand(pUsedHand));
            ItemStack lapisStack = itemHandler.get(1);
            if(lapisStack.getCount() == 0) return TypedActionResult.fail(pPlayer.getStackInHand(pUsedHand));;
            ItemStack itemStack = itemHandler.get(2);
            //TransDimensionalSword.LOGGER.info(String.valueOf(Rune.getWaypointNBT(itemStack, pPlayer)));
            lapisStack.decrement(1);
            itemStack.writeNbt(Rune.getWaypointNBT(itemStack, pPlayer));

            itemStack.setCustomName(Text.translatable("item.tdsword.filled_rune"));
            NbtCompound nbt = pPlayer.getStackInHand(pUsedHand).getOrCreateNbt();
            Inventories.writeNbt(nbt, itemHandler);
            //var rotation = new Vec3(Math.round(tempLastWaypointRotation), 0, Math.round(tempLastWaypointRotation);

        }else {
            updateItemHandler(pPlayer.getStackInHand(pUsedHand));
            updateActive(pPlayer.getStackInHand(pUsedHand));
            ItemStack stack = itemHandler.get(3);
            //TransDimensionalSword.LOGGER.info("Check Activate");
            //TransDimensionalSword.LOGGER.info(String.valueOf(stack.getCount() == 0));
            if(stack.getCount() == 0) return TypedActionResult.fail(pPlayer.getStackInHand(pUsedHand));
            else {
                setActivated(pPlayer.getStackInHand(pUsedHand), true);
                itemHandler.set(3, ItemStack.EMPTY);
                NbtCompound nbt = pPlayer.getStackInHand(pUsedHand).getOrCreateNbt();
                Inventories.writeNbt(nbt, itemHandler);
                pPlayer.world.playSound(null, Utils.Vec3ToBlockPos(pPlayer.getPos()), new SoundEvent(TDSounds.TD_IGNITE.getId()), SoundCategory.PLAYERS, 1f, 1f);
                //TransDimensionalSword.LOGGER.info(String.valueOf(isActivated(stack)));
            }

        }
        return TypedActionResult.pass(pPlayer.getStackInHand(pUsedHand));

    }
    private void updateItemHandler(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        Inventories.readNbt(nbt, itemHandler);
        //TransDimensionalSword.LOGGER.info(String.valueOf(nbt.getList("Items", 10)));
    }

}
