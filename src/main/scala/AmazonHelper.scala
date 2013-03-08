package jp.nukokusabot

/*******************************************************************************
 *
 * Amazon Product API REST Client
 *
 * http://blog.pfa-labs.com/2009/08/amazon-product-api-rest-client-in-scala.html
 *
 *******************************************************************************/

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone
import java.io.FileInputStream
import java.net.URLEncoder

import javax.crypto.spec.SecretKeySpec
import javax.crypto.Mac

import org.apache.commons.codec.binary.Base64

import scala.collection.immutable.SortedMap
import scala.collection.immutable.TreeMap
import scala.xml.XML

trait SignedRequestsAmazonApi {
  implicit def any2tap[A](obj: A): Tap[A] = new Tap(obj)

  val conf = XML.load(getClass.getResourceAsStream("/config.xml"))

  val UTF8_CHARSET = "UTF-8"
  val HMAC_SHA256_ALGORITHM = "HmacSHA256"
  val REQUEST_URI = "/onca/xml"
  val REQUEST_METHOD = "GET"

  val endpoint = "ecs.amazonaws.jp"
  val service = "AWSECommerceService"
  val version = "2011-08-01"

  val secretKeySpec = new SecretKeySpec((conf \\ "secretAccessKey").text.getBytes(UTF8_CHARSET), HMAC_SHA256_ALGORITHM)

  def sign(params: Map[String, String]) :String = {
    var sortedParamMap = TreeMap.empty[String, String]
    sortedParamMap = sortedParamMap.insert("AWSAccessKeyId", (conf \\ "accessKeyId").text)
    sortedParamMap = sortedParamMap.insert("AssociateTag", (conf \\ "associateTag").text)
    sortedParamMap = sortedParamMap.insert("Service", service)
    sortedParamMap = sortedParamMap.insert("Version", version)
    sortedParamMap = sortedParamMap.insert("Timestamp", timestamp())
    params foreach ( (o) => sortedParamMap = sortedParamMap.insert(o._1, o._2))

    val canonicalQS = canonicalize(sortedParamMap);
    val toSign = REQUEST_METHOD + "\n" + endpoint + "\n" + REQUEST_URI + "\n" + canonicalQS;
    val hmacStr = hmac(toSign);
    val sig = percentEncodeRfc3986(hmacStr);

    "http://" + endpoint + REQUEST_URI + "?" + canonicalQS + "&Signature=" + sig;
  }

  protected def hmac(stringToSign: String) :String = {
    try {
      val mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
      mac.init(secretKeySpec);

      val data = stringToSign.getBytes(UTF8_CHARSET);
      val rawHmac = mac.doFinal(data);

      val encoder = new Base64();
      return encoder.encodeToString(rawHmac).trim();
    } catch {
      case _ => throw new RuntimeException(UTF8_CHARSET + " is unsupported!")
    }
  }

  protected def timestamp(): String = {
    val dfm = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    dfm.setTimeZone(TimeZone.getTimeZone("GMT"));
    return dfm.format(Calendar.getInstance().getTime());
  }

  protected def canonicalize( sortedParamMap: SortedMap[String, String]): String = {
    sortedParamMap.map {
      (key) => percentEncodeRfc3986( key._1 ) + "=" + percentEncodeRfc3986(key._2)
    }.reduceLeft[String]{ (acc, url) => acc + "&" + url }
  }

  protected def percentEncodeRfc3986(s: String): String = {
    try {
      return URLEncoder.encode(s, UTF8_CHARSET).replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
    } catch  {
      case _ => throw new RuntimeException(UTF8_CHARSET + " is unsupported!")
    }
  }
}
