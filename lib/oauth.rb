#-*- coding: utf-8 -*-

module NukokusaBot
  module OAuth

    def self.authorize
      keys = ResourceManager.read_oauth_keys

      TweetStream.configure do |conf|
        conf.consumer_key       = keys[:consumer_key]
        conf.consumer_secret    = keys[:consumer_secret]
        conf.oauth_token        = keys[:token]
        conf.oauth_token_secret = keys[:token_secret]
        conf.auth_method        = :oauth
      end

      Twitter.configure do |conf|
        conf.consumer_key       = keys[:consumer_key]
        conf.consumer_secret    = keys[:consumer_secret]
        conf.oauth_token        = keys[:token]
        conf.oauth_token_secret = keys[:token_secret]
      end
    end

  end
end

