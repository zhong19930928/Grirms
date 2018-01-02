/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: C:\\Users\\yh\\Desktop\\Grirms_ZR\\gRIRMS40\\src\\main\\aidl\\com\\yunhu\\yhshxc\\aidl\\GrirmsData.aidl
 */
package com.yunhu.yhshxc.aidl;
public interface GrirmsData extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.yunhu.yhshxc.aidl.GrirmsData
{
private static final java.lang.String DESCRIPTOR = "com.yunhu.yhshxc.aidl.GrirmsData";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.yunhu.yhshxc.aidl.GrirmsData interface,
 * generating a proxy if needed.
 */
public static com.yunhu.yhshxc.aidl.GrirmsData asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.yunhu.yhshxc.aidl.GrirmsData))) {
return ((com.yunhu.yhshxc.aidl.GrirmsData)iin);
}
return new com.yunhu.yhshxc.aidl.GrirmsData.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
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
case TRANSACTION_getMd5Code:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getMd5Code();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getCompeleteSize:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getCompeleteSize();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_isDowning:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isDowning();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.yunhu.yhshxc.aidl.GrirmsData
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public java.lang.String getMd5Code() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getMd5Code, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getCompeleteSize() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getCompeleteSize, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean isDowning() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isDowning, _data, _reply, 0);
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
static final int TRANSACTION_getMd5Code = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getCompeleteSize = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_isDowning = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
}
public java.lang.String getMd5Code() throws android.os.RemoteException;
public java.lang.String getCompeleteSize() throws android.os.RemoteException;
public boolean isDowning() throws android.os.RemoteException;
}
