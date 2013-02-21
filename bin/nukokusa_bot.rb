#-*- coding: utf-8 -*-
#!/usr/bin/ruby

require "tweetstream"
require "twitter"
require "yaml"

lib_dir = File.expand_path("../../lib", __FILE__)
Dir.glob("#{lib_dir}/*.rb"){ |req| require req }

NukokusaBot::OAuth.authorize

