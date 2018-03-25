package com.kyunggi.lostext;

public class FileExtensionMap
{
	public String filename;
	public String hexSignature;
	public int offset;
	byte [] hex;
	public FileExtensionMap(String ext,String hex,int off)
	{
		filename=ext;
		offset=off;
		hexSignature=hex;
	}
}