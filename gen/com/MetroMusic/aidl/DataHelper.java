/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/Coffee/Code/Eclipseworkspace/Metro-Music-for-Android/src/com/MetroMusic/aidl/DataHelper.aidl
 */
package com.MetroMusic.aidl;
public interface DataHelper extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.MetroMusic.aidl.DataHelper
{
private static final java.lang.String DESCRIPTOR = "com.MetroMusic.aidl.DataHelper";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.MetroMusic.aidl.DataHelper interface,
 * generating a proxy if needed.
 */
public static com.MetroMusic.aidl.DataHelper asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.MetroMusic.aidl.DataHelper))) {
return ((com.MetroMusic.aidl.DataHelper)iin);
}
return new com.MetroMusic.aidl.DataHelper.Stub.Proxy(obj);
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
case TRANSACTION_nextSong:
{
data.enforceInterface(DESCRIPTOR);
this.nextSong();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.MetroMusic.aidl.DataHelper
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
public void nextSong() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_nextSong, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_nextSong = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void nextSong() throws android.os.RemoteException;
}
