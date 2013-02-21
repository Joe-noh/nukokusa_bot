#-*- coding: utf-8 -*-

require "yaml"

module NukokusaBot
  module ResourceManager

    @@res_dir = File.expand_path("../../res", __FILE__)

    @@read_yaml = lambda do |file|
      yaml = YAML.load_file File.expand_path(file, __FILE__)
      Hash[*yaml.keys.map(&:to_sym).zip(yaml.values.map(&:strip)).flatten]
    end

    def self.read_oauth_keys
      @@read_yaml["#{@@res_dir}/oauth_keys.yaml"]
    end

  end
end
