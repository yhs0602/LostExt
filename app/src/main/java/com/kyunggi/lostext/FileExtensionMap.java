package com.kyunggi.lostext;

import java.io.*;

public class FileExtensionMap
{
	public String filename;
	public String hexSignature;
	public int offset;
	byte [] hex;
	public FileExtensionMap(String ext,String hexi,int off)
	{
		filename=ext;
		offset=off;
		hexSignature=hexi;
		hex=hexStringToByteArray(hexi);
	}
	
	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
				+ Character.digit(s.charAt(i+1), 16));
		}
		return data;
	}
	public boolean matches(File f)
	{
		byte [] buffer=new byte[hex.length];
		FileInputStream fis = null;
		try
		{
			fis=new FileInputStream(f);
			if(offset>0)
			{
				for(int i=0;i<offset;++i)
				{
					try
					{
						fis.read();
					}
					catch (IOException e)
					{
						throw new RuntimeException(e);
					}
				}
			}
			try
			{
				fis.read(buffer);
			}
			catch (IOException e)
			{}
			if (java.util.Arrays.equals(buffer, hex))
			{
				return true;
			}
		}
		catch (FileNotFoundException e)
		{}
		finally{
			if(fis!=null)
			{
				try
				{
					fis.close();
				}
				catch (IOException e)
				{}
			}
		}
		return false;
	}
}
