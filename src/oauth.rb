#-*- coding: utf-8 -*-

module NukokusaBot
  module Authorize

    def self.get_oauth_url(keys)
      oauth = OAuth::Consumer.new(keys[:consumer_key],
                                  keys[:consumer_secret],
                                  :site => "https://twitter.com")
      @@req_token = oauth.get_request_token
      @@req_token.authorize_url
    end

    def self.get_oauth_token(pin)
      token = @@req_token.get_access_token(:oauth_verifier => pin)
      {:token => token.token, :token_secret => token.secret}
    end

    def self.authorize
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

