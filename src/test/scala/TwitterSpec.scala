import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import org.specs2.specification.BeforeExample

import java.util.Properties
import scala.xml._

class TwitterSpec extends Specification {

    "Tokens" should {

        "be read correctly" in {
            val key = new Properties()
            key.load(getClass.getResourceAsStream("/oauth_keys.properties"))
            println(key.getProperty("access"))
            assert(key.getProperty("access_token").length > 5)
//            println(keys \ "access_token")
//            println(name)
        }
    }
}
