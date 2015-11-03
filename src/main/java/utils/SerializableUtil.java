package utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SerializableUtil {
	private static final Logger log = LoggerFactory
	.getLogger(SerializableUtil.class);
	public static final int NULL = 0x00;
	public static final int NOTNULL = 0x01;

	public static byte[] serialize(Object object) {
		ByteArrayOutputStream bos = null;
		ObjectOutputStream oos = null;
		try {
			bos = new ByteArrayOutputStream(8 * 1024);
			oos = new ObjectOutputStream(bos);
			oos.writeObject(object);		
			byte[] data = bos.toByteArray();			
			return data;
		} catch (IOException ioe) {
			log.error("serialize exception {}", ioe.getMessage());
			
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException ioe) {
					log.error("serialize exception {}", ioe.getMessage());
				}
			}
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException ioe) {
					log.error("serialize exception {}", ioe.getMessage());
				}
			}
		}
		return null;
	}

	public static Object deSerialize(byte[] data){
		Object object = null;
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		try{
			if (data != null) {
				bis = new ByteArrayInputStream(data);
				ois = new ObjectInputStream(bis);
				object = ois.readObject();
				bis.close();
			}
		}
		catch(Exception ioe){
			log.error("deSerialize exception {}", ioe.getMessage());
		}
		finally{
			if(bis != null){
				try{
					bis.close();
				}
				catch(IOException ioe){
					log.error("deSerialize exception {}", ioe.getMessage());
				}
			}
			
			if(ois != null){
				try{
					ois.close();
				}
				catch(IOException ioe){
					log.error("deSerialize exception {}", ioe.getMessage());
				}
			}	
		
		}
		return object;
	}

	public static Timestamp readTimestamp(ObjectInput in, long nullFlag)
			throws ClassNotFoundException, IOException {
		Timestamp stamp = null;
		long value = in.readLong();
		if (value != nullFlag) {
			stamp = new Timestamp(value);
		}
		return stamp;
	}

	public static void writeTimestamp(Timestamp stamp, ObjectOutput out,
			long nullFlag) throws IOException {
		if (stamp != null) {
			long value = stamp.getTime();
			out.writeLong(value);
		} else {
			out.writeLong(nullFlag);
		}
	}

	public static String readString(ObjectInput in)
			throws ClassNotFoundException, IOException {
		byte b = in.readByte();
		switch (b) {
		case NULL:
			return null;
		case NOTNULL:
			return in.readUTF();
		}
		throw new InvalidObjectException("null flag broken:" + b);
	}

	public static void writeString(String string, ObjectOutput out)
			throws IOException {
		if (string == null) {
			out.writeByte(NULL);
		} else {
			out.writeByte(NOTNULL);
			out.writeUTF(string);
		}
	}

	public static Long readLong(ObjectInput in) throws ClassNotFoundException,
			IOException {
		byte b = in.readByte();
		switch (b) {
		case NULL:
			return null;
		case NOTNULL:
			return in.readLong();
		}
		throw new InvalidObjectException("null flag broken:" + b);
	}

	public static void writeLong(Long num, ObjectOutput out) throws IOException {
		if (num == null) {
			out.writeByte(NULL);
		} else {
			out.writeByte(NOTNULL);
			out.writeLong(num);
		}
	}

	public static void writeInt(Integer num, ObjectOutput out)
			throws IOException {
		if (num == null) {
			out.writeByte(NULL);
		} else {
			out.writeByte(NOTNULL);
			out.writeInt(num);
		}
	}
	
	public static void writeDouble(Double num, ObjectOutput out)
			throws IOException {
		if (num == null) {
			out.writeByte(NULL);
		} else {
			out.writeByte(NOTNULL);
			out.writeDouble(num);
		}
	}
	

	public static Integer readInt(ObjectInput in)
			throws ClassNotFoundException, IOException {
		byte b = in.readByte();
		switch (b) {
		case NULL:
			return null;
		case NOTNULL:
			return in.readInt();
		}
		throw new InvalidObjectException("null flag broken:" + b);
	}
	
	public static Double readDouble(ObjectInput in)
			throws ClassNotFoundException, IOException {
		byte b = in.readByte();
		switch (b) {
		case NULL:
			return null;
		case NOTNULL:
			return in.readDouble();
		}
		throw new InvalidObjectException("null flag broken:" + b);
	}

	public static void writeByte(Byte num, ObjectOutput out) throws IOException {
		if (num == null) {
			out.writeByte(NULL);
		} else {
			out.writeByte(NOTNULL);
			out.writeByte(num);
		}
	}

	public static Byte readByte(ObjectInput in) throws ClassNotFoundException,
			IOException {
		byte b = in.readByte();
		switch (b) {
		case NULL:
			return null;
		case NOTNULL:
			return in.readByte();
		}
		throw new InvalidObjectException("null flag broken:" + b);

	}

	public static void writeObject(Object obj, ObjectOutput out)
			throws IOException {
		if (obj == null) {
			out.writeObject(NULL);
		} else {
			out.writeObject(NOTNULL);
			out.writeObject(obj);
		}
	}

	public static Object readObject(ObjectInput in)
			throws ClassNotFoundException, IOException {
		byte b = in.readByte();
		switch (b) {
		case NULL:
			return null;
		case NOTNULL:
			return in.readObject();
		}
		throw new InvalidObjectException("null flag broken:" + b);
	}

}
