package com.metacube.noteprise.common.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;

@SuppressLint("ValidFragment")
public final class NotepriseFragment
{
	public String _tag = null;
	public Class<?> _class = null;
	public Bundle _args = null;
	public Fragment _fragment = null;
	
	public NotepriseFragment(String _tag, Class<?> _class) 
	{
		this._tag = _tag;
		this._class = _class;
	}
	
	public NotepriseFragment(String _tag, Class<?> _class, Bundle _args) 
	{
		this._tag = _tag;
		this._class = _class;
		this._args = _args;
	}
}
