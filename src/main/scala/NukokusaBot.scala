package jp.nukokusabot

import twitter4j._
import java.util.Date
import java.util.Calendar

import com.typesafe.scalalogging.slf4j._

class NukokusaBot extends Logging with WeeklyJUMP {

  val twitter = new TwitterFactory().getInstance
  val markov  = new MarkovChain
  val amazon  = new AmazonProductAdvertising

  val rules = makeRules
  val schedules = makeSchedules

  val listener = new UserStreamListener {
    def onStatus(status: Status) = {
      if (status.getText.startsWith("@nukokusa_bot") || status.getInReplyToScreenName == "nukokusa_bot") {
        rules.find(_.isMatch(status)).get.respondTo(status)
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

  private def makeRules: List[ResponseRule] = {
    val markovResponse = new ResponseRule {
      def isMatch(status: Status):Boolean = true

      def respondTo(status: Status): Unit = {
	val user = status.getUser
	val userName = user.getScreenName
	val text = "@" + userName + " " + markov.generateSentence(140 - userName.length - 2)

	val statusUpdate = new StatusUpdate(text).inReplyToStatusId(status.getId)
	updateStatus(statusUpdate)
      }
    }

    val amazonResponse = new ResponseRule {
      def isMatch(status: Status): Boolean = {
        status.getText.matches("^(.+)が?(欲しい|買いたい).*$")
      }

      def respondTo(status: Status): Unit = {
        val regexp  = "^(.+)が?(欲しい|買いたい).*$".r
        val keyword = regexp.findFirstMatchIn(status.getText).get.group(1)
        val item = amazon.getFirstItem(keyword)

	val user = status.getUser
	val userName = user.getScreenName

	val text = "@"+userName+" "+item.title +" "+item.price+"円"+" "+item.url+" "+Utils.timestamp

	val statusUpdate = new StatusUpdate(text).inReplyToStatusId(status.getId)
	updateStatus(statusUpdate)
      }
    }

    List[ResponseRule](amazonResponse, markovResponse)
  }

  private def makeSchedules: List[Schedule] = {
    val markovTweet = new Schedule {
      def task = {
        val statusUpdate = new StatusUpdate(markov.generateSentence(140))
        updateStatus(statusUpdate)
      }
    }
    markovTweet.hourRange = 6 to 23
    markovTweet.minRange  = 0 to 0

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

    List[Schedule](markovTweet, todaysTopic, weeklyJUMP)
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
