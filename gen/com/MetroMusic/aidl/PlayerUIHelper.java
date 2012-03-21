/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/Coffee/Code/Eclipseworkspace/Metro-Music-for-Android/src/com/MetroMusic/aidl/PlayerUIHelper.aidl
 */
package com.MetroMusic.aidl;
public interface PlayerUIHelper extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.MetroMusic.aidl.PlayerUIHelper
{
private static final java.lang.String DESCRIPTOR = "com.MetroMusic.aidl.PlayerUIHelper";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.MetroMusic.aidl.PlayerUIHelper interface,
 * generating a proxy if needed.
 */
public static com.MetroMusic.aidl.PlayerUIHelper asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.MetroMusic.aidl.PlayerUIHelper))) {
return ((com.MetroMusic.aidl.PlayerUIHelper)iin);
}
return new com.MetroMusic.aidl.PlayerUIHelper.Stub.Proxy(obj);
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
case TRANSACTION_showWaitBar:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
this.showWaitBar(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_setMusicProgressBarMax:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.setMusicProgressBarMax(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_updateMusicProgress:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.updateMusicProgress(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_updateBufferingProgress:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.updateBufferingProgress(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.MetroMusic.aidl.PlayerUIHelper
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
public void showWaitBar(boolean show) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((show)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_showWaitBar, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void setMusicProgressBarMax(int max) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(max);
mRemote.transact(Stub.TRANSACTION_setMusicProgressBarMax, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void updateMusicProgress(int position) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(position);
mRemote.transact(Stub.TRANSACTION_updateMusicProgress, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void updateBufferingProgress(int position) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(position);
mRemote.transact(Stub.TRANSACTION_updateBufferingProgress, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_showWaitBar = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_setMusicProgressBarMax = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_updateMusicProgress = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_updateBufferingProgress = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
}
public void showWaitBar(boolean show) throws android.os.RemoteException;
public void setMusicProgressBarMax(int max) throws android.os.RemoteException;
public void updateMusicProgress(int position) throws android.os.RemoteException;
public void updateBufferingProgress(int position) throws android.os.RemoteException;
}
