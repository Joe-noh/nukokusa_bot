#-*- coding: utf-8 -*-

require "tweetstream"
require "twitter"

require "./resource-manager"
require "./markov-chain"
require "./oauth"

module NukokusaBot
  class Core

    def initialize
      @response_rules = Array.new
      @stream_client  = TweetStream::Client.new
    end

    def authenticate
      OAuth.authorize
    end

    def add_response_rule
    end

    def start_streaming
      @stream_client.userstream do |status|
        @response_rules.find{|rule| rule.match? status }.execute
      end
    end

  end
end


if __FILE__ == $0
  nukokusa = NukokusaBot::Core.new

  nukokusa.authenticate

  nukokusa.start_streaming
end
