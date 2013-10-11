package net.joe_noh.nukokusabot

import twitter4j._
import java.util.Random
import java.util.Date
import java.util.Calendar

import com.typesafe.scalalogging.slf4j._

class NukokusaBot extends Logging with WeeklyJUMP {

  val twitter = new TwitterFactory().getInstance
  val amazon  = new AmazonProductAdvertising

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
    val workHardResponse = new ResponseRule {
      def isMatch(status: Status): Boolean = {
        val random = new Random()
        val calendar = Calendar.getInstance
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val wday = calendar.get(Calendar.DAY_OF_WEEK)

        random.nextInt(32) == 0 &&
        status.getUser.getScreenName == "JO_RI" &&
        wday != Calendar.SUNDAY &&
        wday != Calendar.SATURDAY &&
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
        val statusUpdate = new StatusUpdate(TodaysTopic.getTopic(new Date)+" "+Utils.timestamp)
        updateStatus(statusUpdate)
      } catch {
        case e: Exception => logger.warn(e.getMessage)
      }
    }
    todaysTopic.hourRange = 6 to 6
    todaysTopic.minRange  = 0 to 0

    val weeklyJUMP = new Schedule {
      def task = try {
        val buyerName = getJUMPBuyerName

        val text = "おい @"+buyerName+"、"+"ジャンプ買ってこいよ"+" "+Utils.timestamp
        val statusUpdate = new StatusUpdate(text)
        updateStatus(statusUpdate)
      } catch {
        case e: Exception => logger.warn(e.getMessage)
      }
    }
    weeklyJUMP.wdayRange = Calendar.MONDAY to Calendar.MONDAY
    weeklyJUMP.hourRange = 7 to 7
    weeklyJUMP.minRange  = 0 to 0

    val preWeeklyJUMP = new Schedule {
      def task = try {
        val buyerName = getJUMPBuyerName
        val nextBuyerName = getNextJUMPBuyerName

        val text = "今週のジャンプ担当は @"+buyerName+" です。\n来週は @"+nextBuyerName+" ですよ"+" "+Utils.timestamp
        val statusUpdate = new StatusUpdate(text)
        updateStatus(statusUpdate)
      } catch {
        case e: Exception => logger.warn(e.getMessage)
      }
    }
    preWeeklyJUMP.wdayRange = Calendar.SUNDAY to Calendar.SUNDAY
    preWeeklyJUMP.hourRange = 7 to 7
    preWeeklyJUMP.minRange  = 0 to 0

    List[Schedule](todaysTopic, weeklyJUMP, preWeeklyJUMP)
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
