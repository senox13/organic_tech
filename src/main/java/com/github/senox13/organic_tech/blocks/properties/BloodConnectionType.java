package com.github.senox13.organic_tech.blocks.properties;

import net.minecraft.util.IStringSerializable;

public enum BloodConnectionType implements IStringSerializable{
	/*
	 * Enum values
	 */
	NONE,
	VEIN,
	ARTERY;
	
	
	/*
	 * Interface methods
	 */
	@Override
	public String getString(){
		return toString().toLowerCase();
	}
}
