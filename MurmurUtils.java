
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author: huahua
 * @description: //TODO
 * @date: create at 下午12:10 2018/1/2
 */
public class MurmurUtils {


  private final static String[] chars = new String[]{"0", "1", "2", "3", "4", "5",
      "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h",
      "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
      "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H",
      "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
      "U", "V", "W", "X", "Y", "Z"
  };

  /**
   * murmur hash算法实现
   */
  public static Long hash(byte[] key) {

    ByteBuffer buf = ByteBuffer.wrap(key);
    int seed = 0x1234ABCD;

    ByteOrder byteOrder = buf.order();
    buf.order(ByteOrder.LITTLE_ENDIAN);

    long m = 0xc6a4a7935bd1e995L;
    int r = 47;

    long h = seed ^ (buf.remaining() * m);

    long k;
    while (buf.remaining() >= 8) {
      k = buf.getLong();

      k *= m;
      k ^= k >>> r;
      k *= m;

      h ^= k;
      h *= m;
    }

    if (buf.remaining() > 0) {
      ByteBuffer finish = ByteBuffer.allocate(8).order(
          ByteOrder.LITTLE_ENDIAN);
      finish.put(buf).rewind();
      h ^= finish.getLong();
      h *= m;
    }

    h ^= h >>> r;
    h *= m;
    h ^= h >>> r;

    buf.order(byteOrder);
    return h;
  }

  public static Long hash(String key) {
    return hash(key.getBytes());
  }


  /**
   * Long转换成无符号长整型（C中数据类型）
   */
  public static BigDecimal readUnsignedLong(long value) {
    if (value >= 0) {
      return new BigDecimal(value);
    }
    long lowValue = value & 0x7fffffffffffffffL;
    return BigDecimal.valueOf(lowValue).add(BigDecimal.valueOf(Long.MAX_VALUE))
        .add(BigDecimal.valueOf(1));
  }

  /**
   * 返回无符号murmur hash值
   */
  public static BigDecimal hashUnsigned(String key) {
    return readUnsignedLong(hash(key));
  }

  public static BigDecimal hashUnsigned(byte[] key) {
    return readUnsignedLong(hash(key));
  }


  public static String to62String(Long hash) {
    Integer size = chars.length;
    if (size == 0) {
      return "0";
    }
    StringBuilder result = new StringBuilder();
    while (hash / size > 0) {
      result.append(chars[(int)(hash % size)]);
      hash /= size;
    }
    if (result.length()<1){
      return "0";
    }else {
      return result.reverse().toString();
    }
  }

  public static String to62Hash(String key) {
    Long hash = hash(key);
    return to62String(Math.abs(hash));
  }
}
