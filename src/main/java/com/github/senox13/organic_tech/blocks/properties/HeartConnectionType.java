package com.github.senox13.organic_tech.blocks.properties;

import net.minecraft.util.IStringSerializable;

public enum HeartConnectionType implements IStringSerializable{
	/*
	 * Enum values
	 */
	NONE,
	VEIN,
	ARTERY,
	FURNACE;
	
	
	/*
	 * Interface methods
	 */
	@Override
	public String getString(){
		return toString().toLowerCase();
	}
}
