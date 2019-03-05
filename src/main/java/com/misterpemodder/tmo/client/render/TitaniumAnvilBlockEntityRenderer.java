package com.misterpemodder.tmo.client.render;

import com.misterpemodder.tmo.block.TitaniumAnvilBlock;
import com.misterpemodder.tmo.block.entity.TitaniumAnvilBlockEntity;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.item.ItemStack;

public class TitaniumAnvilBlockEntityRenderer
    extends BlockEntityRenderer<TitaniumAnvilBlockEntity> {
  public void render(TitaniumAnvilBlockEntity be, double x, double y, double z, float delta,
      int destroyStage) {
    ItemStack stack = be.getInvStack(0);
    // stack = new ItemStack(TmoItems.TITANIUM_HAMMER);
    if (!stack.isEmpty()) {
      GlStateManager.pushMatrix();
      GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.translatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
      GlStateManager.scalef(1.0F, -1.0F, -1.0F);

      BlockState state = be.getCachedState();
      float rotation = 0;
      if (state != null) {
        rotation = state.get(TitaniumAnvilBlock.FACING).asRotation();
      }

      GlStateManager.rotatef(rotation, 0.0F, 1.0F, 0.0F);
      GlStateManager.translatef(0, -0.52F, 0.1F);
      GlStateManager.scalef(0.7F, -0.7F, -0.7F);

      GlStateManager.rotatef(90F, 1.0F, 0.0F, 0.0F);
      GlStateManager.rotatef(180F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotatef(15F, 0.0F, 0.0F, 1.0F);

      MinecraftClient.getInstance().getItemRenderer().renderItem(stack,
          ModelTransformation.Type.FIXED);
      GlStateManager.popMatrix();
    }
  }
}
