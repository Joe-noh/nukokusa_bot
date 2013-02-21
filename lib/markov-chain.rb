#-*- coding: utf-8 -*-

require "natto"

module NukokusaBot

  class Natto::MeCabNode
    def compress
      self.feature[0, 2] + self.surface
    end
  end

  class MarkovChain

    def initialize
      @word_id  = Hash.new  # {'word1' => id1, 'word2' => id2}
      @tailings = Hash.new  # {id1 => {id2 => count, id3 => count}}
      @mecab = Natto::MeCab.new

      @max_id  = -1
      register_words(['先頭', '末尾'])
    end

    def append(sentence)
      words = @mecab.parse_as_nodes(sentence)[0..-2].map(&:compress)

      register_words(words)

      count_tailings('先頭', words.first)
      words.each_cons(2){ |pre, suc| count_tailings(pre, suc) }
      count_tailings(words.last, '末尾')

      require "pp"
      pp @tailings
    end
    alias_method :<<, :append

    private

    def register_words(words)
      words.each do |word|
        unless @word_id.has_key? word
          @max_id += 1
          @word_id[word] = @max_id
        end
        puts @max_id
      end
    end

    def count_tailings(pre, suc)
      pre_id = @word_id[pre]
      suc_id = @word_id[suc]

      if @tailings.has_key? pre_id
        if @tailings[pre_id].has_key? suc_id
          @tailings[pre_id][suc_id] += 1
        else
          @tailings[pre_id][suc_id] = 1
        end
      else
        @tailings[pre_id] = Hash.new
        @tailings[pre_id][suc_id] = 1
      end
    end
  end
end

