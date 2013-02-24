package jp.nukokusabot

import twitter4j._

class NukokusaBot {

  val twitter = new TwitterFactory().getInstance
  val markov = new MarkovChain

  val rules = makeRules
  val defaultRule = markovResponseRule

  val listener = new UserStreamListener {
    def onStatus(status: Status) = {
      if (status.getText.startsWith("@nukokusa_bot")) {
	rules.find(_.isMatch(status)) match {
	  case Some(rule) => rule.respondTo(status)
	  case None => defaultRule.respondTo(status)
	}
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
    val periodical = new PeriodicPost(45)
    periodical.start

    val stream = new TwitterStreamFactory().getInstance
    stream.addListener(listener)
    stream.user
  }

  def markov_test {
    val mc = new MarkovChain
    mc.addSentence("あいうえおあうえいあうえいあうえおいあうえいうえおあいえうえあいえおあ")
    mc.addSentence("あえあえお")
    mc.addSentence("おえういあ")
    println(mc.generateSentence())
  }

  private def makeRules: List[ResponseRule] = {
    List[ResponseRule]()
  }

  private def markovResponseRule: ResponseRule = {
    new ResponseRule {
      def isMatch(status: Status):Boolean = true

      def respondTo(status: Status): Unit = {
	val user = status.getUser
	val userName = user.getScreenName
	val text = "@" + userName + " " + "honesty"
	//"@" + userName + " " + markov.generateSentence(140 - userName.length - 2)

	val statusUpdate = new StatusUpdate(text).inReplyToStatusId(status.getId)
	twitter.updateStatus(statusUpdate)

	println(status.getText())
	println(text)
      }
    }
  }

}
