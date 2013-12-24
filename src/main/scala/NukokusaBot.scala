package net.joe_noh.nukokusabot

import twitter4j._
import scala.util.Random
import java.util.Date
import java.util.Calendar

import com.typesafe.scalalogging.slf4j._
import com.github.nscala_time.time.Imports._
import jp.t2v.util.locale.Implicits._

class NukokusaBot extends Logging with WeeklyJUMP {

  val twitter = new TwitterFactory().getInstance
  val amazon  = new AmazonProductAdvertising
  //val markov  = new MarkovChain

  val replyToReplyRules = makeReplyToReplyRules
  val replyToMonologueRules = makeReplyToMonologueRules
  val schedules = makeSchedules

  val listener = new UserStreamListener {
    def onStatus(status: Status) = {
      println(status.getText)
      if (status.getText.startsWith("@nukokusa_bot") || status.getInReplyToScreenName == "nukokusa_bot") {
        replyToReplyRules.find(_.isMatch(status)).get.respondTo(status)
      } else {
        replyToMonologueRules.find(_.isMatch(status)).get.respondTo(status)
      }
    }
    def onBlock(sourse: User, blockedUser: User) = {}
    def onDirectMessage(directMessage: DirectMessage) = {}
    def onDeletionNotice(directMessageIdd: Long, userId: Long) = {}
    def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice) = {}
    def onFavorite(source: User, target: User, favoritedStatus: Status) = {}
    def onFollow(source: User, followedUser: User) = {}
    def onFriendList(friendIds: Array[Long]) = {}
    def onUnblock(source: User, unblockedUser: User) = {}
    def onUnfavorite(source: User, target: User, unfavoritedStatus: Status) = {}
    def onUserListCreation(listOwner: User, list: UserList) = {}
    def onUserListDeletion(listOwner: User, list: UserList) = {}
    def onUserListMemberAddition(addedMember: User, listOwner: User, list: UserList) = {}
    def onUserListMemberDeletion(deletedMember: User, listOwner: User, list: UserList) = {}
    def onUserListSubscription(subscriber: User, listOwner: User, list: UserList) = {}
    def onUserListUnsubscription(subscriber: User, listOwner: User, list: UserList) = {}
    def onUserListUpdate(listOwner: User, list: UserList) = {}
    def onUserProfileUpdate(updatedUser: User) = {}
    def onTrackLimitationNotice(numberOfLimitedStatuses: Int) = {}
    def onScrubGeo(userId: Long, upToStatusId: Long) = {}
    def onStallWarning(warning: StallWarning) = {}
    def onException(ex: Exception) = ex.printStackTrace()
  }

  def run = {
    val scheduler = new Scheduler(schedules)
    scheduler.start

    val stream = new TwitterStreamFactory().getInstance
    stream.addListener(listener)
    stream.user
  }

  private def makeReplyToReplyRules: List[ResponseRule] = {
    val amazonResponse = new ResponseRule {
      def isMatch(status: Status): Boolean = {
        status.getText.matches("^(.+)が?(ほしい|欲しい|かいたい|買いたい).*$")
      }

      def respondTo(status: Status): Unit = {
        val regexp  = "^(.+)が?(ほしい|欲しい|かいたい|買いたい).*$".r
        val keyword = regexp.findFirstMatchIn(status.getText).get.group(1).diff("@nukokusa_bot").trim
        val userName = status.getUser.getScreenName

        var text = ""
        try {
          val item = amazon.getFirstItem(keyword)
          text = "@"+userName+" "+item.title +" "+item.price+"円"+" "+item.url+" "+Utils.timestamp
        } catch {
          case e: Exception =>
            text = "@"+userName+" Not Found. "+Utils.timestamp
        } finally {
          val statusUpdate = new StatusUpdate(text).inReplyToStatusId(status.getId)
          updateStatus(statusUpdate)
        }
      }
    }

    val emptyResponse = new ResponseRule {
      def isMatch(status: Status):Boolean = true
      def respondTo(status: Status): Unit = {}
    }

    List[ResponseRule](amazonResponse, emptyResponse)
  }

  private def makeReplyToMonologueRules: List[ResponseRule] = {
    val crawlNukokusaResponse = new ResponseRule {
      def isMatch(status: Status): Boolean = {
        status.getUser.getScreenName == "nukokusa"
      }

      def respondTo(status: Status): Unit = {
        //markov.addStatus(status)
      }
    }

    val workHardResponse = new ResponseRule {
      def isMatch(status: Status): Boolean = {
        val random = new Random()
        val calendar = Calendar.getInstance
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val wday = calendar.get(Calendar.DAY_OF_WEEK)
        val userName = status.getUser.getScreenName

        random.nextInt(32) == 0 &&
        userName == "JO_RI" &&
        wday != Calendar.SUNDAY &&
        wday != Calendar.SATURDAY &&
        !(DateTime.now.isHoliday) &&
        ( (10 to 11).contains(hour) || (14 to 16).contains(hour) )
      }

      def respondTo(status: Status): Unit = {
        try {
          val text = "@"+status.getUser.getScreenName+" ツイッターしてないで働け "+Utils.timestamp
          val statusUpdate = new StatusUpdate(text).inReplyToStatusId(status.getId)
          updateStatus(statusUpdate)
        } catch {
          case e: Exception => logger.warn(e.getMessage)
        }
      }
    }

    val emptyResponse = new ResponseRule {
      def isMatch(status: Status):Boolean = true
      def respondTo(status: Status): Unit = {}
    }

    List[ResponseRule](workHardResponse, emptyResponse)
  }

  private def makeSchedules: List[Schedule] = {
    val todaysTopic = new Schedule {
      def task = try {
        val statusUpdate = new StatusUpdate(TodaysTopic.getTopic(new Date())+" "+Utils.timestamp)
        updateStatus(statusUpdate)
      } catch {
        case e: Exception => logger.warn(e.getMessage)
      }
    }
    todaysTopic.hourRange = 6 to 6
    todaysTopic.minRange  = 0 to 0

    val weeklyJUMPSaturday = new Schedule {
      def task = try {
        if ((DateTime.now + 2.days).isHoliday) {
          val buyerName = getJUMPBuyerName(1)
          val nextBuyerName = getNextJUMPBuyerName(1)

          val text = "@"+buyerName+" 月曜は祝日だ！ジャンプを買え！\n来週は @"+nextBuyerName+" だ！ "+Utils.timestamp
          val statusUpdate = new StatusUpdate(text)
          updateStatus(statusUpdate)
        }
      } catch {
        case e: Exception => logger.warn(e.getMessage)
      }
    }
    weeklyJUMPSaturday.wdayRange = Calendar.SATURDAY to Calendar.SATURDAY
    weeklyJUMPSaturday.hourRange = 7 to 7
    weeklyJUMPSaturday.minRange  = 0 to 0

    val weeklyJUMPSunday = new Schedule {
      def task = try {
        if (!(DateTime.now + 1.day).isHoliday) {
          val buyerName = getJUMPBuyerName
          val nextBuyerName = getNextJUMPBuyerName

          val text = "今週のジャンプ担当は @"+buyerName+" です。\n来週は @"+nextBuyerName+" ですよ "+Utils.timestamp
          val statusUpdate = new StatusUpdate(text)
          updateStatus(statusUpdate)
        }
      } catch {
        case e: Exception => logger.warn(e.getMessage)
      }
    }
    weeklyJUMPSunday.wdayRange = Calendar.SUNDAY to Calendar.SUNDAY
    weeklyJUMPSunday.hourRange = 7 to 7
    weeklyJUMPSunday.minRange  = 0 to 0

    val weeklyJUMPMonday = new Schedule {
      def task = try {
        if (!DateTime.now.isHoliday) {
          val buyerName = getJUMPBuyerName

          val text = "おい @"+buyerName+"、ジャンプ買ってこいよ "+Utils.timestamp
          val statusUpdate = new StatusUpdate(text)
          updateStatus(statusUpdate)
        }
      } catch {
        case e: Exception => logger.warn(e.getMessage)
      }
    }
    weeklyJUMPMonday.wdayRange = Calendar.MONDAY to Calendar.MONDAY
    weeklyJUMPMonday.hourRange = 7 to 7
    weeklyJUMPMonday.minRange  = 0 to 0

    List[Schedule](todaysTopic, weeklyJUMPSaturday, weeklyJUMPSunday, weeklyJUMPMonday)
  }

  private def updateStatus(status: StatusUpdate) = {
    if (Config.debug) {
      logger.info("update '" + status.getStatus + "' DEBUG")
    } else {
      twitter.updateStatus(status)
      logger.info("update '" + status.getStatus + "'")
    }
  }

}
