package shiyan.db;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 封装ByteArrayOutputStream的几个基本操作
 * 
 * @author IcekingT420
 *
 */
public class ByteBuffer {
	private ByteArrayOutputStream baos;

	public ByteBuffer() {
		baos = new ByteArrayOutputStream();
	}

	public void add(byte b) {
		baos.write(b);
	}

	public void add(byte[] array) {
		try {
			baos.write(array);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reset() {
		baos.reset();
	}

	public void close() {
		try {
			baos.close();
		} catch (IOException e) {

		}
	}

	public byte[] toArray() {
		return baos.toByteArray();
	}

	public byte[] toArrayAndClose() {
		byte[] array = baos.toByteArray();
		close();
		return array;
	}
}
