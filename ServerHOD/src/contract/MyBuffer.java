package contract;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class MyBuffer {

    private ArrayList<Byte> buffer;

    public MyBuffer() {
        buffer = new ArrayList<Byte>();
    }

    public void addByte(byte b) {
        buffer.add(b);
    }

    public void addBytes(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            buffer.add(bytes[i]);
        }
    }

    public ByteBuffer toByteBuffer() {
        byte[] byteArray = new byte[buffer.size()];
        for (int i = 0; i < buffer.size(); i++) {
            byteArray[i] = buffer.get(i);
        }
        return ByteBuffer.wrap(byteArray);
    }
}
