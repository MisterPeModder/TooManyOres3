package misterpemodder.tmo.main.tileentity;

import misterpemodder.hc.main.tileentity.TileEntityCustomChest;
import misterpemodder.hc.main.utils.StringUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class TileEntityTitaniumChest extends TileEntityCustomChest {

    @Override
    public int getInventorySize() {
    	return 66;
    }
    
	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentString(TextFormatting.getTextWithoutFormattingCodes(StringUtils.translate("tile.blockTitaniumChest.name")));
	}
    
}
