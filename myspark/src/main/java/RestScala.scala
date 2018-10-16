import java.io.{ByteArrayOutputStream, DataOutputStream}
import java.net.{ConnectException, HttpURLConnection, URL}
import java.nio.charset.StandardCharsets

import com.alibaba.fastjson.util.IOUtils
import org.apache.spark.deploy.rest.SubmitRestConnectionException
import org.apache.spark.util.Utils
import org.apache.spark.{SparkConf, SparkContext}

/**
  * 累加器
  */
object RestScala {
    def main(args: Array[String]): Unit = {
        val json = "{\n\t\"clientSparkVersion\":\"2.1.0\",\n\t\"mainClass\":\"WordCountScala\",\n\t\"appArgs\":[\"--master spark://s101:7077\"]\n}"
        val urlStr = "http://s101:6066/v1/submissions/submit"
        val conn = new URL(urlStr).openConnection().asInstanceOf[HttpURLConnection]
        conn.setRequestMethod("POST")
        conn.setRequestProperty("Content-Type", "application/json")
        conn.setRequestProperty("charset", "utf-8")
        conn.setDoOutput(true)
        try {
            val out = new DataOutputStream(conn.getOutputStream)
            out.write(json.getBytes())
            out.flush()
            out.close()
            println(conn.getResponseCode)
            val in = conn.getInputStream
            val baos = new ByteArrayOutputStream()
            var len = 0
            val buf = new Array[Byte](1024)
            while({len = in.read(buf) ; len} != -1){
                baos.write(buf , 0 , len)
            }
            baos.close()
            println(new String(baos.toByteArray))

        } catch {
            case e => println(e)
        }

    }
}
