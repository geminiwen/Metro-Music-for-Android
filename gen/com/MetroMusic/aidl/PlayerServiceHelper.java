/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/Coffee/Code/Eclipseworkspace/Metro-Music-for-Android/src/com/MetroMusic/aidl/PlayerServiceHelper.aidl
 */
package com.MetroMusic.aidl;
public interface PlayerServiceHelper extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.MetroMusic.aidl.PlayerServiceHelper
{
private static final java.lang.String DESCRIPTOR = "com.MetroMusic.aidl.PlayerServiceHelper";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.MetroMusic.aidl.PlayerServiceHelper interface,
 * generating a proxy if needed.
 */
public static com.MetroMusic.aidl.PlayerServiceHelper asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.MetroMusic.aidl.PlayerServiceHelper))) {
return ((com.MetroMusic.aidl.PlayerServiceHelper)iin);
}
return new com.MetroMusic.aidl.PlayerServiceHelper.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_setDataHelper:
{
data.enforceInterface(DESCRIPTOR);
com.MetroMusic.aidl.DataHelper _arg0;
_arg0 = com.MetroMusic.aidl.DataHelper.Stub.asInterface(data.readStrongBinder());
this.setDataHelper(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_setPlayerUIHelper:
{
data.enforceInterface(DESCRIPTOR);
com.MetroMusic.aidl.PlayerUIHelper _arg0;
_arg0 = com.MetroMusic.aidl.PlayerUIHelper.Stub.asInterface(data.readStrongBinder());
this.setPlayerUIHelper(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_playSong:
{
data.enforceInterface(DESCRIPTOR);
com.MetroMusic.data.Song _arg0;
if ((0!=data.readInt())) {
_arg0 = com.MetroMusic.data.Song.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.playSong(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_stopSong:
{
data.enforceInterface(DESCRIPTOR);
this.stopSong();
reply.writeNoException();
return true;
}
case TRANSACTION_toogleSong:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.toogleSong(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_songIsPlaying:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.songIsPlaying();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.MetroMusic.aidl.PlayerServiceHelper
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
public void setDataHelper(com.MetroMusic.aidl.DataHelper helper) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((helper!=null))?(helper.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_setDataHelper, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void setPlayerUIHelper(com.MetroMusic.aidl.PlayerUIHelper helper) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((helper!=null))?(helper.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_setPlayerUIHelper, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void playSong(com.MetroMusic.data.Song song) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((song!=null)) {
_data.writeInt(1);
song.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_playSong, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void stopSong() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_stopSong, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void toogleSong(int toogle) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(toogle);
mRemote.transact(Stub.TRANSACTION_toogleSong, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public boolean songIsPlaying() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_songIsPlaying, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_setDataHelper = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_setPlayerUIHelper = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_playSong = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_stopSong = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_toogleSong = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_songIsPlaying = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
}
public void setDataHelper(com.MetroMusic.aidl.DataHelper helper) throws android.os.RemoteException;
public void setPlayerUIHelper(com.MetroMusic.aidl.PlayerUIHelper helper) throws android.os.RemoteException;
public void playSong(com.MetroMusic.data.Song song) throws android.os.RemoteException;
public void stopSong() throws android.os.RemoteException;
public void toogleSong(int toogle) throws android.os.RemoteException;
public boolean songIsPlaying() throws android.os.RemoteException;
}
