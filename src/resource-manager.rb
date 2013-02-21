#-*- coding: utf-8 -*-

module NukokusaBot
  module ResourceManager

    @@res_dir = File.expand_path("../../res", __FILE__)

    def self.read_oauth_token
      @read_yaml["#{@@res_dir}/oauth_keys.yaml"]
    end

    @@read_yaml = lambda do |file|
      yaml = YAML.load_file File.expand_path(file, __FILE__)
      Hash[*yaml.keys.map(&:to_sym).zip(yaml.values.map(&:strip)).flatten]
    end

  end
end
