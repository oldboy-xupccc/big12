import java.io.{FileInputStream, FileOutputStream}

/**
  * Created by Administrator on 2018/9/29.
  */
object FileCopy {
    def main(args: Array[String]): Unit = {
        val fis = new FileInputStream("d:\\java\\1.txt")
        val fos = new FileOutputStream("d:\\java\\2.txt")
        val buf = new Array[Byte](1024)
        var len = -1
        /**
          * 这种方式是错误的，原因是len = fis.read(buf)语句的值是Unit,不是读取的字节数，
          * 因此 !=-1恒成立
          * while(({len = fis.read(buf) ; len} != -1){
          *     fos.write(buf,  0 , len) ;
          * }
          */
//        while({len = fis.read(buf) ; len != -1}){
//            fos.write(buf , 0 , len)
//        }
        while(({len = fis.read(buf) ; len}) != -1){
            fos.write(buf , 0 , len) ;
        }
        fos.close()
        fis.close()
    }
}
